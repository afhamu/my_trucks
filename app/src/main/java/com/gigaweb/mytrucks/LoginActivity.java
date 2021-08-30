package com.gigaweb.mytrucks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button btnSignIn;
    private ProgressDialog progressDialog;
    private TextInputLayout email;
    private TextInputLayout password;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = this.getSharedPreferences("loginDetails", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences != null){

            String email = sharedPreferences.getString("email", "");
            if(email != null && !email.matches("")){
               /* if (checkPinEnabled()){
                    Intent intent = new Intent(LoginActivity.this, PinActivity.class);
                    intent.putExtra("email", sharedPreferences.getString("email", ""));
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }else {
                    Intent intent = new Intent(LoginActivity.this, SetupPinActivity.class);
                    intent.putExtra("email", sharedPreferences.getString("email", ""));
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }*/

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("email", sharedPreferences.getString("email", ""));
                startActivity(intent);
                overridePendingTransition(0,0);

            }

        }

        email = findViewById(R.id.user_email);
        password = findViewById(R.id.user_password);
        btnSignIn = findViewById(R.id.btnSignIn);
        progressDialog = new ProgressDialog(this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getEditText().getText().toString();
                String pass = password.getEditText().getText().toString();
                if (mail.isEmpty()) {
                    email.getEditText().setError("Email is required!");
                } else if (pass.isEmpty()) {
                    password.getEditText().setError("Password is required!");
                } else {
                    login(mail, pass);
                }
            }
        });
    }

    void login(String email, String password){
        progressDialog.setMessage("Loggin in...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_LOGIN, response -> {
            if (!response.matches("invalid details")) {
                editor.putString("email", email.trim());
                editor.apply();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String role = object.getString("role");
                        switch (role) {
                            case "admin":
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                overridePendingTransition(0,0);
                                break;
                            case "super_admin":
                                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                overridePendingTransition(0,0);
                                break;
                        }
                    }

                }catch (Exception e){

                }

            }else {
                Toast.makeText(getApplicationContext(),"Incorrect Email or Password",Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();

        },

                error -> {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    public boolean checkPinEnabled(){
        String status = sharedPreferences.getString("status", "");
        if (status != null && status.matches("enabled")){
            return true;
        }else {
            return false;
        }

    }
}