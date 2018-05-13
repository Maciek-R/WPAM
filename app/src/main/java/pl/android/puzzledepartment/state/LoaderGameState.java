package pl.android.puzzledepartment.state;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.managers.EntityManager;
import pl.android.puzzledepartment.managers.GameState;
import pl.android.puzzledepartment.managers.TextureManager;
import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Key;
import pl.android.puzzledepartment.util.geometry.Point;

import static pl.android.puzzledepartment.state.Consts.CAMERA_POS_X;
import static pl.android.puzzledepartment.state.Consts.CAMERA_POS_Y;
import static pl.android.puzzledepartment.state.Consts.CAMERA_POS_Z;
import static pl.android.puzzledepartment.state.Consts.CAMERA_ROT_X;
import static pl.android.puzzledepartment.state.Consts.CAMERA_ROT_Y;
import static pl.android.puzzledepartment.state.Consts.KEY_COLLECTED_COLOR;
import static pl.android.puzzledepartment.state.Consts.KEY_COLOR;
import static pl.android.puzzledepartment.state.Consts.KEY_POS_X;
import static pl.android.puzzledepartment.state.Consts.KEY_POS_Y;
import static pl.android.puzzledepartment.state.Consts.KEY_POS_Z;

/**
 * Created by Maciek Ruszczyk on 2017-12-27.
 */

public class LoaderGameState {

    private HashMap<String, Float> hashValues;
    private EntityManager entityManager;
    private TextureManager textureManager;

    private List<Key> keys = new ArrayList<>();
    private List<Float> collectedColorKeys = new ArrayList<>();

    public LoaderGameState(EntityManager entityManager, TextureManager textureManager) {
        hashValues = new HashMap<>();
        this.entityManager = entityManager;
        this.textureManager = textureManager;
    }

    public void loadGameStateFromFile(Context context, Camera camera, GameState gameState) {

        readStateFromFile(context);

        if(hashValues.get(CAMERA_POS_X) != null)
            camera.setPosX(hashValues.get(CAMERA_POS_X));
        if(hashValues.get(CAMERA_POS_Y) != null)
            camera.setPosY(hashValues.get(CAMERA_POS_Y));
        if(hashValues.get(CAMERA_POS_Z) != null)
            camera.setPosZ(hashValues.get(CAMERA_POS_Z));
        if(hashValues.get(CAMERA_ROT_X) != null)
            camera.setRotationX(hashValues.get(CAMERA_ROT_X));
        if(hashValues.get(CAMERA_ROT_Y) != null)
            camera.setRotationY(hashValues.get(CAMERA_ROT_Y));

        //if(hashValues.get(KEYS_TAKEN_COUNT) != null)
         //   gameState.setKeysTakenCount(hashValues.get(KEYS_TAKEN_COUNT).intValue());

        loadKeys();
        loadCollectedKeys();
    }

    private void loadKeys() {
        for(String colorStr:Key.getColorValues()){
            Float color = hashValues.get(KEY_COLOR + colorStr);
            if(color != null){
                Key key = new Key(new Point(hashValues.get(KEY_POS_X + colorStr), hashValues.get(KEY_POS_Y + colorStr), hashValues.get(KEY_POS_Z + colorStr)),
                        color.intValue(), textureManager.getKeyTextureFromColor(color.intValue()), entityManager.getEntityModel(R.raw.key));
                keys.add(key);
            }
        }
    }

    private void loadCollectedKeys() {
        for(String collectedKeyColor: Key.getColorValues()){
            Float keyColor = hashValues.get(KEY_COLLECTED_COLOR + collectedKeyColor);
            if(keyColor != null){
                collectedColorKeys.add(keyColor);
            }
        }
    }

    private void readStateFromFile(Context context) {
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        try {
            inputStreamReader = new InputStreamReader(context.openFileInput(context.getString(R.string.save)));
            bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while (true) {
                line = bufferedReader.readLine();
                if(line == null)
                    break;
                parseLine(line);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseLine(String line) {
        String[] currentLine = line.split(" ");
        hashValues.put(currentLine[0], Float.parseFloat(currentLine[1]));
    }

    public List<Key> getKeys(){
        return keys;
    }

    public List<Float> getCollectedColorKeys() {
        return collectedColorKeys;
    }
}
