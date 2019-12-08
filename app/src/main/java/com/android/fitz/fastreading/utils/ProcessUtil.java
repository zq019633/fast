package com.android.fitz.fastreading.utils;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/**
 * Created by Dream.F on 27/07/2017.
 */
public class ProcessUtil {
    /**
     * get proceesName by processId
     * @param pid
     * @return
     */
    public static String getProcessName(int pid){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/prc/"+pid+"/cmdline"));
            String processName = reader.readLine();
            if(!TextUtils.isEmpty(processName)){
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
