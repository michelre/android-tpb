package com.torrenttotransmission.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by remimichel on 06/09/14.
 */
public class HumanReadableByteCount {

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String formatFloat(float number){
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        return decimalFormat.format(number);
    }
}
