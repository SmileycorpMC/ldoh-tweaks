package net.smileycorp.ldoh.common;

import java.time.LocalDateTime;
import java.time.Month;

public class TimedEvents {

    public static boolean isHalloween() {
        final LocalDateTime time = LocalDateTime.now();
        return time.getMonth() == Month.OCTOBER;
    }

    public static boolean isChristmas() {
        final LocalDateTime time = LocalDateTime.now();
        return time.getMonth() == Month.DECEMBER || (time.getMonth() == Month.JANUARY && time.getDayOfMonth() <= 2);
    }

}
