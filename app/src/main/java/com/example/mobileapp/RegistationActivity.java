package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobileapp.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class RegistationActivity extends AppCompatActivity {

    public FloatingActionButton btnBack;
    public Button btnSignUp;
    public TextView txtSignIn;
    public EditText inputRegisEmail, inputRegisPass, inputRegisConfirmPass, inputRegisPhone, inputRegisAddress, inputRegisFullname;
    public boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    // Kiểm tra password
    public boolean isPasswordConfirmed(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);

        btnBack = (FloatingActionButton) findViewById(R.id.floatingActionButton_Back);
        btnSignUp = (Button) findViewById(R.id.Button_SignUp);
        inputRegisEmail = (EditText) findViewById(R.id.Input_RegisterEmail);
        inputRegisPass = (EditText) findViewById(R.id.Input_RegisterPassword);
        inputRegisConfirmPass = (EditText) findViewById(R.id.Input_ConfirmPassword);
        inputRegisAddress = (EditText) findViewById(R.id.Input_Address);
        inputRegisPhone = (EditText) findViewById(R.id.Input_Phone);
        inputRegisFullname = (EditText) findViewById(R.id.Input_Fullname);

        btnBack.setOnClickListener(
            new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivity(new Intent(RegistationActivity.this, LoginActivity.class));
                }
            }
        );
        txtSignIn = (TextView) findViewById(R.id.TextView_SignIn);
        txtSignIn.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        startActivity(new Intent(RegistationActivity.this, LoginActivity.class));
                    }
                }
        );
        btnSignUp.setOnClickListener(
            new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    String email = inputRegisEmail.getText().toString();
                    String pass = inputRegisPass.getText().toString();
                    String confirm = inputRegisConfirmPass.getText().toString();
                    String phone = inputRegisPhone.getText().toString();
                    String address = inputRegisAddress.getText().toString();
                    String fullname = inputRegisFullname.getText().toString();


                    if(isValidEmail(email)){
                        if(isPasswordConfirmed(pass, confirm)){
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            CollectionReference usersCollection = db.collection("Users");
                            Query query = usersCollection.whereEqualTo("Email", email).limit(1);

                            // Thực hiện truy vấn
                            query.get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    boolean emailExists = !task.getResult().isEmpty();

                                    if (emailExists) {
                                        // Email tồn tại trong Firestore
                                        // thông báo dăng ký thất bại
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
                                        TextView textMessage = dialog.findViewById(R.id.text_notify);
                                        textMessage.setText("Email đã tồn tại");
                                        dialog.show();

                                        // Đóng Dialog sau vài giây
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                            }
                                        }, 5000);
                                    } else {
                                        //Mã hoá pass
                                        String hashedPassword = BCrypt.hashpw(pass, BCrypt.gensalt(6));

                                        //Thêm user
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("Email", email);
                                        user.put("HashedPassword", hashedPassword);
                                        user.put("Address", address);
                                        user.put("FullName", fullname);
                                        user.put("Phone", phone);
                                        // Add a new document with a generated ID

                                        //Thêm user vào db
                                        db.collection("Users").add(user);

                                        // thông báo dăng ký thành công
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 255, 128)));
                                        TextView textMessage = dialog.findViewById(R.id.text_notify);
                                        textMessage.setText("Đăng ký thành công");
                                        dialog.show();

                                        // Đóng Dialog sau vài giây
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                            }
                                        }, 5000);
                                        // direct to login
                                        startActivity(new Intent(RegistationActivity.this, LoginActivity.class));
                                    }
                                } else {
                                    // thông báo dăng ký thất bại
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
                        }else{
                            // thông báo dăng ký thất bại
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
                            TextView textMessage = dialog.findViewById(R.id.text_notify);
                            textMessage.setText("Sai password");
                            dialog.show();

                            // Đóng Dialog sau vài giây
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                }
                            }, 5000);
                        }
                    }else {
                        // thông báo dăng ký thất bại
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
                        TextView textMessage = dialog.findViewById(R.id.text_notify);
                        textMessage.setText("Sai email");
                        dialog.show();

                        // Đóng Dialog sau vài giây
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 5000);
                    }
                }
            }
        );
    }
}