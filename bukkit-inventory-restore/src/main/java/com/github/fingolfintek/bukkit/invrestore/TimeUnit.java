package com.github.fingolfintek.bukkit.invrestore;

import java.util.Calendar;

public enum TimeUnit {
    DAYS(Calendar.DAY_OF_YEAR), HOURS(Calendar.HOUR), MINUTES(Calendar.MINUTE);

    private final int type;

    TimeUnit(int type) {
        this.type = type;
    }

    public static TimeUnit fromString(String timeUnit) {
        for (TimeUnit unit : values()) {
            if (unit.name().startsWith(timeUnit.toUpperCase()))
                return unit;
        }
        return null;
    }

    public static int getCalendarField(String timeUnit) {
        return TimeUnit.fromString(timeUnit).type;
    }
}
