package com.example.application02.TEST;

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