package pl.android.puzzledepartment.objects;

import pl.android.puzzledepartment.data.VertexArray;
import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static pl.android.puzzledepartment.util.Constants.BYTES_PER_FLOAT;

/**
 * Created by Maciek Ruszczyk on 2017-12-22.
 */

public class TexturedCube extends Cube{
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int NORMAL_COMPONENT_COUNT = 3;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final static float[] VERTEX_DATA = {
            //Back
            0.5f,   0.5f, -0.5f,	 0.0f,  0.0f, -1.0f,		0.0f, 0.0f,
            0.5f,  -0.5f, -0.5f,	 0.0f,  0.0f, -1.0f,		0.0f, 1.0f,
            -0.5f,   0.5f, -0.5f,	 0.0f,  0.0f, -1.0f,		1.0f, 0.0f,
            -0.5f,   0.5f, -0.5f,	 0.0f,  0.0f, -1.0f,		1.0f, 0.0f,
            0.5f,  -0.5f, -0.5f,	 0.0f,  0.0f, -1.0f,		0.0f, 1.0f,
            -0.5f,  -0.5f, -0.5f,	 0.0f,  0.0f, -1.0f,		1.0f, 1.0f,
            //Front
            -0.5f,   0.5f,  0.5f,	 0.0f,  0.0f,  1.0f,		0.0f, 0.0f,
            -0.5f,  -0.5f,  0.5f,	 0.0f,  0.0f,  1.0f,		0.0f, 1.0f,
            0.5f,   0.5f,  0.5f,	 0.0f,  0.0f,  1.0f,		1.0f, 0.0f,
            0.5f,   0.5f,  0.5f,	 0.0f,  0.0f,  1.0f,		1.0f, 0.0f,
            -0.5f,  -0.5f,  0.5f,	 0.0f,  0.0f,  1.0f,		0.0f, 1.0f,
            0.5f,  -0.5f,  0.5f,     0.0f,  0.0f,  1.0f,		1.0f, 1.0f,
            //Left
            -0.5f,  0.5f, -0.5f,	-1.0f,  0.0f,  0.0f,		0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f,	-1.0f,  0.0f,  0.0f,		0.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,	-1.0f,  0.0f,  0.0f,		1.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,	-1.0f,  0.0f,  0.0f,		1.0f, 0.0f,
            -0.5f, -0.5f, -0.5f,	-1.0f,  0.0f,  0.0f,		0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,	-1.0f,  0.0f,  0.0f,		1.0f, 1.0f,
            //Right
            0.5f,  0.5f,  0.5f,		 1.0f,  0.0f,  0.0f,		0.0f, 0.0f,
            0.5f, -0.5f,  0.5f,		 1.0f,  0.0f,  0.0f,		0.0f, 1.0f,
            0.5f,  0.5f, -0.5f,		 1.0f,  0.0f,  0.0f,		1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,		 1.0f,  0.0f,  0.0f,		1.0f, 0.0f,
            0.5f, -0.5f,  0.5f,		 1.0f,  0.0f,  0.0f,		0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,	     1.0f,  0.0f,  0.0f,		1.0f, 1.0f,
            //Bottom
            -0.5f,  -0.5f,  0.5f,	 0.0f, -1.0f,  0.0f,		0.0f, 0.0f,
            -0.5f,  -0.5f, -0.5f,	 0.0f, -1.0f,  0.0f,		0.0f, 1.0f,
            0.5f,  -0.5f,  0.5f,	 0.0f, -1.0f,  0.0f,		1.0f, 0.0f,
            0.5f,  -0.5f,  0.5f,	 0.0f, -1.0f,  0.0f,		1.0f, 0.0f,
            -0.5f,  -0.5f, -0.5f,	 0.0f, -1.0f,  0.0f,		0.0f, 1.0f,
            0.5f,  -0.5f, -0.5f,	 0.0f, -1.0f,  0.0f,		1.0f, 1.0f,
            //Top
            -0.5f,  0.5f, -0.5f,	 0.0f,  1.0f,  0.0f,		0.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,	 0.0f,  1.0f,  0.0f,		0.0f, 1.0f,
            0.5f,  0.5f, -0.5f,	     0.0f,  1.0f,  0.0f,		1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,	     0.0f,  1.0f,  0.0f,		1.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,	 0.0f,  1.0f,  0.0f,		0.0f, 1.0f,
            0.5f,  0.5f,  0.5f,	     0.0f,  1.0f,  0.0f,		1.0f, 1.0f,
    };

    private VertexArray vertexArray;

    public TexturedCube(Point pos, int textureId) {
        super(pos, 0, new Vector3f(1f, 1f, 1f), textureId);
    }

    @Override
    protected void initVertexData() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    @Override
    protected void initObjectProperties() {
        type = Type.TEXTURED;
        isShining = true;
    }

    @Override
    public void bindData(ShaderProgram shaderProgram) {
        int offset = 0;
        vertexArray.setVertexAttribPointer(offset, shaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        offset+=POSITION_COMPONENT_COUNT;
        vertexArray.setVertexAttribPointer(offset, shaderProgram.getNormalAttributeLocation(), NORMAL_COMPONENT_COUNT, STRIDE);
        offset+=NORMAL_COMPONENT_COUNT;
        vertexArray.setVertexAttribPointer(offset, shaderProgram.getTextureCoordsAttributeLocation(), TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
    }

    @Override
    public void draw() {
        glDrawArrays(GL_TRIANGLES, 0, 36);
    }
}
