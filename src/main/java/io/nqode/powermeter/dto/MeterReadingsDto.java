package io.nqode.powermeter.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MeterReadingsDto extends MeterDto implements UploadResponseDto {

    private ReadingsDto readingsDto;

}
