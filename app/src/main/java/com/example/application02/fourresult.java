package com.example.application02;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class fourresult extends Fragment {

    private List<Question> questions; // Список вопросов
    private int currentQuestionIndex = 0; // Индекс текущего вопроса
    private int totalScore = 0; // Общий счёт
    private boolean isTestFinished = false; // Флаг завершения теста

    private TextView questionTextView; // Поле для вопроса
    private TextView scoreTextView; // Поле для счёта
    private Button buttonYes, buttonSometimes, buttonRarely, buttonNever; // Кнопки ответов

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fourresult, container, false);

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Results");

        // Находим элементы интерфейса
        questionTextView = view.findViewById(R.id.questionTextView);
        scoreTextView = view.findViewById(R.id.scoreTextView);
        buttonYes = view.findViewById(R.id.buttonYes4);
        buttonSometimes = view.findViewById(R.id.buttonSometimes4);
        buttonRarely = view.findViewById(R.id.buttonRarely4);
        buttonNever = view.findViewById(R.id.buttonNever4);

        // Устанавливаем обработчики кнопок
        buttonYes.setOnClickListener(v -> handleAnswer(4));
        buttonSometimes.setOnClickListener(v -> handleAnswer(3));
        buttonRarely.setOnClickListener(v -> handleAnswer(2));
        buttonNever.setOnClickListener(v -> handleAnswer(1));

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
            totalScore += score;
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
        scoreTextView.setText("Ваш итоговый счёт: " + totalScore);

        // Проверяем авторизацию пользователя
        if (currentUser == null) {
            Log.e("Firebase", "currentUser is null. User is not authenticated.");
            return;
        }

        String userId = currentUser.getUid();
        String testName = "Тест_ЧетыреОтвета"; // Уникальное имя теста
        TestResult newResult = new TestResult(testName, totalScore);

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