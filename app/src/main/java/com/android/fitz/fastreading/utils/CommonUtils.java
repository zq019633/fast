package com.android.fitz.fastreading.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by fitz on 2017/07/25.
 */

public class CommonUtils {
    // (min,max]
    public static int getRandom(int min, int max){
        int iResult = 1;
        for(int i=0; i<Integer.MAX_VALUE; i++) {
            iResult = new Random().nextInt(max);
            if(iResult > min){
                break;
            }
        }
        return iResult;
    }

    // (min,max] 不包含ignore
    public static int getRandom(int min, int max, int ignore){
        int iResult = 1;
        for(int i=0; i<Integer.MAX_VALUE; i++) {
            iResult = new Random().nextInt(max);
            if (iResult > min && iResult != ignore) {
                break;
            }
        }
        return iResult;
    }

    /**
     * 获取课堂训练的速度
     * @param type
     * @return
     */
    public static List<String> getSpeedList(String type){
        List<String> list = new ArrayList<>();
        switch (type){
            case "01":
                list = getSpeed(200, 1000);
                break;
            case "02":
                list = getSpeed(1000, 2000);
                break;
            case "03":
                list = getSpeed(2000, 3000);
                break;
            case "04":
                list = getSpeed(3000, 4000);
                break;
            case "05":
                list = getSpeed(4000, 5000);
                break;
            case "06":
                list = getSpeed(5000, 6500);
                break;
            case "07":
                list = getSpeed(6500, 8000);
                break;
            case "08":
                list = getSpeed(8000, 9500);
                break;
            case "09":
                list = getSpeed(9500, 11000);
                break;
            case "10":
                list = getSpeed(11000, 12500);
                break;
            case "11":
                list = getSpeed(12500, 14000);
                break;
            case "12":
                list = getSpeed(14000, 15000);
                break;
            case "13":
                list = getSpeed(15000, 16000);
                break;
            case "14":
                list = getSpeed(16000, 17000);
                break;
        }
        return list;
    }

    /**
     * 获取速度区间
     * @param min
     * @param max
     * @return
     */
    public static List<String> getSpeed(int min, int max){
        String speed = String.valueOf(min);
        List<String> list = new ArrayList<>();
        list.add(speed);

        int sp = 200;
        for(int i=0; i<Integer.MAX_VALUE; i++) {
            sp = min + 200 * (i + 1);
            if(sp < max){
                list.add(String.valueOf(sp));
            } else{
                list.add(String.valueOf(max));
                break;
            }
        }

        return list;
    }
}
