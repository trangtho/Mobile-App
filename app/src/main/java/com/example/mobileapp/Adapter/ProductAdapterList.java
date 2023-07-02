package com.example.mobileapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileapp.ProductDetailActivity;
import com.example.mobileapp.R;
import com.example.mobileapp.model.Product;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ProductAdapterList extends RecyclerView.Adapter<ProductAdapterList.ViewHolder>{
    private Context context;
    private List<Product> ListProduct;

    public ProductAdapterList(Context context) {
        this.context = context;
    }

    public void setData(List<Product> listProduct ){
        this.ListProduct = listProduct;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_product, parent,false);

        return new ProductAdapterList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product product = ListProduct.get(position);
        if(product==null){
            return;
        }

        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open( product.getImage() +".jpg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Drawable drawable = Drawable.createFromStream(inputStream, null);
        holder.productImg.setImageDrawable(drawable);

        holder.ProductName.setText(product.getProductName());
        holder.Price.setText(product.getPrice());

    }

    @Override
    public int getItemCount() {
        return ListProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private ImageView productImg;
        private TextView ProductName;
        private TextView Price;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImg = itemView.findViewById(R.id.imgProduct);
            ProductName = itemView.findViewById(R.id.txtproductName);
            Price = itemView.findViewById(R.id.txtPrice);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product_name", ProductName.getText());
            context.startActivities(new Intent[]{intent});
        }
    }









}