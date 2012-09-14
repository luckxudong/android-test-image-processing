package com.jason.graphics;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.View;
import com.jason.R;

import static android.graphics.BitmapFactory.decodeResource;
import static android.graphics.Path.Direction.CW;

/**
 * User: jason
 * Date: 12-9-14
 * Time: 下午5:37
 */
public class ClipZoom extends Activity {
    //放大镜的半径
    private static final int RADIUS = 80;
    //放大倍数
    private static final int FACTOR = 2;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new ClipZoomView(this));
    }

    @Override
    protected void onResume() {
        super.onResume();

        bitmap = decodeResource(getResources(), R.drawable.bg);
    }

    @Override
    protected void onPause() {
        super.onPause();

        bitmap = null;
    }

    private class ClipZoomView extends View {

        private Path clipPath;
        private Matrix zoomMatrix;

        private PointF pos;
        private PointF speed;

        private long lastUpdateTime;

        public ClipZoomView(Context context) {
            super(context);

            setLayerType(LAYER_TYPE_SOFTWARE, null);

            clipPath = new Path();
            clipPath.addCircle(RADIUS, RADIUS, RADIUS, CW);

            zoomMatrix = new Matrix();
            zoomMatrix.postScale(FACTOR, FACTOR);

            pos = new PointF();
            speed = new PointF(100, 100);

            lastUpdateTime = System.currentTimeMillis();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Bitmap img = bitmap;

            if (img != null) {
                canvas.drawBitmap(img, 0, 0, null);

                canvas.save(Canvas.CLIP_SAVE_FLAG | Canvas.MATRIX_SAVE_FLAG);

                canvas.translate(pos.x - RADIUS, pos.y - RADIUS);
                canvas.clipPath(clipPath);

                canvas.translate(RADIUS - pos.x * FACTOR, RADIUS - pos.y * FACTOR);
                canvas.drawBitmap(img, zoomMatrix, null);

                canvas.restore();

                update(img.getWidth(), img.getHeight());
            }
        }

        private void update(int width, int height) {
            long now = System.currentTimeMillis();

            if (lastUpdateTime != 0)
                updatePosition(width, height, now);

            lastUpdateTime = now;
            postInvalidateDelayed(30);
        }

        private void updatePosition(int width, int height, long now) {
            float x = pos.x + (now - lastUpdateTime) * speed.x / 1000f;
            float y = pos.y + (now - lastUpdateTime) * speed.y / 1000f;

            if (x < 0) {
                speed.x = Math.abs(speed.x);
            }

            if (x > width) {
                speed.x = -Math.abs(speed.x);
            }

            if (y < 0) {
                speed.y = Math.abs(speed.y);
            }

            if (y > height) {
                speed.y = -Math.abs(speed.y);
            }

            pos.set(x, y);
        }

    }

}
