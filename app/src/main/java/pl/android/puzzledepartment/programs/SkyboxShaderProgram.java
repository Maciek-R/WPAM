package pl.android.puzzledepartment.programs;

import android.content.Context;
import android.graphics.Color;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.objects.Skybox;

import static android.opengl.GLES30.GL_TEXTURE0;
import static android.opengl.GLES30.GL_TEXTURE_CUBE_MAP;
import static android.opengl.GLES30.glActiveTexture;
import static android.opengl.GLES30.glBindTexture;
import static android.opengl.GLES30.glGetAttribLocation;
import static android.opengl.GLES30.glGetUniformLocation;
import static android.opengl.GLES30.glUniform1i;
import static android.opengl.GLES30.glUniform3f;
import static android.opengl.GLES30.glUniformMatrix4fv;

/**
 * Created by Maciek Ruszczyk on 2017-10-28.
 */

public class SkyboxShaderProgram extends ShaderProgram{

    private final int uViewMatrixLocation;
    private final int uProjectionMatrixLocation;
    private final int uTextureUnitLocation;
    private final int uSkyColourLocation;
    private final int aPositionLocation;

    public SkyboxShaderProgram(Context context) {
        super(context, R.raw.skybox_vertex_shader, R.raw.skybox_fragment_shader);

        uViewMatrixLocation = glGetUniformLocation(program, U_VIEW_MATRIX);
        uProjectionMatrixLocation = glGetUniformLocation(program, U_PROJECTION_MATRIX);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
        uSkyColourLocation = glGetUniformLocation(program, U_SKY_COLOUR);
    }

    public void loadTextureUnits(){
        glUniform1i(uTextureUnitLocation, 0);
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

    public void bindTextures(Skybox skybox) {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, skybox.getTextureId());
    }

    @Override
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
}
