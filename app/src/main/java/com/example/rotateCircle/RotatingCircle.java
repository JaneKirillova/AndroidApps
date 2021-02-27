package com.example.rotateCircle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class RotatingCircle extends Activity {
    private static Bitmap imageOriginalFirst, imageOriginalSecond; // для работы с *.png
    private static Matrix matrixFirst, matrixSecond;

    private ImageView dialerFirst, dialerSecond;
    private int dialerHeightFirst, dialerWidthFirst, dialerHeightSecond, dialerWidthSecond;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // инициализация активности
        setContentView(R.layout.main);

        if (imageOriginalFirst == null) {
            imageOriginalFirst = BitmapFactory.decodeResource(getResources(), R.drawable.graphic_ring);
        }

        if (imageOriginalSecond == null) {
            imageOriginalSecond = BitmapFactory.decodeResource(getResources(), R.drawable.graphic_ring);
        }

        if (matrixFirst == null) {
            matrixFirst = new Matrix();
        } else {
            matrixFirst.reset();
        }


        if (matrixSecond == null) {
            matrixSecond = new Matrix();
        } else {
            matrixSecond.reset();
        }

        dialerFirst = findViewById(R.id.imageView_ring);
        dialerFirst.setOnTouchListener(new MyOnTouchListener());
        dialerFirst.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (dialerHeightFirst == 0 || dialerWidthFirst == 0) {
                dialerHeightFirst = dialerFirst.getHeight();
                dialerWidthFirst = dialerFirst.getWidth();

                Matrix resize = new Matrix();
                resize.postScale((float)Math.min(dialerWidthFirst, dialerHeightFirst) / (float) imageOriginalFirst.getWidth(), (float)Math.min(dialerWidthFirst, dialerHeightFirst) / (float) imageOriginalFirst.getHeight());
                imageOriginalFirst = Bitmap.createBitmap(imageOriginalFirst, 0, 0, imageOriginalFirst.getWidth(), imageOriginalFirst.getHeight(), resize, false);

                float translateX = dialerWidthFirst / 2 - imageOriginalFirst.getWidth() / 2;
                float translateY = dialerHeightFirst / 2 - imageOriginalFirst.getHeight() / 2;
                matrixFirst.postTranslate(translateX, translateY);

                dialerFirst.setImageBitmap(imageOriginalFirst);
                dialerFirst.setImageMatrix(matrixFirst);
            }
        });


        dialerSecond = findViewById(R.id.imageView_ring1);
        dialerSecond.setOnTouchListener(new MyOnTouchListener2());
        dialerSecond.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (dialerHeightSecond == 0 || dialerWidthSecond == 0) {
                dialerHeightSecond = dialerSecond.getHeight();
                dialerWidthSecond = dialerSecond.getWidth();

                Matrix resize = new Matrix();
                resize.postScale((float)Math.min(dialerWidthSecond, dialerHeightSecond) / (float) imageOriginalSecond.getWidth(), (float)Math.min(dialerWidthSecond, dialerHeightSecond) / (float) imageOriginalSecond.getHeight());
                imageOriginalSecond = Bitmap.createBitmap(imageOriginalSecond, 0, 0, imageOriginalSecond.getWidth(), imageOriginalSecond.getHeight(), resize, false);

                float translateX = dialerWidthSecond / 2 - imageOriginalSecond.getWidth() / 2;
                float translateY = dialerHeightSecond / 2 - imageOriginalSecond.getHeight() / 2;
                matrixSecond.postTranslate(translateX, translateY);

                dialerSecond.setImageBitmap(imageOriginalSecond);
                dialerSecond.setImageMatrix(matrixSecond);
            }
        });



    }

    private class MyOnTouchListener implements View.OnTouchListener {

        private double startAngle;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN: // начало действия
                    startAngle = getAngle(event.getX(), event.getY());
                    break;

                case MotionEvent.ACTION_MOVE: // произошло изменение в активом действии
                    double currentAngle = getAngle(event.getX(), event.getY());
                    rotateDialer((float) (startAngle - currentAngle));
                    startAngle = currentAngle;
                    break;

                case MotionEvent.ACTION_UP: // действие закончилось

                    break;
            }

            return true;
        }

        private double getAngle(double xTouch, double yTouch) {
            double x = xTouch - (dialerWidthSecond / 2d);
            double y = dialerHeightFirst - yTouch - (dialerHeightFirst / 2d);

            switch (getQuadrant(x, y)) {
                case 1:
                    return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
                case 2:
                    return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
                case 3:
                    return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
                case 4:
                    return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
                default:
                    return 0;
            }
        }

        private int getQuadrant(double x, double y) {
            if (x >= 0) {
                return y >= 0 ? 1 : 4;
            } else {
                return y >= 0 ? 2 : 3;
            }
        }
        private void rotateDialer(float degrees) {
            matrixFirst.postRotate(degrees, dialerWidthFirst / 2, dialerHeightFirst / 2);

            dialerFirst.setImageMatrix(matrixFirst);
        }
    }


    private class MyOnTouchListener2 implements View.OnTouchListener {

        private double startAngle;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN: // начало действия
                    startAngle = getAngle(event.getX(), event.getY());
                    break;

                case MotionEvent.ACTION_MOVE: // произошло изменение в активом действии
                    double currentAngle = getAngle(event.getX(), event.getY());
                    rotateDialer((float) (startAngle - currentAngle));
                    startAngle = currentAngle;
                    break;

                case MotionEvent.ACTION_UP: // действие закончилось

                    break;
            }

            return true;
        }

        private double getAngle(double xTouch, double yTouch) {
            double x = xTouch - (dialerWidthSecond / 2d);
            double y = dialerHeightSecond - yTouch - (dialerHeightSecond / 2d);

            switch (getQuadrant(x, y)) {
                case 1:
                    return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
                case 2:
                    return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
                case 3:
                    return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
                case 4:
                    return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
                default:
                    return 0;
            }
        }

        private int getQuadrant(double x, double y) {
            if (x >= 0) {
                return y >= 0 ? 1 : 4;
            } else {
                return y >= 0 ? 2 : 3;
            }
        }

        private void rotateDialer(float degrees) {
            matrixSecond.postRotate(degrees, dialerWidthSecond / 2, dialerHeightSecond / 2);

            dialerSecond.setImageMatrix(matrixSecond);
        }
    }


}

