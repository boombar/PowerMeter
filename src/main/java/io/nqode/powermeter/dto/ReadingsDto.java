package io.nqode.powermeter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReadingsDto {

    @NotNull
    private Integer januaryReading;

    @NotNull
    private Integer februaryReading;

    @NotNull
    private Integer marchReading;

    @NotNull
    private Integer aprilReading;

    @NotNull
    private Integer mayReading;

    @NotNull
    private Integer juneReading;

    @NotNull
    private Integer julyReading;

    @NotNull
    private Integer augustReading;

    @NotNull
    private Integer septemberReading;

    @NotNull
    private Integer octoberReading;

    @NotNull
    private Integer novemberReading;

    @NotNull
    private Integer decemberReading;

}
