package com.example.mobileapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mobileapp.Adapter.CartAdapter;
import com.example.mobileapp.DeliveryActivity;
import com.example.mobileapp.ProductDetailActivity;
import com.example.mobileapp.R;
import com.example.mobileapp.model.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btnBuyNow;

    private RecyclerView rcvCart;

    private CartAdapter cartAdapter;
    private TextView totalPrice;

    private String ProductInfo = "";
    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        btnBuyNow = (Button) view.findViewById(R.id.button_PayForCartList);

        rcvCart = view.findViewById(R.id.rcvCart);
        cartAdapter = new CartAdapter(getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rcvCart.setLayoutManager(linearLayoutManager);

        cartAdapter.setData(getListCart());
        rcvCart.setAdapter(cartAdapter);

        totalPrice = view.findViewById(R.id.textView_PaymentValue);
        calculateTotalPrice();

        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(getActivity(), DeliveryActivity.class);
                intent.putExtra("total_price", sum);
                startActivity(intent);*/
                Intent intent_Login = new Intent(getActivity(), DeliveryActivity.class);
                intent_Login.putExtra("Price_Product", String.valueOf(sum));
                intent_Login.putExtra("ProductInfo", ProductInfo);
                startActivity(intent_Login);
            }
        });

        return view;
    }

    private List<Cart> getListCart() {
        List<Cart> list = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String email = getEmail();
        CollectionReference usersCollection = db.collection("Cart");
        usersCollection.whereEqualTo("Email", email)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                List<DocumentSnapshot> documents = querySnapshot.getDocuments();

                                for (DocumentSnapshot document : documents) {
                                    String productID = document.getString("Product_ID");
                                    String quantity = document.getString("Quantity");

                                    CollectionReference collectionRef = db.collection("Product");
                                    collectionRef.document(productID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document != null && document.exists()) {
                                                    String productname = document.getString("ProductName");

                                                    String productName = document.getString("ProductName");
                                                    String price =document.getString("ProductPrice");
                                                    String picture = document.getString("Picture");
                                                    ProductInfo=ProductInfo+"Product name: " + productName + "\n Quantity: " + quantity + "\n";
                                                    list.add(new Cart(picture, productName, quantity, price));
                                                    cartAdapter.notifyDataSetChanged();
                                                } else {
                                                    Log.d("TAG", "Error getting document: ", task.getException());
                                                }
                                            } else {
                                                Log.d("TAG", "Error getting document: ", task.getException());
                                            }
                                        }
                                    });


                                }
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return list;
    }

    private String getEmail() {
        SharedPreferences preferences = getActivity().getSharedPreferences("UserEmail", Context.MODE_PRIVATE);
        return preferences.getString("User_Email", "");
    }

    public int sum;

    private void calculateTotalPrice() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String email = getEmail();
        CollectionReference usersCollection = db.collection("Cart");
        usersCollection.whereEqualTo("Email", email)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                List<DocumentSnapshot> documents = querySnapshot.getDocuments();

                                //int sum = 0; // Khởi tạo biến sum ở đây

                                for (DocumentSnapshot document : documents) {
                                    // Lấy dữ liệu từ Firebase
                                    String productID = document.getString("Product_ID");
                                    String quantity = document.getString("Quantity");
                                    CollectionReference collectionRef = db.collection("Product");
                                    collectionRef.document(productID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document != null && document.exists()) {
                                                    String price = document.getString("ProductPrice");
                                                    int intQuantity = Integer.parseInt(quantity);
                                                    int intPrice = Integer.parseInt(price);
                                                    int total = intPrice * intQuantity;
                                                    sum = sum + total;
                                                    processTotalPrice(sum);
                                                } else {
                                                    Log.d("TAG", "Error getting document: ", task.getException());
                                                }
                                            } else {
                                                Log.d("TAG", "Error getting document: ", task.getException());
                                            }
                                        }
                                    });
                                }
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void processTotalPrice(int totalprice) {
        Log.d("TAG", "Total price: " + totalprice);
        totalPrice.setText(String.valueOf(totalprice));
    }
}