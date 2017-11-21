package com.code.deventhusiast.alibaba;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.code.deventhusiast.alibaba.adapters.CategorySpinnerAdapter;
import com.code.deventhusiast.alibaba.api.APIClient;
import com.code.deventhusiast.alibaba.api.APIService;
import com.code.deventhusiast.alibaba.models.Category;
import com.code.deventhusiast.alibaba.models.Product;
import com.code.deventhusiast.alibaba.models.User;
import com.code.deventhusiast.alibaba.utils.UserSession;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewProductActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private List categories;
    private LinearLayout image_container, product_image_placeholder;
    private Spinner categorySpinner;
    private Integer GALLERY = 0, CAMERA = 1;
    private String categoryId;
    private File photoPath;
    private EditText price, category, min_quantity, title, description, userEnterprise;
    private ImageView product_image;
    private User user = UserSession.Instance(this).getUser();
    CategorySpinnerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        categorySpinner = findViewById(R.id.categorySpinner);
        toolbar = findViewById(R.id.toolbar);
        price = findViewById(R.id.product_price);
        min_quantity = findViewById(R.id.product_min_quantity);
        title = findViewById(R.id.product_title);
        description = findViewById(R.id.product_description);
        userEnterprise = findViewById(R.id.user_enterprise);
        image_container = findViewById(R.id.product_image_container);
        product_image_placeholder = findViewById(R.id.product_image_placeholder);
        product_image = findViewById(R.id.product_image);

        toolbar.setTitle("Publier un produit");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            setSupportActionBar(toolbar);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        userEnterprise.setText(UserSession.Instance(this).getUser().getEnterprise());

        this.getCategories();

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = view.findViewById(R.id.category_id);
//                String id = textView.getText().toString();
                categoryId = textView.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        image_container.setOnClickListener(v -> {
            String[] items = {"Prendre une photo", "Choisir une image"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Ajouter  une image")
                    .setItems(items, (dialogInterface, i) -> {
                        if (items[i].equals("Prendre une photo"))
                            takePhotoFromCamera();
                        else
                            choosePhotoFromGallery();
                    });

            builder.show();
        });
        categorySpinner.setSelection(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_product_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_product: {

                String price = this.price.getText().toString(),
                        min_quantity = this.min_quantity.getText().toString(),
                        description = this.description.getText().toString(),
                        title = this.title.getText().toString();

                if (product_image.getDrawable() == null) {
                    Toasty.info(this, "Veuillez choisir une iamge pour votre produit", Toast.LENGTH_LONG).show();
                    break;
                } else {
                    if (!isValid(price, min_quantity, title, description)) {
                        break;
                    }
                }


                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Publication de votre produit...\nVeuillez Patienter svp");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
                this.addProduct(price, min_quantity, title, description, this.categoryId);
                progressDialog.dismiss();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;

        } else if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                Toasty.normal(getBaseContext(), contentURI.toString(), Toast.LENGTH_LONG).show();
                image_container.setPadding(0, 0, 0, 0);
                product_image_placeholder.setVisibility(View.GONE);
                product_image.setImageURI(contentURI);
                product_image.setVisibility(View.VISIBLE);
               /* Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
//                    String path = saveImage(bitmap);
                String path = this.getPath(contentURI);
                photoPath = new File(path);
                Toasty.info(NewProductActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
//                    photo.setImageBitmap(bitmap);
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            product_image.setImageBitmap(thumbnail);
//            saveImage(thumbnail);
            Toasty.info(NewProductActivity.this, "Image enregistree!", Toast.LENGTH_SHORT).show();

        }
    }

    private void getCategories() {
        APIService service = APIClient.getRetrofitClient().create(APIService.class);
        Call<List<Category>> call = service.getCategoriesList();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                categories = response.body();
                adapter = new CategorySpinnerAdapter(getApplicationContext(), R.layout.category_item_spinner, categories, getResources());
                categorySpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

                Toasty.error(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean isValid(String price, String min_quantity, String title, String description) {
//        loginButton.setEnabled(false);
        boolean valid = true;
        if (price.isEmpty()) {
            valid = false;
            this.price.setError("Veuillez renseigner le prix ");
        } else {
            this.price.setError(null);
        }
        if (min_quantity.isEmpty()) {
            valid = false;
            this.min_quantity.setError("Veuillez renseigner la quantite minimale");
        } else {
            this.min_quantity.setError(null);
        }
        if (title.isEmpty()) {
            valid = false;
            this.title.setError("Veuillez renseigner le nom du produit");
        } else {
            this.title.setError(null);
        }
        if (description.isEmpty()) {
            valid = false;
            this.description.setError("Veuillez renseigner la description");
        } else {
            this.description.setError(null);
        }
        return valid;
    }

    private void addProduct(String priceValue, String min_quantityValue, String titleValue, String descriptionValue, String categoryId) {
        MultipartBody.Part images = null;
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"), priceValue);
        RequestBody min_quantity = RequestBody.create(MediaType.parse("text/plain"), min_quantityValue);
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), titleValue);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), descriptionValue);
        RequestBody category_id = RequestBody.create(MediaType.parse("text/plain"), categoryId);
        RequestBody apiToken = RequestBody.create(MediaType.parse("text/plain"), user.getApiToken());

        if (photoPath != null) {
            RequestBody mFile = RequestBody.create(MediaType.parse("image/*."), photoPath);
            images = MultipartBody.Part.createFormData("images", photoPath.getName(), mFile);
        }

        APIService apiService = APIClient.getRetrofitClient().create(APIService.class);

        Call<Product> call = apiService.createProduct(apiToken, images, title, description, price, min_quantity, category_id);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    NewProductActivity.this.price.setText("");
                    NewProductActivity.this.description.setText("");
                    NewProductActivity.this.min_quantity.setText("");
                    NewProductActivity.this.title.setText("");

                    image_container.setPadding(70, 70, 70, 70);
                    product_image_placeholder.setVisibility(View.VISIBLE);
                    product_image.setImageURI(null);
                    product_image.setVisibility(View.GONE);
                    Toasty.success(getApplicationContext(), "Produit bien publie", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toasty.error(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
//        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Choisir une image"), GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoPath = createImageFile();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
        startActivityForResult(intent, CAMERA);
    }

    //Get photo file path
    private String getPath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
