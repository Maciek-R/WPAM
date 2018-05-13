package pl.android.puzzledepartment.programs.entity_programs;

import android.content.Context;
import android.graphics.Color;

import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.Light;
import pl.android.puzzledepartment.programs.ShaderProgram;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform3f;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Maciek Ruszczyk on 2017-11-18.
 */

public abstract class EntityShaderProgram extends ShaderProgram {

    private final int uModelMatrixLocation;
    private final int uViewMatrixLocation;
    private final int uProjectionMatrixLocation;
    private final int uIT_ModelMatrixLocation;
    private final int uColorLocation;
    private final int uCameraPos;
    private final int uLightPos;
    private final int uLightColor;
    private final int uDamper;
    private final int uReflectivity;
    private final int uIsShining;
    private final int uSkyColourLocation;
    private final int uTextureUnitLocation;

    private final int aPositionLocation;
    private final int aColorLocation;
    private final int aNormalLocation;
    private final int aTextureCoordinatesLocation;

    protected EntityShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        super(context, vertexShaderResourceId, fragmentShaderResourceId);

        uModelMatrixLocation = glGetUniformLocation(program, U_MODEL_MATRIX);
        uViewMatrixLocation = glGetUniformLocation(program, U_VIEW_MATRIX);
        uProjectionMatrixLocation = glGetUniformLocation(program, U_PROJECTION_MATRIX);
        uIT_ModelMatrixLocation = glGetUniformLocation(program, U_IT_MODEL_VIEW_MATRIX);
        uColorLocation = glGetUniformLocation(program, U_COLOR);
        uCameraPos = glGetUniformLocation(program, U_CAMERA_POS);
        uLightPos = glGetUniformLocation(program, U_LIGHT_POS);
        uLightColor = glGetUniformLocation(program, U_LIGHT_COLOR);
        uDamper = glGetUniformLocation(program, U_DAMPER);
        uReflectivity = glGetUniformLocation(program, U_REFLECTIVITY);
        uIsShining = glGetAttribLocation(program, U_IS_SHINING);
        uSkyColourLocation = glGetUniformLocation(program, U_SKY_COLOUR);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);

        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);
        aNormalLocation = glGetAttribLocation(program, A_NORMAL);
        aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
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

    public void loadInvertedModelMatrix(final float[] matrix)
    {
        glUniformMatrix4fv(uIT_ModelMatrixLocation, 1, false, matrix, 0);
    }

    public void loadColor(int color)
    {
        glUniform4f(uColorLocation, Color.red(color) / 255f, Color.green(color) / 255f, Color.blue(color) / 255f, 1.0f);
    }

    public void loadLight(Light light)
    {
        glUniform3f(uLightPos, light.getPos().x, light.getPos().y, light.getPos().z);
        glUniform3f(uLightColor, Color.red(light.getColor()) / 255f, Color.green(light.getColor()) / 255f, Color.blue(light.getColor()) / 255f);
    }
    public void loadCamera(Camera camera) {
        glUniform3f(uCameraPos, camera.getPosX(), camera.getLookPosY(), camera.getPosZ());
    }

    public void loadShining(boolean shining) {
        if(shining)
            glUniform1f(uIsShining, 1.0f);
        else
            glUniform1f(uIsShining, 0.0f);
    }

    public void loadSkyColour(int color) {
        glUniform3f(uSkyColourLocation, Color.red(color) / 255f, Color.green(color) / 255f, Color.blue(color) / 255f);
    }

    public void loadDamper(float damper) {
        glUniform1f(uDamper, damper);
    }
    public void loadReflectivity(float reflectivity) {
        glUniform1f(uReflectivity, reflectivity);
    }

    public void loadTextureUnits()
    {
        glUniform1i(uTextureUnitLocation, 0);
    }

    public void bindTextures(Entity entity){
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, entity.getTextureId());
    }

    @Override
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    @Override
    public int getNormalAttributeLocation() {
        return aNormalLocation;
    }
    @Override
    public int getColorAttributeLocation() {
        return aColorLocation;
    }
    @Override
    public int getTextureCoordsAttributeLocation() { return aTextureCoordinatesLocation; }
}
