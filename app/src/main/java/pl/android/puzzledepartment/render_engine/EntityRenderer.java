package pl.android.puzzledepartment.render_engine;


import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.Light;
import pl.android.puzzledepartment.objects.Skybox;
import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.programs.color_programs.ColorShaderProgram;
import pl.android.puzzledepartment.programs.entity_programs.EntityShaderProgram;

import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static android.opengl.Matrix.transposeM;

/**
 * Created by Maciek Ruszczyk on 2017-10-08.
 */

public class EntityRenderer {
    private final float[] modelMatrix = new float[16];
    private final float[] invertedModelMatrix = new float[16];
    private final float[] tempMatrix = new float[16];

    public void render(ColorShaderProgram shaderProgram, Entity entity, final float[] viewMatrix, final float[] projectionMatrix) {
        prepareModelMatrix(entity);
        shaderProgram.useProgram();
        shaderProgram.loadModelMatrix(modelMatrix);
        shaderProgram.loadViewMatrix(viewMatrix);
        shaderProgram.loadProjectionMatrix(projectionMatrix);
        shaderProgram.loadSkyColour(Skybox.getColour());
        if(Entity.Type.UNCOLOURED.equals(entity.getType()))
            shaderProgram.loadColour(entity.getColor());
        bindDataAndDraw(shaderProgram, entity);
    }

    public void renderWithNormals(EntityShaderProgram shaderProgram, Entity entity, final float[] viewMatrix, final float[] projectionMatrix, Light light, Camera camera) {
        prepareMatricesForNormalVectors(entity, viewMatrix, projectionMatrix);
        shaderProgram.useProgram();
        shaderProgram.loadModelMatrix(modelMatrix);
        shaderProgram.loadViewMatrix(viewMatrix);
        shaderProgram.loadProjectionMatrix(projectionMatrix);
        shaderProgram.loadInvertedModelMatrix(invertedModelMatrix);
        shaderProgram.loadLight(light);
        shaderProgram.loadCamera(camera);
        shaderProgram.loadSkyColour(Skybox.getColour());
        shaderProgram.loadShining(entity.isShining());
        if(entity.isShining())
        {
            shaderProgram.loadDamper(entity.getDamper());
            shaderProgram.loadReflectivity(entity.getReflectivity());
        }
        if(Entity.Type.UNCOLOURED.equals(entity.getType()))
            shaderProgram.loadColor(entity.getColor());
        if(Entity.Type.TEXTURED.equals(entity.getType())){
            shaderProgram.loadTextureUnits();
            shaderProgram.bindTextures(entity);
        }
        bindDataAndDraw(shaderProgram, entity);
    }

    private void prepareModelMatrix(Entity entity) {
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, entity.getPos().x, entity.getPos().y, entity.getPos().z);
        rotateM(modelMatrix, 0, entity.getVerRotation(), 0f, 1f, 0f);
        rotateM(modelMatrix, 0, entity.getHorRotation(), 1f, 0f, 0f);
        scaleM(modelMatrix, 0, entity.getScale().x, entity.getScale().y, entity.getScale().z);
    }

    private void prepareMatricesForNormalVectors(Entity entity, float[] viewMatrix, float[] projectionMatrix) {
        prepareModelMatrix(entity);
        invertM(tempMatrix, 0, modelMatrix, 0);
        transposeM(invertedModelMatrix, 0, tempMatrix, 0);
    }

    private void bindDataAndDraw(ShaderProgram shaderProgram, Entity entity) {
        entity.bindData(shaderProgram);
        entity.draw();
    }
}
