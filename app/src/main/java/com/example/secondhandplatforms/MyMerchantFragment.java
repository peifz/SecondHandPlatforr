
package com.example.secondhandplatforms;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyMerchantFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;

    public MyMerchantFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_merchant, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        // 初始化数据
        userList = new ArrayList<>();

        // 发起网络请求获取数据
        sendProductDetailsRequest();

        // 创建适配器并设置给RecyclerView
        userAdapter = new UserAdapter(requireContext(), userList);
        recyclerView.setAdapter(userAdapter);

        // 设置RecyclerView的布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return view;
    }

    private void sendProductDetailsRequest() {
        OkHttpClient client = new OkHttpClient();
        String Url = "http://47.107.52.7:88/member/tran/chat/user?userId=" + MainActivity.UserId;

        // 请求头
        Headers headers = new Headers.Builder()
                .add("appId", "8d7539b50797443788485b81f0660ce1")
                .add("appSecret", "3636148e54bbef9044a5a8647598c1ee004a4")
                .add("Accept", "application/json, text/plain, */*")
                .build();

        // 请求创建
        Request request = new Request.Builder()
                .url(Url)
                .headers(headers)
                .get()
                .build();


        // 发起请求，传入callback进行回调
        client.newCall(request).enqueue(callback);
    }

    private final Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            // 处理网络请求失败的逻辑，可以在这里添加错误处理代码
            e.printStackTrace();
        }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    // 处理网络请求成功的逻辑
                    if (response.isSuccessful()) {
                        // 解析JSON数据
                        String responseData = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseData); // 解析为 JSON 对象

                        // 检查是否存在 "data" 键
                        if (jsonResponse.has("data")) {
                            JSONArray jsonArray = jsonResponse.getJSONArray("data"); // 获取 "data" 键对应的 JSON 数组

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String fromUserId = jsonObject.getString("fromUserId");
                                String username = jsonObject.getString("username");
                                String unReadNum = jsonObject.getString("unReadNum");
                                // 创建User对象并添加到列表
                                User user = new User(fromUserId, username, unReadNum);
                                userList.add(user);
                            }
                            // 数据获取成功后通知适配器刷新UI
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    userAdapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            // 如果没有 "data" 键，可以根据实际情况进行处理
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }


    };
}
