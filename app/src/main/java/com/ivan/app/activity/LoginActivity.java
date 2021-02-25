package com.ivan.app.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.ivan.app.R;
import com.ivan.app.api.Api;
import com.ivan.app.api.ApiConfig;
import com.ivan.app.api.IvanCallback;
import com.ivan.app.entity.LoginResponse;
import com.ivan.app.util.StringUtils;

import java.util.HashMap;

/**
 * 登录页
 */
public class LoginActivity extends BaseActivity {

    //用户名
    private EditText etAccount;
    //密码
    private EditText etPwd;
    //登录按钮
    private Button btnLogin;

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        btnLogin = findViewById(R.id.btn_login);
    }

    @Override
    protected void initData() {
        //点击登录按钮，触发登录事件
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                login(account, pwd);
            }
        });
    }

    //登录方法
    private void login(String account, String pwd) {
        if (StringUtils.isEmpty(account)){
//            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            showToast("请输入用户名");
            return;
        }
        if (StringUtils.isEmpty(pwd)){
//            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            showToast("请输入密码");
            return;
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("account", account);
        params.put("password", pwd);
        Api.config(ApiConfig.LOGIN, params).postRequest(this,new IvanCallback() {
            @Override
            public void onSuccess(final String res) {
                Log.e("onSuccess", res);
                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);
                if (loginResponse.getCode() == 0) {
                    String token = loginResponse.getToken();
                    //保存token到本地
                    saveStringToSp("token", token);
                    //登录成功跳转到首页
                    navigateTo(HomeActivity.class);
//                    insertVal("token", token);
//                    navigateToWithFlag(HomeActivity.class,
//                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    showToastSync("登录成功");
                } else {
                    showToastSync("登录失败");
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("onFailure", e.toString());
            }
        });
    }
//    private void login(String account, String pwd) {
//        if (StringUtils.isEmpty(account)){
////            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
//            showToast("请输入用户名");
//        }
//        if (StringUtils.isEmpty(pwd)){
////            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
//            showToast("请输入密码");
//        }
//
//        //第一步创建OKHttpClient
//        OkHttpClient client = new OkHttpClient.Builder().build();
//        Map m = new HashMap();
//        m.put("account", account);
//        m.put("password", pwd);
//        JSONObject jsonObject = new JSONObject(m);
//        String jsonStr = jsonObject.toString();
//        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8") , jsonStr);
//        //第三步创建Rquest
//        Request request = new Request.Builder()
//                .url(AppConfig.BASE_URl + "/app/login")
//                .addHeader("contentType", "application/json;charset=UTF-8")
//                .post(requestBodyJson)
//                .build();
//        //第四步创建call回调对象
//        final Call call = client.newCall(request);
//        //第五步发起请求
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("onFailure", e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String result = response.body().string();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        showToast(result);
//                    }
//                });
//            }
//        });
//    }
}