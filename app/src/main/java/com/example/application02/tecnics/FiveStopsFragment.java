package com.example.application02.tecnics;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.example.application02.R;

import java.util.ArrayList;
import java.util.List;

public class FiveStopsFragment extends Fragment {

    private EditText problemEditText, answerEditText;
    private Button confirmButton, solvedButton;

    private String problem; // Проблема
    private List<String> answers = new ArrayList<>(); // Ответы "Почему?"

    private int step = 0; // Текущий шаг (0 - ввод проблемы, 1-5 - ответы)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Инициализация макета фрагмента
        View view = inflater.inflate(R.layout.fragment_five_stops, container, false);

        // Инициализация элементов интерфейса
        problemEditText = view.findViewById(R.id.problemEditText);
        answerEditText = view.findViewById(R.id.answerEditText);
        confirmButton = view.findViewById(R.id.confirmButton);
        solvedButton = view.findViewById(R.id.solvedButton);

        // Устанавливаем обработчики кнопок
        confirmButton.setOnClickListener(v -> handleConfirm());
        solvedButton.setOnClickListener(v -> showResults());

        return view;
    }

    private void showCustomToast(String message, View anchorView) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View layout = inflater.inflate(R.layout.custom_toast, null);

        // Устанавливаем текст сообщения
        TextView toastText = layout.findViewById(R.id.toastText);
        toastText.setText(message);

        // Создаём Toast
        Toast toast = new Toast(requireContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        // Располагаем Toast под указанным элементом (anchorView)
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location); // Получаем координаты кнопки
        int yOffset = location[1] + anchorView.getHeight(); // Смещение вниз на высоту кнопки

        toast.setGravity(Gravity.TOP | Gravity.START, location[0], yOffset);
        toast.show();
    }

    private void handleConfirm() {
        if (step == 0) {
            problem = problemEditText.getText().toString().trim();
            if (TextUtils.isEmpty(problem)) {
                showCustomToast("Введите проблему", confirmButton);
                return;
            }

            step++;
            problemEditText.setText(""); // Очищаем поле проблемы
            answerEditText.requestFocus(); // Устанавливаем фокус на поле для ответа
            showCustomToast("Теперь ответьте, почему это произошло", confirmButton);
        } else if (step <= 5) {
            String answer = answerEditText.getText().toString().trim();
            if (TextUtils.isEmpty(answer)) {
                showCustomToast("Введите ответ", confirmButton);
                return;
            }

            answers.add(answer); // Сохраняем ответ
            problemEditText.setText(answer); // Переносим ответ в поле проблемы
            answerEditText.setText(""); // Очищаем поле для следующего ответа
            answerEditText.requestFocus(); // Устанавливаем фокус на поле ответа
            step++;

            if (step <= 5) {
                showCustomToast("Ответьте ещё раз, почему это произошло", confirmButton);
            }

            if (step > 5) {
                confirmButton.setVisibility(View.GONE);
                solvedButton.setVisibility(View.VISIBLE);
                showCustomToast("Вы ответили на все вопросы. Проблема решена?", solvedButton);
            }
        }
    }

    private void showResults() {
        StringBuilder result = new StringBuilder();
        result.append("Проблема: ").append(problem).append("\n\n");
        for (int i = 0; i < answers.size(); i++) {
            result.append("Почему ").append(i + 1).append(": ").append(answers.get(i)).append("\n");
        }

        // Показываем результат через кастомный Toast
        showCustomToast(result.toString(), solvedButton);
    }
}