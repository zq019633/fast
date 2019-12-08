package com.android.fitz.fastreading.utils;

import android.content.Context;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Dream.F on 16-11-4.
 */

public class ConfigPropertiesUtil {
    private static Properties properties;
    public static Properties getProperties(Context context){
        Properties props = new Properties();
        try {
            InputStream inputStream = context.getAssets().open("config.properties");
            props.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        properties = props;
        return properties;
    }
}
