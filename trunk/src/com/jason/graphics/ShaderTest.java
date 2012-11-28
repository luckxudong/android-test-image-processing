package com.jason.graphics;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * User: jason
 * Date: 12-10-29
 * Time: ÏÂÎç12:03
 */
public class ShaderTest extends Activity implements View.OnTouchListener {
    private Path path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        path = makeFollowPath();
        path.addRect(new RectF(200, 200, 500, 600), Path.Direction.CCW);
        path.addCircle(500, 500, 200, Path.Direction.CCW);

        ShaderPathView pathView = new ShaderPathView(this, path);
        pathView.setOnTouchListener(this);

        setContentView(pathView);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //path.rewind();
                path.moveTo(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;

            case MotionEvent.ACTION_UP:
                //path.close();
                break;
        }

        return true;
    }

    private class ShaderPathView extends View {
        private Path path;
        private Paint shaderPaint;
        private Shader shader;
        private Matrix shaderMatrix = new Matrix();

        private Paint blurPaint;

        public ShaderPathView(Context context, Path path) {
            super(context);
            this.path = path;

            shaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            shaderPaint.setStyle(Paint.Style.STROKE);

            shader = new LinearGradient(
                    0, 0, 5, 5,
                    WHITE, BLACK,
                    Shader.TileMode.MIRROR
            );

            shaderPaint.setShader(shader);

            blurPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            BlurMaskFilter blurFilter = new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID);
            blurPaint.setMaskFilter(blurFilter);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            canvas.drawPath(path, shaderPaint);

            Shader tempShader = shaderPaint.getShader();

            if (tempShader != null) {
                tempShader.getLocalMatrix(shaderMatrix);
                shaderMatrix.postTranslate(0.5f, 0.5f);
                tempShader.setLocalMatrix(shaderMatrix);
                invalidate();
            }

            RectF rect = new RectF(100, 100, 200, 200);
            //canvas.drawRoundRect(rect, 10, 10, blurPaint);
            canvas.drawRect(rect, blurPaint);
        }

    }

    private Path makeFollowPath() {
        Path p = new Path();
        p.moveTo(0, 0);
        for (int i = 1; i <= 15; i++) {
            p.lineTo(i * 20, (float) Math.random() * 35);
        }
        return p;
    }

}
