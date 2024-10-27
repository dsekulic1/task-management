package com.example.taskmanagement.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateUtils {
    private DateUtils() {}

    public static String formatZonedDate(String dueDate) {
        try {
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
            return ZonedDateTime.parse(dueDate).format(displayFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for dueDate: " + dueDate);
        }
    }

    public static Date getDateFromStringDate(String dueDateString) {
        try {
            ZonedDateTime dueDate = ZonedDateTime.parse(dueDateString);
            return Date.from(dueDate.toInstant());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for dueDate: " + dueDateString);
        }
    }
}
