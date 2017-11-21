package com.code.deventhusiast.alibaba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.code.deventhusiast.alibaba.api.APIClient;
import com.code.deventhusiast.alibaba.api.APIService;
import com.code.deventhusiast.alibaba.models.Category;
import com.code.deventhusiast.alibaba.models.Product;
import com.code.deventhusiast.alibaba.models.ProductPhotosItem;
import com.code.deventhusiast.alibaba.models.User;
import com.code.deventhusiast.alibaba.utils.DateUtil;
import com.code.deventhusiast.alibaba.utils.UserSession;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private User user;
    private Category category;
    private static Product product;
    /*  private BottomSheetBehavior
              bottomSheetBehavior;
      private CoordinatorLayout bottomSheetLayout;*/
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton love_fab, share_fab, comment_fab;
    private ArrayList<ProductPhotosItem> productPhotosItems;
    private List<Product> favoriteProducts = new ArrayList<>();
    private TextView user_fullname, category_name;
    private CardView cardView1, cardView2;
    public static int LOVE_PRODUCT = 1;
    ImageView imageView;
    CircleImageView userImageView, categoryImage, profileImage;

    private TextView titleTextView, descriptionTextView, priceTextView, min_quantityTextView, countryTextView, createdAtTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        profileImage = findViewById(R.id.profile_image);

        cardView1 = findViewById(R.id.cardView_1);
        cardView2 = findViewById(R.id.cardView_2);

        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);

        toolbar = findViewById(R.id.toolbar);

        toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        love_fab = findViewById(R.id.fab_love);
        share_fab = findViewById(R.id.fab_share);
        comment_fab = findViewById(R.id.fab_comment);


        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().useFont(Typeface.SANS_SERIF).
                withBorder(0).width(45).height(45).endConfig().round();

        Bundle extras = getIntent().getExtras();

        user = extras.getParcelable("USER");
        product = extras.getParcelable("PRODUCT");
        productPhotosItems = extras.getParcelableArrayList("PHOTOS");
        category = extras.getParcelable("CATEGORY");

        String username = user.getFname().substring(0, 1) + user.getLname().substring(0, 1);

        category_name = findViewById(R.id.category_name);
        categoryImage = findViewById(R.id.category_image);
        imageView = findViewById(R.id.product_image);
        titleTextView = findViewById(R.id.title);
        descriptionTextView = findViewById(R.id.description);
        priceTextView = findViewById(R.id.price);
        min_quantityTextView = findViewById(R.id.min_quantity);
        countryTextView = findViewById(R.id.country);
        createdAtTextView = findViewById(R.id.created_at);
        userImageView = findViewById(R.id.user_image);
        user_fullname = findViewById(R.id.user_fullname);

        collapsingToolbarLayout.setTitle(product.getTitle());

        Picasso.with(this).load(APIClient.PHOTO_BASE_URL + productPhotosItems.get(0).getFilename()).into(imageView);
        titleTextView.setText(product.getTitle());
        descriptionTextView.setText(product.getDescription());
        priceTextView.setText(String.format("%s FCFA/pieces", String.valueOf(product.getPrice())));
        min_quantityTextView.setText(String.valueOf(product.getMinQuantity()));
        countryTextView.setText(user.getCountry());
        createdAtTextView.setText(String.format("il y a %s", DateUtil.dateToElapsed(product.getCreatedAt())));
        user_fullname.setText(String.format("%s %s", user.getLname(), user.getFname()));

        category_name.setText(category.getName());

        if (category != null) {
            Picasso.with(this).load(APIClient.PHOTO_BASE_URL + category.getImage()).fit().centerInside().into(categoryImage);
        } else {
            Picasso.with(this).load(R.drawable.ico_kassoua).into(categoryImage);
        }

        TextDrawable drawable = builder.build(username, R.color.colorPrimaryDark);

        String photos = user.getPhoto();

        if (user.getPhoto() != null) {
            Picasso.with(this).load(APIClient.PHOTO_BASE_URL + user.getPhoto()).fit().centerInside().into(userImageView);

        } else {
            userImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ico_kassoua));
        }

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        love_fab.setOnClickListener(this);
        share_fab.setOnClickListener(this);
        comment_fab.setOnClickListener(this);
        this.getUserFavorites();

        LinearLayout root = findViewById(R.id.linear_root);
      /*  userImageView.bringToFront();
        root.requestLayout();
        root.invalidate();*/

        userImageView.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserActivity.class);
            Bundle bundle = new Bundle();

            bundle.putParcelable("USER", user);
            bundle.putParcelable("CATEGORY", category);
            intent.putExtras(bundle);

            startActivity(intent);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.account_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        APIService apiService;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Ajout a vos favoris en cours ...\n");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(false);
