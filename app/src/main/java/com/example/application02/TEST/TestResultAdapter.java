package com.example.application02.TEST;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application02.R;

import java.util.List;

public class TestResultAdapter extends RecyclerView.Adapter<TestResultAdapter.TestResultViewHolder> {

    private List<TestResult> testResults;

    public TestResultAdapter(List<TestResult> testResults) {
        this.testResults = testResults;
    }

    @NonNull
    @Override
    public TestResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test_result, parent, false);
        return new TestResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestResultViewHolder holder, int position) {
        TestResult testResult = testResults.get(position);
        holder.testNameTextView.setText(testResult.getTestName());
        holder.scoreTextView.setText(String.valueOf(testResult.getScore()));
    }

    @Override
    public int getItemCount() {
        return testResults.size();
    }

    static class TestResultViewHolder extends RecyclerView.ViewHolder {
        TextView testNameTextView;
        TextView scoreTextView;

        public TestResultViewHolder(@NonNull View itemView) {
            super(itemView);
            testNameTextView = itemView.findViewById(R.id.testNameTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
        }
    }
}
