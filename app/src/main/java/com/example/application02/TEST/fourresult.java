package com.example.application02.TEST;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class fourresult extends Fragment {

    private List<Question> questions; // Список вопросов
    private int currentQuestionIndex = 0; // Индекс текущего вопроса
    private int totalScore = 0; // Общий счёт
    private boolean isTestFinished = false; // Флаг завершения теста

    private TextView questionTextView; // Поле для вопроса
    private Button buttonYes, buttonSometimes, buttonRarely, buttonNever, buttonResult; // Кнопки ответов

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    private int rtScore1 = 0; // Σ1 для РТ
    private int rtScore2 = 0; // Σ2 для РТ
    private int ltScore1 = 0; // Σ1 для ЛТ
    private int ltScore2 = 0; // Σ2 для ЛТ

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fourresult, container, false);

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

        // Отображаем первый вопрос
        displayQuestion();

        return view;
    }

    private void handleAnswer(int score) {
        if (!isTestFinished) {
            // Распределяем баллы по шкалам
            int questionNumber = currentQuestionIndex + 1;

            // Вопросы для РТ
            if (isReactiveQuestion(questionNumber)) {
                if (isPositiveRT(questionNumber)) {
                    rtScore1 += score; // Σ1
                } else {
                    rtScore2 += score; // Σ2
                }
            }
            // Вопросы для ЛТ
            else if (isLabileQuestion(questionNumber)) {
                if (isPositiveLT(questionNumber)) {
                    ltScore1 += score; // Σ1
                } else {
                    ltScore2 += score; // Σ2
                }
            }

            totalScore += score;
            currentQuestionIndex++;

            // Переходим к следующему вопросу или завершаем тест
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
    }

    private void hideButtons() {
        buttonYes.setVisibility(View.GONE);
        buttonSometimes.setVisibility(View.GONE);
        buttonRarely.setVisibility(View.GONE);
        buttonNever.setVisibility(View.GONE);
        buttonResult.setVisibility(View.VISIBLE);
    }

    private void showResults() {
        // Расчёт результатов
        int rtResult = rtScore1 - rtScore2 + 35;
        int ltResult = ltScore1 - ltScore2 + 35;
        int averageResult = (rtResult + ltResult) / 2;

        String recommendation;
        if (averageResult <= 30) {
            recommendation = "Низкая тревожность. Рекомендуется повысить чувство ответственности.";
        } else if (averageResult <= 45) {
            recommendation = "Умеренная тревожность. Всё в пределах нормы.";
        } else {
            recommendation = "Высокая тревожность. Рекомендуется снизить значимость стрессовых ситуаций.";
        }

        String resultMessage = "РТ: " + rtResult + "\nЛТ: " + ltResult + "\nСредний балл: " + averageResult + "\n" + recommendation;

        // Вывод результата в Toast
        Toast.makeText(getContext(), resultMessage, Toast.LENGTH_LONG).show();

        // Сохранение результата в Firebase
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String testName = "Шкала оценки уровня реактивной и личностной тревожности";
            databaseReference.child(userId).child(testName).setValue(averageResult)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Результат сохранён"))
                    .addOnFailureListener(e -> Log.e("Firebase", "Ошибка сохранения", e));
        }
    }

    // Методы для проверки типа вопроса
    private boolean isReactiveQuestion(int questionNumber) {
        return (questionNumber >= 1 && questionNumber <= 20);
    }

    private boolean isLabileQuestion(int questionNumber) {
        return (questionNumber >= 21 && questionNumber <= 40);
    }

    private boolean isPositiveRT(int questionNumber) {
        return questionNumber == 3 || questionNumber == 4 || questionNumber == 6 || questionNumber == 7 || questionNumber == 9
                || questionNumber == 12 || questionNumber == 13 || questionNumber == 14 || questionNumber == 17 || questionNumber == 18;
    }

    private boolean isPositiveLT(int questionNumber) {
        return questionNumber == 22 || questionNumber == 23 || questionNumber == 24 || questionNumber == 25 || questionNumber == 28
                || questionNumber == 29 || questionNumber == 31 || questionNumber == 32 || questionNumber == 34 || questionNumber == 35
                || questionNumber == 37 || questionNumber == 38 || questionNumber == 40;
    }
}