package com.example.application02.tecnics;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.application02.R;

import java.util.ArrayList;
import java.util.List;

public class FiveStopsFragment extends Fragment {

    private EditText problemEditText, answerEditText;
    private Button confirmButton, solvedButton;
    private TextView messageTextView;

    private String problem; // Проблема
    private List<String> answers = new ArrayList<>(); // Ответы "Почему?"
    private int step = 0; // Текущий шаг (0 - ввод проблемы, 1-5 - ответы)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_five_stops, container, false);

        // Инициализация элементов интерфейса
        problemEditText = view.findViewById(R.id.problemEditText);
        answerEditText = view.findViewById(R.id.answerEditText);
        confirmButton = view.findViewById(R.id.confirmButton);
        solvedButton = view.findViewById(R.id.solvedButton);
        messageTextView = view.findViewById(R.id.messageTextView);

        // Блокировка кнопки подтверждения по умолчанию
        confirmButton.setEnabled(false);

        // Добавление TextWatcher для активации кнопки, если поля заполнены
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        problemEditText.addTextChangedListener(textWatcher);
        answerEditText.addTextChangedListener(textWatcher);

        // Установка обработчиков кнопок
        confirmButton.setOnClickListener(v -> handleConfirm());
        solvedButton.setOnClickListener(v -> showResults());

        return view;
    }

    private void validateInputs() {
        // Активируем кнопку только если оба поля заполнены
        boolean isProblemFilled = !TextUtils.isEmpty(problemEditText.getText().toString().trim());
        boolean isAnswerFilled = !TextUtils.isEmpty(answerEditText.getText().toString().trim());
        confirmButton.setEnabled(isProblemFilled && isAnswerFilled);
    }

    private void handleConfirm() {
        if (step == 0) {
            // Сохранение начальной проблемы
            problem = problemEditText.getText().toString().trim();
            step++;
            setMessage("Теперь ответьте: почему это произошло?");
        } else if (step <= 5) {
            // Сохранение ответа
            String answerText = answerEditText.getText().toString().trim();
            answers.add(answerText);

            // Перенос ответа в поле проблемы
            problemEditText.setText(answerText);

            // Очищаем поле ответа
            answerEditText.setText("");

            // Увеличиваем шаг
            step++;

            if (step <= 5) {
                setMessage("Почему это произошло?");
            } else {
                confirmButton.setVisibility(View.GONE);
                solvedButton.setVisibility(View.VISIBLE);
                setMessage("Все ответы сохранены. Проверьте результат.");
            }
        }

        validateInputs();
    }

    private void setMessage(String message) {
        messageTextView.setText(message);
    }

    private void showResults() {
        // Формирование результата
        StringBuilder result = new StringBuilder();
        result.append("Проблема: ").append(problem).append("\n\n");
        for (int i = 0; i < answers.size(); i++) {
            result.append("Почему ").append(i + 1).append(": ").append(answers.get(i)).append("\n");
        }

        setMessage(result.toString());
    }
}