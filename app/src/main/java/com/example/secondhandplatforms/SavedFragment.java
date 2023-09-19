package com.example.secondhandplatforms;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SavedFragment extends Fragment {

    private static final String API_ENDPOINT = "http://47.107.52.7:88/member/tran/goods/save";
    private static final String APP_ID = "8d7539b50797443788485b81f0660ce1";
    private static final String APP_SECRET = "3636148e54bbef9044a5a8647598c1ee004a4";

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<MyDataItem> dataItemList;

    public SavedFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publish, container, false);
        // 初始化 RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataItemList = new ArrayList<>();
        adapter = new MyAdapter(dataItemList);

        // 添加发布按钮的点击事件监听器
        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MyDataItem dataItem) {
                // 处理点击商品卡片事件
                Log.d("id", "执行了");
            }

            @Override
            public void onPublishClick(MyDataItem dataItem) {
                // 处理点击发布按钮事件，使用 dataItem 对象的数据，发送请求
                sendPublishRequest(dataItem);
            }
        });

        recyclerView.setAdapter(adapter);

        // 发送网络请求获取数据
        fetchData();

        return view;
    }


    private void fetchData() {
        OkHttpClient client = new OkHttpClient();

        // 创建请求并添加请求头
        Request request = new Request.Builder()
                .url(API_ENDPOINT + "?current=1&userId=" + MainActivity.UserId)
                .addHeader("appId", APP_ID)
                .addHeader("appSecret", APP_SECRET)
                .build();

        // 异步执行请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 网络请求失败
                e.printStackTrace();
                // 在此处理失败情况，例如显示错误消息
                displayErrorMessage("网络请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 网络请求成功
                    final String responseData = response.body().string();
                    handleResponseData(responseData);
                } else {
                    // 网络请求失败
                    // 在此处理失败情况，例如显示错误消息
                    displayErrorMessage("网络请求失败");
                }
            }
        });
    }

    private void handleResponseData(final String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                JSONArray records = jsonObject.getJSONObject("data").getJSONArray("records");
                Log.d("sb", records.toString());
                for (int i = 0; i < records.length(); i++) {
                    JSONObject record = records.getJSONObject(i);
                    //获取对象进行渲染
                    String id = record.getString("id");
                    String content = record.getString("content");
                    String price = record.getString("price");
                    String addr = record.getString("addr");
                    String imageCode = record.getString("imageCode");
                    String typeid = record.getString("typeId");
                    String typeName = record.getString("typeName");
                    String userid = record.getString("tuserId");
                    //解析商品出对象，并在此同时渲染
                    MyDataItem dataItem = new MyDataItem(id, content, price, addr, imageCode, typeid, typeName, userid);
                    dataItemList.add(dataItem);
                }

                // 更新 UI 线程中的数据
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

            } else {
                String errorMsg = jsonObject.getString("msg");
                displayErrorMessage("Error: " + errorMsg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            displayErrorMessage("数据解析失败");
        }
    }

    private void sendPublishRequest(MyDataItem dataItem) {
        OkHttpClient client = new OkHttpClient();
        // 创建发布请求并添加请求参数，例如使用 POST 请求
        RequestBody requestBody = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url("http://47.107.52.7:88/member/tran/goods/change?id="+dataItem.getId()+"&content="+dataItem.getContent()
                +"&addr="+dataItem.getAddr()+"&imageCode="+dataItem.getImageCode()+"&price="+dataItem.getPrice()+"&" +
                        "typeId="+dataItem.getTypeid()+"&typeName="+dataItem.getTypeName()+"&userId="+dataItem.getUserid())
                .addHeader("appId", APP_ID)
                .addHeader("appSecret", APP_SECRET)
                .post(requestBody)
                .build();
        // 异步执行发布请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 发布请求失败
                e.printStackTrace();
                // 在此处理失败情况，例如显示错误消息
                displayErrorMessage("发布请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 发布请求成功
                    final String responseData = response.body().string();
                    handlePublishResponse(responseData);
                } else {
                    // 发布请求失败
                    // 在此处理失败情况，例如显示错误消息
                    displayErrorMessage("发布请求失败");
                }
            }
        });
    }

    // 处理发布响应
    private void handlePublishResponse(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                displaySuccessMessage("发布成功");
            } else {
                String errorMsg = jsonObject.getString("msg");
                displayErrorMessage("发布失败: " + errorMsg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            displayErrorMessage("发布响应解析失败");
        }
    }

    // 显示成功消息
    private void displaySuccessMessage(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run(){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void displayErrorMessage(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}






