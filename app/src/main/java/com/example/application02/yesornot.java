package com.example.application02;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class yesornot extends Fragment {

    private List<Question> questions; // Список вопросов
    private int currentQuestionIndex = 0; // Индекс текущего вопроса
    private int yesCount = 0; // Счётчик ответов "Да"
    private boolean isTestFinished = false; // Флаг завершения теста

    private TextView questionTextView; // Поле для вопроса
    private TextView resultTextView; // Поле для результата
    private Button yesButton; // Кнопка "Да"
    private Button noButton; // Кнопка "Нет"
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Инициализация макета фрагмента
        View view = inflater.inflate(R.layout.fragment_yesornot, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Results");
        // Находим элементы интерфейса
        questionTextView = view.findViewById(R.id.question);
        resultTextView = view.findViewById(R.id.resultest);
        yesButton = view.findViewById(R.id.yesbtn);
        noButton = view.findViewById(R.id.notbtn);

        // Устанавливаем обработчики кнопок
        yesButton.setOnClickListener(v -> {
            if (!isTestFinished) {
                yesCount++;
                nextQuestion();
            }
        });

        noButton.setOnClickListener(v -> {
            if (!isTestFinished) {
                nextQuestion();
            }
        });

        // Получаем список вопросов из аргументов
        if (getArguments() != null) {
            questions = getArguments().getParcelableArrayList("questions");
        }

        // Отображаем первый вопрос
        displayQuestion();

        return view;
    }

    private void displayQuestion() {
        if (questions == null || currentQuestionIndex >= questions.size()) {
            // Если вопросы закончились, завершаем тест
            finishTest();
            return;
        }

        // Отображаем текущий вопрос
        Question currentQuestion = questions.get(currentQuestionIndex);
        questionTextView.setText(currentQuestion.getText());
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            displayQuestion();
        } else {
            finishTest();
        }
    }

    private void finishTest() {
        isTestFinished = true;
        questionTextView.setText("Тест завершён");
        resultTextView.setText("Вы ответили 'Да' на " + yesCount + " вопросов.");

        // Проверка авторизованного пользователя
        if (currentUser == null) {
            Log.e("Firebase", "currentUser is null. User is not authenticated.");
            return;
        }

        String userId = currentUser.getUid();
        String testName = "Тест1_" + (currentQuestionIndex + 1); // Уникальное имя теста
        TestResult result = new TestResult(testName, yesCount);

        // Проверяем, что databaseReference инициализирован
        if (databaseReference == null) {
            Log.e("Firebase", "databaseReference is null. Firebase Database is not initialized.");
            return;
        }

        // Проверяем testName на недопустимые символы
        if (testName.contains(".") || testName.contains("/") || testName.contains("#") || testName.contains("$") || testName.contains("[")) {
            Log.e("Firebase", "testName contains invalid characters: " + testName);
            return;
        }

        // Сохраняем данные в Firebase
        databaseReference.child(userId).child(testName).setValue(result)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Data saved successfully for test: " + testName);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Failed to save data for test: " + testName, e);
                });
    }
}