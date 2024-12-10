package com.example.application02;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application02.R;

import java.util.List;

public class TestResult {
    private String testName;
    private int score;

    // Конструктор по умолчанию (обязательно для Firebase)
    public TestResult() {}

    public TestResult(String testName, int score) {
        this.testName = testName;
        this.score = score;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}