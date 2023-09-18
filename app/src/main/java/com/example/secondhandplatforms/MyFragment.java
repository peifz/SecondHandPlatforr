package com.example.secondhandplatforms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyFragment extends Fragment {
    public static String NewURL;

    public static LayoutInflater Inflater;
    public static ViewGroup Container;
    public MyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Inflater = inflater;
        Container = container;
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        // 获取文本框
        final EditText editText = view.findViewById(R.id.usernameEditText);



        // 设置文本框的内容为 MainActivity.Username
        if (MainActivity.Username != null) {
            editText.setText(MainActivity.Username);
        }

        // 导航选项 2（修改头像）的点击事件
        View modifyAvatar = view.findViewById(R.id.modifyAvatar);
        modifyAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvatarDialog(editText);
            }
        });

        // 显示用户头像
        ImageView avatarImageView = view.findViewById(R.id.avatarImageView);
        if (NewURL != null) {
            // 如果 NewURL 不为空，加载新头像
            Glide.with(this).load(NewURL).into(avatarImageView);
        } else {
            // 否则，加载默认头像
            Glide.with(this).load(R.drawable.baseline_person_24).into(avatarImageView);
        }
        return view;
    }









    // 弹出对话框以获取头像URL并保存在变量中
    private void showAvatarDialog(final EditText editText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("修改头像");
        builder.setMessage("请输入新的头像URL:");

        final EditText input = new EditText(getActivity());
        builder.setView(input);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newAvatarUrl = input.getText().toString();
                // 将新的头像URL存储在适当的变量中
                NewURL = newAvatarUrl;
                // 执行头像更新任务
                AvatarUpdateTask updateTask = new AvatarUpdateTask();
                updateTask.execute(newAvatarUrl, String.valueOf(MainActivity.UserId));
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }



    // 异步任务用于执行头像更新的网络请求
    private class AvatarUpdateTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            if (params.length != 2) {
                return null;
            }
            String avatarUrl = params[0];
            String userId = params[1];

            OkHttpClient client = new OkHttpClient();
            try {
                // 构建请求体
                JSONObject requestBody = new JSONObject();
                requestBody.put("avatar", avatarUrl);
                requestBody.put("userId", userId);

                // 创建请求对象
                Request request = new Request.Builder()
                        .url("http://47.107.52.7:88/member/tran/user/update")
                        .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody.toString()))
                        .addHeader("appId", "8d7539b50797443788485b81f0660ce1")
                        .addHeader("appSecret", "3636148e54bbef9044a5a8647598c1ee004a4")
                        .build();

                // 发送请求
                Response response = client.newCall(request).execute();

                // 处理响应
                if (response.isSuccessful()) {
                    Log.d("haoshuxin",response.body().string());
                    return response.body().string();
                } else {
                    return null;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }



        @Override
        protected void onPostExecute(String result) {
            // 在请求完成后，你可以在这里处理响应结果
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        showSuccessDialog("修改成功");
                    } else {
                        // 修改失败，处理相应逻辑
                        // 显示修改失败的提示
                        showFailureDialog(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // 网络请求失败，处理相应逻辑
                // 显示请求失败的提示
                showFailureDialog("网络请求失败");
            }
        }
    }

    // 显示头像更新成功的提示
    private void showSuccessDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage(message);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        if (NewURL != null) {
            // 如果 NewURL 不为空，加载新头像
            View view = Inflater.inflate(R.layout.fragment_my, Container, false);
            ImageView avatarImageView = view.findViewById(R.id.avatarImageView);
            Glide.with(this).load(NewURL).into(avatarImageView);
        }
        builder.show();
    }
    // 显示头像更新失败的提示
    private void showFailureDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("头像修改失败: " + message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}
