package pl.android.puzzledepartment.state;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.managers.GameState;
import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Key;

import static pl.android.puzzledepartment.state.Consts.CAMERA_POS_X;
import static pl.android.puzzledepartment.state.Consts.CAMERA_POS_Y;
import static pl.android.puzzledepartment.state.Consts.CAMERA_POS_Z;
import static pl.android.puzzledepartment.state.Consts.CAMERA_ROT_X;
import static pl.android.puzzledepartment.state.Consts.CAMERA_ROT_Y;
import static pl.android.puzzledepartment.state.Consts.KEYS_TAKEN_COUNT;
import static pl.android.puzzledepartment.state.Consts.KEY_COLLECTED_COLOR;
import static pl.android.puzzledepartment.state.Consts.KEY_COLOR;
import static pl.android.puzzledepartment.state.Consts.KEY_POS_X;
import static pl.android.puzzledepartment.state.Consts.KEY_POS_Y;
import static pl.android.puzzledepartment.state.Consts.KEY_POS_Z;

/**
 * Created by Maciek Ruszczyk on 2017-12-27.
 */

public class SaverGameState {

    private StringBuilder textToSave;

    public SaverGameState() {
        textToSave = new StringBuilder();
    }

    public void saveGameStateToFile(Context context, Camera camera, GameState gameState, List<Key> keys, List<Integer> keyCollectedColors) {
        OutputStreamWriter outputStreamWriter;
        try {

            File[] files =  context.getFilesDir().listFiles();
            for (File f : files) {
                System.out.println(f.getAbsolutePath());
            }
            outputStreamWriter = new OutputStreamWriter(context.openFileOutput(context.getString(R.string.save), Context.MODE_PRIVATE));
            addText(camera);
            addText(gameState);
            addText(keys);
            addTextCollectedKeys(keyCollectedColors);
            outputStreamWriter.write(textToSave.toString());
            outputStreamWriter.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addTextCollectedKeys(List<Integer> keyCollectedColors) {
        for (Integer collectedColorKey : keyCollectedColors) {
            textToSave.append(getString(KEY_COLLECTED_COLOR + Key.getColorStr(collectedColorKey), Float.valueOf(collectedColorKey)));
        }
    }

    private void addText(List<Key> keys) {
        for (Key k : keys) {
            textToSave.append(getString(KEY_POS_X + k.getColorStr(), k.getPos().x));
            textToSave.append(getString(KEY_POS_Y + k.getColorStr(), k.getPos().y));
            textToSave.append(getString(KEY_POS_Z + k.getColorStr(), k.getPos().z));
            textToSave.append(getString(KEY_COLOR + k.getColorStr(), k.getColor()));
        }
    }

    private void addText(GameState gameState) {
        textToSave.append(getString(KEYS_TAKEN_COUNT, gameState.getKeysTakenCount()));
    }

    private void addText(Camera camera) {
        textToSave.append(getString(CAMERA_POS_X, camera.getPosX()));
        textToSave.append(getString(CAMERA_POS_Y, camera.getPosY()));
        textToSave.append(getString(CAMERA_POS_Z, camera.getPosZ()));
        textToSave.append(getString(CAMERA_ROT_X, camera.getRotationX()));
        textToSave.append(getString(CAMERA_ROT_Y, camera.getRotationY()));
    }

    private String getString(String string, float f) {
        return string + " " + String.valueOf(f) + '\n';
    }
}
