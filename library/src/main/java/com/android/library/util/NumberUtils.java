package com.android.library.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * List Utils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2011-7-22
 */
public class NumberUtils {

    /** default join separator **/
    public static final String DEFAULT_JOIN_SEPARATOR = ",";

    private NumberUtils() {
        throw new AssertionError();
    }

   public static boolean isDecimal(Object value) {
       if (StringUtils.isEquals(ObjectUtils.nullStrToEmpty(value), "")) {
           return false;
       } else {
           try {
               BigDecimal bd = new BigDecimal(ObjectUtils.nullStrToEmpty(value));
               return true;
           } catch (Exception ex) {
               return false;
           }
       }
   }
}
