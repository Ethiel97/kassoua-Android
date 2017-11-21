package com.code.deventhusiast.alibaba;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.code.deventhusiast.alibaba.api.APIClient;
import com.code.deventhusiast.alibaba.fragments.AccountFragment;
import com.code.deventhusiast.alibaba.fragments.CategoryFragment;
import com.code.deventhusiast.alibaba.fragments.ProductsFragment;
import com.code.deventhusiast.alibaba.utils.UserSession;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import net.danlew.android.joda.JodaTimeAndroid;

import java.lang.reflect.Field;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private RecyclerView products_recylclerview;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private MaterialSearchBar searchBar;

    private TextView login, signup;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JodaTimeAndroid.init(this);
        setContentView(R.layout.activity_main);

        searchBar = findViewById(R.id.searchBar);
        displaySelectedFragment(R.id.nav_explore);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_explore);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_explore);

        setUserData();

        disableShiftMode(bottomNavigationView);

        /*toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitle(getResources().getString(R.string.app_name));

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            setSupportActionBar(toolbar);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();*/

        //
        navigationView.setNavigationItemSelectedListener(item -> {


            if (bottomNavigationView.getMenu().findItem(item.getItemId()) != null) {
                bottomNavigationView.getMenu().findItem(item.getItemId()).setChecked(true);
            } else {
                MenuItem menuItem = bottomNavigationView.getMenu().findItem(bottomNavigationView.getSelectedItemId());
                menuItem.setChecked(false);
                int[] state = {menuItem.isChecked() ? android.R.attr.state_checked : android.R.attr.state_enabled};
                menuItem.getIcon().setTint(getResources().getColor(R.color.textColorPrimaryDark));
//                bottomNavigationView.getMenu().findItem(bottomNavigationView.getSelectedItemId()).setChecked(false);
            }
//                bottomNavigationView.getSelectedItemId()
            /*  MenuItem item1 = (MenuItem) bottomNavigationView.findViewById(item.getItemId());
            item1.setChecked(true);*/
//            ((MenuItem) bottomNavigationView.findViewById(item.getItemId())).setChecked(true);
            if (!item.isChecked()) {
                item.setChecked(true);
                displaySelectedFragment(item.getItemId());
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });

        //BottomNavigationView click Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            if (item.getItemId() != R.id.fab_publish) {
                navigationView.getMenu().findItem(item.getItemId()).setChecked(true);
            }

            if (bottomNavigationView.getSelectedItemId() != item.getItemId()) {
                item.setChecked(true);
//            ((MenuItem) navigationView.findViewById(item.getItemId())).setChecked(true);
                displaySelectedFragment(item.getItemId());
            }
          /*  if (!item.isChecked()) {
                item.setChecked(true);
//            ((MenuItem) navigationView.findViewById(item.getItemId())).setChecked(true);
                displaySelectedFragment(item.getItemId());
            }*/
            return false;
        });

//        initRecyclerView();

        /*View headerLayout = navigationView.getHeaderView(0);
        login = headerLayout.findViewById(R.id.login);
        signup = headerLayout.findViewById(R.id.signup);*/


    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        super.onBackPressed();
    }

    private void displaySelectedFragment(int itemId) {
        Fragment currentFragment = null;
        switch (itemId) {
            case R.id.fab_publish:
                if (UserSession.Instance(this).isLoggedIn()) {
                    Intent intent = new Intent(this, NewProductActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                return;
            case R.id.nav_explore:
                currentFragment = ProductsFragment.Instance();
                break;
            case R.id.nav_categories:
                currentFragment = CategoryFragment.Instance();
                break;
            case R.id.nav_chat:
                break;
            case R.id.nav_account:
                currentFragment = AccountFragment.Instance();
                break;
            case R.id.nav_favs:
                break;
            case R.id.nav_qrcode_scan:
                break;
            case R.id.nav_converter:
                break;
            case R.id.nav_faq:
                break;
            case R.id.nav_settings:
                break;
          /*  case R.id.nav_share:
                break;*/
            default:
                break;
        }
        if (currentFragment != null && !currentFragment.isAdded()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.replace(R.id.fragment_container, currentFragment).commit();
            return;
        }
        Toasty.error(getApplicationContext(), "Pas de fragments", Toast.LENGTH_SHORT).show();
    }

/*  private void loadJSON() {

        APIService service = APIClient.getRetrofitClient().create(APIService.class);

        Call<List<Product>> call = service.getProductsList();

        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Product<ArrayList<Product>> response) {
                ArrayList<Product> products = response.body();
                ProductAdapter adapter = new ProductAdapter(getApplicationContext(), products);
                RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                products_recylclerview.setLayoutManager(gridLayoutManager);
                products_recylclerview.setItemAnimator(new DefaultItemAnimator());
                products_recylclerview.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {

            }
        });

    }
*/

    // Method for disabling ShiftMode of BottomNavigationView
    @SuppressLint("RestrictedApi")
    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }

    private void setUserData() {
        View headerLayout = navigationView.getHeaderView(0);
        TextView useremail = headerLayout.findViewById(R.id.user_email);
        TextView userfullname = headerLayout.findViewById(R.id.user_fullname);
        LinearLayout nav_header_logged = headerLayout.findViewById(R.id.nav_header_logged);
        LinearLayout nav_header_guest = headerLayout.findViewById(R.id.nav_header_guest);

        de.hdodenhof.circleimageview.CircleImageView imageView = headerLayout.findViewById(R.id.profile_link);

        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().useFont(Typeface.SANS_SERIF).withBorder(0).endConfig().rect();

        if (UserSession.Instance(getApplicationContext()).isLoggedIn()) {
            nav_header_logged.setVisibility(View.VISIBLE);
            nav_header_guest.setVisibility(View.GONE);
            String lname = UserSession.Instance(getApplicationContext()).getUser().getFname();
            String fname = UserSession.Instance(getApplicationContext()).getUser().getLname();
            String photo = UserSession.Instance(getApplicationContext()).getUser().getPhoto();
            useremail.setText(UserSession.Instance(getApplicationContext()).getUser().getEmail());

            TextDrawable drawable = builder.build(lname.substring(0, 1) + fname.substring(0, 1), R.color.colorPrimaryDark);
            //set user profile
            if (photo != null) {
                if (photo.length() == APIClient.PHOTO_BASE_URL.length()) {
                    imageView.setImageDrawable(drawable);
                } else {
                    Picasso.with(this).load(photo).into(imageView);
                }
            } else {
                imageView.setImageDrawable(drawable);
            }

            userfullname.setText(String.format("%s %s", lname, fname));

//            return;
        }

        //
        imageView.setOnClickListener(v -> {
            navigationView.getMenu().findItem(R.id.nav_account).setChecked(true);
            bottomNavigationView.getMenu().findItem(R.id.nav_account).setChecked(true);
            displaySelectedFragment(R.id.nav_account);
        });

        login = headerLayout.findViewById(R.id.login);
        signup = headerLayout.findViewById(R.id.signup);

        login.setOnClickListener(view -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        signup.setOnClickListener(view -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
        });

        nav_header_logged.setOnClickListener(view -> {
            UserSession.Instance(getApplicationContext()).logout();
            Intent intent = getIntent();
//            progressDialog.dismiss();
            finish();
            startActivity(intent);
            String apiToken = UserSession.Instance(getApplicationContext()).getUser().getApiToken();
         /*
            APIService apiService = APIClient.getRetrofitClient().create(APIService.class);
            Call<Result> call = apiService.userLogout(apiToken);
            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {

                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {

                }
            });*/
            Log.d("Listener", "yes");
        });
    }
}
