package com.example.mobileapp.fragment;

import android.content.Context;
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

import com.example.mobileapp.Adapter.CartAdapter;
import com.example.mobileapp.Adapter.FavoriteAdapter;
import com.example.mobileapp.R;
import com.example.mobileapp.model.Cart;
import com.example.mobileapp.model.Favorite;
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
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView rcvFavorite;

    private FavoriteAdapter FavoriteAdapter;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
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
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        rcvFavorite = view.findViewById(R.id.rcvFavorite);

        FavoriteAdapter = new FavoriteAdapter(getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rcvFavorite.setLayoutManager(linearLayoutManager);

        FavoriteAdapter.setData(getListFavorite());
        rcvFavorite.setAdapter(FavoriteAdapter);
        return view;

    }
    private List<Favorite> getListFavorite() {
        List<Favorite> list = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String email = getEmail();
        CollectionReference usersCollection = db.collection("Favorite");
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
                                                    //String productname = document.getString("ProductName");

                                                    String productName = document.getString("ProductName");
                                                    String price =document.getString("ProductPrice");
                                                    String picture = document.getString("Picture");
                                                    list.add(new Favorite(picture, productName, price));
                                                    FavoriteAdapter.notifyDataSetChanged();
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


}