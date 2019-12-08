package com.android.library.util;

import android.util.Xml;

import com.android.library.entity.UpdateAppInfo;
import com.android.library.enums.AppUpdateInfoEnum;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author:  ljo_h
 * Date:    2016/6/7
 * Description:
 */
public class UpdateAppUtils {
    public static UpdateAppInfo getUpdateAppInfo(String path){
        InputStream is = null;
        UpdateAppInfo info = null;
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // 从服务器获得一个输入流
                is = conn.getInputStream();
            }
            info = getUpdataInfo(is);
        } catch (Exception e) {
            LogUtils.e("UpdateAppUtils", e.getMessage());
        }
        return info;
    }

    public static UpdateAppInfo getUpdataInfo(InputStream is) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "utf-8");
        int type = parser.getEventType();
        UpdateAppInfo info = new UpdateAppInfo();
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                case XmlPullParser.START_TAG:
                    if (AppUpdateInfoEnum.VERSIONCODE.getValue().equals(parser.getName())) {
                        info.setVersionCode(parser.nextText());
                    } else if (AppUpdateInfoEnum.VERSIONNAME.getValue().equals(parser.getName())) {
                        info.setVersionName(parser.nextText());
                    } else if (AppUpdateInfoEnum.APKNAME.getValue().equals(parser.getName())) {
                        info.setApkName(parser.nextText());
                    } else if (AppUpdateInfoEnum.APKSIZE.getValue().equals(parser.getName())) {
                        info.setApkSize(parser.nextText());
                    } else if (AppUpdateInfoEnum.UPDATEDATE.getValue().equals(parser.getName())) {
                        info.setUpdateDate(parser.nextText());
                    } else if (AppUpdateInfoEnum.APKURL.getValue().equals(parser.getName())) {
                        info.setApkUrl(parser.nextText());
                    } else if (AppUpdateInfoEnum.UPDATECONTENT.getValue().equals(parser.getName())) {
                        info.setUpdateContent(parser.nextText());
                    }
                    break;
            }
            type = parser.next();
        }
        return info;
    }
}
