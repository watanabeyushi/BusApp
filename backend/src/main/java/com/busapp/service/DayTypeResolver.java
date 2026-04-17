package com.busapp.service;

import com.busapp.entity.DayType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class DayTypeResolver {

    private DayTypeResolver() {
    }

    /** Day types that apply to the given calendar date (includes {@link DayType#ALL}). */
    public static List<DayType> applicableDayTypes(LocalDate date) {
        List<DayType> list = new ArrayList<>();
        list.add(DayType.ALL);
        switch (date.getDayOfWeek()) {
            case SATURDAY -> list.add(DayType.SATURDAY);
            case SUNDAY -> list.add(DayType.SUNDAY);
            default -> list.add(DayType.WEEKDAY);
        }
        return list;
    }
}
