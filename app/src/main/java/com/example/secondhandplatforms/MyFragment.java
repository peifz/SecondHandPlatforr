package com.example.secondhandplatforms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
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
    // 存储新头像URL的变量
    public static String NewURL = MainActivity.AvatarUrl;

    // 构造函数
    public MyFragment() {
        // 必要的空构造函数
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 创建视图并绑定到 fragment_my.xml 布局
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        // 获取文本框
        final EditText editText = view.findViewById(R.id.usernameEditText);

        // 如果 MainActivity.Username 不为空，则设置文本框的内容为用户名
        if (MainActivity.Username != null) {
            editText.setText(MainActivity.Username);
        }

        // 弹出充值对话框
        View rechargeOption = view.findViewById(R.id.rechargeOption);
        rechargeOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击时弹出充值对话框
                showRechargeDialog();
            }
        });

        // 设置 "修改头像" 选项的点击事件监听器
        View modifyAvatar = view.findViewById(R.id.modifyAvatar);
        modifyAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击时弹出头像修改对话框
                showAvatarDialog(editText);
            }
        });

        // 显示用户头像
        ImageView avatarImageView = view.findViewById(R.id.avatarImageView);
        if (NewURL != null) {
            // 如果 NewURL 不为空，加载新头像
            Glide.with(this).load(NewURL).into(avatarImageView);
        } else {
            // 若为空则加载默认头像
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
                // 获取用户输入的新头像URL
                String newAvatarUrl = input.getText().toString();
                // 将新的头像URL存储在适当的变量中
                NewURL = newAvatarUrl;
                // 执行头像更新任务
                new AvatarUpdateTask().execute(newAvatarUrl, String.valueOf(MainActivity.UserId));
                // 加载最新的头像URL到 ImageView 中
                ImageView avatarImageView = getView().findViewById(R.id.avatarImageView);
                if (NewURL != null) {
                    // 如果 NewURL 不为空，加载新头像
                    Glide.with(getActivity()).load(NewURL).into(avatarImageView);
                }
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击取消按钮时关闭对话框
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void showRechargeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("充值");
        builder.setMessage("请输入充值金额:");
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER); // Allow only numeric input
        builder.setView(input);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 获取用户输入的充值金额
                String rechargeAmount = input.getText().toString();
                // 执行充值任务
                new RechargeTask().execute(rechargeAmount, String.valueOf(MainActivity.UserId));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击取消按钮时关闭对话框
                dialog.cancel();
            }
        });
        builder.show();
    }

    // 异步任务用于执行充值的网络请求
    private class RechargeTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            if (params.length != 2) {
                return null;
            }
            String rechargeAmount = params[0];
            String userId = params[1];
            OkHttpClient client = new OkHttpClient();
            try {
                // 构建请求体
                JSONObject requestBody = new JSONObject();
                // 创建请求对象
                Request request = new Request.Builder()
                        .url("http://47.107.52.7:88/member/tran/goods/recharge?tranMoney="+rechargeAmount+"&userId="+userId)
                        .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody.toString()))
                        .addHeader("appId", "8d7539b50797443788485b81f0660ce1")
                        .addHeader("appSecret", "3636148e54bbef9044a5a8647598c1ee004a4")
                        .build();
                // 发送请求
                Log.d("sb", requestBody.toString());
                Response response = client.newCall(request).execute();
                // 处理响应
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d( "result", result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        showSuccessDialog("充值成功");
                    } else {
                        showFailureDialog(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                showFailureDialog("网络请求失败");
            }
        }
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

            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        showSuccessDialog("修改成功");
                    } else {
                        showFailureDialog(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
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