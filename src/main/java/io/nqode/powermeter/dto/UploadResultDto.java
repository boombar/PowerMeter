package io.nqode.powermeter.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UploadResultDto {

    List<UploadResponseDto> uploaded;

    List<String> errors;

}
