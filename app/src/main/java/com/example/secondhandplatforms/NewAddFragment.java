package com.example.secondhandplatforms;

import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.List;

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
                if (TextUtils.isEmpty(addrEditText.getText().toString())) {
                    Toast.makeText(getActivity(), "请填写卖家所在地", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(contentEditText.getText().toString())) {
                    Toast.makeText(getActivity(), "请填写商品描述", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(priceEditText.getText().toString())) {
                    Toast.makeText(getActivity(), "请填写商品价格", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(imageCodeEditText.getText().toString())) {
                    Toast.makeText(getActivity(), "请填写图片标识", Toast.LENGTH_SHORT).show();
                } else if (selectedTypeId == null || selectedTypeName == null) {
                    Toast.makeText(getActivity(), "请选择商品类型", Toast.LENGTH_SHORT).show();
                } else {
                    // 执行添加商品的逻辑
                    String typeId = selectedTypeId;
                    String typeName = selectedTypeName;

                    boolean isAddSuccessful = addProduct(typeId, typeName);

                    if (isAddSuccessful) {
                        Toast.makeText(getActivity(), "商品添加成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "商品添加失败", Toast.LENGTH_SHORT).show();
                    }
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

    private boolean addProduct(String typeId, String typeName) {
        // 执行添加商品的逻辑
        // 返回添加是否成功的结果，这里假设添加成功
        return true;
    }
}