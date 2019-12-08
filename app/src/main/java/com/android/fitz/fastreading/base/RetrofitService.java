package com.android.fitz.fastreading.base;

import com.android.fitz.fastreading.bean.CommonResult;
import com.android.fitz.fastreading.bean.DownLoadResult;
import com.android.fitz.fastreading.bean.UploadResult;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Author:  ljo_h
 * Date:    2016/8/10
 * Description:
 */
public interface RetrofitService {

    // 绑定code
    @GET("http://dqrx.hzshenlan.com/fastread/api.ashx/")
    Flowable<CommonResult> checkCode(@Query("act") String act, @Query("mac") String mac, @Query("vercode") String vercode);

    // 更新
    @GET("http://dqrx.hzshenlan.com/fastread/api.ashx/")
    Flowable<DownLoadResult> getCode(@Query("act") String act, @Query("vercode") String vercode, @Query("mod") String mod, @Query("lasttime") String lasttime);

    // 文件上传（测试）
    @Multipart
    @POST("http://www.lintuotuoguan.com/LinTuoJGWeb/Login/Upload")
    Flowable<UploadResult> uploadFile(@Part MultipartBody.Part file);

//    @FormUrlEncoded
//    @POST("/")
//    Flowable<DownLoadResult> getCode(@Field("act") String act, @Field("vercode") String vercode, @Field("mod") String mod);

    // 登录
    /*@FormUrlEncoded
    @POST("/user/login")
    Flowable<UserLoginResult> Login(@Field("mobile") String mobile, @Field("verifyCode") String verifyCode);*/

}
