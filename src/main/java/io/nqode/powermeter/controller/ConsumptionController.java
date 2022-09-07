package io.nqode.powermeter.controller;

import io.nqode.powermeter.dto.ConsumptionDto;
import io.nqode.powermeter.service.MeterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("profiles/{profileId}/meters/{meterId}/consumption")
@RequiredArgsConstructor
public class ConsumptionController {

    private final MeterService meterService;

    @Operation(
            summary = "Serves consumption per meter",
            operationId = "findConsumption",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The action was successful", content = @Content(schema = @Schema(implementation = ConsumptionDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not found e.g. the meter with provided ID is not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping()
    public ResponseEntity<ConsumptionDto> getConsumptionForMonth(@PathVariable Long profileId,
                                                                 @PathVariable Long meterId,
                                                                 @RequestParam String month) {
        return ResponseEntity.ok(meterService.getMeterConsumptionFomMonth(profileId, meterId, month));
    }
}
