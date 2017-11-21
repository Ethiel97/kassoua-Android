package com.code.deventhusiast.alibaba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.code.deventhusiast.alibaba.api.APIClient;
import com.code.deventhusiast.alibaba.api.APIService;
import com.code.deventhusiast.alibaba.models.Result;
import com.code.deventhusiast.alibaba.models.User;
import com.code.deventhusiast.alibaba.utils.UserSession;
import com.r0adkll.slidr.Slidr;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button signupButton;
    private EditText countryField, enterpriseField, lnameField, fnameField, emailField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Slidr.attach(this);


        countryField = findViewById(R.id.country);
        enterpriseField = findViewById(R.id.enterprise);
        lnameField = findViewById(R.id.lname);
        fnameField = findViewById(R.id.fname);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        toolbar = findViewById(R.id.toolbar);

        toolbar = findViewById(R.id.toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            setSupportActionBar(toolbar);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView login_link = findViewById(R.id.login_link);
        login_link.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        signupButton = findViewById(R.id.signup_button);
        signupButton.setOnClickListener(view -> signup());

    }

    private void signup() {
        String country = countryField.getText().toString(), enterprise = enterpriseField.getText().toString(),
                lname = lnameField.getText().toString(), fname = fnameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (!isValid(country, enterprise, lname, fname, email, password)) {
//            progressDialog.dismiss();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Enregistrement en cours...\nVeuillez patienter svp");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        User user = new User(country, enterprise, lname, fname, email, password);

        //building retrofit apiservice
        APIService apiService = APIClient.getRetrofitClient().create(APIService.class);

        //Defining the call
        Call<Result> call = apiService.createUser(
                user.getCountry(), user.getEnterprise(),
                user.getLname(), user.getFname(),
                user.getEmail(), user.getPassword());

        //Calling the api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();

                if (!response.body().getError()) {
                    UserSession.Instance(getApplicationContext()).UserLogin(response.body().getUser());
                    Toasty.success(getApplicationContext(), String.valueOf(response.body().getMessage()), Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Log.d("Message\n", String.valueOf(response.body().getMessage()));
                    Toasty.error(getApplicationContext(), String.valueOf(response.body().getMessage()), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("Error\n", t.getMessage());
                Toasty.error(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }


    public boolean isValid(String country, String enterprise, String lname, String fname, String email, String password) {
//        loginButton.setEnabled(false);
        boolean valid = true;

        if (country.isEmpty()) {
            valid = false;
            countryField.setError("Veuillez bien choisir votre pays");
        } else {
            countryField.setError(null);
        }
        if (enterprise.isEmpty()) {
            valid = false;
            enterpriseField.setError("Veuillez entrer le nom de votre entreprise");
        } else {
            enterpriseField.setError(null);
        }
        if (lname.isEmpty()) {
            valid = false;
            lnameField.setError("Veuillez entrer votre nom");
        } else {
            lnameField.setError(null);
        }
        if (fname.isEmpty()) {
            valid = false;
            fnameField.setError("Veuillez entrer votre prenom");
        } else {
            fnameField.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false;
            emailField.setError("Veuillez une adresse email valide");
        } else {
            emailField.setError(null);
        }
        if (password.isEmpty()) {
            valid = false;
            passwordField.setError("Veuillez entrer votre mot de passe");
        } else {
            passwordField.setError(null);
        }
        return valid;
    }

}
