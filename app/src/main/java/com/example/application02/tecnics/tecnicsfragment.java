package com.example.application02.tecnics;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.application02.R;

public class tecnicsfragment extends AppCompatActivity {

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_form);

        frameLayout = findViewById(R.id.frameLayout);

        // Получаем название техники из Intent
        String testName = getIntent().getStringExtra("test_name");
        if (testName == null || testName.isEmpty()) {
            throw new IllegalArgumentException("Название техники не передано или пустое!");
        }

       //  Определяем, какой фрагмент показать
        Fragment fragment = getFragmentByTechnique(testName);

        // Устанавливаем фрагмент
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }

    /**
     * Возвращает фрагмент в зависимости от названия техники.
     */
    private Fragment getFragmentByTechnique(String techniqueName) {
        switch (techniqueName) {
            case "Дыхание по квадрату":
                return new timetosquere();
            case "Аффирмации":
                return new AffirmationsFragment();
            case "Спокойное место":
                return new CalmPlaceFragment();
            case "Заметки":
                return new NotesFragment();
            case "5 Почему":
                return new FiveStopsFragment();
            default:
                throw new IllegalArgumentException("Неизвестная техника: " + techniqueName);
        }
    }
}
