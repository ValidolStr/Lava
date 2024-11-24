package com.example.application02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.EdgeToEdge;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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