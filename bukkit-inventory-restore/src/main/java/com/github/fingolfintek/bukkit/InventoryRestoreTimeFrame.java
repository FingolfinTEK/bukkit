package com.github.fingolfintek.bukkit;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InventoryRestoreTimeFrame {

    private static final Pattern TIME_PATTERN = Pattern.compile("-t(%d+)(d|h|m)");

    private static final int TIME_QUANTITY_GROUP = 1;
    private static final int TIME_UNIT_GROUP = 2;

    private final Date lowerBound;
    private final Date upperBound;

    public InventoryRestoreTimeFrame(String timeFrame) {
        Calendar calendar = getRequestedTimeFrameLowerBound(timeFrame);
        lowerBound = calendar.getTime();

        calendar.add(Calendar.MINUTE, 5);
        upperBound = calendar.getTime();
    }


    private Calendar getRequestedTimeFrameLowerBound(String timeFrame) {
        Calendar calendar = Calendar.getInstance();

        Matcher matcher = TIME_PATTERN.matcher(timeFrame);
        if (matcher.matches()) {
            Integer timeQuantity = Integer.parseInt(matcher.group(InventoryRestoreTimeFrame.TIME_QUANTITY_GROUP));
            String timeUnit = matcher.group(InventoryRestoreTimeFrame.TIME_UNIT_GROUP);

            calendar.add(InventoryRestoreTimeFrame.TimeUnit.getCalendarField(timeUnit), -timeQuantity);
        }

        return calendar;
    }

    public Date getLowerBound() {
        return lowerBound;
    }

    public Date getUpperBound() {
        return upperBound;
    }

    public static boolean matches(String arg) {
        return TIME_PATTERN.matcher(arg).matches();
    }

    private static enum TimeUnit {
        DAYS(Calendar.DAY_OF_YEAR), HOURS(Calendar.HOUR), MINUTES(Calendar.MINUTE);

        private final int type;

        TimeUnit(int type) {
            this.type = type;
        }

        public static TimeUnit fromString(String timeUnit) {
            for (TimeUnit unit : values()) {
                if (unit.name().startsWith(timeUnit))
                    return unit;
            }
            return null;
        }

        public static int getCalendarField(String timeUnit) {
            return TimeUnit.fromString(timeUnit).type;
        }
    }
}
