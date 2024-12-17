package com.example.application02.TEST;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application02.R;
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
    private Button yesButton; // Кнопка "Да"
    private Button noButton; // Кнопка "Нет"
    private Button resultButton; // Кнопка для отображения результата

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
        yesButton = view.findViewById(R.id.yesbtn);
        noButton = view.findViewById(R.id.notbtn);
        resultButton = view.findViewById(R.id.resultbtn); // Кнопка результата

        // Кнопка результата изначально невидима
        resultButton.setVisibility(View.GONE);

        // Устанавливаем обработчики кнопок
        yesButton.setOnClickListener(v -> handleAnswer(1)); // "Да" добавляет 1 балл
        noButton.setOnClickListener(v -> handleAnswer(0)); // "Нет" добавляет 0 баллов
        resultButton.setOnClickListener(v -> showResult()); // По нажатию выводим результат

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

        // Скрываем кнопки "Да" и "Нет"
        yesButton.setVisibility(View.GONE);
        noButton.setVisibility(View.GONE);

        // Показываем кнопку результата
        resultButton.setVisibility(View.VISIBLE);

        // Сохраняем результаты в Firebase
        saveResultsToFirebase();
    }

    private void saveResultsToFirebase() {
        if (currentUser == null) {
            Log.e("Firebase", "currentUser is null. User is not authenticated.");
            return;
        }

        String userId = currentUser.getUid();
        String testName = "Экспрес диагностика невротизации"; // Уникальное имя теста
        TestResult newResult = new TestResult(testName, yesCount);

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

    private void showResult() {
        String recommendation;

        // Интерпретация уровней
        if (yesCount >= 20) {
            recommendation = "Высокий уровень невротизации: выраженная эмоциональная возбудимость, тревожность, напряжённость, беспокойство.";
        } else if (yesCount >= 10) {
            recommendation = "Средний уровень невротизации: умеренная эмоциональная устойчивость, возможны временные проявления напряжённости.";
        } else {
            recommendation = "Низкий уровень невротизации: эмоциональная устойчивость, спокойствие, оптимизм.";
        }

        String resultMessage = "Вы ответили 'Да' на " + yesCount + " вопросов.\n" + recommendation;

        // Создаём диалоговое окно
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Результат теста")
                .setMessage(resultMessage)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Закрываем диалог
                    dialog.dismiss();
                })
                .setCancelable(false) // Диалог нельзя закрыть нажатием вне его
                .show();
    }
}