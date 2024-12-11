package com.example.application02;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

    private TableLayout tableLayout;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        tableLayout = findViewById(R.id.tableLayout);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Results");

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Загружаем результаты для всех тестов
            databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    tableLayout.removeAllViews(); // Очищаем таблицу перед заполнением

                    // Добавляем заголовок таблицы
                    addTableHeader();

                    for (DataSnapshot testSnapshot : dataSnapshot.getChildren()) {
                        String testName = testSnapshot.getKey();
                        TestResult previousResult = testSnapshot.child("previousResult").getValue(TestResult.class);
                        TestResult lastResult = testSnapshot.child("lastResult").getValue(TestResult.class);

                        // Добавляем строку с результатами для каждого теста
                        addTableRow(testName, previousResult, lastResult);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Failed to load test results", databaseError.toException());
                }
            });
        }
    }

    // Метод для добавления заголовка таблицы
    private void addTableHeader() {
        TableRow headerRow = new TableRow(this);

        TextView testColumn = createTextView("Тест");
        TextView previousResultColumn = createTextView("Предыдущий результат");
        TextView lastResultColumn = createTextView("Нынешний результат");
        TextView dynamicColumn = createTextView("Динамика");

        headerRow.addView(testColumn);
        headerRow.addView(previousResultColumn);
        headerRow.addView(lastResultColumn);
        headerRow.addView(dynamicColumn);

        tableLayout.addView(headerRow);
    }

    // Метод для добавления строки с результатами
    private void addTableRow(String testName, TestResult previousResult, TestResult lastResult) {
        TableRow row = new TableRow(this);

        TextView testNameView = createTextView(testName);
        TextView previousResultView = createTextView(previousResult != null ? String.valueOf(previousResult.getScore()) : "-");
        TextView lastResultView = createTextView(lastResult != null ? String.valueOf(lastResult.getScore()) : "-");

        // Рассчитываем динамику
        String dynamic = "-";
        if (previousResult != null && lastResult != null) {
            int diff = lastResult.getScore() - previousResult.getScore();
            dynamic = diff > 0 ? "+" + diff : String.valueOf(diff);
        }
        TextView dynamicView = createTextView(dynamic);

        row.addView(testNameView);
        row.addView(previousResultView);
        row.addView(lastResultView);
        row.addView(dynamicView);

        tableLayout.addView(row);
    }

    // Вспомогательный метод для создания текстового поля
    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}