//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.code.deventhusiast.alibaba.fragments;

import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.TextDrawable.IBuilder;
import com.code.deventhusiast.alibaba.R;
import com.code.deventhusiast.alibaba.UserProfileActivity;
import com.code.deventhusiast.alibaba.api.APIClient;
import com.code.deventhusiast.alibaba.api.APIService;
import com.code.deventhusiast.alibaba.models.Product;
import com.code.deventhusiast.alibaba.models.User;
import com.code.deventhusiast.alibaba.utils.UserSession;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private CollapsingToolbarLayout collapsingToolbar;
    private DrawerLayout drawerLayout;
    private LinearLayout linearLayout;
    private LinearLayout linearLayout2;
    private LinearLayout userProfile;
    private LinearLayout favs;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private TextView useremail;
    private TextView userfullname;
    private TextView favs_count;
    private TextView products_count;
    private CircleImageView imageView;
    private SwipeRefreshLayout refreshLayout;
    private NestedScrollView nestedScrollView;
    private BottomNavigationView bottomNavigationView;
    private AccountFragment.OnFragmentInteractionListener mListener;

    public AccountFragment() {
    }

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static AccountFragment Instance() {
        return new AccountFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        if (this.getArguments() != null) {
            this.mParam1 = this.getArguments().getString("param1");
            this.mParam2 = this.getArguments().getString("param2");
        }

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        this.collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.account_collapsingToolbar);
        this.appbar = (AppBarLayout) view.findViewById(R.id.appbar);
        this.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        this.nestedScrollView = (NestedScrollView) view.findViewById(R.id.nested_scroll_view);
        this.linearLayout = (LinearLayout) view.findViewById(R.id.title_container);
        this.linearLayout2 = (LinearLayout) view.findViewById(R.id.account_tabs);
        this.favs = (LinearLayout) view.findViewById(R.id.favs);
        this.bottomNavigationView = (BottomNavigationView) this.getActivity().findViewById(R.id.bottom_navigation);
        this.imageView = (CircleImageView) view.findViewById(R.id.profile_image);
        this.useremail = (TextView) view.findViewById(R.id.user_email);
        this.userfullname = (TextView) view.findViewById(R.id.user_fullname);
        this.favs_count = (TextView) view.findViewById(R.id.favs_count);
        this.products_count = (TextView) view.findViewById(R.id.products_count);
        this.userProfile = (LinearLayout) view.findViewById(R.id.user_profile);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.toolbar.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.transparent)));
        this.drawerLayout = (DrawerLayout) this.getActivity().findViewById(R.id.drawer_layout);
        ActionBar actionBar = ((AppCompatActivity) this.getActivity()).getSupportActionBar();
        if (actionBar != null) {
            ((AppCompatActivity) this.getActivity()).setSupportActionBar(this.toolbar);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this.getActivity(), this.drawerLayout, this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        IBuilder builder = TextDrawable.builder().beginConfig().useFont(Typeface.SANS_SERIF).withBorder(0).endConfig().round();
        if (UserSession.Instance(this.getActivity()).isLoggedIn()) {
            String lname = UserSession.Instance(this.getActivity()).getUser().getFname();
            String fname = UserSession.Instance(this.getActivity()).getUser().getLname();
            String userEmail = UserSession.Instance(this.getActivity()).getUser().getEmail();
            String userEnterprise = UserSession.Instance(this.getActivity()).getUser().getEnterprise();
            String photo = UserSession.Instance(this.getActivity()).getUser().getPhoto();
            TextDrawable drawable = builder.build(lname.substring(0, 1) + fname.substring(0, 1), R.color.colorPrimary);

            if (photo != null) {
                if (photo.length() == APIClient.PHOTO_BASE_URL.length()) {
                    this.imageView.setImageDrawable(drawable);
                } else {
                    Picasso.with(this.getActivity()).load(photo).into(this.imageView);
                }
            } else {
                this.imageView.setImageDrawable(drawable);
            }

            this.useremail.setText(String.format("%s | %s", userEmail, userEnterprise));
            this.userfullname.setText(String.format("%s %s", lname, fname));
            this.imageView.setBackground(drawable);
        }

        this.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
            startActivity(intent);
        });

        this.collapsingToolbar.setTitle(" ");
        this.appbar.setExpanded(true);
        this.appbar.addOnOffsetChangedListener(new OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (this.scrollRange == -1) {
                    this.scrollRange = appBarLayout.getTotalScrollRange();
                    AccountFragment.this.toolbar.setBackgroundDrawable(new ColorDrawable(AccountFragment.this.getResources().getColor(R.color.transparent)));
                }

                if (this.scrollRange + verticalOffset == 0) {
                    AccountFragment.this.collapsingToolbar.setTitle("Mon compte Kassoua");
                    AccountFragment.this.linearLayout.setVisibility(View.GONE);
                    AccountFragment.this.linearLayout2.setVisibility(View.GONE);
                    this.isShow = true;
                } else if (this.isShow) {
                    AccountFragment.this.toolbar.setBackgroundDrawable(new ColorDrawable(AccountFragment.this.getResources().getColor(R.color.transparent)));
                    AccountFragment.this.collapsingToolbar.setTitle(" ");
                    AccountFragment.this.linearLayout.setVisibility(View.VISIBLE);
                    AccountFragment.this.linearLayout2.setVisibility(View.VISIBLE);
                    this.isShow = false;
                }

                if (verticalOffset > 0 && AccountFragment.this.bottomNavigationView.isShown()) {
                    AccountFragment.this.bottomNavigationView.animate().translationY((float) AccountFragment.this.bottomNavigationView.getHeight()).setDuration(200);
                } else if (verticalOffset < 0) {
                    bottomNavigationView.animate().translationY(0);
                }

            }
        });
        this.getUserData();
    }

    public void onButtonPressed(Uri uri) {
        if (this.mListener != null) {
            this.mListener.onFragmentInteraction(uri);
        }

    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AccountFragment.OnFragmentInteractionListener) {
            this.mListener = (AccountFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.account_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                String[] items = new String[]{"Non", "Oui"};
                Builder builder = new Builder(this.getActivity());
                builder.setTitle("Deconnexion");
                builder.setMessage("Etes vous certain de vouloir vous deconnecter ?");
                builder.setItems(items, (dialogInterface, i) -> {
                    if (items[i].equals("Oui")) {
                        UserSession.Instance(getActivity()).logout();
                        Intent intent = getActivity().getIntent();
                        getActivity().finish();
                        startActivity(intent);
                    } else {
                        dialogInterface.dismiss();
                    }
                });
            case R.id.edit:
                break;
            case R.id.nav_settings:
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void getUserData() {
        User user = UserSession.Instance(this.getActivity()).getUser();
        APIService apiService = (APIService) APIClient.getRetrofitClient().create(APIService.class);
        Call<List<Product>> call = apiService.getFavoritesList("Bearer " + user.getApiToken());
        call.enqueue(new Callback<List<Product>>() {
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                System.out.println("Nombre de favoris: " + ((List) response.body()).size());
                AccountFragment.this.favs_count.setText(String.valueOf(((List) response.body()).size()));
            }

            public void onFailure(Call<List<Product>> call, Throwable t) {
            }
        });
        apiService = (APIService) APIClient.getRetrofitClient().create(APIService.class);
        Call<List<Product>> call2 = apiService.getUserProducts(user.getId());
        call2.enqueue(new Callback<List<Product>>() {
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    AccountFragment.this.products_count.setText(String.valueOf(((List) response.body()).size()));
                }

            }

            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.d("Message :", t.getMessage());
            }
        });
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri var1);
    }
}
