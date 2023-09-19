package com.example.secondhandplatforms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<MyDataItem> dataItemList;
    private OnItemClickListener onItemClickListener;

    public MyAdapter(List<MyDataItem> dataItemList) {
        this.dataItemList = dataItemList;
    }

    // 定义一个接口用于处理点击事件
    public interface OnItemClickListener {
        void onItemClick(MyDataItem dataItem);
        void onPublishClick(MyDataItem dataItem);
    }

    // 设置点击事件监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyDataItem dataItem = dataItemList.get(position);

        holder.textTitle.setText(dataItem.getContent());
        holder.textPrice.setText("¥" + dataItem.getPrice());
        holder.textAddress.setText(dataItem.getAddr());

        // 设置项目的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(dataItem);
                }
            }
        });

        // 设置发布按钮的点击事件
        holder.buttonPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onPublishClick(dataItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle;
        public TextView textPrice;
        public TextView textAddress;
        public Button buttonPublish; // 发布按钮

        public ViewHolder(View view) {
            super(view);
            textTitle = view.findViewById(R.id.textTitle);
            textPrice = view.findViewById(R.id.textPrice);
            textAddress = view.findViewById(R.id.textAddress);
            buttonPublish = view.findViewById(R.id.buttonPublish); // 发布按钮
        }
    }
}
