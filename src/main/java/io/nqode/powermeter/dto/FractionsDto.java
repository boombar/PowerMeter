package io.nqode.powermeter.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FractionsDto {

    @NotNull
    private BigDecimal januaryFraction;

    @NotNull
    private BigDecimal februaryFraction;

    @NotNull
    private BigDecimal marchFraction;

    @NotNull
    private BigDecimal aprilFraction;

    @NotNull
    private BigDecimal mayFraction;

    @NotNull
    private BigDecimal juneFraction;

    @NotNull
    private BigDecimal julyFraction;

    @NotNull
    private BigDecimal augustFraction;

    @NotNull
    private BigDecimal septemberFraction;

    @NotNull
    private BigDecimal octoberFraction;

    @NotNull
    private BigDecimal novemberFraction;

    @NotNull
    private BigDecimal decemberFraction;

}
