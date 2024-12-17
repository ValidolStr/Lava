package com.example.application02;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application02.TEST.TestResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatisticsActivity extends AppCompatActivity {

    private TableLayout statisticsTableLayout;
    private TableLayout dynamicTableLayout;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Инициализация таблиц
        statisticsTableLayout = findViewById(R.id.statisticsTableLayout);
        dynamicTableLayout = findViewById(R.id.dynamicTableLayout);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Results");

        // Проверяем, что пользователь авторизован
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Загружаем результаты из Firebase
            databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Очищаем таблицы перед добавлением новых данных
                    statisticsTableLayout.removeAllViews();
                    dynamicTableLayout.removeAllViews();

                    // Добавляем заголовки таблиц
                    addStatisticsHeader();
                    addDynamicHeader();

                    for (DataSnapshot testSnapshot : dataSnapshot.getChildren()) {
                        String testName = testSnapshot.getKey();
                        TestResult previousResult = testSnapshot.child("previousResult").getValue(TestResult.class);
                        TestResult lastResult = testSnapshot.child("lastResult").getValue(TestResult.class);

                        // Логируем данные для отладки
                        Log.d("Firebase", "Test Name: " + testName);
                        Log.d("Firebase", "Previous Result: " + (previousResult != null ? previousResult.getScore() : "null"));
                        Log.d("Firebase", "Last Result: " + (lastResult != null ? lastResult.getScore() : "null"));

                        // Добавляем строку для таблицы статистики
                        addStatisticsRow(testName, previousResult, lastResult);

                        // Добавляем строку для таблицы динамики
                        addDynamicRow(testName, previousResult, lastResult);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Failed to load test results", databaseError.toException());
                }
            });
        }
    }

    // Добавление заголовков для таблицы статистики
    private void addStatisticsHeader() {
        TableRow headerRow = new TableRow(this);

        TextView testColumn = createTextView("Тесты", true);
        TextView previousResultColumn = createTextView("Предыдущий результат", true);
        TextView lastResultColumn = createTextView("Нынешний результат", true);

        headerRow.addView(testColumn);
        headerRow.addView(previousResultColumn);
        headerRow.addView(lastResultColumn);

        statisticsTableLayout.addView(headerRow);
    }

    // Добавление строки в таблицу статистики
    private void addStatisticsRow(String testName, TestResult previousResult, TestResult lastResult) {
        TableRow row = new TableRow(this);

        TextView testNameView = createTextView(testName != null ? testName : "—", false);
        TextView previousResultView = createTextView(previousResult != null ? String.valueOf(previousResult.getScore()) : "—", false);
        TextView lastResultView = createTextView(lastResult != null ? String.valueOf(lastResult.getScore()) : "—", false);

        row.addView(testNameView);
        row.addView(previousResultView);
        row.addView(lastResultView);

        statisticsTableLayout.addView(row);
    }

    // Добавление заголовков для таблицы динамики
    private void addDynamicHeader() {
        TableRow headerRow = new TableRow(this);

        TextView testColumn = createTextView("Тесты", true);

        headerRow.addView(testColumn);

        dynamicTableLayout.addView(headerRow);
    }

    // Добавление строки в таблицу динамики
    private void addDynamicRow(String testName, TestResult previousResult, TestResult lastResult) {
        if (testName == null || previousResult == null || lastResult == null) {
            return; // Если данных нет, пропускаем
        }

        // Верхняя строка с названием теста
        TableRow headerRow = new TableRow(this);
        headerRow.addView(createTextView(testName, true));
        dynamicTableLayout.addView(headerRow);

        // Нижняя строка с разницей результатов
        TableRow resultRow = new TableRow(this);

        int previousScore = previousResult.getScore();
        int lastScore = lastResult.getScore();
        int difference = lastScore - previousScore;

        TextView differenceView = createTextView(String.valueOf(difference), false);
        resultRow.addView(differenceView);

        dynamicTableLayout.addView(resultRow);
    }

    // Вспомогательный метод для создания текстового поля
    private TextView createTextView(String text, boolean isHeader) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        textView.setTypeface(null, isHeader ? Typeface.BOLD : Typeface.NORMAL);
        textView.setGravity(android.view.Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        // Установка параметров для поддержки переноса текста
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                0, // ширина 0dp для использования layout_weight
                TableRow.LayoutParams.WRAP_CONTENT
        );
        params.weight = 1; // равномерное распределение ширины между столбцами
        textView.setLayoutParams(params);

        // Установка свойств для переноса текста
        textView.setSingleLine(false); // Разрешить множественные строки
        textView.setMaxLines(2); // Ограничить количество строк
        textView.setEllipsize(TextUtils.TruncateAt.END); // Добавить многоточие, если текст длиннее

        return textView;
    }
}