//        progressDialog.show();
        switch (view.getId()) {
            case R.id.fab_love:
                // redirect user to login page if not logged in simply
                apiService = APIClient.getRetrofitClient().create(APIService.class);
                if (!UserSession.Instance(getBaseContext()).isLoggedIn()) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent, LOVE_PRODUCT);
                    return;
                }

                if (favoriteProducts.isEmpty()) {
                    Call<String> call = apiService.loveProduct(user.getApiToken(), ProductDetailActivity.product.getId());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.body().equals("Success")) {
//                                    progressDialog.dismiss();
                                if (favoriteProducts.add(product)) {

                                    love_fab.setImageResource(R.drawable.ic_favorite_plain);
                                    Toasty.success(getApplicationContext(), "Produit ajoute aux favoris", Toast.LENGTH_LONG).show();
                                }
                            } else {
//                                    progressDialog.dismiss();
                                Toasty.info(getApplicationContext(), "Nous n'avons pas pu ajouter ce produit a vos favoris ", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
//                                progressDialog.dismiss();
                            Log.d("Erreur", t.getMessage());
                            Toasty.info(getApplicationContext(), "Nous n'avons pas pu effectuer cette action.\n Veuilez verifier votre connexion internet svp", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    for (Product favoriteProduct : favoriteProducts) {
                        //Supprimer le produit des favoris de l'utilisateur

                        if (favoriteProduct.getId() == ProductDetailActivity.product.getId()) {
                            Call<String> call = apiService.unloveProduct(ProductDetailActivity.product.getId(), "Bearer " + user.getApiToken());
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.isSuccessful() || response.body().equals("Success")) {
                                        love_fab.setImageResource(R.drawable.ic_favorite);

                                        if (favoriteProducts.remove(favoriteProduct))
                                            Toasty.success(getApplicationContext(), "Produit supprime de vos favoris", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d("Message", t.getMessage());
                                    Toasty.info(getApplicationContext(), "Nous n'avons pas pu effectuer cette action.\n Veuilez verifier votre connexion internet svp", Toast.LENGTH_LONG).show();
                                }
                            });
//                        break;
                        } //Sinon l'y ajouter tout simplement
                        else {

                            Call<String> call = apiService.loveProduct(user.getApiToken(), ProductDetailActivity.product.getId());
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.body().equals("Success")) {
//                                    progressDialog.dismiss();
                                        if (favoriteProducts.add(favoriteProduct)) {

                                            love_fab.setImageResource(R.drawable.ic_favorite_plain);
                                            Toasty.success(getApplicationContext(), "Produit ajoute aux favoris", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
//                                    progressDialog.dismiss();
                                        Toasty.info(getApplicationContext(), "Nous n'avons pas pu ajouter ce produit a vos favoris ", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
//                                progressDialog.dismiss();
                                    Toasty.info(getApplicationContext(), "Nous n'avons pas pu effectuer cette action.\n Veuilez verifier votre connexion internet svp", Toast.LENGTH_LONG).show();
                                    Log.d("Erreur", t.getMessage());
                                }
                            });
//                        break;
                        }
                    }
                }
                break;
            case R.id.fab_share: {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, productPhotosItems.get(0).getFilename());
                if (shareIntent.resolveActivity(getPackageManager()) != null)
                    startActivity(Intent.createChooser(shareIntent, "Partager ce produit"));
                break;
            }
            case R.id.fab_comment:
                break;

        }
    }

    public void getUserFavorites() {
        if (UserSession.Instance(getApplicationContext()).isLoggedIn()) {
            APIService apiService = APIClient.getRetrofitClient().create(APIService.class);
            Call<List<Product>> call = apiService.getFavoritesList("Bearer " + user.getApiToken());
            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    favoriteProducts = response.body();
                    if (favoriteProducts != null) {
                        for (Product favoriteProduct : favoriteProducts) {
                            if (favoriteProduct.getId() == ProductDetailActivity.product.getId()) {
                                love_fab.setImageResource(R.drawable.ic_favorite_plain);
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
//                    Log.d("Message", t.getMessage());
                }
            });

        }
    }
}
