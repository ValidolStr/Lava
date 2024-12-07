package com.example.application02;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class fourresult extends Fragment {

    private List<Question> questions; // Список вопросов
    private int currentQuestionIndex = 0; // Индекс текущего вопроса
    private int totalScore = 0; // Общий счёт
    private boolean isTestFinished = false; // Флаг завершения теста

    private TextView questionTextView; // Поле для вопроса
    private TextView scoreTextView; // Поле для текущего счёта
    private Button buttonYes; // Кнопка "Да" (1 очко)
    private Button buttonSometimes; // Кнопка "Иногда" (2 очка)
    private Button buttonRarely; // Кнопка "Редко" (3 очка)
    private Button buttonNever; // Кнопка "Никогда" (4 очка)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fourresult, container, false);

        // Находим элементы интерфейса
        questionTextView = view.findViewById(R.id.questionTextView);
        scoreTextView = view.findViewById(R.id.scoreTextView);
        buttonYes = view.findViewById(R.id.buttonYes4);
        buttonSometimes = view.findViewById(R.id.buttonSometimes4);
        buttonRarely = view.findViewById(R.id.buttonRarely4);
        buttonNever = view.findViewById(R.id.buttonNever4);

        // Устанавливаем обработчики кнопок
        buttonYes.setOnClickListener(v -> handleAnswer(1));
        buttonSometimes.setOnClickListener(v -> handleAnswer(2));
        buttonRarely.setOnClickListener(v -> handleAnswer(3));
        buttonNever.setOnClickListener(v -> handleAnswer(4));

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
        scoreTextView.setText("Ваш результат: " + totalScore);
    }
}