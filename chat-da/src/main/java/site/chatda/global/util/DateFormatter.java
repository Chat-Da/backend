package site.chatda.global.util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.Locale.KOREAN;

public class DateFormatter {

    public static String convertToString(LocalDateTime date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return date.format(formatter);
    }

    public static String convertToKoreanFormat(LocalDateTime date) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일", KOREAN);
        String formattedDate = date.format(dateFormatter);
        String dayOfWeekKorean = getKoreanDayOfWeek(date.getDayOfWeek());

        return formattedDate + " (" + dayOfWeekKorean + ")";
    }

    private static String getKoreanDayOfWeek(DayOfWeek dayOfWeek) {

        return switch (dayOfWeek) {
            case MONDAY -> "월";
            case TUESDAY -> "화";
            case WEDNESDAY -> "수";
            case THURSDAY -> "목";
            case FRIDAY -> "금";
            case SATURDAY -> "토";
            case SUNDAY -> "일";
        };
    }
}