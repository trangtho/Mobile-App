package com.example.mobileapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Dialog;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {


    Button btnBuyNow, btnAddToCart;

    ImageView Image, imgFavorite;
    TextView txtPrice, txtProductName, txtDescription;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String ProductInfo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_product_detail);
        btnBuyNow = (Button) findViewById(R.id.button_buy_now);
        btnAddToCart = (Button) findViewById(R.id.button_add_to_cart);
        imgFavorite = (ImageView) findViewById(R.id.imgFavorite);

        Image = (ImageView) findViewById(R.id.image_product);
        txtPrice = (TextView) findViewById(R.id.text_view_product_price);
        txtProductName = (TextView) findViewById(R.id.text_view_product_name);
        txtDescription = (TextView) findViewById(R.id.text_view_product_title);



        Intent intent = getIntent();
        String productName = "";
        if (intent != null) {
            productName = intent.getStringExtra("product_name");
        }
        ShowDetail(productName);
        Log.d(TAG,  " => " + productName);

        ProductInfo = "Product name: " + productName + "\n Quantity: 1";

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("UserEmail", MODE_PRIVATE);
                String userEmail = preferences.getString("User_Email", "");

                String productName = txtProductName.getText().toString();
                String price = txtPrice.getText().toString();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionProduct = db.collection("Product");

                Query query = collectionProduct.whereEqualTo("ProductName", productName);
                query.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    String productId = documentSnapshot.getId();

                                    // Kiểm tra sản phẩm trong giỏ hàng
                                    CollectionReference cartCollection = db.collection("Cart");
                                    Query cartQuery = cartCollection.whereEqualTo("Product_ID", productId)
                                            .whereEqualTo("Email", userEmail);

                                    cartQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot cartQuerySnapshot) {
                                                    if (!cartQuerySnapshot.isEmpty()) {
                                                        // Sản phẩm đã tồn tại trong giỏ hàng, tăng số lượng lên 1 đơn vị
                                                        DocumentSnapshot cartDocument = cartQuerySnapshot.getDocuments().get(0);
                                                        String cartId = cartDocument.getId();
                                                        long quantity = Long.parseLong(cartDocument.getString("Quantity")) +1;

                                                        // Cập nhật số lượng sản phẩm trong giỏ hàng
                                                        cartCollection.document(cartId).update("Quantity", String.valueOf(quantity))
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        LayoutInflater inflater = getLayoutInflater();
                                                                        View layout = inflater.inflate(R.layout.custom_toast_layout, null);
                                                                        Toast toast = new Toast(getApplicationContext());
                                                                        toast.setDuration(Toast.LENGTH_SHORT);
                                                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                                                        toast.setView(layout);
                                                                        toast.show();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        // Xử lý khi cập nhật không thành công
                                                                    }
                                                                });
                                                    } else {
                                                        // Sản phẩm chưa tồn tại trong giỏ hàng, thêm mới sản phẩm
                                                        Map<String, Object> cartData = new HashMap<>();
                                                        cartData.put("Product_ID", productId);
                                                        cartData.put("Quantity", "1");
                                                        cartData.put("Email", userEmail);

                                                        cartCollection.add(cartData)
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                        LayoutInflater inflater = getLayoutInflater();
                                                                        View layout = inflater.inflate(R.layout.custom_toast_layout, null);
                                                                        Toast toast = new Toast(getApplicationContext());
                                                                        toast.setDuration(Toast.LENGTH_SHORT);
                                                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                                                        toast.setView(layout);
                                                                        toast.show();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        // Xử lý khi thêm mới không thành công
                                                                    }
                                                                });
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Xử lý khi không thể truy vấn giỏ hàng
                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Xử lý khi không thể lấy dữ liệu từ Firestore
                            }
                        });
            }
        });


        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("UserEmail", MODE_PRIVATE);
                String userEmail = preferences.getString("User_Email", "");

                String productName = txtProductName.getText().toString();
                String price = txtPrice.getText().toString();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionProduct = db.collection("Product");

                Query query = collectionProduct.whereEqualTo("ProductName", productName);
                query.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    String ProductId = documentSnapshot.getId();
                                    CollectionReference CartCollection = db.collection("Favorite");

                                    Map<String, Object> Favorite = new HashMap<>();
                                    Favorite.put("Product_ID", ProductId);
                                    Favorite.put("Quantity", "1");
                                    Favorite.put("Email", userEmail);

                                    db.collection("Favorite").add(Favorite);

                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.custom_toast_layout, null);

                                    Toast toast = new Toast(getApplicationContext());
                                    toast.setDuration(Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.setView(layout);
                                    toast.show();

                                }
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Xử lý khi không thể lấy dữ liệu từ Firestore
                            }
                        });


            }
        });


        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("UserEmail", MODE_PRIVATE);
                String userEmail = preferences.getString("User_Email", "");

                String productName = txtProductName.getText().toString();
                String price = txtPrice.getText().toString();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionProduct = db.collection("Product");

                Query query = collectionProduct.whereEqualTo("ProductName", productName);
                query.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    String productId = documentSnapshot.getId();

                                    // Kiểm tra sản phẩm trong giỏ hàng
                                    CollectionReference cartCollection = db.collection("Cart");
                                    Query cartQuery = cartCollection.whereEqualTo("Product_ID", productId)
                                            .whereEqualTo("Email", userEmail);

                                    cartQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot cartQuerySnapshot) {
                                                    if (!cartQuerySnapshot.isEmpty()) {
                                                        // Sản phẩm đã tồn tại trong giỏ hàng, tăng số lượng lên 1 đơn vị
                                                        DocumentSnapshot cartDocument = cartQuerySnapshot.getDocuments().get(0);
                                                        String cartId = cartDocument.getId();
                                                        long quantity = Long.parseLong(cartDocument.getString("Quantity")) +1;

                                                        // Cập nhật số lượng sản phẩm trong giỏ hàng
                                                        cartCollection.document(cartId).update("Quantity", String.valueOf(quantity))
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        LayoutInflater inflater = getLayoutInflater();
                                                                        View layout = inflater.inflate(R.layout.custom_toast_layout, null);
                                                                        Toast toast = new Toast(getApplicationContext());
                                                                        toast.setDuration(Toast.LENGTH_SHORT);
                                                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                                                        toast.setView(layout);
                                                                        toast.show();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        // Xử lý khi cập nhật không thành công
                                                                    }
                                                                });
                                                    } else {
                                                        // Sản phẩm chưa tồn tại trong giỏ hàng, thêm mới sản phẩm
                                                        Map<String, Object> cartData = new HashMap<>();
                                                        cartData.put("Product_ID", productId);
                                                        cartData.put("Quantity", "1");
                                                        cartData.put("Email", userEmail);

                                                        cartCollection.add(cartData)
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                        LayoutInflater inflater = getLayoutInflater();
                                                                        View layout = inflater.inflate(R.layout.custom_toast_layout, null);
                                                                        Toast toast = new Toast(getApplicationContext());
                                                                        toast.setDuration(Toast.LENGTH_SHORT);
                                                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                                                        toast.setView(layout);
                                                                        toast.show();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        // Xử lý khi thêm mới không thành công
                                                                    }
                                                                });
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Xử lý khi không thể truy vấn giỏ hàng
                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Xử lý khi không thể lấy dữ liệu từ Firestore
                            }
                        });
            }
        });


        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_Login = new Intent(ProductDetailActivity.this, DeliveryActivity.class);
                intent_Login.putExtra("Price_Product", txtPrice.getText().toString());
                intent_Login.putExtra("ProductInfo", ProductInfo);
                startActivity(intent_Login);

            }
        });
    }

    private void ShowDetail(String x) {
        CollectionReference productCollection = db.collection("Product");
        productCollection.whereEqualTo("ProductName", x)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);

                        // Kiểm tra xem trường "ProductName" có tồn tại trong tài liệu hay không
                        if (document.contains("ProductName")) {
                            String productname = document.getString("ProductName");
                            String description = document.getString("Description");
                            String price = document.getString("ProductPrice");
                            String image = document.getString("Picture");
                            String Category = document.getString("Category");

                            InputStream inputStream = null;
                            try {
                                inputStream = getAssets().open(image +".jpg");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Drawable drawable = Drawable.createFromStream(inputStream, null);
                            Image.setImageDrawable(drawable);
                            txtPrice.setText(price);
                            txtProductName.setText(productname);
                            txtDescription.setText(description);

                        }
                    }
                });
    }

}