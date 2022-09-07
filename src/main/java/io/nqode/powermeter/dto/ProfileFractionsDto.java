package io.nqode.powermeter.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ProfileFractionsDto extends ProfileDto implements UploadResponseDto {

    private FractionsDto fractionsDto;

}
