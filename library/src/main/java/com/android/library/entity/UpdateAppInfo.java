package com.android.library.entity;

/**
 * Created by ljo_h on 2016/6/2.
 */
public class UpdateAppInfo {

    public String VersionCode;
    public String VersionName;
    public String ApkName;
    public String ApkSize;
    public String UpdateDate;
    public String ApkUrl;
    public String UpdateContent;

    public String getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(String versionCode) {
        VersionCode = versionCode;
    }

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String versionName) {
        VersionName = versionName;
    }

    public String getApkName() {
        return ApkName;
    }

    public void setApkName(String apkName) {
        ApkName = apkName;
    }

    public String getApkSize() {
        return ApkSize;
    }

    public void setApkSize(String apkSize) {
        ApkSize = apkSize;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public String getApkUrl() {
        return ApkUrl;
    }

    public void setApkUrl(String apkUrl) {
        ApkUrl = apkUrl;
    }

    public String getUpdateContent() {
        return UpdateContent;
    }

    public void setUpdateContent(String updateContent) {
        UpdateContent = updateContent;
    }
}
