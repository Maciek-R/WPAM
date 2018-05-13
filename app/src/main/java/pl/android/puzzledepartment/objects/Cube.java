package pl.android.puzzledepartment.objects;

import java.nio.ByteBuffer;
import pl.android.puzzledepartment.data.VertexArray;
import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.GL_UNSIGNED_BYTE;
import static android.opengl.GLES30.glDrawElements;
import static pl.android.puzzledepartment.util.Constants.BYTES_PER_FLOAT;
/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public class Cube extends Entity {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COORDINATES_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final static float[] VERTEX_DATA = {
            // Order: X, Y, Z, R, G, B
            -0.5f, 0.5f, 0.5f,           1f, 0f, 0f,
            0.5f, 0.5f, 0.5f,            0f, 1f, 0f,
            -0.5f, -0.5f, 0.5f,          0f, 0f, 1f,
            0.5f, -0.5f, 0.5f,           1f, 1f, 0f,
            -0.5f, 0.5f, -0.5f,          1f, 0f, 1f,
            0.5f, 0.5f, -0.5f,           0f, 1f, 1f,
            -0.5f, -0.5f, -0.5f,         1f, 0f, 1f,
            0.5f, -0.5f, -0.5f,          1f, 1f, 1f
    };
    private final static byte[] INDEX_DATA = {
            // Front
            0, 2, 1,
            1, 2, 3,
            // Back
            5, 7, 4,
            4, 7, 6,
            // Left
            4, 6, 0,
            0, 6, 2,
            // Right
            1, 3, 5,
            5, 3, 7,
            // Top
            4, 0, 5,
            5, 0, 1,
            // Bottom
            2, 6, 3,
            3, 6, 7
    };

    private VertexArray vertexArray;
    private ByteBuffer indexArray;

    public Cube(Point pos) {
        this(pos, new Vector3f(1.0f, 1.0f, 1.0f));
    }

    public Cube(Point pos, Vector3f scale) {
        super(pos, scale);
        initVertexData();
    }

    public Cube(Point pos, int angle, Vector3f scale, int textureId) {
        super(pos, angle, scale, textureId);
        initVertexData();
    }

    protected void initVertexData() {
        vertexArray = new VertexArray(VERTEX_DATA);
        indexArray = ByteBuffer.allocateDirect(6*6).put(INDEX_DATA);
        indexArray.position(0);
    }

    @Override
    protected void initObjectProperties() {
        this.type = Type.COLOURED;
    }

    public void bindData(ShaderProgram shaderProgram) {
        vertexArray.setVertexAttribPointer(0, shaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, shaderProgram.getColorAttributeLocation(), COLOR_COORDINATES_COMPONENT_COUNT, STRIDE);
    }

    @Override
    public void draw() {
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_BYTE, indexArray);
    }
}
