package io.nqode.powermeter.service;

import com.opencsv.bean.CsvToBeanBuilder;
import io.nqode.powermeter.dto.UploadFractionDto;
import io.nqode.powermeter.dto.UploadReadingDto;
import io.nqode.powermeter.dto.UploadResponseDto;
import io.nqode.powermeter.dto.UploadResultDto;
import io.nqode.powermeter.exception.AbstractException;
import io.nqode.powermeter.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.file.Files.deleteIfExists;

@Slf4j
@Service
@RequiredArgsConstructor
public class LegacyServiceImpl implements LegacyService {

    public static final String UPLOAD_LOG = "upload.log";
    private static final String ERROR_PARSING_UPLOADED_FILE = "Error parsing uploaded file";
    private static final String FRACTIONS_FILE_NAME = "fractions.csv";
    private static final String READINGS_FILE_NAME = "readings.csv";
    private final ProfileService profileService;

    private final MeterService meterService;

    @Value("${monitoring-folder}")
    private String uploadFolder;

    @Override
    public UploadResultDto parseFractionCSV(MultipartFile csvFile) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(csvFile.getInputStream());
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            return getFractionsResultDto(bufferedReader);
        } catch (IOException exception) {
            throw new ValidationException("Error while uploading csv");
        }
    }

    private UploadResultDto getFractionsResultDto(Reader bufferedReader) {
        List<UploadFractionDto> fractionDtos = new CsvToBeanBuilder<UploadFractionDto>(bufferedReader)
                .withType(UploadFractionDto.class)
                .build()
                .parse();

        List<String> validationErrors = new ArrayList<>();
        Map<String, List<UploadFractionDto>> profileFractionsMap = fractionDtos
                .stream()
                .collect(Collectors.groupingBy(UploadFractionDto::getProfile));

        List<UploadResponseDto> profileFractionsDtos = new ArrayList<>();
        profileFractionsMap.forEach((profileName, uploadFractionDtos)
                -> saveAndExtractFractions(profileName, uploadFractionDtos, profileFractionsDtos, validationErrors));

        return UploadResultDto.builder()
                .uploaded(profileFractionsDtos)
                .errors(validationErrors)
                .build();
    }

    private void saveAndExtractFractions(String profileName, List<UploadFractionDto> uploadFractionDtos, List<UploadResponseDto> profileFractionsDtos, List<String> validationErrors) {
        try {
            profileFractionsDtos.add(profileService.saveProfileWithFractions(profileName, uploadFractionDtos));
        } catch (AbstractException exception) {
            validationErrors.add("Profile %s: ".formatted(profileName) + exception.getMessage());
        }
    }

    @Override
    public UploadResultDto parseReadingCSV(MultipartFile csvFile) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(csvFile.getInputStream());
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            return getUploadResultDto(bufferedReader);

        } catch (IOException e) {
            throw new ValidationException("Error while uploading csv");
        }
    }

    private UploadResultDto getUploadResultDto(Reader reader) {
        List<UploadReadingDto> readingDtos = new CsvToBeanBuilder<UploadReadingDto>(reader)
                .withType(UploadReadingDto.class)
                .build()
                .parse();

        List<String> validationErrors = new ArrayList<>();
        Map<String, Map<String, List<UploadReadingDto>>> profileMeterReadingsMap = readingDtos
                .stream()
                .collect(Collectors.groupingBy(UploadReadingDto::getProfile,
                        Collectors.groupingBy(UploadReadingDto::getMeterIdentifier)));

        List<UploadResponseDto> meterReadingsDtos = new ArrayList<>();
        profileMeterReadingsMap.forEach((profileName, meterReadings)
                -> meterReadings.forEach((meterIdentifier, readings)
                -> saveAndExtract(profileName, meterIdentifier, readings, meterReadingsDtos, validationErrors)));

        return UploadResultDto.builder()
                .uploaded(meterReadingsDtos)
                .errors(validationErrors)
                .build();
    }

    private void saveAndExtract(String profileName, String meterIdentifier, List<UploadReadingDto> readings, List<UploadResponseDto> meterReadingsDtos, List<String> validationErrors) {
        try {
            meterReadingsDtos.add(meterService.saveUploadedMeterReadings(profileName, meterIdentifier, readings));
        } catch (AbstractException exception) {
            validationErrors.add("Meter %s: ".formatted(meterIdentifier) + exception.getMessage());
        }
    }

    @Override
    public void parseCSVFromFile(String filename) {
        log.info("Start uploading file {}", filename);

        String absolutePath = Paths.get("", uploadFolder, filename).toAbsolutePath().toString();
        Path logPath = Paths.get("", uploadFolder, UPLOAD_LOG).toAbsolutePath();

        UploadResultDto uploadResultDto = switch (filename) {
            case FRACTIONS_FILE_NAME -> parseFractionsFromFile(absolutePath);
            case READINGS_FILE_NAME -> parseReactionsFromFile(absolutePath);
            default -> {
                log.info("File not for uploading");
                yield UploadResultDto.builder().build();
            }
        };

        try {
            if (uploadResultDto.getErrors().isEmpty()) {
                deleteIfExists(Paths.get(absolutePath));
                deleteIfExists(logPath);
            } else {
                deleteIfExists(logPath);
                Files.writeString(logPath, String.join("\n", uploadResultDto.getErrors()));
            }
        } catch (IOException exception) {
            log.error("Deleting/creating log file", exception);
        }

        log.info("Finish uploading file {}", filename);
    }

    private UploadResultDto parseFractionsFromFile(String path) {
        try (InputStreamReader inputStreamReader = new FileReader(path);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            return getFractionsResultDto(bufferedReader);
        } catch (IOException exception) {
            log.error(ERROR_PARSING_UPLOADED_FILE, exception);
            return UploadResultDto.builder()
                    .errors(List.of(ERROR_PARSING_UPLOADED_FILE))
                    .build();
        }
    }

    private UploadResultDto parseReactionsFromFile(String path) {
        try (InputStreamReader inputStreamReader = new FileReader(path);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            return getUploadResultDto(bufferedReader);
        } catch (IOException exception) {
            log.error(ERROR_PARSING_UPLOADED_FILE, exception);
            return UploadResultDto.builder()
                    .errors(List.of(ERROR_PARSING_UPLOADED_FILE))
                    .build();
        }
    }
}
