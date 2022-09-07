package io.nqode.powermeter.controller;

import io.nqode.powermeter.dto.UploadResultDto;
import io.nqode.powermeter.service.LegacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/legacy")
@RequiredArgsConstructor
public class LegacyController {

    private final LegacyService legacyService;

    @Operation(
            summary = "Uploads fractions in a form of a CSV file",
            operationId = "uploadFractions",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The action was successful", content = @Content(schema = @Schema(implementation = UploadResultDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping("/fractions")
    public ResponseEntity<UploadResultDto> uploadFractions(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(legacyService.parseFractionCSV(file));
    }

    @Operation(
            summary = "Uploads readings in a form of a CSV file",
            operationId = "uploadReadings",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The action was successful", content = @Content(schema = @Schema(implementation = UploadResultDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping("/readings")
    public ResponseEntity<UploadResultDto> uploadReadings(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(legacyService.parseReadingCSV(file));
    }
}
