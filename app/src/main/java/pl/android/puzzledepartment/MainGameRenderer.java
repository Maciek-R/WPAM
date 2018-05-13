package pl.android.puzzledepartment;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.android.puzzledepartment.managers.GameManager;
import pl.android.puzzledepartment.managers.TimeManager;
import pl.android.puzzledepartment.util.Logger;

import static android.opengl.GLES30.GL_BACK;
import static android.opengl.GLES30.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES30.GL_CULL_FACE;
import static android.opengl.GLES30.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES30.GL_DEPTH_TEST;
import static android.opengl.GLES30.glClear;
import static android.opengl.GLES30.glClearColor;
import static android.opengl.GLES30.glCullFace;
import static android.opengl.GLES30.glEnable;
import static android.opengl.GLES30.glViewport;


/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public class MainGameRenderer implements Renderer {

    private final Context context;
    private GameManager gameManager;
    private LoadGameMode loadGameMode;

    private long frameStartTimeMs;
    private long startTimeMs;
    private int frameCount;

    public MainGameRenderer(Context context, LoadGameMode loadGameMode){
        this.context = context;
        this.loadGameMode = loadGameMode;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_DEPTH_TEST);

        gameManager = new GameManager(context, loadGameMode);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);
        gameManager.createProjectionMatrix(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        //limitFrameRate(80);
        logFrameRate();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        TimeManager.update();
        gameManager.update();
    }

    private void limitFrameRate(int framesPerSecond){
        long elapsedFrameTimeMs = SystemClock.elapsedRealtime() - frameStartTimeMs;
        long expectedFrameTimeMs = 1000 / framesPerSecond;
        long timeToSleepMs = expectedFrameTimeMs - elapsedFrameTimeMs;

        if(timeToSleepMs > 0){
            SystemClock.sleep(timeToSleepMs);
        }
        frameStartTimeMs = SystemClock.elapsedRealtime();
    }
    private void logFrameRate() {
        if(Logger.FPS_ON){
            long elapsedRealTimeMs = SystemClock.elapsedRealtime();
            double elapsedSeconds = (elapsedRealTimeMs - startTimeMs) / 1000.0;

            if(elapsedSeconds > 1.0){
                Log.v("FPS: ", frameCount / elapsedSeconds + "fps");
                startTimeMs = SystemClock.elapsedRealtime();
                frameCount = 0;
            }
            frameCount++;
        }
    }

    public void handleMoveCamera(float deltaMoveX, float deltaMoveY) {
        gameManager.setCameraDirection(deltaMoveX/1024f, deltaMoveY/1024f);
    }

    public void handleRotationCamera(float deltaRotateX, float deltaRotateY) {
        gameManager.setCameraRotation(deltaRotateX / 8f, deltaRotateY / 8f);

    }

    public void handleTouchPress(float normalizedX, float normalizedY){
        if(Logger.ON)
            Log.v("RENDERER", "Touch Press: X: "+normalizedX+" Y: "+normalizedY);

        gameManager.handleTouchPress(normalizedX, normalizedY);
    }

    public void handleJumpCamera() {
        gameManager.handleJump();
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
