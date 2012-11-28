package com.jason.graphics;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

/**
 * User: jason
 * Date: 12-10-30
 * Time: ÉÏÎç10:36
 */
public class ImageConvolution extends Activity {

    final static int KERNAL_WIDTH = 3;
    final static int KERNAL_HEIGHT = 3;

    int[][] kernalBlur = {
            {0, -1, 0},
            {-1, 5, -1},
            {0, -1, 0}
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Bitmap processingBitmap(Bitmap src, int[][] knl) {
        Bitmap des = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        int bmWidth = src.getWidth();
        int bmHeight = src.getHeight();
        int bmWidth_MINUS_2 = bmWidth - 2;
        int bmHeight_MINUS_2 = bmHeight - 2;

        for (int i = 1; i <= bmWidth_MINUS_2; i++) {
            for (int j = 1; j <= bmHeight_MINUS_2; j++) {

                //get the surround 3*3 pixel of current src[i][j] into a matrix subSrc[][]
                int[][] subSrc = new int[KERNAL_WIDTH][KERNAL_HEIGHT];
                for (int k = 0; k < KERNAL_WIDTH; k++) {
                    for (int l = 0; l < KERNAL_HEIGHT; l++) {
                        subSrc[k][l] = src.getPixel(i - 1 + k, j - 1 + l);
                    }
                }

                //subSum = subSrc[][] * knl[][]
                int subSumA = 0;
                int subSumR = 0;
                int subSumG = 0;
                int subSumB = 0;

                for (int k = 0; k < KERNAL_WIDTH; k++) {
                    for (int l = 0; l < KERNAL_HEIGHT; l++) {
                        int pixel = subSrc[k][l];
                        subSumA += Color.alpha(pixel) * knl[k][l];
                        subSumR += Color.red(pixel) * knl[k][l];
                        subSumG += Color.green(pixel) * knl[k][l];
                        subSumB += Color.blue(pixel) * knl[k][l];
                    }
                }

                if (subSumA < 0) {
                    subSumA = 0;
                }
                else if (subSumA > 255) {
                    subSumA = 255;
                }

                if (subSumR < 0) {
                    subSumR = 0;
                }
                else if (subSumR > 255) {
                    subSumR = 255;
                }

                if (subSumG < 0) {
                    subSumG = 0;
                }
                else if (subSumG > 255) {
                    subSumG = 255;
                }

                if (subSumB < 0) {
                    subSumB = 0;
                }
                else if (subSumB > 255) {
                    subSumB = 255;
                }

                des.setPixel(
                        i,
                        j,
                        Color.argb(subSumA, subSumR, subSumG, subSumB)
                );
            }
        }

        return des;
    }

}

