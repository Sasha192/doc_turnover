package app.utils;

import app.configuration.spring.constants.Constants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.Date;

public class CustomAppDateTimeUtil {

    private static final SimpleDateFormat FORMATTER = Constants.DATE_FORMAT;

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            Constants.DATE_TIME_FORMATTER;

    public static Date now() {
        return java.sql.Date.valueOf(LocalDateTime.now().toLocalDate());
    }

    public static Date nowMinus(long amount, TemporalUnit unit) {
        return java.sql.Date.valueOf(
                LocalDateTime.now().minus(amount, unit).toLocalDate()
        );
    }

    public static Date parseMinus(String date, long amount, TemporalUnit unit) {
        return java.sql.Date.valueOf(
                LocalDateTime.parse(date, DATE_TIME_FORMATTER)
                        .minus(amount, unit).toLocalDate()
        );
    }

    public static Date nowPlus(long amount, TemporalUnit unit) {
        return java.sql.Date.valueOf(
                LocalDateTime.now().plus(amount, unit).toLocalDate()
        );
    }

    public static Date parsePlus(String date, long amount, TemporalUnit unit) {
        return java.sql.Date.valueOf(
                LocalDateTime.parse(date, DATE_TIME_FORMATTER)
                        .plus(amount, unit).toLocalDate()
        );
    }

    public static Date parse(String date)
            throws ParseException {
        return new java.sql.Date(FORMATTER.parse(date).getTime());
    }
}
