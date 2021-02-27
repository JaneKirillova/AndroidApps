package com.example.rotatingCircle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class RotatingCircle extends Activity {
    private static Bitmap imageOriginal; // для работы с *.png
    private static Matrix matrix;

    private ImageView dialer;
    private int dialerHeight, dialerWidth;

    private double certainPointAngle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // инициализация активности
        setContentView(R.layout.main);

        if (imageOriginal == null) {
            imageOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.graphic_ring);
        }

        if (matrix == null) {
            matrix = new Matrix();
        } else {
            matrix.reset();
        }

        dialer = (ImageView) findViewById(R.id.imageView_ring);
        dialer.setOnTouchListener(new MyOnTouchListener());
        dialer.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (dialerHeight == 0 || dialerWidth == 0) {
                dialerHeight = dialer.getHeight();
                dialerWidth = dialer.getWidth();

                Matrix resize = new Matrix();
                resize.postScale((float)Math.min(dialerWidth, dialerHeight) / (float)imageOriginal.getWidth(), (float)Math.min(dialerWidth, dialerHeight) / (float)imageOriginal.getHeight());
                imageOriginal = Bitmap.createBitmap(imageOriginal, 0, 0, imageOriginal.getWidth(), imageOriginal.getHeight(), resize, false);

                float translateX = dialerWidth / 2 - imageOriginal.getWidth() / 2;
                float translateY = dialerHeight / 2 - imageOriginal.getHeight() / 2;
                matrix.postTranslate(translateX, translateY);

                dialer.setImageBitmap(imageOriginal);
                dialer.setImageMatrix(matrix);
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
                    certainPointAngle = startAngle;
//                    System.out.println(certainPointAngle);
                    break;
            }

            return true;
        }

        /**
         * @return The angle of the unit circle with the image view's center
         */
        private double getAngle(double xTouch, double yTouch) {
            double x = xTouch - (dialerWidth / 2d);
            double y = dialerHeight - yTouch - (dialerHeight / 2d);

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
            matrix.postRotate(degrees, dialerWidth / 2, dialerHeight / 2);

            dialer.setImageMatrix(matrix);
        }
    }

}

