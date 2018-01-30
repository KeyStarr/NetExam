package kstarostin.thumbtack.net.netexam.ui.utils;

/**
 * Created by Bizarre on 13.05.2017.
 */

public class TimeBoardFormatUtils {
    
    private static String formatTimeUnit(long time){
        String timeString = String.valueOf(time);
        timeString = timeString.length() == 1 ? '0' + timeString : timeString;
        return timeString;
    }

    public static String formatToTimeBoard(long time){
        time/=1000;
        String secs = formatTimeUnit(time%60);
        time/=60;
        String mins = formatTimeUnit(time%60);
        time/=60;
        String hours = formatTimeUnit(time%24);
        return String.format("%s:%s:%s", hours, mins, secs);
    }
    
}
