package com.example.mobileapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mobileapp.HomeActivity;
import com.example.mobileapp.LoginActivity;
import com.example.mobileapp.R;
import com.example.mobileapp.ViewpageActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button btnLogOut;

    TextView txtFullName, txtPhone, txtAddress;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        String userEmail = getEmail();
        LoadInfo(userEmail);





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btnLogOut = (Button) view.findViewById(R.id.buttonLogOut);
        txtFullName = (TextView) view.findViewById(R.id.textView);
        txtPhone = (TextView) view.findViewById(R.id.textView2);
        txtAddress = (TextView) view.findViewById(R.id.textView4);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    private String getEmail() {
        SharedPreferences preferences = getActivity().getSharedPreferences("UserEmail", Context.MODE_PRIVATE);
        return preferences.getString("User_Email", "");
    }

    private void LoadInfo(String userEmail){
        CollectionReference usersCollection = db.collection("Users");
        usersCollection.whereEqualTo("Email", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);

                            String fullname = document.getString("FullName");
                            String phone = document.getString("Phone");
                            String address = document.getString("Address");


                            txtFullName.setText(fullname);
                            txtPhone.setText(phone);
                            txtAddress.setText(address);


                    }
                });
    }

}