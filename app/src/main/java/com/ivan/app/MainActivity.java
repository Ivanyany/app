package com.ivan.app;

import android.view.View;
import android.widget.Button;

import com.ivan.app.activity.BaseActivity;
import com.ivan.app.activity.LoginActivity;
import com.ivan.app.activity.RegisterActivity;

public class MainActivity extends BaseActivity {

    //登录按钮
    private Button btnLogin;
    //注册按钮
    private Button btnRegister;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
    }

    @Override
    protected void initData() {
        //点击登录按钮，弹出登录页面
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(in);
                navigateTo(LoginActivity.class);
            }
        });

        //点击注册按钮，弹出注册页面
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in = new Intent(MainActivity.this, RegisterActivity.class);
//                startActivity(in);
                navigateTo(RegisterActivity.class);
            }
        });
    }
}