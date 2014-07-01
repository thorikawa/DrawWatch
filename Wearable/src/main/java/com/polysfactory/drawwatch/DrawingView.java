package com.polysfactory.drawwatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class DrawingView extends SurfaceView {
    SurfaceHolder holder;
    Paint paint;
    float prevX = -1f;
    float prevY = -1f;
    private Bitmap bitmap;
    SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            initOffScreenBuffer();
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawBitmap(bitmap, 0, 0, paint);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        }
    };
    private Canvas offScreen;

    public DrawingView(Context context) {
        super(context);
        init(null, 0);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        holder = getHolder();
        holder.addCallback(callback);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4f);
    }

    private void initOffScreenBuffer() {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        offScreen = new Canvas(bitmap);
        offScreen.drawColor(Color.WHITE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (holder.getSurface().isValid()) {
            float x = event.getX();
            float y = event.getY();
            int action = event.getAction();
            if (prevX >= 0f && prevY >= 0f && action != MotionEvent.ACTION_DOWN) {
                Canvas canvas = holder.lockCanvas();
                offScreen.drawLine(prevX, prevY, x, y, paint);
                canvas.drawBitmap(bitmap, 0, 0, paint);
                holder.unlockCanvasAndPost(canvas);
            }
            prevX = x;
            prevY = y;
            return true;
        }
        return false;
    }

    public void setPaintColor(int color) {
        paint.setColor(color);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
