package com.code.deventhusiast.alibaba.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.code.deventhusiast.alibaba.R;
import com.code.deventhusiast.alibaba.adapters.CategoryAdapter;
import com.code.deventhusiast.alibaba.adapters.ProductAdapter;
import com.code.deventhusiast.alibaba.api.APIClient;
import com.code.deventhusiast.alibaba.api.APIService;
import com.code.deventhusiast.alibaba.models.Category;
import com.code.deventhusiast.alibaba.models.Product;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.polyak.iconswitch.IconSwitch;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsFragment extends Fragment implements MaterialSearchBar.OnSearchActionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Toolbar toolbar;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter adapter;
    List<Product> products = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout, swipeRefreshLayout1;
    private ShimmerRecyclerView recyclerView;
    private RecyclerView category_recyclerView;
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private MaterialSearchBar searchBar;
    private IconSwitch iconSwitch;
    private NestedScrollView nestedScrollView;


    public ProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static synchronized ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public static synchronized ProductsFragment Instance() {
//        return new ProductsFragment();
        return new ProductsFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
       /* MaterialSearchBar searchBar = getActivity().findViewById(R.id.searchBar);
        searchBar.setVisibility(View.GONE);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        //SwipeRefreshLayout
//        nestedScrollView = view.findViewById(R.id.nested_scroll_view);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout1 = view.findViewById(R.id.swipeRefreshLayout1);
        swipeRefreshLayout1.setEnabled(false);

        //Recyclerview setup
        recyclerView = view.findViewById(R.id.products_recyclerview);
        category_recyclerView = view.findViewById(R.id.category_recyclerview);

//        recyclerView.hideShimmerAdapter();

        //SearchBar
        searchBar = view.findViewById(R.id.searchBar);
        toolbar = view.findViewById(R.id.toolbar);

        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        drawerLayout = getActivity().findViewById(R.id.drawer_layout);

        iconSwitch = view.findViewById(R.id.icon_switch);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        category_recyclerView.setItemAnimator(new DefaultItemAnimator());
        category_recyclerView.setHasFixedSize(true);
        category_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));

        this.getCategories();
//        nestedScrollView.setNestedScrollingEnabled(false);

        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.showShimmerAdapter();
//        swipeRefreshLayout.setRefreshing(true);

        swipeRefreshLayout.postDelayed(() -> {
            getProductsList();
            swipeRefreshLayout.setRefreshing(false);
        }, 2500);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        //searchbar listener
        searchBar.setOnSearchActionListener(this);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        //swiperefresh listener
        swipeRefreshLayout.setOnRefreshListener(() -> {
            recyclerView.showShimmerAdapter();
            swipeRefreshLayout.postDelayed(() -> {
                this.getProductsList();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                swipeRefreshLayout.setRefreshing(false);
            }, 800);

        });
       /* if (adapter.getItemCount() == 0) {
            iconSwitch.setEnabled(false);
        } else {
            iconSwitch.setEnabled(true);
        }*/

        //iconSwitch listener
        iconSwitch.setCheckedChangeListener(current -> {
            boolean isSwitched;
            switch (current) {
                case LEFT:
                    isSwitched = adapter.toggleItemViewType();
                    recyclerView.setLayoutManager(isSwitched ? new GridLayoutManager(getActivity(), 2) : new LinearLayoutManager(getActivity()));
                    recyclerView.setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.LINEAR_HORIZONTAL);
                    adapter.notifyDataSetChanged();
                    Toasty.info(getActivity(), "Vue en Listes", Toast.LENGTH_LONG).show();
                    break;
                case RIGHT:
                    isSwitched = adapter.toggleItemViewType();
                    recyclerView.setLayoutManager(isSwitched ? new GridLayoutManager(getActivity(), 2) : new LinearLayoutManager(getActivity()));
                    adapter.notifyDataSetChanged();
                    Toasty.info(getActivity(), "Vue en grilles", Toast.LENGTH_LONG).show();
                    break;
                default:
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    adapter.notifyDataSetChanged();
                    Toasty.info(getActivity(), "Vue en Grilles", Toast.LENGTH_LONG).show();
                    break;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getCategories() {
        APIService service = APIClient.getRetrofitClient().create(APIService.class);
        Call<List<Category>> call = service.getCategoriesList();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                categories = response.body();
                categoryAdapter = new CategoryAdapter(getActivity(), categories);
                category_recyclerView.setAdapter(categoryAdapter);
                categoryAdapter.toggleItemViewType();
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toasty.warning(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getProductsList() {
//        recyclerView.showShimmerAdapter();
        APIService service = APIClient.getRetrofitClient().create(APIService.class);
        Call<List<Product>> call = service.getProductsList();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                products = response.body();
                adapter = new ProductAdapter(getActivity(), products);
                recyclerView.hideShimmerAdapter();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
    }


}
