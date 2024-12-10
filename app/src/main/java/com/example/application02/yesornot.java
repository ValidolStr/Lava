package com.example.application02;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
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
    private TextView scoreTextView; // Поле для текущего счёта
    private Button yesButton; // Кнопка "Да"
    private Button noButton; // Кнопка "Нет"

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yesornot, container, false);

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Results");

        // Находим элементы интерфейса
        questionTextView = view.findViewById(R.id.questionTextView);
        scoreTextView = view.findViewById(R.id.resultest);
        yesButton = view.findViewById(R.id.yesbtn);
        noButton = view.findViewById(R.id.notbtn);

        // Устанавливаем обработчики кнопок
        yesButton.setOnClickListener(v -> handleAnswer(1)); // "Да" добавляет 1 балл
        noButton.setOnClickListener(v -> handleAnswer(0)); // "Нет" добавляет 0 баллов

        // Получаем список вопросов из аргументов
        if (getArguments() != null) {
            questions = getArguments().getParcelableArrayList("questions");
        }

        // Отображаем первый вопрос
        displayQuestion();

        return view;
    }

    private void handleAnswer(int score) {
        if (!isTestFinished) {
            yesCount += score;
            nextQuestion();
        }
    }

    private void displayQuestion() {
        if (questions == null || currentQuestionIndex >= questions.size()) {
            finishTest();
            return;
        }

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
        scoreTextView.setText("Вы ответили 'Да' на " + yesCount + " вопросов.");

        // Проверяем авторизацию пользователя
        if (currentUser == null) {
            Log.e("Firebase", "currentUser is null. User is not authenticated.");
            return;
        }

        String userId = currentUser.getUid();
        String testName = "Тест_ДаНет"; // Уникальное имя теста
        TestResult newResult = new TestResult(testName, yesCount);

        // Сохраняем результаты
        databaseReference.child(userId).child(testName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();

                TestResult lastResult = snapshot.child("lastResult").getValue(TestResult.class);

                databaseReference.child(userId).child(testName).child("previousResult").setValue(lastResult);
                databaseReference.child(userId).child(testName).child("lastResult").setValue(newResult)
                        .addOnSuccessListener(aVoid -> Log.d("Firebase", "Test result saved successfully"))
                        .addOnFailureListener(e -> Log.e("Firebase", "Failed to save test result", e));
            } else {
                Log.e("Firebase", "Failed to fetch current results", task.getException());
            }
        });
    }
}