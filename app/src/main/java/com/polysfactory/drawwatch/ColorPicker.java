package com.polysfactory.drawwatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class ColorPicker extends View {

    private static final int COLORS[] = new int[]{Color.BLACK, Color.GRAY, Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA, Color.MAGENTA, Color.CYAN, Color.YELLOW};
    private static final int COLUMN = 3;
    private static final int ROW = (COLORS.length / COLUMN) + (COLORS.length % COLUMN > 0 ? 1 : 0);
    private Paint paint = new Paint();
    private int contentWidth;
    private int contentHeight;
    private float cellWidth;
    private float cellHeight;
    private GestureDetector gestureDetector;
    private OnColorPickedListener onColorPickedListener;

    public ColorPicker(Context context) {
        super(context);
        init(null, 0);
    }

    public ColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ColorPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                int col = (int) (x / cellWidth);
                int row = (int) (y / cellHeight);
                int index = row * COLUMN + col;
                Log.d(Constants.TAG, "picker.onSingleTapConfirm");
                if (index >= COLORS.length) {
                    Log.e(Constants.TAG, "color index is out of range");
                } else {
                    if (onColorPickedListener != null) {
                        onColorPickedListener.onColorPicked(COLORS[index]);
                    }
                }
                return true;
            }
        });

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        contentWidth = getWidth() - paddingLeft - paddingRight;
        contentHeight = getHeight() - paddingTop - paddingBottom;

        cellWidth = (float) contentWidth / (float) COLUMN;
        cellHeight = (float) contentHeight / (float) ROW;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COLUMN; c++) {
                int index = COLUMN * r + c;
                paint.setColor(COLORS[index]);
                float top = cellHeight * r;
                float left = cellWidth * c;
                canvas.drawRect(left, top, left + cellWidth, top + cellHeight, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(Constants.TAG, "picker.onTouchEvent");
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public void setOnColorPickedListener(OnColorPickedListener onColorPickedListener) {
        this.onColorPickedListener = onColorPickedListener;
    }

    public static interface OnColorPickedListener {
        void onColorPicked(int color);
    }
}
