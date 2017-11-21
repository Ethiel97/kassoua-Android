package com.code.deventhusiast.alibaba;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.code.deventhusiast.alibaba.api.APIClient;
import com.code.deventhusiast.alibaba.api.APIService;
import com.code.deventhusiast.alibaba.fragments.AccountFragment;
import com.code.deventhusiast.alibaba.models.Result;
import com.code.deventhusiast.alibaba.models.User;
import com.code.deventhusiast.alibaba.utils.UserSession;
import com.r0adkll.slidr.Slidr;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private User user;
    private EditText fname, lname, address, phone, country, email, job, enterprise;
    private Spinner genderSpinner;
    private ImageView photo;
    private NestedScrollView nestedScrollView;
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout galleryChooser, camerapicker;
    private String gender;
    private File photoPath;
    private static final String IMAGE_DIRECTORY = "/kassoua";
    private Integer GALLERY = 0, CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Slidr.attach(this);
        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().useFont(Typeface.SANS_SERIF).withBorder(0).endConfig().round();

//        Bundle extras = getIntent().getExtras();

//        user = extras.getParcelable("USER");
        user = UserSession.Instance(getBaseContext()).getUser();
        galleryChooser = findViewById(R.id.pickGallery);
        camerapicker = findViewById(R.id.capturePhoto);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        genderSpinner = findViewById(R.id.gender);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        country = findViewById(R.id.country);
        photo = findViewById(R.id.photo);
        email = findViewById(R.id.email);
        job = findViewById(R.id.job);
        enterprise = findViewById(R.id.enterprise);

        nestedScrollView = findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(nestedScrollView);

        String username = user.getFname().substring(0, 1) + user.getLname().substring(0, 1);
        TextDrawable drawable = builder.build(username, R.color.colorPrimaryDark);

        photo.setImageDrawable(drawable);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        photo.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        galleryChooser.setOnClickListener(v -> choosePhotoFromGallery());
        camerapicker.setOnClickListener(v -> takePhotoFromCamera());

        //set user profile
        if (user.getPhoto().length() == APIClient.PHOTO_BASE_URL.length()) {
            photo.setImageDrawable(drawable);
        } else {
            Picasso.with(this).load(user.getPhoto()).into(photo);
        }
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = adapterView.getSelectedItem().toString();
                Toasty.info(getBaseContext(), "Vous etes de sexe :" + gender, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*switch (user.getGender()) {
            case "Masculin":
                genderSpinner.setSelection(0);
                break;
            case "Feminin":
                genderSpinner.setSelection(1);
                break;
            default:
                genderSpinner.setSelection(2);
        }*/

        fname.setText(user.getFname());
        lname.setText(user.getLname());
        phone.setText(user.getPhone());
        address.setText(user.getAddress());
        country.setText(user.getCountry());
        email.setText(user.getEmail());
        enterprise.setText(user.getEnterprise());
        job.setText(user.getJob());

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Votre profil");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
                photo.setImageURI(contentURI);
               /* Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
//                    String path = saveImage(bitmap);
                String path = this.getPath(contentURI);
                photoPath = new File(path);
                Toasty.info(UserProfileActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
//                    photo.setImageBitmap(bitmap);
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            photo.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toasty.info(UserProfileActivity.this, "Image enregistree!", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable("file_uri", (Parcelable) photoPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getParcelable("file_uri");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                int id = user.getId();
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Mise a jour en cours de vos informations...\nVeuillez Patienter svp");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

                MultipartBody.Part photo = null;
                RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), this.gender);
                if (photoPath != null) {
                    RequestBody mFile = RequestBody.create(MediaType.parse("image/*."), photoPath);
                    photo = MultipartBody.Part.createFormData("image", photoPath.getName(), mFile);
                }

                RequestBody apiToken = RequestBody.create(MediaType.parse("text/plain"), user.getApiToken());
                RequestBody country = RequestBody.create(MediaType.parse("text/plain"), this.country.getText().toString());
                RequestBody lname = RequestBody.create(MediaType.parse("text/plain"), this.lname.getText().toString());
                RequestBody fname = RequestBody.create(MediaType.parse("text/plain"), this.fname.getText().toString());
                RequestBody job = RequestBody.create(MediaType.parse("text/plain"), this.job.getText().toString());
                RequestBody email = RequestBody.create(MediaType.parse("text/plain"), this.email.getText().toString());
                RequestBody enterprise = RequestBody.create(MediaType.parse("text/plain"), this.enterprise.getText().toString());
                RequestBody address = RequestBody.create(MediaType.parse("text/plain"), this.address.getText().toString());
                RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), this.phone.getText().toString());
                RequestBody method = RequestBody.create(MediaType.parse("text/plain"), APIClient.PUT_METHOD);
                APIService apiService = APIClient.getRetrofitClient().create(APIService.class);

                Call<Result> call = apiService.updateUser(apiToken, id, photo, country, enterprise, gender, lname, fname, email, phone, address, job, method);

                call.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        progressDialog.dismiss();
                        if (!response.body().getError()) {
                            UserSession.Instance(getApplicationContext()).updateUser(response.body().getUser());
                            Toasty.success(getApplicationContext(), String.valueOf(response.body().getMessage()), Toast.LENGTH_LONG).show();
                        } else {
                            Log.d("Message\n", String.valueOf(response.body().getMessage()));
                            Toasty.error(getApplicationContext(), String.valueOf(response.body().getMessage()), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        /*progressDialog.dismiss();
                        progressDialog.setMessage(t.getMessage() + "\n\n" + Arrays.toString(t.getStackTrace()));
                        progressDialog.setCanceledOnTouchOutside(true);
                        progressDialog.setCancelable(true);
                        progressDialog.show();*/
                        Log.d("Message", t.getMessage());
                        Toasty.error(getApplicationContext(), t.getMessage().trim(), Toast.LENGTH_LONG).show();
                    }
                });
                break;

            case R.id.homeAsUp: {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.replace(R.id.fragment_container, AccountFragment.Instance()).commit();
                break;
            }
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

     /*   getFragmentManager().beginTransaction().
                replace(R.id.fragment_container, AccountFragment.Instance()).commit();*/
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

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 95, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
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

}
