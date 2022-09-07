package io.nqode.powermeter.controller;


import io.nqode.powermeter.dto.FractionsDto;
import io.nqode.powermeter.service.FractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static io.nqode.powermeter.converter.FractionConverter.convertToDto;

@RestController
@RequestMapping("/profiles/{profileId}/fractions")
@RequiredArgsConstructor
public class FractionController {

    private final FractionService fractionService;

    @Operation(
            summary = "Serves fractions by profile",
            operationId = "findFractions",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The action was successful", content = @Content(schema = @Schema(implementation = FractionsDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not found e.g. the profile with provided ID is not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping
    public ResponseEntity<FractionsDto> getFractionsByProfile(@PathVariable Long profileId) {
        return ResponseEntity.ok(convertToDto(fractionService.getAllByProfileId(profileId)));
    }

    @Operation(
            summary = "Creates fractions for profile",
            operationId = "createFractions",
            responses = {
                    @ApiResponse(responseCode = "201", description = "The action was successful", content = @Content(schema = @Schema(implementation = FractionsDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not found e.g. the profile with provided ID is not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public ResponseEntity<FractionsDto> postFractionsForProfile(@PathVariable Long profileId,
                                                                @RequestBody @Valid FractionsDto fractionDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertToDto(fractionService.createFractions(profileId, fractionDto)));
    }


    @Operation(
            summary = "Updates fractions for profile",
            operationId = "updateFractions",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The action was successful", content = @Content(schema = @Schema(implementation = FractionsDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not found e.g. the profile with provided ID is not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @PutMapping
    public ResponseEntity<FractionsDto> putFractionsForProfile(@PathVariable Long profileId,
                                                               @RequestBody @Valid FractionsDto fractionDto) {
        return ResponseEntity.ok(convertToDto(fractionService.updateFractions(profileId, fractionDto)));
    }

    @Operation(
            summary = "Delete fractions for profile",
            operationId = "deleteFractions",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The action was successful"),
                    @ApiResponse(responseCode = "404", description = "Not found e.g. the profile with provided ID is not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @DeleteMapping
    ResponseEntity<Void> deleteFractionsForProfile(@PathVariable Long profileId) {
        fractionService.deleteFractions(profileId);
        return ResponseEntity.ok().build();
    }
}
