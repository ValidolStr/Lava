package com.example.application02.tecnics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class SquareView extends View {
    private Paint paint;
    private int activeSide = -1; // Текущая активная сторона квадрата (0 - верх, 1 - правая, 2 - нижняя, 3 - левая)
    private float progress = 0; // Прогресс закраски текущей стороны (от 0 до 1)
    private Handler handler = new Handler();
    private int animationDuration = 3000; // Длительность анимации в миллисекундах
    private int strokeThickness = 20; // Толщина линий

    public SquareView(Context context) {
        super(context);
        init();
    }

    public SquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeThickness); // Устанавливаем толщину линии
        paint.setAntiAlias(true); // Сглаживание
        paint.setStrokeJoin(Paint.Join.MITER); // Убираем лишние углы
        paint.setStrokeCap(Paint.Cap.BUTT); // Убираем закругления на концах линий
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int padding = 50;

        // Координаты сторон квадрата
        int left = padding;
        int top = padding;
        int right = width - padding;
        int bottom = height - padding;

        // Смещение для устранения наложения линий
        float offset = strokeThickness / 2f;

        // Рисуем стороны квадрата с прогрессом
        paint.setColor(0xFF888888); // Цвет неактивных сторон
        canvas.drawLine(left + offset, top, right - offset, top, paint); // Верхняя сторона
        canvas.drawLine(right, top + offset, right, bottom - offset, paint); // Правая сторона
        canvas.drawLine(right - offset, bottom, left + offset, bottom, paint); // Нижняя сторона
        canvas.drawLine(left, bottom - offset, left, top + offset, paint); // Левая сторона

        // Рисуем активную сторону с прогрессом
        if (activeSide != -1) {
            paint.setColor(0xFF00FF00); // Цвет активной стороны

            // Вычисляем длину линии в зависимости от прогресса
            float progressLength;
            switch (activeSide) {
                case 0: // Верхняя сторона
                    progressLength = progress * (right - left - offset * 2);
                    canvas.drawLine(left + offset, top, left + offset + progressLength, top, paint);
                    break;
                case 1: // Правая сторона
                    progressLength = progress * (bottom - top - offset * 2);
                    canvas.drawLine(right, top + offset, right, top + offset + progressLength, paint);
                    break;
                case 2: // Нижняя сторона
                    progressLength = progress * (right - left - offset * 2);
                    canvas.drawLine(right - offset, bottom, right - offset - progressLength, bottom, paint);
                    break;
                case 3: // Левая сторона
                    progressLength = progress * (bottom - top - offset * 2);
                    canvas.drawLine(left, bottom - offset, left, bottom - offset - progressLength, paint);
                    break;
            }
        }
    }

    public void updateProgress(int sideIndex) {
        activeSide = sideIndex; // Устанавливаем текущую активную сторону
        progress = 0; // Сбрасываем прогресс
        startAnimation(); // Запускаем анимацию закраски
    }

    private void startAnimation() {
        final long startTime = System.currentTimeMillis();
        final long endTime = startTime + animationDuration;

        handler.post(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                if (currentTime >= endTime) {
                    // Анимация завершена
                    progress = 1;
                    invalidate();
                } else {
                    // Обновляем прогресс
                    progress = (float) (currentTime - startTime) / animationDuration;
                    invalidate(); // Перерисовываем View
                    handler.postDelayed(this, 16); // Запускаем следующий кадр (примерно 60 FPS)
                }
            }
        });
    }

    public void reset() {
        activeSide = -1; // Сбрасываем активную сторону
        progress = 0; // Сбрасываем прогресс
        invalidate(); // Перерисовываем View
    }
}