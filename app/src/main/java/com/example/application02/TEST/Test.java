package com.example.application02.TEST;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;


// Базовый класс для всех тестов
public abstract class Test {
    private String title; // Название теста
    private List<Question> questions; // Список вопросов
    private int type; // Тип теста

    public Test(String title, List<Question> questions, int type) {
        this.title = title;
        this.questions = questions;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public List<Question> getQuestions() {
        // Проверяем, есть ли вопросы в списке

        return questions;
    }

    public int getType() {
        return type;
    }

    public Test() {
    }

    // Абстрактный метод для прохождения теста
    public abstract void takeTest();

    // Абстрактный метод обработки результата
    public abstract void processResult(int result);
}

// Подкласс для тестов с ответами "Да" или "Нет" (Тип 1)
class YesNoTest extends Test {

    public YesNoTest(String title, List<Question> questions) {
        super(title, questions, 1);
    }

    @Override
    public void takeTest() {
        System.out.println("Проведение теста с ответами Да/Нет: " + getTitle());
        for (Question question : getQuestions()) {
            System.out.println("Вопрос: " + question.getText());
            System.out.println("Ответы: Да / Нет");
            // Здесь может быть логика для получения ответа пользователя
        }
    }

    @Override
    public void processResult(int result) {
        System.out.println("Обработка результата для теста 'Да/Нет' с результатом: " + result);
        // Логика обработки результата теста "Да/Нет"
    }
}

// Подкласс для тестов с оценкой (от 1 до 4) (Тип 2)
class RatingTest extends Test {

    public RatingTest(String title, List<Question> questions) {
        super(title, questions, 2);
    }

    @Override
    public void takeTest() {
        System.out.println("Проведение теста с оценкой: " + getTitle());
        for (Question question : getQuestions()) {
            System.out.println("Вопрос: " + question.getText());
            System.out.println("Оцените от 1 до 4");
            // Здесь может быть логика для получения оценки от пользователя
        }
    }

    @Override
    public void processResult(int result) {
        System.out.println("Обработка результата для теста с оценкой: " + result);
        // Логика обработки результата для рейтингового теста
    }
}

// Подкласс для тестов с выбором одного варианта ответа (Тип 3)
class SingleChoiceTest extends Test {

    public SingleChoiceTest(String title, List<Question> questions) {
        super(title, questions, 3);
    }

    @Override
    public void takeTest() {
        System.out.println("Проведение теста с выбором одного варианта: " + getTitle());
        for (Question question : getQuestions()) {
            System.out.println("Вопрос: " + question.getText());
            System.out.println("Выберите один из предложенных вариантов");
            // Здесь может быть логика для получения выбора пользователя
        }
    }

    @Override
    public void processResult(int result) {
        System.out.println("Обработка результата для теста с выбором одного варианта: " + result);
        // Логика обработки результата для теста с одним выбором
    }
}

// Подкласс для тестов с выбором нескольких вариантов ответа (Тип 4)
class MultipleChoiceTest extends Test {

    public MultipleChoiceTest(String title, List<Question> questions) {
        super(title, questions, 4);
    }

    @Override
    public void takeTest() {
        System.out.println("Проведение теста с выбором нескольких вариантов: " + getTitle());
        for (Question question : getQuestions()) {
            System.out.println("Вопрос: " + question.getText());
            System.out.println("Выберите несколько из предложенных вариантов");
            // Здесь может быть логика для получения выбора пользователя
        }
    }

    @Override
    public void processResult(int result) {
        System.out.println("Обработка результата для теста с выбором нескольких вариантов: " + result);
        // Логика обработки результата для теста с несколькими выборами
    }
}

// Класс для вывода списка тестов
class TestManager {

    private List<Test> tests;

    public TestManager(List<Test> tests) {
        this.tests = tests;
    }

    public void printTests() {
        System.out.println("Список доступных тестов:");
        for (Test test : tests) {
            System.out.println("Название: " + test.getTitle() + ", Тип: " + test.getType());
        }
    }
}

// Класс вопроса
class Question implements Parcelable {
    private String text;

    public Question(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    // Конструктор для восстановления объекта из Parcel
    protected Question(Parcel in) {
        text = in.readString();
    }

    public Question() {
    }

    // Реализация метода writeToParcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // CREATOR для создания объектов Question из Parcel
    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}

