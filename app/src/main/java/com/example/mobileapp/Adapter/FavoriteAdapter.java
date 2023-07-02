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
import com.example.mobileapp.model.Cart;
import com.example.mobileapp.model.Favorite;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FavoriteAdapter extends  RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private Context context;
    private List<Favorite> ListFavorite;

    public FavoriteAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Favorite> list){
        this.ListFavorite = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent,false);

        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {

        Favorite favorite = ListFavorite.get(position);
        if(favorite==null){
            return;
        }

        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open( favorite.getProductPicture() +".jpg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Drawable drawable = Drawable.createFromStream(inputStream, null);
        holder.imgItem.setImageDrawable(drawable);

        holder.txtProductName.setText(favorite.getProductName());

        holder.txtPrice.setText(favorite.getPrice());

    }

    @Override
    public int getItemCount() {
        if(ListFavorite!=null){
            return ListFavorite.size();
        }
        return 0;
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgItem;
        private TextView txtProductName;
        private TextView txtPrice;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            imgItem = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("product_name", txtProductName.getText());
                    context.startActivities(new Intent[]{intent});
                }
            });
        }
    }
}