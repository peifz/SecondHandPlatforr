package com.example.secondhandplatforms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {

    private List<TransactionsData> transactionList;
    private Context context;

    public TransactionsAdapter(List<TransactionsData> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.transactions_card, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransactionsData transaction = transactionList.get(position);

        // Set data to the views
        holder.textTitle.setText(transaction.getGoodsDescription());
        holder.textSeller.setText("卖家: " + transaction.getSellerName());
        holder.textBuyer.setText("买家: " + transaction.getBuyerName());
        holder.textPrice.setText("价格: ￥" + transaction.getPrice());
        holder.textDescription.setText(transaction.getGoodsDescription());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textTitle;
        TextView textSeller;
        TextView textBuyer;
        TextView textPrice;
        TextView textDescription;

        TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textSeller = itemView.findViewById(R.id.textSeller);
            textBuyer = itemView.findViewById(R.id.textBuyer);
            textPrice = itemView.findViewById(R.id.textPrice);
            textDescription = itemView.findViewById(R.id.textDescription);
        }
    }
}
