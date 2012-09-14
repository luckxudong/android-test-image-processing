package com.jason.graphics;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

/**
 * User: jason
 * Date: 12-9-14
 * Time: ÏÂÎç1:32
 */
public class PathEffect extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new PathEffectView(this));
    }

    private class PathEffectView extends View {

        public PathEffectView(Context context) {
            super(context);
        }


        @Override
        protected void onDraw(Canvas canvas) {

        }

    }
}
