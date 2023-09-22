
package com.example.secondhandplatforms;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class correspondence extends AppCompatActivity {
    private List<messageobject> transactionList = new ArrayList<>();
    private messageAdapter messageAdapter;
    private int currentPage = 1;
    private boolean isLoadingData = false;
    private boolean isLastPage = false;
    private final Gson gson = new Gson();
    private TextView usernameTextView;
    private RecyclerView messageRecyclerView;
    private String userId;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correspondence);

        // 获取传递的用户名和用户ID
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("userId");
            username = bundle.getString("username");
        }

        // 初始化界面元素
        usernameTextView = findViewById(R.id.usernameTextView);
        messageRecyclerView = findViewById(R.id.messageRecyclerView);
        messageAdapter = new messageAdapter(this, transactionList);

        // 设置用户名
        usernameTextView.setText("用户：" + username);

        // 添加发送按钮的点击事件监听器
        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage01();
            }
        });

        // 数据加载完毕后设置适配器
        messageRecyclerView.setAdapter(messageAdapter);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 开始加载消息数据
        sendMessage02();
    }




    // 发送消息的方法
    private void sendMessage01() {
        // 获取输入的消息文本
        EditText messageEditText = findViewById(R.id.messageEditText);
        String messageText = messageEditText.getText().toString().trim();
        {
            String Url = "http://47.107.52.7:88/member/tran/chat";

            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("content", messageText);
            bodyMap.put("toUserId", userId);
            bodyMap.put("userId", MainActivity.UserId);
            // 将Map转换为字符串类型加入请求体中
            String body = gson.toJson(bodyMap);
            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", "8d7539b50797443788485b81f0660ce1")
                    .add("appSecret", "3636148e54bbef9044a5a8647598c1ee004a4")
                    .add("Accept", "application/json, text/plain, */*")
                    .build();

            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //请求组合创建
            Request request = new Request.Builder()
                    .url(Url)
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, body))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(callback01);
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
            messageEditText.setText("");
        }
    }



    private void sendMessage02() {
        isLoadingData = true;
        // 替换成你的实际用户ID
        String url = "http://47.107.52.7:88/member/tran/chat/message?current=" + currentPage + "&fromUserId=" + userId + "&userId=" + MainActivity.UserId;
        // 创建请求，包括当前页的值
        Request request = new Request.Builder()
                .url(url)
                .addHeader("appId", "8d7539b50797443788485b81f0660ce1")
                .addHeader("appSecret", "3636148e54bbef9044a5a8647598c1ee004a4")
                .build();
        // 异步执行请求
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                isLoadingData = false;
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    if (hasMoreData(responseBody)) {
                        parseAndAddTransactions(responseBody);//处理数据
                        currentPage++;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        isLastPage = true;
                    }
                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                isLoadingData = false;
                // 处理网络失败
            }
        });
    }

    private boolean hasMoreData(String responseBody) {
        try {
            // 解析响应数据，获取是否还有更多数据的信息
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            JSONArray recordsArray = dataObject.getJSONArray("records");
            int length = recordsArray.length();
            return length >= 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void parseAndAddTransactions(String responseBody) {
        try {
            // 解析响应数据，获取交易数据数组
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            JSONArray recordsArray = dataObject.getJSONArray("records");// 假设响应中包含了一个名为 "transactions" 的数组
            for (int i = 0; i < recordsArray.length(); i++) {
                JSONObject transactionObject = recordsArray.getJSONObject(i);
                // 解析
                String Id = transactionObject.getString("id");
                String fromUserId = transactionObject.getString("fromUserId");
                String fromUsername = transactionObject.getString("fromUsername");
                String content = transactionObject.getString("content");
                String userId = transactionObject.getString("userId");
                String username = transactionObject.getString("username");
                String status = transactionObject.getString("status");
                String createTime = transactionObject.getString("createTime");

                // 创建 messageobject 对象并添加到列表
                messageobject message = new messageobject(Id, fromUserId, fromUsername, content, userId, username, status, createTime);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        transactionList.add(message);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void showSuccessDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // 直接使用 this 来获取 Activity 的 Context
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private final Callback callback01 = new Callback() {

        @Override
        public void onFailure(Call call, IOException e) {
            runOnUiThread(() -> showSuccessDialog("发送失败"));
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            runOnUiThread(() -> showSuccessDialog("发送成功"));
        }

    };

}
