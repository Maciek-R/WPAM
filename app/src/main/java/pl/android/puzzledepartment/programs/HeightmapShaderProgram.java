package pl.android.puzzledepartment.programs;

import android.content.Context;
import android.graphics.Color;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.objects.HeightMap;
import pl.android.puzzledepartment.objects.TerrainTexturePack;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE1;
import static android.opengl.GLES20.GL_TEXTURE2;
import static android.opengl.GLES20.GL_TEXTURE3;
import static android.opengl.GLES20.GL_TEXTURE4;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform3f;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Maciek Ruszczyk on 2017-10-07.
 */

public class HeightmapShaderProgram extends ShaderProgram{
    private final int uModelMatrixLocation;
    private final int uViewMatrixLocation;
    private final int uProjectionMatrixLocation;
    private final int aPositionLocation;
    private final int aTextureCoordsLocation;
    private final int uBackgroundTextureUnitLocation;
    private final int uRedTextureUnitLocation;
    private final int uGreenTextureUnitLocation;
    private final int uBlueTextureUnitLocation;
    private final int uBlendMapTextureUnitLocation;
    private final int uSkyColourLocation;

    public HeightmapShaderProgram(Context context) {
        super(context, R.raw.heightmap_vertex_shader, R.raw.heightmap_fragment_shader);

        uModelMatrixLocation = glGetUniformLocation(program, U_MODEL_MATRIX);
        uViewMatrixLocation = glGetUniformLocation(program, U_VIEW_MATRIX);
        uProjectionMatrixLocation = glGetUniformLocation(program, U_PROJECTION_MATRIX);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoordsLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
        uBackgroundTextureUnitLocation = glGetUniformLocation(program, U_BACKGROUND_TEXTURE_UNIT);
        uRedTextureUnitLocation = glGetUniformLocation(program, U_RED_TEXTURE_UNIT);
        uGreenTextureUnitLocation = glGetUniformLocation(program, U_GREEN_TEXTURE_UNIT);
        uBlueTextureUnitLocation = glGetUniformLocation(program, U_BLUE_TEXTURE_UNIT);
        uBlendMapTextureUnitLocation = glGetUniformLocation(program, U_BLENDMAP_TEXTURE_UNIT);
        uSkyColourLocation = glGetUniformLocation(program, U_SKY_COLOUR);
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

    public void loadTextureUnits()
    {
        glUniform1i(uBackgroundTextureUnitLocation, 0);
        glUniform1i(uRedTextureUnitLocation, 1);
        glUniform1i(uGreenTextureUnitLocation, 2);
        glUniform1i(uBlueTextureUnitLocation, 3);
        glUniform1i(uBlendMapTextureUnitLocation, 4);
    }

    public void bindTextures(HeightMap heightMap){
        TerrainTexturePack terrainTexturePack = heightMap.getTerrainTexturePack();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, terrainTexturePack.getBackgroundTexture().getTextureId());
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, terrainTexturePack.getRedTexture().getTextureId());
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, terrainTexturePack.getGreenTexture().getTextureId());
        glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, terrainTexturePack.getBlueTexture().getTextureId());
        glActiveTexture(GL_TEXTURE4);
        glBindTexture(GL_TEXTURE_2D, heightMap.getBlendMap().getTextureId());
    }

    @Override
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    @Override
    public int getTextureCoordsAttributeLocation() { return aTextureCoordsLocation; }
}
