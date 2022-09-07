package io.nqode.powermeter.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class UploadReadingDto {

    @CsvBindByName(column = "meterID")
    private String meterIdentifier;

    @CsvBindByName
    private String profile;

    @CsvBindByName
    private String month;

    @CsvBindByName
    private Integer meterReading;

}
