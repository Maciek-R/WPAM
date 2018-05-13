package pl.android.puzzledepartment;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public class MainGameActivity extends Activity {

    private GLSurfaceView glSurfaceView;
    private MainGameRenderer mainGameRenderer;
    private boolean rendererSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        LoadGameMode loadGameMode = LoadGameMode.NEW;
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            loadGameMode = LoadGameMode.fromIntValue(bundle.getInt("mode"));
        }
        mainGameRenderer = new MainGameRenderer(this, loadGameMode);

        final boolean supportsEs3 = configurationInfo.reqGlEsVersion >= 0x30000
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("emulator")
                || Build.MODEL.contains("Android SDK built for x86")));

        if(supportsEs3){
            glSurfaceView.setEGLContextClientVersion(3);
            glSurfaceView.setRenderer(mainGameRenderer);
            rendererSet = true;
        }
        else{
            Toast.makeText(this, "This device does not support OpenGL ES 3.0.", Toast.LENGTH_LONG).show();
            return;
        }

        glSurfaceView.setOnTouchListener(new OnTouchListener(glSurfaceView, mainGameRenderer));
        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(rendererSet)
            glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (rendererSet)
            glSurfaceView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mainGameRenderer != null){
            if(mainGameRenderer.getGameManager() != null)
                mainGameRenderer.getGameManager().saveGame();
        }
    }
}
