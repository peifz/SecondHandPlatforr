package com.example.secondhandplatforms;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserRegistration extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);


        TextView backToLoginTextView = findViewById(R.id.backToLoginTextView);
        backToLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在点击时返回到MainActivity
                finish(); // 关闭当前注册页面
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                new RegisterTask().execute(username, password);
            }
        });
    }

    private class RegisterTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            try {
                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                Map<String, String> requestData = new HashMap<>();
                requestData.put("username", username);
                requestData.put("password", password);
                String requestBody = new Gson().toJson(requestData);

                Request request = new Request.Builder()
                        .url("http://47.107.52.7:88/member/tran/user/register")
                        .addHeader("Accept", "application/json, text/plain, */*")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("appId", "8d7539b50797443788485b81f0660ce1")
                        .addHeader("appSecret", "3636148e54bbef9044a5a8647598c1ee004a4")
                        .post(RequestBody.create(JSON, requestBody))
                        .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return "注册失败，HTTP响应码：" + response.code();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "注册失败，错误信息：" + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            handleRegisterResponse(result);
        }
    }

    private void handleRegisterResponse(String response) {
        try {
            Gson gson = new Gson();
            RegisterResponse registerResponse = gson.fromJson(response, RegisterResponse.class);

            if (registerResponse != null) {
                if (registerResponse.getCode() == 200) {
                    showAlert("注册成功");
                } else {
                    showAlert(registerResponse.getMsg());
                }
            } else {
                showAlert("系统异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("注册失败，请重试");
        }
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("注册结果")
                .setPositiveButton("确定", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}