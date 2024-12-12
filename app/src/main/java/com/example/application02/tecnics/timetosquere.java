package com.example.application02.tecnics;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.application02.R;

public class timetosquere extends Fragment {
    private TextView timerTextView;
    private TextView actionTextView;
    private Button toggleButton;
    private CountDownTimer countDownTimer;
    private SquareView squareView;

    // Этапы дыхательной практики
    private String[] actions = {
            "Вдохните",       // Вдох
            "Задержите дыхание", // Задержка после вдоха
            "Выдохните",      // Выдох
            "Задержите дыхание"  // Задержка после выдоха
    };
    private int actionIndex = 0; // Текущий этап дыхания
    private boolean isRunning = false; // Флаг для отслеживания, запущена ли практика

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Инфлейтим разметку для фрагмента
        View view = inflater.inflate(R.layout.fragment_timetosquere, container, false);

        // Инициализация UI элементов
        timerTextView = view.findViewById(R.id.timerTextView);
        actionTextView = view.findViewById(R.id.actionTextView);
        toggleButton = view.findViewById(R.id.toggleButton);
        squareView = view.findViewById(R.id.squareView); // Инициализация кастомного View

        // Устанавливаем обработчик для кнопки
        toggleButton.setOnClickListener(v -> {
            if (isRunning) {
                stopBreathingPractice(); // Остановить практику
            } else {
                startBreathingPractice(); // Начать практику
            }
        });

        return view;
    }

    private void startBreathingPractice() {
        isRunning = true; // Устанавливаем флаг, что практика запущена
        toggleButton.setText("Остановить"); // Меняем текст кнопки
        actionIndex = 0; // Сбрасываем этап дыхания
        runBreathingCycle();
    }

    private void stopBreathingPractice() {
        isRunning = false; // Устанавливаем флаг, что практика остановлена
        toggleButton.setText("Начать"); // Меняем текст кнопки
        actionTextView.setText("Практика остановлена"); // Сообщение об остановке
        timerTextView.setText("0"); // Сбрасываем таймер
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Останавливаем текущий таймер
        }
        squareView.reset(); // Сбрасываем визуализацию квадрата
    }

    private void runBreathingCycle() {
        if (!isRunning) return; // Если практика остановлена, прерываем выполнение

        // Устанавливаем действие на основе текущего этапа
        actionTextView.setText(actions[actionIndex]);

        // Обновляем визуализацию квадрата
        squareView.updateProgress(actionIndex);

        // Таймер на 3 секунды для каждого этапа
        countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(String.valueOf(millisUntilFinished / 1000)); // Отображаем оставшееся время
            }

            @Override
            public void onFinish() {
                if (!isRunning) return; // Если практика остановлена, не продолжаем
                // Переходим к следующему этапу
                actionIndex = (actionIndex + 1) % actions.length; // Зацикливаем этапы
                runBreathingCycle(); // Запускаем следующий этап
            }
        };

        countDownTimer.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Отменяем таймер при уничтожении представления
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}