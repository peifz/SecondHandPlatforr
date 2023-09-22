package com.example.secondhandplatforms;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MypurchasesFragment extends Fragment {

    private RecyclerView recyclerView;
    private TransactionsAdapter adapter;
    private List<TransactionsData> transactionList;
    private boolean isLoadingData = false;
    private int currentPage = 1; // 从第1页开始
    private boolean isLastPage = false;

    private OkHttpClient okHttpClient = new OkHttpClient();

    public MypurchasesFragment() {
        // 空的构造函数
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transactionList = new ArrayList<>();
        adapter = new TransactionsAdapter(transactionList);
        recyclerView.setAdapter(adapter);

        // 加载初始数据
        loadTransactions();

        return view;
    }

    private void loadTransactions() {
        isLoadingData = true;

        // 替换成你的实际用户ID
        String userId = String.valueOf(MainActivity.UserId);

        // 创建请求，包括当前页的值
        Request request = new Request.Builder()
                .url("http://47.107.52.7:88/member/tran/trading/buy?current=" + currentPage + "&userId=" + userId)
                .addHeader("appId", "8d7539b50797443788485b81f0660ce1")
                .addHeader("appSecret", "3636148e54bbef9044a5a8647598c1ee004a4")
                .build();
        // 异步执行请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                isLoadingData = false;
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("result", responseBody);
                    if (hasMoreData(responseBody)) {
                        parseAndAddTransactions(responseBody);
                        currentPage++;
                        loadTransactions(); // 加载更多数据
                    } else {
                        Log.d("number", String.valueOf(transactionList.size()));
                        isLastPage = true;
                    }
                } else {
                    Log.d("der", "onResponse: ");
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
            int  length = recordsArray.length();
            return  length >=1 ?true:false;
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

                // 解析交易数据并创建 TransactionsData 对象
                String id = transactionObject.getString("id");
                String goodsId = transactionObject.getString("goodsId");
                String sellerId =transactionObject.getString("sellerId");
                String price =transactionObject.getString("price");
                String buyerId =transactionObject.getString("buyerId");
                String createTime =transactionObject.getString("createTime");
                String sellerName =transactionObject.getString("sellerName");
                String buyerAvatar =transactionObject.getString("buyerAvatar");
                String goodsDescription =transactionObject.getString("goodsDescription");
                String imageUrlList =transactionObject.getString("imageUrlList");
                String buyerName = transactionObject.getString("buyerName");
                String sellerAvatar =transactionObject.getString("sellerAvatar");

                // 创建 TransactionsData 对象并添加到列表
                TransactionsData transaction = new TransactionsData( id, goodsId,
                        sellerId,  price,
                        buyerId,  createTime,
                        sellerName,  buyerName,
                        sellerAvatar,  buyerAvatar,
                        goodsDescription,  imageUrlList);
                transactionList.add(transaction);
            }



            // 刷新适配器以显示新数据
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
