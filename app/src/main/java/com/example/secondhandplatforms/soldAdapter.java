package com.example.secondhandplatforms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class soldAdapter extends RecyclerView.Adapter<soldAdapter.ViewHolder> {

    private List<MyDataItem> dataItemList;
    private OnItemClickListener onItemClickListener;

    public soldAdapter(List<MyDataItem> dataItemList) {
        this.dataItemList = dataItemList;
    }

    // 定义一个接口用于处理点击事件
    public interface OnItemClickListener {

    }

    // 设置点击事件监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sold_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyDataItem dataItem = dataItemList.get(position);
        holder.textTitle.setText(dataItem.getContent());
        holder.textPrice.setText("¥" + dataItem.getPrice());

        holder.textCreateTime.setText(dataItem.getCreateTime());
        holder.textBuyerName.setText(dataItem.getBuyerName());

    }

    @Override
    public int getItemCount() {
        return dataItemList.size();
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle;
        public TextView textPrice;
        public TextView textAddress;

        public TextView textBuyerName;
        public  TextView textCreateTime;

        public ViewHolder(View view) {
            super(view);
            textTitle = view.findViewById(R.id.textTitle);
            textPrice = view.findViewById(R.id.textPrice);
            textBuyerName = view.findViewById(R.id.textBuyerName);
            textAddress = view.findViewById(R.id.textAddress);
            textCreateTime = view.findViewById(R.id.textCreateTime);
        }
    }
}
