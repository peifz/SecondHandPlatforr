package com.example.secondhandplatforms;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ProductInformation extends AppCompatActivity {
    private String USERID;
    private String USERNAME;
     private Product product;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_information);

        // 获取启动该 Activity 的 Intent
        Intent intent = getIntent();

        // 检查 Intent 是否包含名为 "productId" 的额外参数
        if (intent.hasExtra("productId")) {
            // 从 Intent 中获取传递的 productId
            String  productId = intent.getStringExtra("productId");

            // 发送请求获取商品详情
            sendProductDetailsRequest(productId);
        }



        Button purchaseButton = findViewById(R.id.btn_details);
        Log.d("user", purchaseButton.toString());
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 执行购买操作
                performPurchase();
            }
        });




        Button user = findViewById(R.id.btn_user);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductInformation.this, correspondence.class);
                // 添加额外数据
                intent.putExtra("userId", USERID);
                intent.putExtra("username", USERNAME);
                // 启动Activity
                startActivity(intent);
            }
        });





    }

    private void sendProductDetailsRequest(String  productId) {
        OkHttpClient client = new OkHttpClient();
        String apiUrl = "http://47.107.52.7:88/member/tran/goods/details?goodsId=" + productId;

        // 添加请求头参数
        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Content-Type", "application/json")
                .addHeader("appId", "8d7539b50797443788485b81f0660ce1")
                .addHeader("appSecret", "3636148e54bbef9044a5a8647598c1ee004a4")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败处理
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 在 UI 线程中显示错误消息或进行其他处理
                        showAlert("请求商品详情失败，请重试");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功处理
                final String responseBody = response.body().string();
                Log.d("sh", responseBody);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            handleProductDetailsResponse(responseBody);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void handleProductDetailsResponse(String response) throws JSONException {
        // 解析响应数据并更新商品详情页面
        JSONObject jsonResponse = new JSONObject(response);
        int code = jsonResponse.getInt("code");
        if (code == 200) {
            JSONObject dataObject = jsonResponse.getJSONObject("data");

            // 从 dataObject 中获取商品详情的各个字段
            String id = dataObject.getString("id");
            String productName = dataObject.getString("content");
            String productDescription = dataObject.getString("content"); // 这里使用 content 字段，你可以根据需要修改
            String userInfo = "User • " + dataObject.getString("createTime"); // 拼接用户名和日期

            double price = dataObject.getDouble("price");
            String address = dataObject.getString("addr");
            String category = dataObject.getString("typeName");
            String username = dataObject.getString("username");
            USERNAME = dataObject.getString("username");
            USERID = dataObject.getString("tuserId");
            product = new Product();
            product.setId(id);
            product.setPrice(price);
            product.setTUserId(dataObject.getString("tuserId"));
            // 设置视图元素的内容
            TextView productNameTextView = findViewById(R.id.tv_product_name);
            TextView productDescriptionTextView = findViewById(R.id.tv_product_description);
            TextView userInfoTextView = findViewById(R.id.tv_user_info);
            TextView priceTextView = findViewById(R.id.tv_price);
            TextView addressTextView = findViewById(R.id.tv_address);
            TextView categoryTextView = findViewById(R.id.tv_category);
            TextView usernameTextView = findViewById(R.id.tv_username);

            productNameTextView.setText(productName);
            productDescriptionTextView.setText(productDescription);
            userInfoTextView.setText(userInfo);
            priceTextView.setText("Price: $" + price);
            addressTextView.setText("Address: " + address);
            categoryTextView.setText("Category: " + category);
            usernameTextView.setText(username);



            ImageView productImage = findViewById(R.id.iv_product_image);

// 获取图片URL，这里使用默认图片URL作为备用
            String imageUrl = dataObject.getJSONArray("imageUrlList").optString(0, "");

// 使用Glide加载图片
            Glide.with(this)
                    .load(imageUrl)
                    .apply(new RequestOptions().placeholder(R.drawable.product)) // 设置默认图片
                    .into(productImage);
            // 设置商品图片（你需要使用图像加载库，如Glide或Picasso）
        } else {
            String msg = jsonResponse.getString("msg");
            showAlert(msg);
        }
    }

    private void showAlert(String message) {
        // 在这里显示警告对话框或其他错误提示方式
    }




    private void performPurchase() {
        // 获取当前用户的ID，你需要根据你的实现方式获取它
        // 这里假设你已经有了一个类似 MainActivity.UserId 的变量来存储用户ID
        int buyerId = MainActivity.UserId;

        // 获取商品的ID、价格和卖家ID
        String productId = product.getId();
        double price = product.getPrice();
        String  sellerId = product.getTUserId();

        // 构建购买请求的参数
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("buyerId", MainActivity.UserId);
            requestBody.put("goodsId", productId);
            requestBody.put("price", price);
            requestBody.put("sellerId", sellerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 创建OkHttpClient和请求
        OkHttpClient client = new OkHttpClient();
        String apiUrl = "http://47.107.52.7:88/member/tran/trading";

        RequestBody body = RequestBody.create(requestBody.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("appId", "8d7539b50797443788485b81f0660ce1")
                .addHeader("appSecret", "3636148e54bbef9044a5a8647598c1ee004a4")
                .post(body)
                .build();

        // 发送请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败处理
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAlert("购买失败，请重试");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功处理
                final String responseBody = response.body().string();
                showAlert("购买成功！");
                Log.d("sh", responseBody);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            int code = jsonResponse.getInt("code");
                            if (code == 200) {

                            } else {
                                String msg = jsonResponse.getString("msg");
                                showAlert("购买失败：" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
