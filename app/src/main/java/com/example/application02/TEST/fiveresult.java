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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class fiveresult extends Fragment {

    private List<Question> questions; // Список вопросов
    private int currentQuestionIndex = 0; // Индекс текущего вопроса
    private int totalScore = 0; // Общий счёт
    private boolean isTestFinished = false; // Флаг завершения теста

    private TextView questionTextView; // Поле для вопроса
    private Button button5Points, button4Points, button3Points, button2Points, button1Point; // Кнопки ответов
    private Button resultButton; // Кнопка для показа результата

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fiveresult, container, false);

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Results");

        // Находим элементы интерфейса
        questionTextView = view.findViewById(R.id.questionTextView);
        button5Points = view.findViewById(R.id.button5Points);
        button4Points = view.findViewById(R.id.button4Points);
        button3Points = view.findViewById(R.id.button3Points);
        button2Points = view.findViewById(R.id.button2Points);
        button1Point = view.findViewById(R.id.button1Point);
        resultButton = view.findViewById(R.id.resultbtn); // Кнопка результата

        // Скрываем кнопку результата до завершения теста
        resultButton.setVisibility(View.GONE);

        // Устанавливаем обработчики кнопок
        button5Points.setOnClickListener(v -> handleAnswer(4));
        button4Points.setOnClickListener(v -> handleAnswer(3));
        button3Points.setOnClickListener(v -> handleAnswer(2));
        button2Points.setOnClickListener(v -> handleAnswer(1));
        button1Point.setOnClickListener(v -> handleAnswer(0));
        resultButton.setOnClickListener(v -> showResult()); // Показываем результат по нажатию

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

        // Скрываем кнопки ответов
        button5Points.setVisibility(View.GONE);
        button4Points.setVisibility(View.GONE);
        button3Points.setVisibility(View.GONE);
        button2Points.setVisibility(View.GONE);
        button1Point.setVisibility(View.GONE);

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
        String testName = "Определение уровня самооценки"; // Уникальное имя теста
        TestResult newResult = new TestResult(testName, totalScore);

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

        // Интерпретация результатов
        if (totalScore <= 43) {
            recommendation = "Высокий уровень самооценки: Вы не страдаете от \"комплекса неполноценности\", правильно реагируете на замечания и редко сомневаетесь в своих действиях.";
        } else if (totalScore <= 86) {
            recommendation = "Средний уровень самооценки: Вы редко страдаете от \"комплекса неполноценности\" и время от времени стараетесь подстроиться под мнение других.";
        } else {
            recommendation = "Низкий уровень самооценки: Вы болезненно реагируете на критику, часто стараетесь угодить другим и страдаете от \"комплекса неполноценности\".";
        }

        String resultMessage = "Ваш итоговый счёт: " + totalScore + "\n" + recommendation;

        // Создаём и показываем диалоговое окно
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Результат теста")
                .setMessage(resultMessage)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Закрываем диалог
                    dialog.dismiss();
                })
                .setCancelable(false) // Запрещаем закрытие диалога вне его
                .show();
    }
}