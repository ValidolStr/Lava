package com.example.application02.tecnics;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.app.AlertDialog;

import com.example.application02.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AffirmationsFragment extends Fragment {

    private static final String[] AFFIRMATIONS = {
            "Я люблю и принимаю себя таким(такой), какой(какая) я есть.",
            "Я обладаю внутренней силой и мудростью.",
            "Мои мысли создают мою реальность.",
            "Я достоин(достойна) счастья и успеха.",
            "Я привлекаю позитивные энергии и возможности.",
            "Я способен(способна) справиться с любыми трудностями.",
            "Каждый день я делаю шаг к своим целям.",
            "Я открываю свое сердце для любви и дружбы.",
            "Я учусь на своих ошибках и расту.",
            "Я окружен(окружена) поддержкой и заботой.",
            "Я верю в свои способности и таланты.",
            "Я наполняю свою жизнь радостью и счастьем.",
            "Я выбираю быть счастливым(счастливой) здесь и сейчас.",
            "Я создаю свою собственную реальность.",
            "Я принимаю перемены как часть жизни.",
            "Я достоин(достойна) успеха во всех своих начинаниях.",
            "Я излучаю уверенность и позитив.",
            "Я доверяю своему внутреннему голосу.",
            "Я способен(способна) преодолевать любые преграды.",
            "Мое тело здорово, и я забочусь о нем.",
            "Я открываю новые возможности для роста и развития.",
            "Я благодарен(благодарна) за все, что у меня есть.",
            "Я выбираю прощение и освобождаюсь от обид.",
            "Я создаю гармонию в своей жизни.",
            "Я стремяюсь к целям, которые приносят мне радость.",
            "Я верю в свои мечты и стремлюсь к их реализации.",
            "Я привлекаю успех и процветание.",
            "Я осознаю свою ценность и уникальность.",
            "Я наполняю свою жизнь положительными эмоциями.",
            "Я принимаю себя и свои чувства без осуждения."
    };

    private static final String PREFS_NAME = "AffirmationsPrefs";
    private static final String KEY_CURRENT_AFFIRMATION = "currentAffirmation";
    private static final String KEY_LAST_UPDATE_DATE = "lastUpdateDate";
    private static final String KEY_AFFIRMATION_USED = "affirmationUsed";

    private TextView affirmationTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_affirmations, container, false);

        affirmationTextView = view.findViewById(R.id.affirmation_text);
        Button getAffirmationButton = view.findViewById(R.id.get_affirmation_button);
        Button instructionButton = view.findViewById(R.id.instruction_button); // Новая кнопка для инструкции

        // Load and display the current affirmation
        loadCurrentAffirmation();

        getAffirmationButton.setOnClickListener(v -> {
            // Check if the affirmation was already used today
            if (!canGetAffirmation()) {
                affirmationTextView.setText("Вы уже получили аффирмацию на сегодня. Попробуйте завтра!");
                return;
            }

            // Get a new affirmation and update UI
            String newAffirmation = getRandomAffirmation();
            saveCurrentAffirmation(newAffirmation);
            affirmationTextView.setText(newAffirmation);
        });

        // Обработчик для кнопки инструкции
        instructionButton.setOnClickListener(v -> showInstructionDialog());

        // Schedule daily notification and reset
        scheduleDailyReset();
        scheduleDailyNotification();

        return view;
    }

    private boolean canGetAffirmation() {
        Context context = requireContext();
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return !prefs.getBoolean(KEY_AFFIRMATION_USED, false);
    }

    private void loadCurrentAffirmation() {
        Context context = requireContext();
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String lastDate = prefs.getString(KEY_LAST_UPDATE_DATE, "");
        String currentAffirmation = prefs.getString(KEY_CURRENT_AFFIRMATION, null);

        // Check if the date has changed
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        if (!lastDate.equals(today) || currentAffirmation == null) {
            // Update to a new affirmation
            currentAffirmation = getRandomAffirmation();
            saveCurrentAffirmation(currentAffirmation);
        }

        affirmationTextView.setText(currentAffirmation);
    }

    private void saveCurrentAffirmation(String affirmation) {
        Context context = requireContext();
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Save the current affirmation, the date, and mark it as "used"
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        editor.putString(KEY_CURRENT_AFFIRMATION, affirmation);
        editor.putString(KEY_LAST_UPDATE_DATE, today);
        editor.putBoolean(KEY_AFFIRMATION_USED, true);
        editor.apply();
    }

    private String getRandomAffirmation() {
        Random random = new Random();
        return AFFIRMATIONS[random.nextInt(AFFIRMATIONS.length)];
    }

    private void scheduleDailyReset() {
        Context context = requireContext();
        Intent intent = new Intent(context, ResetReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void scheduleDailyNotification() {
        Context context = requireContext();
        Intent intent = new Intent(context, ResetReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    // Метод для отображения диалога с инструкцией
    private void showInstructionDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Инструкция")
                .setMessage("Нажми на кнопку и получите аффирмацию дня, которая поможет тебе начать этот день с хорошим настроением, укрепить уверенность в себе и настроить на позитив.")
                .setPositiveButton("ОК", null)
                .show();
    }
}