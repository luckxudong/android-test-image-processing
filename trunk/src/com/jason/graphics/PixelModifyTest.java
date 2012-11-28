package com.jason.graphics;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;

/**
 * User: jason
 * Date: 12-10-30
 * Time: ÉÏÎç10:36
 */
public class PixelModifyTest extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new TestView(this));
    }


    private class TestView extends View {
        public TestView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {


            try {
                Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);

                BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance("/mnt/sdcard/1280_720_1.png", false);

                BitmapFactory.Options ops = new BitmapFactory.Options();
                ops.inBitmap = bitmap;
                ops.inSampleSize = 1;

                bitmap = decoder.decodeRegion(
                        new Rect(0, 0, 500, 500),
                        ops
                );

                System.out.println("reuse ? = " + (bitmap == ops.inBitmap));
                canvas.drawBitmap(bitmap, 0, 0, null);

                ops.inBitmap = bitmap;

                bitmap = decoder.decodeRegion(
                        new Rect(100, 100, 600, 600),
                        ops
                );

                System.out.println("reuse ? = " + (bitmap == ops.inBitmap));

                canvas.drawBitmap(bitmap, 0, 500, null);

                ops.inBitmap = bitmap;

                bitmap = decoder.decodeRegion(
                        new Rect(200, 200, 600, 600),
                        ops
                );

                System.out.println("reuse ? = " + (bitmap == ops.inBitmap));
                canvas.drawBitmap(bitmap, 100, 300, null);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

