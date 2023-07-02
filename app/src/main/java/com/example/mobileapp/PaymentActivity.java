package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {

    String temporaryPrice ="", ShipFee = "", TotalPrice ="", PaymentMethod ="", DeliveryMethod ="", ProductInfo="";
    Button btnNextPayment;
    TextView textViewTemFee, textViewTranFee, textViewTotalFee;
    RadioGroup RdoPaymentMethod;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        btnNextPayment = (Button) findViewById(R.id.ButtonNextPayment);
        textViewTemFee = (TextView) findViewById(R.id.textViewTemFee);
        textViewTranFee = (TextView) findViewById(R.id.textViewTranFee);
        textViewTotalFee = (TextView) findViewById(R.id.textViewTotalFee);

        RdoPaymentMethod = (RadioGroup) findViewById(R.id.PaymentMethod);


        Intent intent_Receive = getIntent();
        if (intent_Receive != null) {
            temporaryPrice = intent_Receive.getStringExtra("TemporaryPrice");
            ShipFee = intent_Receive.getStringExtra("ShipFee");
            TotalPrice = intent_Receive.getStringExtra("Total_Price");
            DeliveryMethod = intent_Receive.getStringExtra("Delevery_Method");
            ProductInfo = intent_Receive.getStringExtra("ProductInfo");
        }

        textViewTemFee.setText(temporaryPrice);
        textViewTranFee.setText(ShipFee);
        textViewTotalFee.setText(TotalPrice);


        RdoPaymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.CashPayment:
                        PaymentMethod = "Cash Payment";
                        break;
                    case R.id.ATMCard:
                        PaymentMethod = "Internet Banking";
                        break;
                    case R.id.EWallet:
                        PaymentMethod = "E-Wallet";
                        break;
                }

            }
        });


        btnNextPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this, ConfirmActivity.class);
                intent.putExtra("TemporaryPrice", temporaryPrice);
                intent.putExtra("ShipFee", ShipFee);
                intent.putExtra("Total_Price", TotalPrice);
                intent.putExtra("Payment_Method", PaymentMethod);
                intent.putExtra("Delevery_Method", DeliveryMethod);
                intent.putExtra("ProductInfo", ProductInfo);
                startActivity(intent);
            }
        });

    }
}