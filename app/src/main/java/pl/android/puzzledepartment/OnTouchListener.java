package pl.android.puzzledepartment;

import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public class OnTouchListener implements View.OnTouchListener {
    private final GLSurfaceView glSurfaceView;
    private final MainGameRenderer mainGameRenderer;

    private float startX, startY;
    private int indexMove = -1;
    private float previousRotateX, previousRotateY;
    private int indexRotate = -1;
    private long touchTime;

    public OnTouchListener(GLSurfaceView glSurfaceView, MainGameRenderer mainGameRenderer) {
        this.glSurfaceView = glSurfaceView;
        this.mainGameRenderer = mainGameRenderer;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event == null)
            return false;

        final int pointerIndex = event.getActionIndex();
        final float normalizedX = (event.getX(pointerIndex) / (float) view.getWidth()) * 2 - 1;         // to range (-1; 1)
        final float normalizedY = -((event.getY(pointerIndex) / (float) view.getHeight()) * 2 - 1);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                actionDown(event, normalizedX, normalizedY, pointerIndex);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                actionUp(event);
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(event);
                break;
        }
        return true;
    }

    private void actionMove(MotionEvent event) {
        for (int i = 0; i < event.getPointerCount(); ++i) {
            final float deltaMoveX;
            final float deltaMoveY;
            final float deltaRotateX;
            final float deltaRotateY;
            if (event.getPointerId(i) == indexMove) {
                deltaMoveX = event.getX(i) - startX;
                deltaMoveY = event.getY(i) - startY;
                glSurfaceView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        mainGameRenderer.handleMoveCamera(deltaMoveX, deltaMoveY);
                    }
                });
            }
            else if (event.getPointerId(i) == indexRotate) {
                deltaRotateX = event.getX(i) - previousRotateX;
                deltaRotateY = event.getY(i) - previousRotateY;

                previousRotateX = event.getX(i);
                previousRotateY = event.getY(i);
                glSurfaceView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        mainGameRenderer.handleRotationCamera(deltaRotateX, deltaRotateY);
                    }
                });
            }
        }
    }

    private void actionDown(MotionEvent event, final float normalizedX, final float normalizedY, final int pointerIndex) {
        if (normalizedX < 0) {
            if (indexMove == -1) {
                startX = event.getX(pointerIndex);
                startY = event.getY(pointerIndex);
                indexMove = event.getPointerId(event.getActionIndex());
            }

            glSurfaceView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    mainGameRenderer.handleTouchPress(normalizedX, normalizedY);
                }
            });
        } else {
            if (indexRotate == -1) {
                previousRotateX = event.getX(pointerIndex);
                previousRotateY = event.getY(pointerIndex);
                indexRotate = event.getPointerId(event.getActionIndex());
                touchTime = SystemClock.elapsedRealtime();
            }
        }
    }

    private void actionUp(MotionEvent event) {
        if (event.getPointerId(event.getActionIndex()) == indexMove) {
            indexMove = -1;
            glSurfaceView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    mainGameRenderer.handleMoveCamera(0, 0);
                }
            });
        } else if (event.getPointerId(event.getActionIndex()) == indexRotate) {
            indexRotate = -1;
            if((SystemClock.elapsedRealtime() - touchTime)/1000f < 0.3)
                glSurfaceView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        mainGameRenderer.handleJumpCamera();
                    }
                });
        }
    }
}