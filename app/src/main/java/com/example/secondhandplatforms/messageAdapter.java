
package com.example.secondhandplatforms;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.ViewHolder> {
    private List<messageobject> messageList;

    public messageAdapter(){

    }

    private Context context;

    public messageAdapter(Context context, List<messageobject> messageList) {
        this.context = context;
        this.messageList = messageList;
    }
    public messageAdapter(List<messageobject> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.communicationcards, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        messageobject message = messageList.get(position);

        holder.idTextView.setText("ID: " + message.getId());
        holder.fromUserIdTextView.setText("FromUserID: " + message.getFromUserId());
        holder.fromUsernameTextView.setText("FromUsername: " + message.getFromUsername());
        holder.contentTextView.setText("Content: " + message.getContent());
        holder.userIdTextView.setText("UserID: " + message.getUserId());
        holder.usernameTextView.setText("Username: " + message.getUsername());
        holder.statusTextView.setText("Status: " + message.getStatus());
        holder.createTimeTextView.setText("CreateTime: " + message.getCreateTime());
        // 添加其他视图元素的绑定逻辑

        if(message.getStatus().equals("false")) {
            holder.button.setText("标记已读");
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 获取message id
                    int chatId = Integer.parseInt(message.getId());
                    OkHttpClient client = new OkHttpClient();
                    // 发送请求
                    Request request = new Request.Builder()
                            .url("http://47.107.52.7:88/member/tran/chat/change?chatId="+chatId)
                            .addHeader("appId", "8d7539b50797443788485b81f0660ce1")
                            .addHeader("appSecret", "3636148e54bbef9044a5a8647598c1ee004a4")
                            .get()
                            .build();
                    Log.d("tag", String.valueOf(request));
                    client.newCall(request).enqueue(new Callback() {

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                        }

                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("Error", e.getMessage());
                        }
                    });




                }
                // click listener
            });
        } else {
            holder.button.setText("已读");
            holder.button.setClickable(false);
        }


    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView;
        TextView fromUserIdTextView;
        TextView fromUsernameTextView;
        TextView contentTextView;
        TextView userIdTextView;
        TextView usernameTextView;
        TextView statusTextView;
        TextView createTimeTextView;
        Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView);
            fromUserIdTextView = itemView.findViewById(R.id.fromUserIdTextView);
            fromUsernameTextView = itemView.findViewById(R.id.fromUsernameTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            userIdTextView = itemView.findViewById(R.id.userIdTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            createTimeTextView = itemView.findViewById(R.id.createTimeTextView);
            button = itemView.findViewById(R.id.createbutton);

        }
    }
}
