package com.example.silidcheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Home_activity extends AppCompatActivity {
    Button btn_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btn_profile = findViewById(R.id.btnprofile);
        btn_profile.setOnClickListener(v -> startActivity(new Intent(Home_activity.this, ProfileView.class)));
    }
}