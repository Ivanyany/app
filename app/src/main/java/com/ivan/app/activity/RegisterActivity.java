package com.ivan.app.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ivan.app.R;
import com.ivan.app.api.Api;
import com.ivan.app.api.ApiConfig;
import com.ivan.app.api.IvanCallback;
import com.ivan.app.util.StringUtils;

import java.util.HashMap;

/**
 * 注册页
 */
public class RegisterActivity extends BaseActivity {

    //用户名
    private EditText etAccount;
    //密码
    private EditText etPwd;
    //登录按钮
    private Button btnRegister;

    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        btnRegister = findViewById(R.id.btn_register);
    }

    @Override
    protected void initData() {
        //点击注册按钮，触发注册事件
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                register(account, pwd);
            }
        });
    }

    //注册方法
    private void register(String account, String pwd) {
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

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("account", account);
        params.put("password", pwd);
        Api.config(ApiConfig.REGISTER, params).postRequest(this,new IvanCallback() {
            @Override
            public void onSuccess(final String res) {
                Log.e("onSuccess", res);
                showToastSync(res);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("onFailure", e.toString());
            }
        });
    }
}