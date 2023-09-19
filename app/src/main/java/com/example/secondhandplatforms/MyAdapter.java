package com.example.secondhandplatforms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<MyDataItem> dataItemList;

    public MyAdapter(List<MyDataItem> dataItemList) {
        this.dataItemList = dataItemList;
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
    }

    @Override
    public int getItemCount() {
        return dataItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle;
        public TextView textPrice;
        public TextView textAddress;

        public ViewHolder(View view) {
            super(view);
            textTitle = view.findViewById(R.id.textTitle);
            textPrice = view.findViewById(R.id.textPrice);
            textAddress = view.findViewById(R.id.textAddress);
        }
    }
}
