package com.example.application02.tecnics;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.application02.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CalmPlaceFragment extends Fragment {

    private EditText editTextInput;
    private Button buttonSave;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calm_place, container, false);

        editTextInput = view.findViewById(R.id.editTextInput);
        buttonSave = view.findViewById(R.id.buttonSave);

        // Инициализация Firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("UserInputs");

        // Загружаем предыдущие данные, если они есть
        loadData();

        // Устанавливаем слушатель на кнопку сохранения
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        return view;
    }

    private void loadData() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference.child(userId).child("inputText").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String inputText = dataSnapshot.getValue(String.class);
                    if (inputText != null) {
                        editTextInput.setText(inputText);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Обработка ошибок
                }
            });
        }
    }

    private void saveData() {
        String inputText = editTextInput.getText().toString().trim();
        if (!TextUtils.isEmpty(inputText) && currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference.child(userId).child("inputText").setValue(inputText);
        }
    }
}