package com.example.mobileapp.fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mobileapp.Adapter.CartAdapter;
import com.example.mobileapp.Adapter.ProductAdapter;
import com.example.mobileapp.HomeActivity;
import com.example.mobileapp.LoginActivity;
import com.example.mobileapp.ProductDetailActivity;
import com.example.mobileapp.R;
import com.example.mobileapp.SearchProductActivity;
import com.example.mobileapp.ViewpageActivity;
import com.example.mobileapp.model.Cart;
import com.example.mobileapp.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ImageView imgPhone;
    public ImageView imgLaptop;
    public ImageView imgSound;
    public ImageView imgOther;

    ImageView imgItem1;

    private RecyclerView rcvProduct, rcvBestSellingProduct, rcvDiscountProduct;
    private ProductAdapter productAdapter, BestSellingProductAdapter, DiscountProductAdapter;
    private EditText editSearchText;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        imgPhone = (ImageView) view.findViewById(R.id.imageView_MobileCategory);
        imgLaptop = (ImageView) view.findViewById(R.id.imageView_LaptopCategory);
        imgSound = (ImageView) view.findViewById(R.id.imageView_SoundCategory);
        imgOther = (ImageView) view.findViewById(R.id.imageView_OtherCategory);
        editSearchText = view.findViewById(R.id.Input_Search_Home);

        rcvProduct = view.findViewById(R.id.rcvProduct);
        productAdapter = new ProductAdapter(getActivity());

        rcvBestSellingProduct = view.findViewById(R.id.rcvBestProduct);
        BestSellingProductAdapter = new ProductAdapter(getActivity());

        rcvDiscountProduct = view.findViewById(R.id.rcvDiscountProduct);
        DiscountProductAdapter = new ProductAdapter(getActivity());


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rcvProduct.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rcvBestSellingProduct.setLayoutManager(linearLayoutManager1);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rcvDiscountProduct.setLayoutManager(linearLayoutManager2);

        productAdapter.setData(getListProduct());
        rcvProduct.setAdapter(productAdapter);

        BestSellingProductAdapter.setData(getListBestSellingProduct());
        rcvBestSellingProduct.setAdapter(BestSellingProductAdapter);

        DiscountProductAdapter.setData(getDiscountProduct());
        rcvDiscountProduct.setAdapter(DiscountProductAdapter);

        editSearchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    // Xử lý sự kiện khi nhấn phím "Enter"
                    Intent intent = new Intent(getActivity(), SearchProductActivity.class);
                    intent.putExtra("search_text", editSearchText.getText().toString());
                    Log.d(TAG, "DocumentSnapshot added with ID: " + editSearchText.getText().toString());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });


        //laptop category
        imgLaptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPhone = new Intent(getActivity(), ViewpageActivity.class);
                String catid = "1";
//                Chỗ này làm sao để lấy user_id từ login
//                intentPhone.putExtra("user_id", id);
                intentPhone.putExtra("category_id", catid);
                startActivity(intentPhone);
            }
        });
        //phone category
        imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPhone = new Intent(getActivity(), ViewpageActivity.class);
                String catid = "2";
//                Chỗ này làm sao để lấy user_id từ login
//                intentPhone.putExtra("user_id", id);
                intentPhone.putExtra("category_id", catid);
                startActivity(intentPhone);

            }
        });
        //sound category
        imgSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPhone = new Intent(getActivity(), ViewpageActivity.class);
                String catid = "3";
//                Chỗ này làm sao để lấy user_id từ login
//                intentPhone.putExtra("user_id", id);
                intentPhone.putExtra("category_id", catid);
                startActivity(intentPhone);

            }
        });
        //other category
        imgOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPhone = new Intent(getActivity(), ViewpageActivity.class);
                String catid = "4";
//                Chỗ này làm sao để lấy user_id từ login
//                intentPhone.putExtra("user_id", id);
                intentPhone.putExtra("category_id", catid);
                startActivity(intentPhone);

            }
        });

//        imgItem1 = (ImageView) view.findViewById(R.id.imageView_NewItem_1_Img);
//        imgItem1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
//                startActivity(intent);
//            }
//        });

        return view;
    }

    private List<Product> getDiscountProduct() {
        List<Product> productList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("Product");

        collectionRef.whereEqualTo("Discount", "1")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();

                            for (DocumentSnapshot document : documents) {
                                String picture = document.getString("Picture");
                                String productName = document.getString("ProductName");
                                String price = document.getString("ProductPrice");

                                Product product = new Product(picture, productName, price);
                                productList.add(product);
                                DiscountProductAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return productList;
    }

    private List<Product> getListBestSellingProduct() {
        List<Product> productList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("Product");

        collectionRef.orderBy("QuantitySold", Query.Direction.DESCENDING).limit(10)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();

                            for (DocumentSnapshot document : documents) {
                                String picture = document.getString("Picture");
                                String productName = document.getString("ProductName");
                                String price = document.getString("ProductPrice");

                                Product product = new Product(picture, productName, price);
                                productList.add(product);
                                BestSellingProductAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return productList;
    }

    private List<Product> getListProduct() {
        List<Product> productList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("Product");

        collectionRef.orderBy("ProductName", Query.Direction.ASCENDING).limit(15)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();

                    for (DocumentSnapshot document : documents) {
                        String picture = document.getString("Picture");
                        String productName = document.getString("ProductName");
                        String price = document.getString("ProductPrice");

                        Product product = new Product(picture, productName, price);
                        productList.add(product);
                        productAdapter.notifyDataSetChanged();
                    }

                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });

        return productList;
    }
}