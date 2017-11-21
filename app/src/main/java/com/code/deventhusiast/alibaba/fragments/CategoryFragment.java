package com.code.deventhusiast.alibaba.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.code.deventhusiast.alibaba.MainActivity;
import com.code.deventhusiast.alibaba.R;
import com.code.deventhusiast.alibaba.adapters.CategoryAdapter;
import com.code.deventhusiast.alibaba.api.APIClient;
import com.code.deventhusiast.alibaba.api.APIService;
import com.code.deventhusiast.alibaba.models.Category;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment implements MaterialSearchBar.OnSearchActionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerview;
    private Toolbar toolbar;
    private CategoryAdapter adapter;
    private List<Category> categories = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private MaterialSearchBar searchBar;

    private static MainActivity mainActivity = (MainActivity) CategoryFragment.Instance().getActivity();

    private OnFragmentInteractionListener mListener;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static CategoryFragment Instance() {
        return new CategoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout1);
        recyclerview = view.findViewById(R.id.category_recyclerview);

        searchBar = view.findViewById(R.id.searchBar);
        toolbar = view.findViewById(R.id.toolbar);

        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        drawerLayout = getActivity().findViewById(R.id.drawer_layout);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        recyclerview.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(layoutManager);

      /*  DividerItemDecoration verticalDivider = new DividerItemDecoration(getActivity(), VERTICAL);
        verticalDivider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_vertical));

        DividerItemDecoration horizontalDivider = new DividerItemDecoration(getActivity(), HORIZONTAL);
        horizontalDivider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_horizontal));

        recyclerview.addItemDecoration(horizontalDivider);
        recyclerview.addItemDecoration(verticalDivider);*/

//        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), 0));
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout.setRefreshing(true);

        recyclerview.postDelayed(() -> {
            getCategories();
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(false);
        }, 1000);


        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && bottomNavigationView.isShown()) {
                    bottomNavigationView.animate().translationY(bottomNavigationView.getHeight() * 3 / 2).setDuration(200);
                } else if (dy < 0) {
//                    bottomNavigationView.animate().translationY(-1 * (bottomNavigationView.getHeight())).setDuration(200);
                    bottomNavigationView.animate().translationY(0);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }
        });


        //Toolbar setup
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Drawerlayout setup and listener
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        searchBar.setOnSearchActionListener(this);


    }

    private  void getCategories() {
        APIService service = APIClient.getRetrofitClient().create(APIService.class);
        Call<List<Category>> call = service.getCategoriesList();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                categories = response.body();
                adapter = new CategoryAdapter(getActivity(), categories);
                recyclerview.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {
        drawerLayout = getActivity().findViewById(R.id.drawer_layout);
        MaterialSearchBar searchBar = getView().findViewById(R.id.searchBar);
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_NAVIGATION:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case MaterialSearchBar.BUTTON_BACK:
                searchBar.clearAnimation();
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
