package com.example.application02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.activity.EdgeToEdge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TestingActivity extends AppCompatActivity {


    private DatabaseReference testlist;
    private String tests = "tests";
    protected ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> listtest;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_testing);
        init();
        getdata();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
    }
    private void init(){
        listView = findViewById(R.id.listView);
        listtest = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listtest);
        listView.setAdapter(adapter);

        testlist = FirebaseDatabase.getInstance().getReference(tests);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            jumpToNextActivity(position);
        });
    }
    private void getdata(){
        ValueEventListener vlistener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!listtest.isEmpty()) listtest.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    Testlist test = ds.getValue(Testlist.class);
                    assert test != null;
                    listtest.add(test.title);
                }adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }; testlist.addValueEventListener(vlistener);
    }

    // Метод для перехода на другую активность с передачей индекса
    public void jumpToNextActivity(int position) {
        Intent intent = new Intent(this, TestForm.class);
        intent.putExtra("item_index", position); // Передаем индекс выбранного элемента
        startActivity(intent);
    }
    public void Jamptoback (View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}