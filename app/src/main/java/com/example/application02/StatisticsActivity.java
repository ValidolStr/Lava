package com.example.application02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class StatisticsActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private   FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private  List<TestResult> testResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistics);



        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Results");

        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    testResults.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TestResult result = snapshot.getValue(TestResult.class);
                        if (result != null) {
                            testResults.add(result);
                        }
                    }
                    displayStatistics();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Обработка ошибок
                }
            });
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void displayStatistics() {
        // Пример: используем TextView для отображения статистики
        TextView statisticsTextView = findViewById(R.id.statistics);

        StringBuilder statistics = new StringBuilder();
        statistics.append("Название тестов:\n");
        for (TestResult result : testResults) {
            statistics.append(result.getTestName()).append("\n");
        }

        statistics.append("\nПоследние результаты:\n");
        for (TestResult result : testResults) {
            statistics.append(result.getScore()).append("\n");
        }

        // Динамика
        statistics.append("\nДинамика:\n");
        for (int i = 1; i < testResults.size(); i++) {
            int previousScore = testResults.get(i - 1).getScore();
            int currentScore = testResults.get(i).getScore();
            String trend = currentScore > previousScore ? "Улучшение" : "Ухудшение";
            statistics.append(trend).append("\n");
        }

        statisticsTextView.setText(statistics.toString());
    }

    public void Jamptoback (View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}