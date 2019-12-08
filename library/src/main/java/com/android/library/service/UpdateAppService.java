package com.android.library.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.android.library.util.NotifyUtils;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UpdateAppService extends IntentService {

    private static final String SMALLICON = "com.android.library.service.smallicon";
    private static final String URL = "com.android.library.service.url";
    private static final String SAVEPATH = "com.android.library.service.savepath";
    private static final String SAVENAME = "com.android.library.service.savename";
    private static final String APKNAME = "com.android.library.service.apkname";

    NotifyUtils notify7;
    int smallIcon;
    int iProgress = 0;
    String url;
    String Savepath;
    String SaveName;
    String ApkName;

    public UpdateAppService() {
        super("UpdateAppService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notify7 = new NotifyUtils(getApplicationContext(), 7);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            smallIcon = intent.getIntExtra(SMALLICON, 0);
            url = intent.getStringExtra(URL);
            Savepath = intent.getStringExtra(SAVEPATH);
            SaveName = intent.getStringExtra(SAVENAME);
            ApkName = intent.getStringExtra(APKNAME);
        }
        /*OkHttpUtils.get().url(url)
                .build()
                .execute(new FileCallBack(Savepath, SaveName) {

                    @Override
                    public void inProgress(float progress, long total , int id) {
                        if(iProgress == 0) {
                            notify7.notify_normal_singline(null, smallIcon, "开始下载", ApkName, "正在下载中", false, false, false);
                        }
                        if(progress * 100 > iProgress){
                            iProgress += 1;

                            notify7.getcBuilder().setContentText("下载中..." + iProgress + "%");
                            notify7.sent();
                        }
                        if(progress == 1){
                            // 进度满了后，设置提示信息
                            notify7.getcBuilder().setContentText("下载完成");
                            notify7.sent();

                            // 安装应用
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setDataAndType(Uri.parse("file://" + Savepath + "/" + SaveName),"application/vnd.android.package-archive");
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(File response, int id) {

                    }
        });*/

    }

    public static void StartService(Context context, int smallIcon, String url, String savePath, String saveName, String ApkName) {
        Intent intent = new Intent(context, UpdateAppService.class);
        intent.putExtra(SMALLICON, smallIcon);
        intent.putExtra(URL, url);
        intent.putExtra(SAVEPATH, savePath);
        intent.putExtra(SAVENAME, saveName);
        intent.putExtra(APKNAME, ApkName);
        context.startService(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //OkHttpUtils.getInstance().cancelTag(this);
    }

}
