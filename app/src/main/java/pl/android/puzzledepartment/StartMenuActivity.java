package pl.android.puzzledepartment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;

import pl.android.puzzledepartment.R;

/**
 * Created by Maciek Ruszczyk on 2017-12-21.
 */

public class StartMenuActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.start_activity);

    }

    public void checkButton(View view){

        Intent intent;
        switch(view.getId()){

            case R.id.NewGame:
                intent = new Intent(this, MainGameActivity.class);
                startActivity(intent);
                break;

            case R.id.LoadLastGame:
                if(checkSaveExists()) {
                    intent = new Intent(this, MainGameActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("mode", LoadGameMode.LOAD.toInt());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "Brak pliku z zapisem", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.Exit:
                finish();
                break;
        }
    }

    private boolean checkSaveExists() {
        return new File(this.getFilesDir(), this.getString(R.string.save)).exists();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
