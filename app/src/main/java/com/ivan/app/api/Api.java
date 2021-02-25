package com.ivan.app.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.ivan.app.activity.LoginActivity;
import com.ivan.app.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * 封装发送请求的api
 */
public class Api {
    //http请求客户端
    private static OkHttpClient client;
    //请求地址
    private static String requestUrl;
    //请求参数
    private static HashMap<String, Object> mParams;
    //请求对象
    public static Api api = new Api();

    /**
     * 无参构造方法
     */
    public Api() {}

    /**
     * 初始化请求地址、请求参数
     * @param url
     * @param params
     * @return
     */
    public static Api config(String url, HashMap<String, Object> params) {
        client = new OkHttpClient.Builder().build();
        requestUrl = ApiConfig.BASE_URl + url;
        mParams = params;
        return api;
    }

    /**
     * 封装post请求
     * @param context
     * @param callback
     */
    public void postRequest(Context context, final IvanCallback callback) {
        //第一步获取本地存储的token
        SharedPreferences sp = context.getSharedPreferences("sp_ivan", MODE_PRIVATE);
        String token = sp.getString("token", "");

        //第二步封装请求参数
        JSONObject jsonObject = new JSONObject(mParams);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson =
                RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        //第三步创建Request请求
        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("contentType", "application/json;charset=UTF-8")
                .addHeader("token", token)
                .post(requestBodyJson)
                .build();

        //第四步创建call回调对象
        final Call call = client.newCall(request);

        //第五步发起请求
        call.enqueue(new Callback() {
            //请求失败
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", e.getMessage());
                callback.onFailure(e);
            }

            //请求成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 封装get请求
     * @param context
     * @param callback
     */
    public void getRequest(Context context, final IvanCallback callback) {
        //第一步获取本地存储的token
        SharedPreferences sp = context.getSharedPreferences("sp_ivan", MODE_PRIVATE);
        String token = sp.getString("token", "");

        //第二部拼接get请求url地址
        String url = getAppendUrl(requestUrl, mParams);

        //第三步创建Request请求
        Request request = new Request.Builder()
                .url(url)
                .addHeader("token", token)
                .get()
                .build();

        //第四步创建call回调对象
        Call call = client.newCall(request);

        //第五步发起请求
        call.enqueue(new Callback() {
            //请求失败
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", e.getMessage());
                callback.onFailure(e);
            }

            //请求成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                try {
                    //401未授权, 跳转到登录页面
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("401")) {
                        Intent in = new Intent(context, LoginActivity.class);
                        context.startActivity(in);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 拼接请求路径
     * @param url
     * @param map
     * @return
     */
    private String getAppendUrl(String url, Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            //迭代器
            Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                //获取请求参数的键
                Entry<String, Object> entry = iterator.next();
                if (StringUtils.isEmpty(buffer.toString())) {
                    //第一个参数用?拼接
                    buffer.append("?");
                } else {
                    //剩余参数用&拼接
                    buffer.append("&");
                }
                buffer.append(entry.getKey()).append("=").append(entry.getValue());
            }
            url += buffer.toString();
        }
        return url;
    }
}
