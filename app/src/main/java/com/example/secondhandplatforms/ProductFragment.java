package com.example.secondhandplatforms;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class ProductFragment extends Fragment {
    private String selectedCategory = "";
    ProductAdapter productAdapter;
    private EditText searchEditText;
    private Button searchButton;
    private LinearLayout categoryLayout;
    private RecyclerView recyclerView;
    private List<Product> productList;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        searchEditText = view.findViewById(R.id.et_search);
        searchButton = view.findViewById(R.id.btn_search);
        categoryLayout = view.findViewById(R.id.ll_category_nav);
        recyclerView = view.findViewById(R.id.rv_products);

        // 获取并渲染分类数据
        fetchCategories();

        // 初始化 RecyclerView 和适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);
        Button recommendButton = view.findViewById(R.id.btn_recommend);
        recommendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 发送推荐商品请求（无关键字）
                for(int i= 1;i<=9;i++) {
                    fetchProducts("",i);
                }
            }
        });

        productList.clear();
        // 请求并渲染商品列表（页面开始时）
        for(int i= 0;i<=9;i++) {
            fetchProducts("",i);
        } // 空字符串表示不传递关键字


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        return view;
    }



    private void performSearch() {
        String keyword = searchEditText.getText().toString().trim();

        // 发送搜索请求
        productList.clear();
        for(int i= 0;i<=9;i++) {
            fetchProducts(keyword,i);
        }
    }




    //单个搜索
    private void fetchProducts(String keyword,Integer current) {
        String id = String.valueOf(MainActivity.UserId);
        String apiUrl;

        if (TextUtils.isEmpty(keyword)) {
            // 如果关键字为空，只传递 userId 和分类
            apiUrl = "http://47.107.52.7:88/member/tran/goods/all?userId=" + id +"&current="+current+ "&category=" + selectedCategory;
        } else {
            // 如果关键字不为空，同时传递 userId、分类和关键字参数
            apiUrl = "http://47.107.52.7:88/member/tran/goods/all?keyword=" + keyword + "&userId=" + id +"&current="+ current + "&category=" + selectedCategory;
        }
        // 添加其他请求参数（appId、appSecret等）
        OkHttpClient client = new OkHttpClient();
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAlert("请求失败，请重试");
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功处理
                final String responseBody = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleSearchResponse(responseBody);
                    }
                });
            }
        });
    }






    private void handleSearchResponse(String response) {
        // 解析响应数据并更新商品列表
        List<Product> products = new ArrayList<>();
        //解析数据
        try {
            JSONObject jsonResponse = new JSONObject(response);
            int code = jsonResponse.getInt("code");
            if (code == 200) {
                JSONObject dataObject = jsonResponse.getJSONObject("data");
                JSONArray recordsArray = dataObject.getJSONArray("records");

                for (int i = 0; i < recordsArray.length(); i++) {
                    JSONObject productObject = recordsArray.getJSONObject(i);
                    Product product = new Product(productObject);
                    products.add(product);
                }
                // 更新 RecyclerView 数据
                productList.addAll(products);
                productAdapter.notifyDataSetChanged();
            } else {
                String msg = jsonResponse.getString("msg");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showAlert("处理响应数据失败");
        }
    }






    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setTitle("搜索结果")
                .setPositiveButton("确定", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }




    //请求分类
    private void fetchCategories() {
        // 发送请求以获取分类数据
        String apiUrl = "http://47.107.52.7:88/member/tran/goods/type";
        OkHttpClient client = new OkHttpClient();
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAlert("请求分类数据失败，请重试");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功处理
                final String responseBody = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleCategoryResponse(responseBody);
                    }
                });
            }
        });
    }




    private void handleCategoryResponse(String response) {
        // 处理分类数据
        try {
            JSONObject jsonResponse = new JSONObject(response);
            int code = jsonResponse.getInt("code");
            if (code == 200) {
                JSONArray data = jsonResponse.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject categoryObject = data.getJSONObject(i);
                    String category = categoryObject.getString("type");
                    TextView categoryTextView = new TextView(getActivity());
                    categoryTextView.setText(category);
                    categoryTextView.setPadding(16, 8, 16, 8);
                    categoryTextView.setTextColor(getResources().getColor(R.color.category_text_color));
                    categoryTextView.setTextSize(16);
                    categoryTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 更新选中的分类
                            updateSelectedCategory(category);
                        }
                    });
                    categoryLayout.addView(categoryTextView);
                }
            } else {
                showAlert("请求分类数据失败，请重试");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showAlert("处理分类数据失败");
        }
    }


    private void updateSelectedCategory(String category) {
        for (int i = 0; i < categoryLayout.getChildCount(); i++) {
            View child = categoryLayout.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                textView.setTextColor(getResources().getColor(R.color.category_text_color));
                if (textView.getText().toString().equals(category)) {
                    textView.setTextColor(getResources().getColor(R.color.selected_category_text_color));
                }
            }
        }
        selectedCategory = category;
        productList.clear();
        for(int i= 0;i<=9;i++) {
            fetchProducts(category,i);
        }
    }
}