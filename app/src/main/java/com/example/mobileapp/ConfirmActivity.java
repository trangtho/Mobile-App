package com.example.mobileapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ConfirmActivity extends AppCompatActivity {

    String temporaryPrice ="", ShipFee = "", TotalPrice ="", PaymentMethod ="", DeliveryMeothod ="", ProductInfo="";
    Button btnFinish;
    TextView txtInfo, txtAddress, txtPaymentMetohd, textViewTemFee2, textViewTranFee2, textViewTotalFee2, textView14, txtProductInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        btnFinish = (Button) findViewById(R.id.buttonFinish);
        txtInfo = (TextView) findViewById(R.id.textViewDetail);
        txtAddress = (TextView) findViewById(R.id.textViewAddress);

        textView14 = (TextView) findViewById(R.id.textView14);
        txtPaymentMetohd = (TextView) findViewById(R.id.textView17);
        textViewTemFee2 = (TextView) findViewById(R.id.textViewTemFee2);
        textViewTranFee2 = (TextView) findViewById(R.id.textViewTranFee2);
        textViewTotalFee2 = (TextView) findViewById(R.id.textViewTotalFee2);
        txtProductInfo = (TextView) findViewById(R.id.txtProductInfo);

        Intent intent_Receive = getIntent();
        if (intent_Receive != null) {
            temporaryPrice = intent_Receive.getStringExtra("TemporaryPrice");
            ShipFee = intent_Receive.getStringExtra("ShipFee");
            TotalPrice = intent_Receive.getStringExtra("Total_Price");
            PaymentMethod = intent_Receive.getStringExtra("Payment_Method");
            DeliveryMeothod = intent_Receive.getStringExtra("Delevery_Method");
            ProductInfo = intent_Receive.getStringExtra("ProductInfo");
        }

        txtPaymentMetohd.setText(PaymentMethod);
        textViewTemFee2.setText(temporaryPrice);
        textViewTranFee2.setText(ShipFee);
        textViewTotalFee2.setText(TotalPrice);
        textView14.setText(DeliveryMeothod);
        txtProductInfo.setText(ProductInfo);



        showInfoCustomer();
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_Login = new Intent(ConfirmActivity.this, HomeActivity.class);
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast_layout, null);
                addToBill();
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setView(layout);
                toast.show();
                startActivity(intent_Login);
            }
        });
    }

    private void deleteCart(){
        SharedPreferences preferences = getSharedPreferences("UserEmail", MODE_PRIVATE);
        String userEmail = preferences.getString("User_Email", "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("Cart");

        collectionRef.whereEqualTo("Email", userEmail)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            // Xóa từng tài liệu
                            document.getReference().delete();
                        }
                        // Hoàn thành xóa các tài liệu
                        Log.d("TAG", "Xóa tài liệu thành công");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi nếu không thể truy vấn tài liệu
                        Log.w("TAG", "Lỗi khi truy vấn tài liệu", e);
                    }
                });
    }
    private void addToBill(){
        SharedPreferences preferences = getSharedPreferences("UserEmail", MODE_PRIVATE);
        String userEmail = preferences.getString("User_Email", "");

        // Lấy ngày giờ hiện tại
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Định dạng ngày giờ
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        // Chuyển đổi ngày giờ thành chuỗi
        String formattedDate = dateFormat.format(currentDate);
        String formattedTime = timeFormat.format(currentDate);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionProduct = db.collection("Bill");

        Map<String, Object> Data = new HashMap<>();

        Data.put("Email", userEmail);
        Data.put("Total",TotalPrice );
        Data.put("Delivery_method", DeliveryMeothod);
        Data.put("Date", formattedDate);
        Data.put("Time", formattedTime);
        Data.put("Address", txtAddress.getText());
        // Add a new document with a generated ID
        db.collection("Bill")
                .add(Data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


    }

    private void showInfoCustomer(){
        SharedPreferences preferences = getSharedPreferences("UserEmail", MODE_PRIVATE);
        String userEmail = preferences.getString("User_Email", "");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionUser = db.collection("Users");

        collectionUser.whereEqualTo("Email", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);

                        // Kiểm tra xem trường "ProductName" có tồn tại trong tài liệu hay không
                        if (document.contains("Email")) {
                            String fullname = document.getString("FullName");
                            String phone = document.getString("Phone");
                            String tmp = fullname + " - " + phone;
                            String address = document.getString("Address");
                            txtInfo.setText(tmp);
                            txtAddress.setText(address);

                        }
                    }
                });
    }

}