

package com.example.secondhandplatforms;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        // 设置产品信息
        holder.productNameTextView.setText(product.getUsername());
        holder.productDescriptionTextView.setText(product.getContent());
        holder.userInfoTextView.setText(product.getCreateTime());

        // 使用 Picasso 加载产品图片
        if (product.getImageUrlList() != null && product.getImageUrlList().size() > 0) {
            Picasso.get()
                    .load(product.getImageUrlList().get(0)) // 加载第一个图像URL
                    .placeholder(R.drawable.ic_launcher_background) // 在加载时显示占位图像
                    .into(holder.productImageView);
        } else {
            // 处理没有图像URL的情况
            holder.productImageView.setImageResource(R.drawable.ic_launcher_background);
        }

        // 添加点击事件处理器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个 Intent 用于启动 ProductInformation Activity
                Context context = v.getContext();
                Intent intent = new Intent(context, ProductInformation.class);
                // 将选定的产品数据添加到 Intent 中
                intent.putExtra("productId", product.getId());
                // 启动 Activity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productDescriptionTextView;
        TextView userInfoTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.iv_product_image);
            productNameTextView = itemView.findViewById(R.id.tv_product_name);
            productDescriptionTextView = itemView.findViewById(R.id.tv_product_description);
            userInfoTextView = itemView.findViewById(R.id.tv_user_info);
        }
    }
}