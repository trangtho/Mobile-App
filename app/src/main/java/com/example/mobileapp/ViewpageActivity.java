package com.example.mobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.mobileapp.Adapter.ProductAdapterList;
import com.example.mobileapp.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mobileapp.Adapter.ProductAdapterList;


public class ViewpageActivity extends AppCompatActivity {

    private RecyclerView rcvProduct;
    private ProductAdapterList ProductAdapterList;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpage);

        rcvProduct = findViewById(R.id.rcvItems);
        ProductAdapterList = new ProductAdapterList(this);

        // Thiết lập LayoutManager cho RecyclerView
        rcvProduct.setLayoutManager(new LinearLayoutManager(this));

        // Thiết lập adapter cho RecyclerView
        rcvProduct.setAdapter(ProductAdapterList);

        // Lấy danh sách sản phẩm và đặt dữ liệu cho adapter\
        Intent intent = getIntent();
        //String id = intent.getStringExtra("category_id");
        String id = "1";
        String categoryId = intent.getStringExtra("category_id");
        productList = getListProduct(categoryId);
        ProductAdapterList.setData(productList);
    }
    private List<Product> getListProduct(String id) {
        List<Product> productList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("Product");

        collectionRef.whereEqualTo("Category", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();

                    for (DocumentSnapshot document : documents) {
                        // Lấy các trường dữ liệu của sản phẩm từ tài liệu Firestore
                        String picture = document.getString("Picture");
                        String productName = document.getString("ProductName");
                        String price = document.getString("ProductPrice");

                        // Tạo đối tượng Product
                        Product product = new Product(picture, productName, price);

                        // Thêm sản phẩm vào danh sách sản phẩm
                        productList.add(product);

                        // Cập nhật adapter
                        ProductAdapterList.notifyDataSetChanged();
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });


        return productList;
    }
}