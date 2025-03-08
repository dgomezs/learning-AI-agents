package com.example.ecommerce.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for date and time operations.
 */
public final class DateTimeUtils {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    private DateTimeUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Formats a LocalDateTime object to string using the default format.
     *
     * @param dateTime the LocalDateTime to format
     * @return formatted date time string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT).format(dateTime);
    }

    /**
     * Formats a LocalDate object to string using the default format.
     *
     * @param date the LocalDate to format
     * @return formatted date string
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT).format(date);
    }

    /**
     * Converts an Instant to LocalDateTime using the system default time zone.
     *
     * @param instant the Instant to convert
     * @return LocalDateTime representation of the instant
     */
    public static LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, DEFAULT_ZONE_ID);
    }

    /**
     * Converts a LocalDateTime to Instant using the system default time zone.
     *
     * @param localDateTime the LocalDateTime to convert
     * @return Instant representation of the local date time
     */
    public static Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atZone(DEFAULT_ZONE_ID).toInstant();
    }
}
