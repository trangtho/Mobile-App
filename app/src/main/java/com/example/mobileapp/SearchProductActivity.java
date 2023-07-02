package com.example.mobileapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mobileapp.Adapter.ProductAdapter;
import com.example.mobileapp.Adapter.SearchProductAdapter;
import com.example.mobileapp.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchProductActivity extends AppCompatActivity {

    RecyclerView rcvSearchProduct;
    private SearchProductAdapter searchproductAdapter;
    String search_text = "";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        rcvSearchProduct = (RecyclerView) findViewById(R.id.rcvSearchProduct);
        searchproductAdapter = new SearchProductAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvSearchProduct.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();

        if (intent != null) {
            search_text = intent.getStringExtra("search_text");
        }


        searchproductAdapter.setData(getListProduct());
        rcvSearchProduct.setAdapter(searchproductAdapter);
    }

    private List<Product> getListProduct() {
        List<Product> productList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("Product");

        collectionRef
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();

                            for (DocumentSnapshot document : documents) {
                                String picture = document.getString("Picture");
                                String productName = document.getString("ProductName");
                                String price = document.getString("ProductPrice");

                                if (productName.toLowerCase().contains(search_text.toLowerCase())) {
                                    Product product = new Product(picture, productName, price);
                                    productList.add(product);
                                }
                                searchproductAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


        return productList;
    }
}