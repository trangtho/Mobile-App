package com.example.mobileapp;
import com.example.mobileapp.model.User;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    public Button btnLogIn;
    public EditText inputEmail, inputPass;
    public TextView txtSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);

        btnLogIn = (Button) findViewById(R.id.Button_Login);
        inputEmail = (EditText) findViewById(R.id.Input_Email);
        inputPass = (EditText) findViewById(R.id.Input_Password);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = inputEmail.getText().toString();
                String pass = inputPass.getText().toString();


                // Tham chiếu đến collection "Users"
                CollectionReference usersCollection = db.collection("Users");

                usersCollection.whereEqualTo("Email", email).limit(1)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);

                            // Kiểm tra xem trường "password" có tồn tại trong tài liệu hay không
                            if (document.contains("HashedPassword")) {
                                // Lấy mật khẩu từ trường "password"
                                String password = document.getString("HashedPassword");

                                boolean isPasswordMatched = BCrypt.checkpw(pass, password);
                                if(isPasswordMatched){
                                    // thông báo dăng nhập thành công
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 255, 128)));
                                    TextView textMessage = dialog.findViewById(R.id.text_notify);
                                    String id = document.getId();
                                    textMessage.setText("Đăng nhập thành công");
                                    dialog.show();

                                    // Đóng Dialog sau vài giây
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                        }
                                    }, 5000);

                                    //direct to homepage

                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.putExtra("user_id", id);
                                    startActivity(intent);


                                    String EMAIL= document.getString("Email");

                                    SharedPreferences preferences = getSharedPreferences("UserEmail", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("User_Email", EMAIL);
                                    editor.apply();
                                } else{
                                    // thông báo sai mật khẩu
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
                                    TextView textMessage = dialog.findViewById(R.id.text_notify);
                                    textMessage.setText("Sai mật khẩu");
                                    dialog.show();

                                    // Đóng Dialog sau vài giây
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                        }
                                    }, 5000);
                                }
                            } else {
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
                                TextView textMessage = dialog.findViewById(R.id.text_notify);
                                textMessage.setText("Không tồn tại");
                                dialog.show();

                                // Đóng Dialog sau vài giây
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                }, 5000);
                            }
                        } else {
                            // thông báo lỗi bất ngờ
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
                            TextView textMessage = dialog.findViewById(R.id.text_notify);
                            textMessage.setText("Lỗi");
                            dialog.show();

                            // Đóng Dialog sau vài giây
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                }
                            }, 5000);
                        }
                    });
                //check password

            }
        });
        /*btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });*/

        txtSignUp = (TextView) findViewById(R.id.TextView_SignIn);
        txtSignUp.setOnClickListener(
            new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent_Login = new Intent(LoginActivity.this, RegistationActivity.class);
                    startActivity(intent_Login);
                }
            }
        );
    }
}