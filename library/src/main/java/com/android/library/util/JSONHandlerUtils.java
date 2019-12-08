package com.android.library.util;

import android.content.ContentProviderOperation;
import android.content.Context;

import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Author:  jact
 * Date:    2016/3/24
 * Description:
 */
public abstract class JSONHandlerUtils {
    protected static Context mContext;

    public JSONHandlerUtils(Context context) {
        mContext = context;
    }

    public abstract void makeContentProviderOperations(ArrayList<ContentProviderOperation> list);

    public abstract void process(JsonElement element);

    // 读取资源文件
    public static String parseResource(Context context, int resource) throws IOException {
        InputStream is = context.getResources().openRawResource(resource);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            //Reader reader = new BufferedReader(new InputStreamReader(is, Charsets.UTF_8));
            Reader reader = new BufferedReader(new InputStreamReader(is));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        return writer.toString();
    }

    /**
     * 功能：Java读取txt文件的内容
     * 步骤：1：先获得文件句柄
     * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流
     * 4：一行一行的输出。readline()。
     * 备注：需要考虑的是异常情况
     * @param filePath
     */
    public static String readTxtFile(String filePath) {
        //String result = "";
        StringBuffer sb = new StringBuffer();
        try {
            File file = new File(filePath);
            if(file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String readline = "";

                while ((readline = br.readLine()) != null) {
                    sb.append(readline);
                }
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
