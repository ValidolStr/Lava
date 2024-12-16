package com.example.application02;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.EdgeToEdge;

import com.example.application02.TEST.TestingActivity;
import com.example.application02.tecnics.TeknicsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        Log.d("Firebase", "Anonymous user authenticated: " + currentUser.getUid());
                    } else {
                        Log.e("Firebase", "Anonymous authentication failed", task.getException());
                    }
                });

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void Jamptotest (View view) {
        Intent intent = new Intent(this, TestingActivity.class);
        startActivity(intent);
    }
    public void Jamptometaforik (View v) {
        Intent intent = new Intent(this, MetaforikActivity.class);
        startActivity(intent);
    }
    public void Jamptostatistic (View v) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }
    public void JamptoTecknics (View v) {
        Intent intent = new Intent(this, TeknicsActivity.class);
        startActivity(intent);
    }


}