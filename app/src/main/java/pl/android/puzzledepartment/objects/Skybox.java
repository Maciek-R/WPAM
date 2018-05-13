package pl.android.puzzledepartment.objects;

import android.graphics.Color;

import java.nio.ByteBuffer;

import pl.android.puzzledepartment.data.VertexArray;
import pl.android.puzzledepartment.managers.TimeManager;
import pl.android.puzzledepartment.programs.SkyboxShaderProgram;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glDrawElements;

/**
 * Created by Maciek Ruszczyk on 2017-10-28.
 */

public class Skybox {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private final VertexArray vertexArray;
    private final ByteBuffer indexArray;
    private final int textureId;
    private static int colour = Color.rgb(128, 158, 176);
    private float rotation = 0;

    public Skybox(int textureId) {
        this.textureId = textureId;
        vertexArray = new VertexArray(new float[]{
                -1, 1, 1,
                1, 1, 1,
                -1, -1, 1,
                1, -1, 1,
                -1, 1, -1,
                1, 1, -1,
                -1, -1, -1,
                1, -1, -1
        });
        indexArray = ByteBuffer.allocateDirect(36).put(new byte[]{
                1, 3, 0,
                0, 3, 2,
                4, 6, 5,
                5, 6, 7,
                0, 2, 4,
                4, 2, 6,
                5, 7, 1,
                1, 7, 3,
                5, 1, 4,
                4, 1, 0,
                6, 2, 7,
                7, 2, 3
        });
        indexArray.position(0);
    }

    public void bindData(SkyboxShaderProgram skyboxShaderProgram) {
        vertexArray.setVertexAttribPointer(0, skyboxShaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, 0);
    }

    public void draw() {
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_BYTE, indexArray);
    }

    public int getTextureId() {
        return textureId;
    }

    public void rotate() {
        this.rotation += TimeManager.getDeltaTimeInSeconds();
    }

    public float getRotation(){return rotation;}

    public static int getColour(){return colour;}
}
