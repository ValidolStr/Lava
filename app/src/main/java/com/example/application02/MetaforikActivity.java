package com.example.application02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    private Random random;
    private Set<String> currentImages;

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

        random = new Random();
        currentImages = new HashSet<>();

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
            changeImage(clickedImageView);
        }
    }

    private void changeImage(ImageView imageView) {
        String imageName;

        // Clear current images set if all 30 images are used
        if (currentImages.size() >= 58) {
            currentImages.clear();
        }

        do {
            int randomIndex = random.nextInt(58) + 1; // Generate a random number from 1 to 30
            imageName = "cart__" + randomIndex + "_";
        } while (currentImages.contains(imageName));

        currentImages.add(imageName);

        int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        imageView.setImageResource(resId);
    }

}