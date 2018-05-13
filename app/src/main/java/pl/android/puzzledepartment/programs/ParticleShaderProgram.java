package pl.android.puzzledepartment.programs;

import android.content.Context;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.objects.particles.ParticleSystem;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Maciek Ruszczyk on 2017-10-21.
 */

public class ParticleShaderProgram extends ShaderProgram{

    private final int uViewMatrixLocation;
    private final int uProjectionMatrixLocation;
    private final int uTimeLocation;
    private final int uTextureUnitLocation;

    private final int aPositionLocation;
    private final int aColorLocation;
    private final int aDirectionVectorLocation;
    private final int aParticleStartTimeLocation;

    public ParticleShaderProgram(Context context) {
        super(context, R.raw.particle_vertex_shader, R.raw.particle_fragment_shader);

        uViewMatrixLocation = glGetUniformLocation(program, U_VIEW_MATRIX);
        uProjectionMatrixLocation = glGetUniformLocation(program, U_PROJECTION_MATRIX);
        uTimeLocation = glGetUniformLocation(program, U_TIME);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);

        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);
        aDirectionVectorLocation = glGetAttribLocation(program, A_DIRECTION_VECTOR);
        aParticleStartTimeLocation = glGetAttribLocation(program, A_PARTICLE_START_TIME);
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

    public void bindTextures(ParticleSystem particleSystem) {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, particleSystem.getTexture());
    }

    public void loadTime(float elapsedTime){
        glUniform1f(uTimeLocation, elapsedTime);
    }

    @Override
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    @Override
    public int getColorAttributeLocation() {
        return aColorLocation;
    }
    @Override
    public int getDirectionVectorAttributeLocation() {
        return aDirectionVectorLocation;
    }
    @Override
    public int getParticleStartTimeAttributeLocation() {
        return aParticleStartTimeLocation;
    }
}
