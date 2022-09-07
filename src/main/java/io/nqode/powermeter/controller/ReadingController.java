package io.nqode.powermeter.controller;

import io.nqode.powermeter.dto.ReadingsDto;
import io.nqode.powermeter.service.ReadingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static io.nqode.powermeter.converter.ReadingConverter.convertToDto;


@RestController
@RequestMapping("profiles/{profileId}/meters/{meterId}/readings")
@RequiredArgsConstructor
public class ReadingController {

    private final ReadingService readingService;

    @Operation(
            summary = "Serves readings for meter of a profile",
            operationId = "getReadings",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The action was successful", content = @Content(schema = @Schema(implementation = ReadingsDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not found, either profile, meter or readings are not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping
    public ResponseEntity<ReadingsDto> getReadings(@PathVariable Long profileId,
                                                   @PathVariable Long meterId) {
        return ResponseEntity.ok(convertToDto(readingService.getAllByProfileIdAndMeterId(profileId, meterId)));
    }

    @Operation(
            summary = "Creates readings",
            operationId = "postReadings",
            responses = {
                    @ApiResponse(responseCode = "201", description = "The action was successful", content = @Content(schema = @Schema(implementation = ReadingsDto.class))),
                    @ApiResponse(responseCode = "400", description = "Readings already exist for a meter", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Not found, either profile or meter are not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public ResponseEntity<ReadingsDto> postReadings(@PathVariable Long profileId,
                                                    @PathVariable Long meterId,
                                                    @RequestBody @Valid ReadingsDto readingsDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertToDto(readingService.createReadings(profileId, meterId, readingsDto)));
    }

    @Operation(
            summary = "Updates readings",
            operationId = "putReadings",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The action was successful", content = @Content(schema = @Schema(implementation = ReadingsDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not found, either profile, meter or readings are not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @PutMapping
    public ResponseEntity<ReadingsDto> putReadings(@PathVariable Long profileId,
                                                   @PathVariable Long meterId,
                                                   @RequestBody @Valid ReadingsDto readingsDto) {
        return ResponseEntity.ok()
                .body(convertToDto(readingService.updateReadings(profileId, meterId, readingsDto)));
    }


    @Operation(
            summary = "Deletes readings",
            operationId = "deleteReading",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The action was successful"),
                    @ApiResponse(responseCode = "404", description = "Not found, either profile, meter or readings are not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @DeleteMapping
    public ResponseEntity<Void> deleteReadings(@PathVariable Long profileId,
                                               @PathVariable Long meterId) {
        readingService.deleteReadings(profileId, meterId);
        return ResponseEntity.ok().build();
    }
}
