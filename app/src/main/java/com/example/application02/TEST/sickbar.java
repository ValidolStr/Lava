package com.example.application02.TEST;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

import com.example.application02.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class sickbar extends Fragment {

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int directScoreSum = 0; // Σ прямых вопросов
    private int inverseScoreSum = 0; // Σ обратных вопросов
    private boolean isTestFinished = false;

    private TextView questionTextView;
    private Button buttonYes, buttonSometimes, buttonRarely, buttonNever, buttonResult;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sickbar, container, false);

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Results");

        // Находим элементы интерфейса
        questionTextView = view.findViewById(R.id.questionTextView);
        buttonYes = view.findViewById(R.id.buttonYes4);
        buttonSometimes = view.findViewById(R.id.buttonSometimes4);
        buttonRarely = view.findViewById(R.id.buttonRarely4);
        buttonNever = view.findViewById(R.id.buttonNever4);
        buttonResult = view.findViewById(R.id.buttonResult);

        // Устанавливаем обработчики кнопок
        buttonYes.setOnClickListener(v -> handleAnswer(1));
        buttonSometimes.setOnClickListener(v -> handleAnswer(2));
        buttonRarely.setOnClickListener(v -> handleAnswer(3));
        buttonNever.setOnClickListener(v -> handleAnswer(4));

        buttonResult.setOnClickListener(v -> showResults());

        // Получаем список вопросов из аргументов
        if (getArguments() != null) {
            questions = getArguments().getParcelableArrayList("questions");
        }

        if (questions == null || questions.isEmpty()) {
            questionTextView.setText("Нет доступных вопросов");
            hideButtons();
        } else {
            displayQuestion();
        }

        return view;
    }

    private void handleAnswer(int selectedScore) {
        if (!isTestFinished) {
            int questionNumber = currentQuestionIndex + 1;

            // Распределение баллов по типу вопроса
            if (isDirectQuestion(questionNumber)) {
                directScoreSum += selectedScore; // Прямой вопрос
            } else if (isInverseQuestion(questionNumber)) {
                inverseScoreSum += (5 - selectedScore); // Обратный вопрос (разворот оценки)
            }

            currentQuestionIndex++; // Переход к следующему вопросу

            if (currentQuestionIndex < questions.size()) {
                displayQuestion();
            } else {
                finishTest();
            }
        }
    }

    private void displayQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            finishTest();
            return;
        }

        Question currentQuestion = questions.get(currentQuestionIndex);
        questionTextView.setText(currentQuestion.getText());
    }

    private void finishTest() {
        isTestFinished = true;
        questionTextView.setText("Тест завершён");
        hideButtons();
        saveResultsToFirebase();
    }

    private void hideButtons() {
        buttonYes.setVisibility(View.GONE);
        buttonSometimes.setVisibility(View.GONE);
        buttonRarely.setVisibility(View.GONE);
        buttonNever.setVisibility(View.GONE);
        buttonResult.setVisibility(View.VISIBLE);
    }

    private void showResults() {
        // Расчёт уровня депрессии
        int depressionLevel = directScoreSum + inverseScoreSum;

        // Интерпретация результатов
        String recommendation;
        if (depressionLevel <= 50) {
            recommendation = "Состояние без депрессии.";
        } else if (depressionLevel <= 59) {
            recommendation = "Легкая депрессия.";
        } else if (depressionLevel <= 69) {
            recommendation = "Субдепрессивное состояние.";
        } else {
            recommendation = "Истинное депрессивное состояние.";
        }

        String resultMessage = "Уровень депрессии: " + depressionLevel + "\n" + recommendation;

        // Отображаем результат в MessageBox
        new AlertDialog.Builder(requireContext())
                .setTitle("Результаты теста")
                .setMessage(resultMessage)
                .setPositiveButton("OK", null)
                .show();
    }

    private void saveResultsToFirebase() {
        if (currentUser == null) {
            Log.e("Firebase", "currentUser is null. User is not authenticated.");
            return;
        }

        String userId = currentUser.getUid();
        String testName = "Шкала депрессии";
        int depressionLevel = directScoreSum + inverseScoreSum;
        TestResult newResult = new TestResult(testName, depressionLevel);

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

    private boolean isDirectQuestion(int questionNumber) {
        return questionNumber == 1 || questionNumber == 3 || questionNumber == 4 || questionNumber == 7 ||
                questionNumber == 8 || questionNumber == 9 || questionNumber == 10 || questionNumber == 13 ||
                questionNumber == 15 || questionNumber == 19;
    }

    private boolean isInverseQuestion(int questionNumber) {
        return questionNumber == 2 || questionNumber == 5 || questionNumber == 6 || questionNumber == 11 ||
                questionNumber == 12 || questionNumber == 14 || questionNumber == 16 || questionNumber == 17 ||
                questionNumber == 18 || questionNumber == 20;
    }
}