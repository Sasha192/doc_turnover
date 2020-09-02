package app.utils;

import app.configuration.spring.constants.Constants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * It was created for customization of App DateTime and storing it correctly;
 * Kinda Like Single Responsibility
 */
public class CustomAppDateTimeUtil {

    private static final SimpleDateFormat FORMATTER = Constants.DATE_FORMAT;

    public static Date now() {
        return java.sql.Date.valueOf(LocalDateTime.now().toLocalDate());
    }

    public static Date parse(String date)
            throws ParseException {
        return new java.sql.Date(FORMATTER.parse(date).getTime());
    }
}
