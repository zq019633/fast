package com.android.library.enums;

/**
 * 字号枚举
 * 
 * @author li jing huan
 * 
 */
public enum AppUpdateInfoEnum {
	VERSIONCODE("VersionCode", "版本号CODE"), VERSIONNAME("VersionName", "版本号NAME"), APKNAME("ApkName", "APK名称")
	, APKSIZE("ApkSize", "APK大小"), UPDATEDATE("UpdateDate", "更新时间"), APKURL("ApkUrl", "更新地址"), UPDATECONTENT("UpdateContent", "更新内容");

	// 枚举值
	private String value = "";
	// 描述值
	private String des = "";

	AppUpdateInfoEnum(String value, String des) {
		this.value = value;
		this.des = des;
	}

	// 获取值
	public String getValue() {
		return this.value;
	}

	// 设置值
	public void setValue(String value) {
		this.value = value;
	}

	// 获取描述
	public String getDes() {
		return this.des;
	}

	// 设置描述
	public void setDes(String des) {
		this.des = des;
	}

	// 根据枚举值获取对应的枚举
	public static final AppUpdateInfoEnum getEnumByValue(int value) {
		AppUpdateInfoEnum currEnum = null;
		for (AppUpdateInfoEnum e : AppUpdateInfoEnum.values()) {
			if (e.getValue().equals(value)) {
				currEnum = e;
				break;
			}
		}
		return currEnum;
	}
}