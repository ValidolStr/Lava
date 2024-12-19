package com.example.application02.tecnics;

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

import com.example.application02.MainActivity;
import com.example.application02.R;

public class TeknicsActivity extends AppCompatActivity {

    private ListView listView;
    private String[] techniques = {
            "Дыхание по квадрату",
            "Аффирмации",
            "Спокойное место",
            "Заметки",
            "5 Почему"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teknics);
        EdgeToEdge.enable(this);
        // Инициализируем ListView
        listView = findViewById(R.id.listView);

        // Создаем адаптер для списка
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.list_item,
                techniques
        );

        // Устанавливаем адаптер на ListView
        listView.setAdapter(adapter);

        // Устанавливаем обработчик кликов на элементы списка
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedTechnique = techniques[position];

            // Переход на следующую активность с передачей выбранной техники
            Intent intent = new Intent(TeknicsActivity.this, tecnicsfragment.class);
            intent.putExtra("test_name", selectedTechnique);
            startActivity(intent);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void Jamptoback (View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}