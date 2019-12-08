package com.android.library.util;

import java.math.BigDecimal;

/**
 * Created by fitz on 2017/1/10.
 */

public class ConvertUtils {
    public static BigDecimal toDecimal(Object value) {
        if (StringUtils.isEquals(ObjectUtils.nullStrToEmpty(value), "")) {
            return BigDecimal.ZERO;
        } else {
            try {
                BigDecimal bd = new BigDecimal(ObjectUtils.nullStrToEmpty(value));
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                return bd;
            } catch (Exception ex) {
                return BigDecimal.ZERO;
            }
        }
    }

    // toInt
    public static int toInt(Object value) {
        int cint = 0;
        try {
            cint = Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return cint;
    }
}
