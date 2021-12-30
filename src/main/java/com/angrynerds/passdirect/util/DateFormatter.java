package com.angrynerds.passdirect.util;

import java.text.*;
import java.util.*;

public class DateFormatter {
    private final static String format = "dd.MM.yy HH:mm";

    public static String format(Date date) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }
}