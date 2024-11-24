package com.example.application02;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class TestForm extends AppCompatActivity {

    private FrameLayout frameLayout;
    private fiveresult fiver = new fiveresult();
    private fourresult fourr = new fourresult();
    private  sickbar sick = new sickbar();
    private yesornot yn = new yesornot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test_form);
        init();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void init (){
        int itemIndex = getIntent().getIntExtra("item_index", 0);
        frameLayout = findViewById(R.id.frameLayout);
        switch (itemIndex) {
            case 0:
                setnewfragment(yn);
                break;

            case 1:
                setnewfragment(fiver);
                break;

            case 2:
                setnewfragment(fourr);
                break;

            case 3:
                setnewfragment(sick);
                break;

            default:
                throw new IllegalArgumentException("Неизвестный itemIndex: " + itemIndex);
        }

    }
    private void setnewfragment (Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }
}