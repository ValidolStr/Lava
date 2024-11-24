package com.example.application02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TestingActivity extends AppCompatActivity {

    private  String[] testlist1 = new String[] {
            "Определение уровня самооценки",
            "Шкала депрессии",
            "Самочувствие,активность, настроение",
            "Определение уровня невротизации",
            "Копинг-стратегия"};
    protected ListView listView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_testing);

        listView = findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, testlist1);
        listView.setAdapter(adapter);

        // Установка обработчика нажатий на элементы списка
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Переход в следующую активность с передачей индекса
            jumpToNextActivity(position);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Метод для перехода на другую активность с передачей индекса
    public void jumpToNextActivity(int position) {
        Intent intent = new Intent(this, TestForm.class);
        intent.putExtra("item_index", position); // Передаем индекс выбранного элемента
        startActivity(intent);
    }
    public void Jamptoback (View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}