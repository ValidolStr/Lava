package com.example.application02;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.EdgeToEdge;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MetaforikActivity extends AppCompatActivity {

    private ImageView mainimg, secondimg1, secondimg2, secondimg3;
    private Random randomMain; // Для mainimg
    private Random randomSecond; // Для secondimg1, secondimg2, secondimg3
    private Set<String> currentMainImages;
    private Set<String> currentSecondImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_metaforik);

        // Initialize ImageView elements
        mainimg = findViewById(R.id.mainimg);
        secondimg1 = findViewById(R.id.secondimg1);
        secondimg2 = findViewById(R.id.secondimg2);
        secondimg3 = findViewById(R.id.secondimg3);

        randomMain = new Random();
        randomSecond = new Random();
        currentMainImages = new HashSet<>();
        currentSecondImages = new HashSet<>();

        Button infobtn = findViewById(R.id.infobtn);

        // Привязываем обработчик нажатия
        infobtn .setOnClickListener(v -> showMessageBox());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void Jamptoback(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void svichCart(View v) {
        if (v instanceof ImageView) {
            ImageView clickedImageView = (ImageView) v;

            // Determine if this is the main image or a secondary image
            if (clickedImageView == mainimg) {
                changeMainImage(clickedImageView);
            } else {
                changeSecondImage(clickedImageView);
            }
        }
    }

    // Метод для рандомизации изображений для mainimg
    private void changeMainImage(ImageView imageView) {
        String imageName;

        // Clear current images set for mainimg if all 75 images are used
        if (currentMainImages.size() >= 17) {
            currentMainImages.clear();
        }

        do {
            int randomIndex = randomMain.nextInt(17) + 1; // Generate a random number from 1 to 75
            imageName = "probl__" + randomIndex + "_";
        } while (currentMainImages.contains(imageName));

        currentMainImages.add(imageName);

        int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        imageView.setImageResource(resId);
    }

    // Метод для рандомизации изображений для secondimg1, secondimg2, secondimg3
    private void changeSecondImage(ImageView imageView) {
        String imageName;

        // Clear current images set for second images if all 75 images are used
        if (currentSecondImages.size() >= 55) {
            currentSecondImages.clear();
        }

        do {
            int randomIndex = randomSecond.nextInt(55) + 1; // Generate a random number from 1 to 75
            imageName = "cart__" + randomIndex + "_";
        } while (currentSecondImages.contains(imageName));

        currentSecondImages.add(imageName);

        int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        imageView.setImageResource(resId);
    }
    public void showMessageBox() {
        // Создаём диалог
        new AlertDialog.Builder(this)
                .setTitle("Инструкция") // Заголовок
                .setMessage("Внимательно посмотри на каждую из трёх карт по очереди и ответь на вопросы:\n\n" +
                        "1. Что ты на ней видишь?\n" +
                        "2. Какое настроение у карты?\n" +
                        "3. Какие чувства и эмоции она вызывает?\n" +
                        "4. Нравится ли она тебе?\n" +
                        "5. Что на ней происходит?\n" +
                        "6. Как она отражает решение твоей проблемы?\n" +
                        "7. Что тебе необходимо сделать, чтобы она решилась?")
                .setPositiveButton("Понятно", (dialog, which) -> {
                    // Действие при нажатии на кнопку "Понятно" (закрыть диалог)
                    dialog.dismiss();
                })
                .setCancelable(true) // Разрешить закрытие диалога по нажатию вне его
                .show();
    }



}