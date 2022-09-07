package io.nqode.powermeter.util;

import io.nqode.powermeter.exception.ValidationException;

import java.time.DateTimeException;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class DateUtil {

    private DateUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final String MONTH_PATTERN = "MMM";

    public static final DateTimeFormatter MONTH_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern(MONTH_PATTERN)
            .toFormatter();

    public static Month getMonth(String monthCode) {
        try {
            return Month.from(MONTH_FORMATTER.parse(monthCode));
        } catch (DateTimeException exception) {
            throw new ValidationException("Invalid month format: %s".formatted(monthCode));
        }

    }
}
