package com.example.application02;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class fiveresult extends Fragment {

    private List<Question> questions; // Список вопросов
    private int currentQuestionIndex = 0; // Индекс текущего вопроса
    private int totalScore = 0; // Общий счёт
    private boolean isTestFinished = false; // Флаг завершения теста

    private TextView questionTextView; // Поле для вопроса
    private TextView scoreTextView; // Поле для текущего счёта
    private Button button5Points; // Кнопка "Да" (5 очков)
    private Button button4Points; // Кнопка "Почти да" (4 очка)
    private Button button3Points; // Кнопка "Иногда" (3 очка)
    private Button button2Points; // Кнопка "Редко" (2 очка)
    private Button button1Point; // Кнопка "Никогда" (1 очко)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Инициализация макета фрагмента
        View view = inflater.inflate(R.layout.fragment_fiveresult, container, false);

        // Находим элементы интерфейса фрагмента
        questionTextView = view.findViewById(R.id.questionTextView);
        scoreTextView = view.findViewById(R.id.scoreTextView);
        button5Points = view.findViewById(R.id.button5Points);
        button4Points = view.findViewById(R.id.button4Points);
        button3Points = view.findViewById(R.id.button3Points);
        button2Points = view.findViewById(R.id.button2Points);
        button1Point = view.findViewById(R.id.button1Point);

        // Устанавливаем обработчики кнопок
        button5Points.setOnClickListener(v -> handleAnswer(5));
        button4Points.setOnClickListener(v -> handleAnswer(4));
        button3Points.setOnClickListener(v -> handleAnswer(3));
        button2Points.setOnClickListener(v -> handleAnswer(2));
        button1Point.setOnClickListener(v -> handleAnswer(1));

        // Получаем список вопросов из аргументов
        if (getArguments() != null) {
            questions = getArguments().getParcelableArrayList("questions");
        }

        // Отображаем первый вопрос
        displayQuestion();

        return view;
    }

    // Метод для передачи списка вопросов в фрагмент
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    // Метод для обработки ответа
    private void handleAnswer(int score) {
        if (!isTestFinished) {
            totalScore += score;
            nextQuestion();
        }
    }

    // Метод для отображения текущего вопрос

    private void displayQuestion() {
        if (questions == null || currentQuestionIndex >= questions.size()) {
            Log.d("FiveResultFragment", "Список вопросов пуст или индекс за пределами списка");
            finishTest();
            return;
        }

        // Отображаем текущий вопрос
        Question currentQuestion = questions.get(currentQuestionIndex);
        if (currentQuestion != null) {
            questionTextView.setText(currentQuestion.getText());
        } else {
            Log.d("FiveResultFragment", "Текущий вопрос равен null");
            finishTest();
        }
    }


    // Метод для перехода к следующему вопросу
    private void nextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            // Отображаем следующий вопрос
            displayQuestion();
        } else {
            // Если вопросы закончились, завершаем тест
            finishTest();
        }
    }

    // Метод для завершения теста
    private void finishTest() {
        isTestFinished = true; // Устанавливаем флаг завершения
        questionTextView.setText("Тест завершён");
        scoreTextView.setText("Ваш итоговый счёт: " + totalScore);
    }
}