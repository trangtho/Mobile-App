package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class TestActivity extends AppCompatActivity {
    Button btnAdd;

    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btnAdd = (Button) findViewById(R.id.buttonAdd);

        btnAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> user = new HashMap<>();
                        user.put("first", "Ada");
                        user.put("last", "Lovelace");
                        user.put("born", 1815);
                        db.collection("User").add(user);
                    }
                }
        );
    }
}