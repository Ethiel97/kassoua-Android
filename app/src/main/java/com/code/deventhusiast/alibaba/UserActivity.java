package com.code.deventhusiast.alibaba;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.code.deventhusiast.alibaba.api.APIClient;
import com.code.deventhusiast.alibaba.api.APIService;
import com.code.deventhusiast.alibaba.fragments.UserCommentsFragment;
import com.code.deventhusiast.alibaba.fragments.UserFavsFragment;
import com.code.deventhusiast.alibaba.fragments.UserProductsFragment;
import com.code.deventhusiast.alibaba.models.Product;
import com.code.deventhusiast.alibaba.models.User;
import com.code.deventhusiast.alibaba.utils.UserSession;
import com.squareup.picasso.Picasso;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    private TextView products_count, favs_count;
    private User user;
    private Toolbar toolbar;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout titleContainer;
    private TextView userEmail, userFullname;
    private CircleImageView user_avatar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDim(true);
        setContentView(R.layout.activity_user);

        findViewById(R.id.touch_outside).setOnClickListener(v -> finish());
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        BottomSheetBehavior.from(findViewById(R.id.bottomSheet)).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        finish();
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        setStatusBarDim(false);
                        break;
                    default:
                        setStatusBarDim(true);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        userEmail = findViewById(R.id.user_email);
        userFullname = findViewById(R.id.user_fullname);
        user_avatar = findViewById(R.id.profile_image);
        products_count = findViewById(R.id.products_count);
        favs_count = findViewById(R.id.favs_count);

        toolbar = findViewById(R.id.toolbar);
        appbar = findViewById(R.id.appbar);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        titleContainer = findViewById(R.id.title_container);

        toolbar.setTitle("");
        toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        collapsingToolbar.setTitleEnabled(false);

//        this.appbar.setExpanded(true);

        this.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (this.scrollRange == -1) {
                    this.scrollRange = appBarLayout.getTotalScrollRange();
                    UserActivity.this.toolbar.setBackgroundDrawable(new ColorDrawable(UserActivity.this.getResources().getColor(R.color.transparent)));
                }

                if (this.scrollRange + verticalOffset == 0) {
                    UserActivity.this.titleContainer.setVisibility(View.GONE);
                    this.isShow = true;
                } else if (this.isShow) {
                    UserActivity.this.toolbar.setBackgroundDrawable(new ColorDrawable(UserActivity.this.getResources().getColor(R.color.transparent)));
                    UserActivity.this.collapsingToolbar.setTitle(" ");
                    UserActivity.this.titleContainer.setVisibility(View.VISIBLE);
                    this.isShow = false;
                }

                if (verticalOffset > 0 && UserActivity.this.bottomNavigationView.isShown()) {
                    UserActivity.this.bottomNavigationView.animate().translationY((float) UserActivity.this.bottomNavigationView.getHeight()).setDuration(200);
                } else if (verticalOffset < 0) {
                    bottomNavigationView.animate().translationY(0);
                }

            }
        });
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        adapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        this.setUserData();

    }

    private void setStatusBarDim(boolean dim) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(dim ? Color.TRANSPARENT : ContextCompat.getColor(this, getThemedRresId(R.attr.colorPrimaryDark)));
        }
    }

    private int getThemedRresId(@AttrRes int attr) {
        TypedArray a = getTheme().obtainStyledAttributes(new int[]{attr});
        int resId = a.getResourceId(0, 0);
        a.recycle();
        return resId;
    }

    private void setUserData() {
        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().useFont(Typeface.SANS_SERIF).
                withBorder(0).width(45).height(45).endConfig().round();

        Bundle extras = getIntent().getExtras();
        this.user = extras.getParcelable("USER");

        TextDrawable drawable = builder.build(user.getLname() + user.getFname(), R.color.colorPrimaryDark);

        userEmail.setText(String.format("%s | %s", user.getEmail(), user.getEnterprise()));
        userFullname.setText(String.format("%s %s", user.getLname(), user.getFname()));

        if (user.getPhoto() != null) {
            Picasso.with(this).load(APIClient.PHOTO_BASE_URL + user.getPhoto()).fit().centerInside().into(user_avatar);
        } else {
            user_avatar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ico_kassoua));

        }
        this.getUserData();

    }

    private void getUserData() {
        User user = UserSession.Instance(this).getUser();
        APIService apiService = APIClient.getRetrofitClient().create(APIService.class);
        Call<List<Product>> call = apiService.getFavoritesList("Bearer " + user.getApiToken());
        call.enqueue(new Callback<List<Product>>() {
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                System.out.println("Nombre de favoris: " + response.body().size());
                UserActivity.this.favs_count.setText(String.valueOf(response.body().size()));
            }

            public void onFailure(Call<List<Product>> call, Throwable t) {
            }
        });

        apiService = APIClient.getRetrofitClient().create(APIService.class);
        Call<List<Product>> call2 = apiService.getUserProducts(user.getId());
        call2.enqueue(new Callback<List<Product>>() {
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    UserActivity.this.products_count.setText(String.valueOf(((List) response.body()).size()));
                }
            }

            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.d("Message :", t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeAsUp:
                finish();
                break;
            case R.id.nav_chat:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class MyAdapter extends FragmentStatePagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            Bundle bundle = new Bundle();
            if (position == 0) {
                f = UserProductsFragment.Instance();
                bundle.putParcelable("user", user);
                f.setArguments(bundle);
            } else if (position == 1) {
                f = UserFavsFragment.Instance();
                bundle.putParcelable("user", user);
                f.setArguments(bundle);
            } else if (position == 2) {
                f = UserCommentsFragment.Instance();
            }
            return f;
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }


}
