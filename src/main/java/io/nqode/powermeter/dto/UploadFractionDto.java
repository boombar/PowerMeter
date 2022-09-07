package io.nqode.powermeter.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UploadFractionDto {

    private String profile;

    private String month;

    private BigDecimal fraction;

}
