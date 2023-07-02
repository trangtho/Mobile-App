package com.example.mobileapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.StringValue;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;

public class DeliveryActivity extends AppCompatActivity {
    String tempPrice = "", DeliveryMethod = "", ProductInfo="";

    Button btnNextDelivery;
    TextView txtShippingFee, txtTotal, txtTemporary;
    EditText edtName, edtPhone, edtAddress;
    RadioGroup RdoShipping;
    int total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        btnNextDelivery = (Button) findViewById(R.id.btnNext);
        txtShippingFee = (TextView) findViewById(R.id.txtShippingFee);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtTemporary = (TextView) findViewById(R.id.txtTemporaryPrice);

        edtName = (EditText) findViewById(R.id.editFullName);
        edtPhone = (EditText) findViewById(R.id.editPhone);
        edtAddress =(EditText) findViewById(R.id.editAdress);
        RdoShipping = (RadioGroup) findViewById(R.id.RdoShipping);

        Intent intent_Receive = getIntent();
        if (intent_Receive != null) {
            tempPrice = intent_Receive.getStringExtra("Price_Product");
            ProductInfo = intent_Receive.getStringExtra("ProductInfo");
        }

        txtTemporary.setText(tempPrice);

        //Log.d(TAG,"radio : "+ tempPrice);
        // function
        showInfoCustomer();

        btnNextDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeliveryActivity.this, PaymentActivity.class);
                intent.putExtra("TemporaryPrice", tempPrice);
                intent.putExtra("ShipFee", txtShippingFee.getText().toString());
                intent.putExtra("Total_Price", txtTotal.getText().toString());
                intent.putExtra("Delevery_Method", DeliveryMethod);
                intent.putExtra("ProductInfo", ProductInfo);
                startActivity(intent);
            }
        });
        RdoShipping.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioFast: {
                        txtShippingFee.setText("150000");
                        DeliveryMethod = "Fast";
                        TotalPrice();
                        break;
                    }
                    case R.id.radioVeryFast: {
                        txtShippingFee.setText("200000");
                        DeliveryMethod = "Very Fast";
                        TotalPrice();
                        break;
                    }
                    case R.id.radioHoaToc: {
                        txtShippingFee.setText("300000");
                        DeliveryMethod = "Express";
                        TotalPrice();
                        break;
                    }
                }
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
                            String address = document.getString("Address");
                            edtName.setText(fullname);
                            edtPhone.setText(phone);
                            edtAddress.setText(address);

                        }
                    }
                });
    }

    private void TotalPrice(){

        int number = Integer.parseInt(tempPrice);
        String ShipFee = txtShippingFee.getText().toString();
        int ShipFeeNumber = Integer.parseInt(ShipFee);

        int total = number + ShipFeeNumber;

        txtTotal.setText(String.valueOf(total));


    }


}