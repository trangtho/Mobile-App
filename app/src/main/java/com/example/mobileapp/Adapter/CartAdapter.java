package com.example.mobileapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileapp.R;
import com.example.mobileapp.model.Cart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CartAdapter extends  RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<Cart> ListCart;

    public CartAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Cart> list){
        this.ListCart = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent,false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        Cart cart = ListCart.get(position);
        if(cart==null){
            return;
        }

        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open( cart.getProductPicture() +".jpg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Drawable drawable = Drawable.createFromStream(inputStream, null);
        holder.imgItem.setImageDrawable(drawable);

        holder.txtProductName.setText(cart.getProductName());

        holder.txtQuantity.setText(cart.getQuantity());

        holder.txtPrice.setText(cart.getPrice());

    }

    @Override
    public int getItemCount() {
        if(ListCart!=null){
            return ListCart.size();
        }
        return 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgItem;
        private TextView txtProductName;
        private TextView txtQuantity;
        private TextView txtPrice;
        private TextView Delete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            imgItem = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            Delete = itemView.findViewById(R.id.Delete);
            Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }


}