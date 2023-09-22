package com.example.secondhandplatforms;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SaleFragment extends Fragment {

    private static final String API_ENDPOINT = "http://47.107.52.7:88/member/tran/goods/myself";
    private static final String APP_ID = "8d7539b50797443788485b81f0660ce1";
    private static final String APP_SECRET = "3636148e54bbef9044a5a8647598c1ee004a4";

    private RecyclerView recyclerView;
    private soldAdapter adapter;
    private List<MyDataItem> dataItemList;

    public SaleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publish, container, false);

        // 初始化 RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataItemList = new ArrayList<>();
        adapter = new soldAdapter(dataItemList);
        recyclerView.setAdapter(adapter);

        // 发送网络请求获取数据
        fetchData();

        return view;
    }

    private void fetchData() {
        OkHttpClient client = new OkHttpClient();

        // 创建请求并添加请求头,发送请求
        Request request = new Request.Builder()
                .url(API_ENDPOINT + "?userId=" + MainActivity.UserId)
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
            Log.d("result", responseData);
            if (code == 200) {

                JSONArray records = jsonObject.getJSONObject("data").getJSONArray("records");
                for (int i = 0; i < records.length(); i++) {
                    JSONObject record = records.getJSONObject(i);
                    Log.d("result",record.toString());
                    String id = record.getString("id");
                    Log.d("ders", "handleResponseData: ");
                    String goodsDescription = record.getString("content");
                    String addr = record.getString("addr");
                    String price = record.getString("price");
                    String buyerName = record.getString("username");
                    String createTime = record.getString("createTime");
                    Log.d("ders", "handleResponseData: ");
                    MyDataItem dataItem = new MyDataItem(id, goodsDescription,addr, price, buyerName,createTime);
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

    private void displayErrorMessage(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
