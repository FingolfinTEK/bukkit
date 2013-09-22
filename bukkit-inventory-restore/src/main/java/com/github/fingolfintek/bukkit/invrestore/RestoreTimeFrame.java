package com.github.fingolfintek.bukkit.invrestore;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestoreTimeFrame {

    private static final Pattern TIME_PATTERN = Pattern.compile("-t(\\d+)(d|h|m)");

    private static final int TIME_QUANTITY_GROUP = 1;
    private static final int TIME_UNIT_GROUP = 2;

    private final Date lowerBound;
    private final Date upperBound;

    public RestoreTimeFrame(String timeFrame) {
        this(calculateLowerBound(timeFrame).getTime(), new Date());
    }

    public RestoreTimeFrame(Date lowerBound, Date upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    private static Calendar calculateLowerBound(String timeFrame) {
        Matcher matcher = TIME_PATTERN.matcher(timeFrame);

        if (!matcher.matches())
            throw new IllegalArgumentException("Not a valid time frame " + timeFrame);

        Calendar calendar = Calendar.getInstance();
        Integer timeQuantity = Integer.parseInt(matcher.group(RestoreTimeFrame.TIME_QUANTITY_GROUP));
        String timeUnit = matcher.group(RestoreTimeFrame.TIME_UNIT_GROUP);
        calendar.add(TimeUnit.getCalendarField(timeUnit), -timeQuantity);

        return calendar;
    }

    public Date getLowerBound() {
        return lowerBound;
    }

    public Date getUpperBound() {
        return upperBound;
    }

    @Override
    public String toString() {
        return lowerBound + " - " + upperBound;
    }

    public static boolean matches(String arg) {
        return TIME_PATTERN.matcher(arg).matches();
    }

}
