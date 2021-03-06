package pl.android.puzzledepartment.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static android.opengl.GLES30.GL_LINEAR;
import static android.opengl.GLES30.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES30.GL_TEXTURE_2D;
import static android.opengl.GLES30.GL_TEXTURE_CUBE_MAP;
import static android.opengl.GLES30.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
import static android.opengl.GLES30.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
import static android.opengl.GLES30.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
import static android.opengl.GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static android.opengl.GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
import static android.opengl.GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
import static android.opengl.GLES30.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES30.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES30.glBindTexture;
import static android.opengl.GLES30.glDeleteTextures;
import static android.opengl.GLES30.glGenTextures;
import static android.opengl.GLES30.glGenerateMipmap;
import static android.opengl.GLES30.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

/**
 * Created by Maciek Ruszczyk on 2017-10-21.
 */

public class TextureHelper {

    public static int loadTexture(Context context, int resourceId) {
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);

        if(textureObjectIds[0] == 0){
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        if(bitmap == null){
            glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }

        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }

    public static void deleteTexture(int textureObject) {
        final int[] textureObjectIds = new int[1];
        textureObjectIds[0] = textureObject;
        glDeleteTextures(1, textureObjectIds, 0);
    }

    public static int loadCubeMap(Context context, int[] resources) {
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);

        if(textureObjectIds[0] == 0){
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap[] bitmaps = new Bitmap[6];

        for(int i=0; i<6; ++i) {
            bitmaps[i] = BitmapFactory.decodeResource(context.getResources(), resources[i], options);
            if (bitmaps[i] == null) {
                glDeleteTextures(1, textureObjectIds, 0);
                return 0;
            }
        }
        glBindTexture(GL_TEXTURE_CUBE_MAP, textureObjectIds[0]);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, bitmaps[0], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, bitmaps[1], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, bitmaps[2], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, bitmaps[3], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, bitmaps[4], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, bitmaps[5], 0);

        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);//?
        for(int i=0; i<6; ++i) {
            bitmaps[i].recycle();
        }
        return textureObjectIds[0];
    }
}
