package io.nqode.powermeter.controller;

import io.nqode.powermeter.converter.MeterConverter;
import io.nqode.powermeter.dto.MeterDto;
import io.nqode.powermeter.service.MeterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static io.nqode.powermeter.converter.MeterConverter.convertToDto;

@RestController
@RequestMapping("profiles/{profileId}/meters")
@RequiredArgsConstructor
public class MeterController {

    private final MeterService meterService;

    @Operation(
            summary = "Serves meters for profile",
            operationId = "getMeters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The action was successful", content = @Content(schema = @Schema(implementation = List.class))),
                    @ApiResponse(responseCode = "404", description = "Not found e.g. the profile with provided ID is not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping
    public ResponseEntity<List<MeterDto>> getMeters(@PathVariable Long profileId) {
        return ResponseEntity.ok(meterService.getAllByProfileId(profileId)
                .stream()
                .map(MeterConverter::convertToDto)
                .toList());
    }

    @Operation(
            summary = "Serves a meter for profile",
            operationId = "getMeter",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The action was successful", content = @Content(schema = @Schema(implementation = MeterDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not found e.g. the profile with provided ID is not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<MeterDto> getMeter(@PathVariable Long profileId,
                                             @PathVariable Long id) {
        return ResponseEntity.ok(convertToDto(meterService.getByProfileIdAndId(profileId, id)));
    }

    @Operation(
            summary = "Creates a meter for profile",
            operationId = "postMeter",
            responses = {
                    @ApiResponse(responseCode = "201", description = "The action was successful", content = @Content(schema = @Schema(implementation = MeterDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not found e.g. the profile with provided ID is not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public ResponseEntity<MeterDto> postMeter(@PathVariable Long profileId,
                                              @RequestBody @Valid MeterDto meterDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertToDto(meterService.createMeterForProfile(profileId, meterDto)));
    }

    @Operation(
            summary = "Updates the meter for profile",
            operationId = "putMeter",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The action was successful", content = @Content(schema = @Schema(implementation = MeterDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not found e.g. the profile with provided ID is not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<MeterDto> putMeter(@PathVariable Long profileId,
                                             @PathVariable Long id,
                                             @RequestBody @Valid MeterDto meterDto) {
        return ResponseEntity.ok(convertToDto(meterService.updateMeter(profileId, id, meterDto)));
    }

    @Operation(
            summary = "Deletes the meter for profile",
            operationId = "deleteMeter",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The action was successful", content = @Content(schema = @Schema(implementation = MeterDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not found e.g. the profile with provided ID is not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeter(@PathVariable Long profileId,
                                            @PathVariable Long id) {
        meterService.deleteMeter(profileId, id);
        return ResponseEntity.ok().build();
    }
}
