package com.example.secondhandplatforms;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewAddFragment extends Fragment {
    private EditText addrEditText;
    private EditText contentEditText;
    private EditText priceEditText;
    private Spinner typeSpinner;
    private EditText userIdEditText;
    private EditText imageCodeEditText;
    private Button addButton;

    private String selectedTypeId;
    private String selectedTypeName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_add, container, false);

        addrEditText = root.findViewById(R.id.addrEditText);
        contentEditText = root.findViewById(R.id.contentEditText);
        priceEditText = root.findViewById(R.id.priceEditText);
        typeSpinner = root.findViewById(R.id.typeSpinner);
        imageCodeEditText = root.findViewById(R.id.imageCodeEditText);
        addButton = root.findViewById(R.id.addButton);

        // 设置商品类型下拉选择项
        setupTypeSpinner();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String addr = addrEditText.getText().toString();
                String content = contentEditText.getText().toString();
                Integer imageCode = Integer.valueOf(imageCodeEditText.getText().toString());
                int price = Integer.parseInt(priceEditText.getText().toString());

                String selectedType = typeSpinner.getSelectedItem().toString();
                String[] typeArray = selectedType.split(" ", 2);
                int typeId = Integer.parseInt(typeArray[0]);
                String typeName = typeArray[1];

                Integer userId = MainActivity.UserId;

                if (TextUtils.isEmpty(addr) || TextUtils.isEmpty(content) || TextUtils.isEmpty(imageCodeEditText.getText().toString())) {
                    Toast.makeText(getActivity(), "请填写完整信息", Toast.LENGTH_SHORT).show();
                } else if (selectedTypeId == null || selectedTypeName == null) {
                    Toast.makeText(getActivity(), "请选择商品类型", Toast.LENGTH_SHORT).show();
                } else {
                    // 在此处发送POST请求
                    sendPostRequest(addr, content, imageCode, price, typeId, typeName, userId);
                }
            }
        });

        return root;
    }

    private void setupTypeSpinner() {
        List<String> typeList = new ArrayList<>();
        typeList.add("1 手机");
        typeList.add("2 奢品");
        typeList.add("3 潮品");
        typeList.add("4 美妆");
        typeList.add("5 数码");
        typeList.add("6 潮玩");
        typeList.add("7 游戏");
        typeList.add("8 图书");
        typeList.add("9 美食");
        typeList.add("10 文玩");
        typeList.add("11 母婴");
        typeList.add("12 家具");
        typeList.add("13 乐器");
        typeList.add("14 其他");

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSpinner.setAdapter(typeAdapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = typeList.get(position);
                String[] typeArray = selectedType.split(" ", 2);
                selectedTypeId = typeArray[0];
                selectedTypeName = typeArray[1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedTypeId = null;
                selectedTypeName = null;
            }
        });
    }





    private void sendPostRequest(String addr, String content, long imageCode, int price, int typeId, String typeName, long userId) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://47.107.52.7:88/member/tran/goods/save";

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("addr",addr);
        bodyMap.put("userId", String.valueOf(MainActivity.UserId));
        bodyMap.put("imageCode",String.valueOf(imageCode));
        bodyMap.put("price", String.valueOf(price));
        bodyMap.put("typeId", String.valueOf(typeId));
        bodyMap.put("typeName", typeName);
        bodyMap.put("content", content);
        String body = new Gson().toJson(bodyMap);
        // 创建请求体
        // 创建POST请求
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body))
                .addHeader("appId", "8d7539b50797443788485b81f0660ce1")
                .addHeader("appSecret", "3636148e54bbef9044a5a8647598c1ee004a4")
                .build();

        // 发送请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d("der", request.toString());
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "商品添加成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "商品添加失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络错误: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
