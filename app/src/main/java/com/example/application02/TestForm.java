package com.example.application02;

import android.os.Bundle;
import android.widget.FrameLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;


public class TestForm extends AppCompatActivity {

    private FrameLayout frameLayout;
    private List<Question> questions; // Список вопросов
    private int testType; // Тип теста (определяется по названию)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_form);

        init();

        // Настраиваем обработку системных окон (Edge-to-Edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void init() {
        // Получаем название теста из Intent
        String testName = getIntent().getStringExtra("test_name");
        if (testName == null || testName.isEmpty()) {
            throw new IllegalArgumentException("Название теста не передано или пустое!");
        }

        frameLayout = findViewById(R.id.frameLayout);

        // Ищем тест по названию
        Test selectedTest = findTestByName(testName);
        if (selectedTest == null) {
            throw new IllegalArgumentException("Тест с названием \"" + testName + "\" не найден!");
        }

        // Определяем тип теста и список вопросов
        testType = selectedTest.getType();
        questions = selectedTest.getQuestions();

        // Устанавливаем фрагмент с передачей данных
        setNewFragment(testType, questions);
    }

    /**
     * Устанавливает новый фрагмент, передавая тип теста и список вопросов.
     */
    public void setNewFragment(int testType, List<Question> questions) {
        Fragment fragment = getCurrentFragment(testType);

        // Передаем список вопросов в фрагмент через Bundle
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("questions", new ArrayList<>(questions)); // Преобразуем в ArrayList для передачи
        fragment.setArguments(bundle);

        // Заменяем текущий фрагмент
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }

    /**
     * Метод для получения текущего фрагмента в зависимости от типа теста.
     */
    private Fragment getCurrentFragment(int testType) {
        switch (testType) {
            case 1:
                return new yesornot();
            case 2:
                return new fiveresult();
            case 3:
                return new fourresult();
            case 4:
                return new sickbar();
            default:
                throw new IllegalArgumentException("Неизвестный тип теста: " + testType);
        }
    }

    /**
     * Метод для поиска теста по названию.
     */
    private Test findTestByName(String testName) {
        for (Test test : TestingActivity.listTestObjects) {
            if (test.getTitle().equals(testName)) {
                return test;
            }
        }
        return null; // Если тест не найден
    }
}
