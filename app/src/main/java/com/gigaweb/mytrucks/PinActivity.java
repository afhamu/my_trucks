package com.gigaweb.mytrucks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class PinActivity extends AppCompatActivity {
    private String userEmail;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        sharedPreferences = this.getSharedPreferences("userDetails", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Bundle bundle = getIntent().getExtras();
        userEmail = bundle.getString("email");

        checkPinEnabled();

    }

    public void checkPinEnabled(){
        String status = sharedPreferences.getString("status", "");
        if (status != null && status.matches("enabled")){
            Toast.makeText(getApplicationContext(), "enabled", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "disabled", Toast.LENGTH_SHORT).show();
        }

    }
}