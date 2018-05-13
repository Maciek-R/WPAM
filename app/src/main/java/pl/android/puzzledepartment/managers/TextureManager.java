package pl.android.puzzledepartment.managers;

import android.content.Context;
import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.util.TextureHelper;

/**
 * Created by Maciek Ruszczyk on 2017-12-27.
 */

public class TextureManager {

    private static TextureManager instance = null;

    private Context context;
    private Map<Integer, Integer> textures;

    private TextureManager(Context context) {
        textures = new HashMap<>();
        this.context = context;
    }

    public static TextureManager getInstance(Context context) {
        if(instance == null) {
            instance = new TextureManager(context);
        }
        return instance;
    }

    public int getTextureId(int resourceId) {
        Integer textureId = textures.get(resourceId);
        if(textureId != null) {
            return textureId;
        }
        else {
            textureId = TextureHelper.loadTexture(context, resourceId);
            textures.put(resourceId, textureId);
            return textureId;
        }
    }

    public int getKeyTextureFromColor(int color) {
        switch (color) {
            case Color.RED: return getTextureId(R.drawable.redkey);
            case Color.GREEN: return getTextureId(R.drawable.greenkey);
            case Color.BLUE: return getTextureId(R.drawable.bluekey);
            case Color.CYAN: return getTextureId(R.drawable.cyankey);
            case Color.YELLOW: return getTextureId(R.drawable.yellowkey);
        }
        return -1;
    }

    public void clean() {
        for(Integer key:textures.keySet()){
            TextureHelper.deleteTexture(textures.get(key));
        }
        textures.clear();
    }

    public Context getContext(){
        return context;
    }
}
