package com.jason.graphics;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.View;
import com.jason.R;

import java.util.LinkedList;
import java.util.List;

/**
 * User: jason
 * Date: 12-10-29
 * Time: 下午4:42
 */
public class ColorMatrixTest extends Activity {
    //保持原样
    public static final float[] ORIGINAL = new float[]{
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            1, 0, 0, 0, 0,
            0, 0, 0, 1, 0
    };

    //调整亮度
    private static final float BRIGHTNESS = 100;
    public static final float[] BRIGHTNESS_ADJUST = new float[]{
            1, 0, 0, 0, BRIGHTNESS,
            0, 1, 0, 0, BRIGHTNESS,
            0, 0, 1, 0, BRIGHTNESS,
            0, 0, 0, 1, 0
    };

    //颜色反向
    public static final float[] REVERT_ADJUST = new float[]{
            -1, 0, 0, 0, 255,
            0, -1, 0, 0, 255,
            0, 0, -1, 0, 255,
            0, 0, 0, 1, 0
    };

    // 图像去色
    public static final float[] GRAY_ADJUST = new float[]{
            0.3086F, 0.6094F, 0.0820F, 0, 0,
            0.3086F, 0.6094F, 0.0820F, 0, 0,
            0.3086F, 0.6094F, 0.0820F, 0, 0,
            0, 0, 0, 1, 0
    };

    // 色彩饱和度
    private static final Float SAT = 1.5F;
    public static final float[] SAT_ADJUST = new float[]{
            0.3086F * (1 - SAT) + SAT, 0.6094F * (1 - SAT), 0.0820F * (1 - SAT), 0, 0,
            0.3086F * (1 - SAT), 0.6094F * (1 - SAT) + SAT, 0.0820F * (1 - SAT), 0, 0,
            0.3086F * (1 - SAT), 0.6094F * (1 - SAT), 0.0820F * (1 - SAT) + SAT, 0, 0,
            0, 0, 0, 1, 0
    };

    //  对比度 (0-10)
    private static final float N = 8;
    public static final float[] CONTRAST_ADJUST = new float[]{
            N, 0, 0, 0, 128 * (1 - N),
            0, N, 0, 0, 128 * (1 - N),
            0, 0, N, 0, 128 * (1 - N),
            0, 0, 0, 1, 0
    };

    private Bitmap testPixel;
    private List<MatrixInfo> matrixForTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        testPixel = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.color_matrix_test
        );

        loadMatrix();

        setContentView(new ColorMatrixView(this));
    }

    private void loadMatrix() {
        matrixForTest = new LinkedList<MatrixInfo>();

        MatrixInfo matrixInfo = new MatrixInfo();
        matrixInfo.colorMatrixArray = ORIGINAL;
        matrixInfo.info = "ORIGINAL";
        matrixForTest.add(matrixInfo);

        matrixInfo = new MatrixInfo();
        matrixInfo.colorMatrixArray = BRIGHTNESS_ADJUST;
        matrixInfo.info = "Brightness adjust";
        matrixForTest.add(matrixInfo);

        matrixInfo = new MatrixInfo();
        matrixInfo.colorMatrixArray = CONTRAST_ADJUST;
        matrixInfo.info = "CONTRAST_ADJUST";
        matrixForTest.add(matrixInfo);

        matrixInfo = new MatrixInfo();
        matrixInfo.colorMatrixArray = GRAY_ADJUST;
        matrixInfo.info = "GRAY_ADJUST";
        matrixForTest.add(matrixInfo);

        matrixInfo = new MatrixInfo();
        matrixInfo.colorMatrixArray = REVERT_ADJUST;
        matrixInfo.info = "REVERT_ADJUST";
        matrixForTest.add(matrixInfo);

        matrixInfo = new MatrixInfo();
        matrixInfo.colorMatrixArray = SAT_ADJUST;
        matrixInfo.info = "SAT_ADJUST";
        matrixForTest.add(matrixInfo);
    }

    private class ColorMatrixView extends View {
        private Paint mPaint;
        private ColorMatrix colorMatrix;

        public ColorMatrixView(Context context) {
            super(context);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            colorMatrix = new ColorMatrix();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.GRAY);

            layout();

            for (MatrixInfo matrixInfo : matrixForTest) {
                colorMatrix.set(matrixInfo.colorMatrixArray);

                mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

                canvas.drawBitmap(testPixel, null, matrixInfo.bounds, mPaint);
            }
        }

        private void layout() {
            int width = testPixel.getWidth();
            int height = testPixel.getHeight();

            MatrixInfo original = matrixForTest.get(0);
            RectF rectF = new RectF(-width / 2f, -height / 2f, width / 2f, height / 2f);
            original.bounds = rectF;
            original.bounds.offset(getWidth() / 2, getHeight() / 2);

            Matrix matrix = new Matrix();

            int size = matrixForTest.size();
            float degree = 360f / size;

            for (int i = 1; i < size; i++) {
                float[] center = new float[]{
                        0,
                        0
                };

                matrix.reset();
                matrix.postTranslate(original.bounds.centerX(), original.bounds.centerY());
                matrix.postRotate((i - 1) * degree);
                matrix.postTranslate(100, 0);

                matrix.postScale(0.5f, 0.5f);
                //matrix.postTranslate(center[0], center[1]);

                System.out.println("center = " + center[0] + " " + center[1]);
                matrix.mapPoints(center);
                System.out.println("center after= " + center[0] + " " + center[1]);

                MatrixInfo info = matrixForTest.get(i);
                info.bounds = new RectF(rectF);
                info.bounds.offset(center[0], center[1]);

                //info.bounds.offset(center[0], center[1]);
            }
        }

    }

    private class MatrixInfo {
        float[] colorMatrixArray;
        RectF bounds;
        String info;
    }
}
