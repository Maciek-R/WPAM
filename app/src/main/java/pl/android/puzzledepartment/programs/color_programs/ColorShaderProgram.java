package pl.android.puzzledepartment.programs.color_programs;

import android.content.Context;
import android.graphics.Color;

import pl.android.puzzledepartment.programs.entity_programs.EntityShaderProgram;

import static android.opengl.GLES30.glGetAttribLocation;
import static android.opengl.GLES30.glGetUniformLocation;
import static android.opengl.GLES30.glUniform3f;
import static android.opengl.GLES30.glUniformMatrix4fv;

/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public abstract class ColorShaderProgram extends EntityShaderProgram {
    private final int uModelMatrixLocation;
    private final int uViewMatrixLocation;
    private final int uProjectionMatrixLocation;
    private final int uSkyColourLocation;
    private final int aPositionLocation;

    public ColorShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId){
        super(context, vertexShaderResourceId, fragmentShaderResourceId);

        uModelMatrixLocation = glGetUniformLocation(program, U_MODEL_MATRIX);
        uViewMatrixLocation = glGetUniformLocation(program, U_VIEW_MATRIX);
        uProjectionMatrixLocation = glGetUniformLocation(program, U_PROJECTION_MATRIX);
        uSkyColourLocation = glGetUniformLocation(program, U_SKY_COLOUR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
    }

    public void loadModelMatrix(final float[] matrix) {
        glUniformMatrix4fv(uModelMatrixLocation, 1, false, matrix, 0);
    }

    public void loadViewMatrix(final float[] matrix) {
        glUniformMatrix4fv(uViewMatrixLocation, 1, false, matrix, 0);
    }

    public void loadProjectionMatrix(final float[] matrix) {
        glUniformMatrix4fv(uProjectionMatrixLocation, 1, false, matrix, 0);
    }
    public void loadSkyColour(int color) {
        glUniform3f(uSkyColourLocation, Color.red(color) / 255f, Color.green(color) / 255f, Color.blue(color) / 255f);
    }

    public abstract void loadColour(int color);

    @Override
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
}
