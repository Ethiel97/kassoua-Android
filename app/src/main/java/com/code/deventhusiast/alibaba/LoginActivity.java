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
import com.code.deventhusiast.alibaba.utils.UserSession;
import com.r0adkll.slidr.Slidr;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;
    private Button loginButton;
    private Toolbar toolbar;
    private EditText emailField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Slidr.attach(this);

        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        toolbar = findViewById(R.id.toolbar);
        loginButton = findViewById(R.id.login_button);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            setSupportActionBar(toolbar);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView signup_link = findViewById(R.id.signup_link);
        signup_link.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
//            startActivityForResult(intent, REQUEST_SIGNUP);
            startActivity(intent);
        });

        loginButton.setOnClickListener(view -> login());

    }

    private void login() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (!isValid(email, password)) {
//            progressDialog.dismiss();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connexion en cours...\nVeuillez Patienter svp");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();


        //building retrofit apiservice
        APIService apiService = APIClient.getRetrofitClient().create(APIService.class);

        //Defining the call
        Call<Result> call = apiService.userLogin(email, password);

        //Calling the api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();

                if (!response.body().getError()) {
                    Toasty.success(getApplicationContext(), String.valueOf(response.body().getMessage()), Toast.LENGTH_LONG).show();
                    UserSession.Instance(getApplicationContext()).UserLogin(response.body().getUser());
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
                Toasty.error(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isValid(String email, String password) {
//        loginButton.setEnabled(false);
        boolean valid = true;

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
