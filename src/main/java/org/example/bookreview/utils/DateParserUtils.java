package org.example.bookreview.utils;

import java.time.DateTimeException;
import java.time.LocalDate;

public class DateParserUtils {

    private DateParserUtils() {
    }

    public static LocalDate parsePubDate(String pubdate) {
        if (pubdate == null || pubdate.length() != 8) {
            return null;
        }

        try {
            int year = Integer.parseInt(pubdate.substring(0, 4));
            int month = Integer.parseInt(pubdate.substring(4, 6));
            int day = Integer.parseInt(pubdate.substring(6, 8));

            return LocalDate.of(year, month, day);
        } catch (NumberFormatException | DateTimeException e) {
            return null;
        }
    }
}