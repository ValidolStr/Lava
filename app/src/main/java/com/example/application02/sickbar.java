package com.example.application02;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

public class sickbar extends Fragment {

    private List<Question> questions; // Список вопросов
    private int currentQuestionIndex = 0; // Индекс текущего вопроса
    private int totalScore = 0; // Общий счёт
    private boolean isTestFinished = false; // Флаг завершения теста

    private TextView questionTextView; // Поле для вопроса
    private TextView scoreTextView; // Поле для текущего счёта
    private SeekBar seekBar; // Слайдер для выбора ответа
    private Button submitButton; // Кнопка для подтверждения ответа

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sickbar, container, false);

        // Находим элементы интерфейса
        questionTextView = view.findViewById(R.id.questionTextView);
        scoreTextView = view.findViewById(R.id.scoreTextView);
        seekBar = view.findViewById(R.id.seekBar);
        submitButton = view.findViewById(R.id.submitButtonsic);

        // Устанавливаем начальное значение слайдера
        seekBar.setProgress(0);

        // Устанавливаем обработчик кнопки
        submitButton.setOnClickListener(v -> handleAnswer(seekBar.getProgress() + 1));

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