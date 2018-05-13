package pl.android.puzzledepartment.programs;

import android.content.Context;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.gui.GuiEntity;

import static android.opengl.GLES30.GL_TEXTURE0;
import static android.opengl.GLES30.GL_TEXTURE_2D;
import static android.opengl.GLES30.glActiveTexture;
import static android.opengl.GLES30.glBindTexture;
import static android.opengl.GLES30.glGetAttribLocation;
import static android.opengl.GLES30.glGetUniformLocation;
import static android.opengl.GLES30.glUniform1i;
import static android.opengl.GLES30.glUniformMatrix4fv;

/**
 * Created by Maciek Ruszczyk on 2017-10-28.
 */

public class GuiShaderProgram extends ShaderProgram{
    private final int uModelMatrixLocation;
    private final int uTextureUnitLocation;

    private final int aPositionLocation;

    public GuiShaderProgram(Context context) {
        super(context, R.raw.gui_vertex_shader, R.raw.gui_fragment_shader);

        uModelMatrixLocation = glGetUniformLocation(program, U_MODEL_MATRIX);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
    }

    public void loadTextureUnits(){
        glUniform1i(uTextureUnitLocation, 0);
    }

    public void loadMatrix(float[] matrix) {
        glUniformMatrix4fv(uModelMatrixLocation, 1, false, matrix, 0);
    }

    public void bindTextures(GuiEntity gui) {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, gui.getTextureId());
    }

    @Override
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
}
