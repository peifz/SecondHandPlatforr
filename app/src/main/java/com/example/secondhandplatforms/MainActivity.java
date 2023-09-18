package com.example.secondhandplatforms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    public static String  Username;
    public  static  int UserId;
    private Button registraButton;
    private ImageView passwordVisibilityImageView;


    //注册实现页面的跳转
    private void navigateToRegisterPage() {
        Intent intent = new Intent(this, UserRegistration.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.et_pwd);
        loginButton = findViewById(R.id.bt_login);
        registraButton = findViewById(R.id.bt_register);
        passwordVisibilityImageView = findViewById(R.id.iv_pwd_switch);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                new LoginTask().execute(username, password);
            }
        });



        //注册页面跳转
        registraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegisterPage(); // 跳转到注册页面
            }
        });




        //设计密码可见与不可见
        passwordVisibilityImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputType = passwordEditText.getInputType();
                if ((inputType & 0x80) == 0x80) {
                    passwordEditText.setInputType(inputType & ~0x80);
                } else {
                    passwordEditText.setInputType(inputType | 0x80);
                }
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });
    }




    //发送请求
    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            try {
                OkHttpClient client = new OkHttpClient();

                // 构建请求体

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
                Username = username;
                // 创建请求对象
                Request request = new Request.Builder()
                        .url("http://47.107.52.7:88/member/tran/user/login")
                        .addHeader("Accept", "application/json, text/plain, */*")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("appId", "8d7539b50797443788485b81f0660ce1")
                        .addHeader("appSecret", "3636148e54bbef9044a5a8647598c1ee004a4")
                        .post(RequestBody.create(JSON, requestBody))
                        .build();
                // 发送请求并获取响应
                Response response = client.newCall(request).execute();


                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return "登录失败，HTTP响应码：" + response.code();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "登录失败，错误信息：" + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            handleLoginResponse(result);
        }
    }
    //处理返回的结果
    private void handleLoginResponse(String response) {
        try {
            Gson gson = new Gson();
            Log.d("beforeGson",response.toString());
            Log.d("id", String.valueOf(UserId));
            LoginResponse loginResponse = gson.fromJson(response, LoginResponse.class);
            UserId  = Integer.parseInt(loginResponse.getData().getId());
            if (loginResponse != null) {
                if (loginResponse.getCode() == 200) {
                    showAlert("登录成功");
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish(); // 关闭当前登录页面
                } else {
                    showAlert(loginResponse.getMsg());
                }
            } else {
                showAlert("系统异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("登录失败，请重试");
        }
    }




    //重写方法
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("登录结果")
                .setPositiveButton("确定", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.dismiss();
    }
}


