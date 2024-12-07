package com.example.application02;

public class TestResult {
    private String testName;
    private int score;

    public TestResult() {
        // Пустой конструктор для Firebase
    }

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
