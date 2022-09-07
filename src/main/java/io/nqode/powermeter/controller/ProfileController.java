package io.nqode.powermeter.controller;

import io.nqode.powermeter.converter.ProfileConverter;
import io.nqode.powermeter.dto.ProfileDto;
import io.nqode.powermeter.service.ProfileService;
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

import static io.nqode.powermeter.converter.ProfileConverter.convertToDto;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @Operation(
            summary = "Serves all profiles",
            operationId = "findProfiles",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The action was successful", content = @Content(schema = @Schema(implementation = List.class))),
                    @ApiResponse(responseCode = "404", description = "Not found e.g. the profile with provided ID is not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping
    public ResponseEntity<List<ProfileDto>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAll()
                .stream()
                .map(ProfileConverter::convertToDto)
                .toList()
        );
    }

    @Operation(
            summary = "Serves profile by id",
            operationId = "findProfile",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The action was successful", content = @Content(schema = @Schema(implementation = ProfileDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not found e.g. the profile with provided ID is not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(convertToDto(profileService.getById(id)));
    }

    @Operation(
            summary = "Creates profile",
            operationId = "createProfile",
            responses = {
                    @ApiResponse(responseCode = "201", description = "The action was successful", content = @Content(schema = @Schema(implementation = ProfileDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not found e.g. the profile with provided ID is not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public ResponseEntity<ProfileDto> createProfile(@RequestBody @Valid ProfileDto profileDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertToDto(profileService.createProfile(profileDto)));
    }

    @Operation(
            summary = "Updates profile",
            operationId = "updateProfile",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The action was successful", content = @Content(schema = @Schema(implementation = ProfileDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not found e.g. the profile with provided ID is not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProfileDto> putProfile(@PathVariable Long id,
                                                    @RequestBody @Valid ProfileDto profileDto) {
        return ResponseEntity.ok(convertToDto(profileService.updateProfile(id, profileDto)));
    }

    @Operation(
            summary = "Deletes profile",
            operationId = "deleteProfile",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The action was successful"),
                    @ApiResponse(responseCode = "404", description = "Not found e.g. the profile with provided ID is not found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
