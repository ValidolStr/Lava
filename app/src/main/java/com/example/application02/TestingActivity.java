package com.example.application02;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//import androidx.core.view.WindowInsetsCompat.Insets;

import com.example.application02.Test;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.application02.YesNoTest;
import com.example.application02.RatingTest;
import com.example.application02.SingleChoiceTest;
import com.example.application02.MultipleChoiceTest;

import java.util.ArrayList;
import java.util.List;

public class TestingActivity extends AppCompatActivity {
    private DatabaseReference testlistRef; // Ссылка на базу данных Firebase
    private String tests = "tests"; // Имя узла в Firebase, где хранятся тесты
    protected ListView listView; // Для вывода списка тестов
    private ArrayAdapter<String> adapter; // Адаптер для отображения строк в ListView
    public static List<Test> listTestObjects; // Список объектов Test
    private List<String> listTestTitles; // Список названий тестов для отображения

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        init();
        getData();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Инициализация компонентов
    private void init() {
        listView = findViewById(R.id.listView); // Привязываем ListView из разметки
        listTestObjects = new ArrayList<>(); // Инициализируем список объектов Test
        listTestTitles = new ArrayList<>(); // Инициализируем список строк для отображения
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTestTitles); // Создаём адаптер
        listView.setAdapter(adapter); // Устанавливаем адаптер для ListView

        testlistRef = FirebaseDatabase.getInstance().getReference(tests); // Получаем ссылку на узел "tests" в Firebase
        listView.setOnItemClickListener((parent, view, position, id) -> {
            jumpToNextActivity(position); // Переход к следующей активности при клике на элемент списка
        });
    }

    // Получение данных из Firebase
    private void getData() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!listTestObjects.isEmpty()) listTestObjects.clear(); // Очищаем список объектов Test
                if (!listTestTitles.isEmpty()) listTestTitles.clear(); // Очищаем список строк

                for (DataSnapshot ds : snapshot.getChildren()) {
                    TestFirebaseModel testData = ds.getValue(TestFirebaseModel.class); // Получаем данные в виде модели TestFirebaseModel
                    if (testData != null) {
                        // Создаём объект Test на основе данных из Firebase
                        Test test = createTestFromFirebaseModel(testData);
                        listTestObjects.add(test); // Добавляем объект Test в список
                        listTestTitles.add(test.getTitle()); // Добавляем название теста в отображаемый список
                    }
                }
                adapter.notifyDataSetChanged(); // Уведомляем адаптер об изменении данных
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Обработка ошибок
            }
        };

        testlistRef.addValueEventListener(valueEventListener); // Добавляем слушатель к узлу "tests"
    }

    // Метод для создания объекта Test на основе данных из Firebase
    private Test createTestFromFirebaseModel(TestFirebaseModel testData) {
        switch (testData.type) {
            case 1:
                return new YesNoTest(testData.title, testData.questions);
            case 2:
                return new RatingTest(testData.title, testData.questions);
            case 3:
                return new SingleChoiceTest(testData.title, testData.questions);
            case 4:
                return new MultipleChoiceTest(testData.title, testData.questions);
            default:
                throw new IllegalArgumentException("Неизвестный тип теста: " + testData.type);
        }
    }


// Метод для перехода к следующей активности
    private void jumpToNextActivity(int position) {
        Test selectedTest = listTestObjects.get(position); // Получаем выбранный тест
        Intent intent = new Intent(this, TestForm.class); // Создаём Intent для перехода на TestForm
        intent.putExtra("test_name", selectedTest.getTitle()); // Передаём название теста
        startActivity(intent); // Запускаем TestForm
    }

    // Модель для получения данных из Firebase
    public static class TestFirebaseModel {
        public String title; // Название теста
        public int type; // Тип теста (1, 2, 3 или 4)
        public List<Question> questions; // Список вопросов

        public TestFirebaseModel() {
            // Конструктор по умолчанию для Firebase
        }
    }
}