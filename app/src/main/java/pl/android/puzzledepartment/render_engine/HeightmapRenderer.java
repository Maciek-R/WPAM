package pl.android.puzzledepartment.render_engine;

import pl.android.puzzledepartment.objects.HeightMap;
import pl.android.puzzledepartment.objects.Skybox;
import pl.android.puzzledepartment.programs.HeightmapShaderProgram;

import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;

/**
 * Created by Maciek Ruszczyk on 2017-10-08.
 */

public class HeightmapRenderer {
    private final HeightmapShaderProgram heightmapShaderProgram;
    private final float[] modelMatrix = new float[16];

    public HeightmapRenderer(HeightmapShaderProgram heightmapShaderProgram) {
        this.heightmapShaderProgram = heightmapShaderProgram;
        heightmapShaderProgram.useProgram();
        heightmapShaderProgram.loadTextureUnits();
        heightmapShaderProgram.loadSkyColour(Skybox.getColour());
        heightmapShaderProgram.stopProgram();
    }

    public void render(HeightMap heightMap, final float[] viewMatrix, final float[] projectionMatrix) {
        setIdentityM(modelMatrix, 0);
        scaleM(modelMatrix, 0, heightMap.getScale().x, heightMap.getScale().y, heightMap.getScale().z);
        heightmapShaderProgram.useProgram();
        heightmapShaderProgram.loadModelMatrix(modelMatrix);
        heightmapShaderProgram.loadViewMatrix(viewMatrix);
        heightmapShaderProgram.loadProjectionMatrix(projectionMatrix);
        heightmapShaderProgram.bindTextures(heightMap);
        heightMap.bindData(heightmapShaderProgram);
        heightMap.draw();
        heightmapShaderProgram.stopProgram();
    }
}
