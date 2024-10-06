package com.shaktipumplimted.serviceapp.Utils;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import androidx.appcompat.widget.AppCompatImageView;

public class ZoomImageView extends AppCompatImageView {


    private Matrix matrix;
    private float[] matrixValues = new float[9];
    private ScaleGestureDetector scaleGestureDetector;
    private PointF lastTouch = new PointF();
    private float saveScale = 1f;  // Current zoom scale
    private float minScale = 1f;   // Minimum zoom scale
    private float maxScale = 4f;   // Maximum zoom scale
    private float originalWidth, originalHeight;
    private int viewWidth, viewHeight;
    private int mode = NONE;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;

    public ZoomImageView(Context context) {
        super(context);
        init(context);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        super.setClickable(true);
        matrix = new Matrix();
        setScaleType(ScaleType.MATRIX);  // Override default to MATRIX for zoom/pan handling
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        PointF currentTouch = new PointF(event.getX(), event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouch.set(currentTouch);
                mode = DRAG;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    float deltaX = currentTouch.x - lastTouch.x;
                    float deltaY = currentTouch.y - lastTouch.y;
                    float fixTransX = getFixDragTrans(deltaX, viewWidth, originalWidth * saveScale);
                    float fixTransY = getFixDragTrans(deltaY, viewHeight, originalHeight * saveScale);
                    matrix.postTranslate(fixTransX, fixTransY);
                    fixTranslation();
                    lastTouch.set(currentTouch.x, currentTouch.y);
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                mode = NONE;
                break;
        }

        setImageMatrix(matrix);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            float prevScale = saveScale;
            saveScale *= scaleFactor;

            saveScale = Math.max(minScale, Math.min(saveScale, maxScale));
            scaleFactor = saveScale / prevScale;

            matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            fixTranslation();
            return true;
        }
    }

    private void fixTranslation() {
        matrix.getValues(matrixValues);
        float transX = matrixValues[Matrix.MTRANS_X];
        float transY = matrixValues[Matrix.MTRANS_Y];

        float fixTransX = getFixTranslation(transX, viewWidth, originalWidth * saveScale);
        float fixTransY = getFixTranslation(transY, viewHeight, originalHeight * saveScale);

        if (fixTransX != 0 || fixTransY != 0) {
            matrix.postTranslate(fixTransX, fixTransY);
        }
    }

    private float getFixDragTrans(float delta, int viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0;
        }
        return delta;
    }

    private float getFixTranslation(float trans, int viewSize, float contentSize) {
        float minTrans, maxTrans;

        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans) {
            return -trans + minTrans;
        }
        if (trans > maxTrans) {
            return -trans + maxTrans;
        }
        return 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        if (saveScale == 1) {
            fitToScreen();
        }
    }

    private void fitToScreen() {
        Drawable drawable = getDrawable();
        if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0) {
            return;
        }

        float scaleX = (float) viewWidth / (float) drawable.getIntrinsicWidth();
        float scaleY = (float) viewHeight / (float) drawable.getIntrinsicHeight();
        float scale = Math.min(scaleX, scaleY);

        matrix.setScale(scale, scale);

        originalWidth = scale * drawable.getIntrinsicWidth();
        originalHeight = scale * drawable.getIntrinsicHeight();

        // Center the image
        float redundantXSpace = (viewWidth - originalWidth) / 2;
        float redundantYSpace = (viewHeight - originalHeight) / 2;
        matrix.postTranslate(redundantXSpace, redundantYSpace);

        setImageMatrix(matrix);
    }
}
