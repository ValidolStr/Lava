package com.example.application02.tecnics;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application02.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {

    private EditText noteTitleEditText, noteContentEditText;
    private RecyclerView notesRecyclerView;
    private NotesAdapter notesAdapter;
    private List<Note> notesList = new ArrayList<>();
    private DatabaseReference notesRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        noteTitleEditText = view.findViewById(R.id.noteTitleEditText);
        noteContentEditText = view.findViewById(R.id.noteContentEditText);
        notesRecyclerView = view.findViewById(R.id.notesRecyclerView);

        view.findViewById(R.id.saveNoteButton).setOnClickListener(v -> saveNote());

        // Firebase
        String userId = FirebaseAuth.getInstance().getUid();
        notesRef = FirebaseDatabase.getInstance().getReference("notes").child(userId);

        setupRecyclerView();
        loadNotes();

        return view;
    }

    private void setupRecyclerView() {
        notesAdapter = new NotesAdapter(notesList, new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onItemClicked(Note note) {
                // Устанавливаем текст заметки в поля редактирования
                noteTitleEditText.setText(note.getTitle());
                noteContentEditText.setText(note.getContent());
            }

            @Override
            public void onDeleteClicked(String noteId) {
                notesRef.child(noteId).removeValue();
            }
        });
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notesRecyclerView.setAdapter(notesAdapter);
    }

    private void loadNotes() {
        notesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notesList.clear();
                for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                    Note note = noteSnapshot.getValue(Note.class);
                    notesList.add(note);
                }
                notesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Ошибка загрузки заметок", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveNote() {
        String title = noteTitleEditText.getText().toString().trim();
        String content = noteContentEditText.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(getContext(), "Введите название заметки", Toast.LENGTH_SHORT).show();
            return;
        }

        String noteId = notesRef.push().getKey();
        Note note = new Note(noteId, title, content);
        notesRef.child(noteId).setValue(note);

        noteTitleEditText.setText("");
        noteContentEditText.setText("");
        Toast.makeText(getContext(), "Заметка сохранена", Toast.LENGTH_SHORT).show();
    }
}