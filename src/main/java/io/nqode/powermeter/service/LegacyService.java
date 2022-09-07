package io.nqode.powermeter.service;

import io.nqode.powermeter.dto.UploadResultDto;
import org.springframework.web.multipart.MultipartFile;

public interface LegacyService {

    UploadResultDto parseFractionCSV(MultipartFile csvFile);

    UploadResultDto parseReadingCSV(MultipartFile csvFile);

    void parseCSVFromFile(String filename);
}
