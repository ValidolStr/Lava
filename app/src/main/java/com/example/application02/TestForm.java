package com.example.application02;

import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



public class TestForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test_form);

        setContentView(R.layout.activity_test_form);

//        // Получаем индекс из Intent
//        int itemIndex = getIntent().getIntExtra("item_index", -1); // -1 — значение по умолчанию, если данные не передались
//
//        // Используем индекс (например, отображаем его в TextView или используем для логики)
//        TextView textView = findViewById(R.id.textView2);
//        textView.setText("Выбранный индекс: " + itemIndex);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}