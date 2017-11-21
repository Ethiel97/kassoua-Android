package com.code.deventhusiast.alibaba.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.code.deventhusiast.alibaba.R;
import com.code.deventhusiast.alibaba.adapters.ProductAdapter;
import com.code.deventhusiast.alibaba.api.APIClient;
import com.code.deventhusiast.alibaba.api.APIService;
import com.code.deventhusiast.alibaba.models.Product;
import com.code.deventhusiast.alibaba.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProductsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Product> products;
    private ProductAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private User user;


    public UserProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment userProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProductsFragment newInstance(String param1, String param2) {
        UserProductsFragment fragment = new UserProductsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static UserProductsFragment Instance() {
        return new UserProductsFragment();
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
        user = this.getArguments().getParcelable("user");
        View view = inflater.inflate(R.layout.fragment_user_products, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        //Recyclerview setup
        recyclerView = view.findViewById(R.id.products_recyclerview);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        this.getUserData(user);
    }

    public void getUserData(User user) {
//        recyclerView.showShimmerAdapter();
        APIService apiService;

        apiService = (APIService) APIClient.getRetrofitClient().create(APIService.class);
        Call<List<Product>> call2 = apiService.getUserProducts(user.getId());
        call2.enqueue(new Callback<List<Product>>() {
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    products = response.body();
                    adapter = new ProductAdapter(getActivity(), products);
                    recyclerView.setAdapter(adapter);
                    adapter.toggleItemViewType();
                }

            }

            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.d("Message :", t.getMessage());
            }
        });
    }

}
