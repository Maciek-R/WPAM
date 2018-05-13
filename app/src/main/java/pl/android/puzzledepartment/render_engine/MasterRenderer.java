package pl.android.puzzledepartment.render_engine;

import android.content.Context;

import java.util.List;

import pl.android.puzzledepartment.gui.GuiEntity;
import pl.android.puzzledepartment.gui.GuiRenderer;
import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.SimpleColorShaderCube;
import pl.android.puzzledepartment.objects.complex_objects.ComplexEntity;
import pl.android.puzzledepartment.objects.complex_objects.DragonStatue;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.HeightMap;
import pl.android.puzzledepartment.objects.Light;
import pl.android.puzzledepartment.objects.Skybox;
import pl.android.puzzledepartment.objects.complex_objects.Lever;
import pl.android.puzzledepartment.objects.particles.ParticleShooter;
import pl.android.puzzledepartment.objects.particles.ParticleSystem;
import pl.android.puzzledepartment.programs.color_programs.AttributeColorShaderProgram;
import pl.android.puzzledepartment.programs.color_programs.ColorShaderProgram;
import pl.android.puzzledepartment.programs.entity_programs.EntityColouredNotShiningShaderProgram;
import pl.android.puzzledepartment.programs.entity_programs.EntityColouredShiningShaderProgram;
import pl.android.puzzledepartment.programs.entity_programs.EntityShaderProgram;
import pl.android.puzzledepartment.programs.GuiShaderProgram;
import pl.android.puzzledepartment.programs.HeightmapShaderProgram;
import pl.android.puzzledepartment.programs.ParticleShaderProgram;
import pl.android.puzzledepartment.programs.color_programs.SimpleColorShaderProgram;
import pl.android.puzzledepartment.programs.SkyboxShaderProgram;
import pl.android.puzzledepartment.programs.entity_programs.EntityTexturedNotShiningShaderProgram;
import pl.android.puzzledepartment.programs.entity_programs.EntityTexturedShiningShaderProgram;
import pl.android.puzzledepartment.programs.entity_programs.EntityUncolouredNotShiningShaderProgram;
import pl.android.puzzledepartment.programs.entity_programs.EntityUncolouredShiningShaderProgram;
import pl.android.puzzledepartment.puzzles.AbstractPuzzle;
import pl.android.puzzledepartment.puzzles.DragonStatuePuzzle;
import pl.android.puzzledepartment.puzzles.MixColorPuzzle;
import pl.android.puzzledepartment.puzzles.ParticlesOrderPuzzle;
import pl.android.puzzledepartment.puzzles.ParticlesWalkPuzzle;
import pl.android.puzzledepartment.puzzles.TeleportPuzzle;
import pl.android.puzzledepartment.objects.complex_objects.Room;
import pl.android.puzzledepartment.util.MatrixHelper;
import pl.android.puzzledepartment.util.geometry.Point;

import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by Maciek Ruszczyk on 2017-10-08.
 */

public class MasterRenderer {

    private final static float RENDER_ENTITY_DISTANCE = 65.0f;
    private final static float RENDER_PARTICLES_DISTANCE = 80.0f;

    private final EntityRenderer entityRenderer;
    private final EntityShaderProgram entityUnColouredNotShiningShaderProgram;
    private final EntityShaderProgram entityColouredNotShiningShaderProgram;
    private final EntityShaderProgram entityUnColouredShiningShaderProgram;
    private final EntityShaderProgram entityColouredShiningShaderProgram;
    private final EntityShaderProgram entityTexturedShiningShaderProgram;
    private final EntityShaderProgram entityTexturedNotShiningShaderProgram;

    private final ColorShaderProgram attributeColorShaderProgram;
    private final ColorShaderProgram simpleColorShaderProgram;

    private final ParticleRenderer particleRenderer;
    private final HeightmapRenderer heightmapRenderer;
    private final SkyboxRenderer skyboxRenderer;

    private final GuiRenderer guiRenderer;

    private final float[] viewMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];

    private Light light;
    private Camera camera;

    public MasterRenderer(Context context, Light light, Camera camera) {
        entityRenderer = new EntityRenderer();
        entityUnColouredNotShiningShaderProgram = new EntityUncolouredNotShiningShaderProgram(context);
        entityColouredNotShiningShaderProgram = new EntityColouredNotShiningShaderProgram(context);
        entityUnColouredShiningShaderProgram = new EntityUncolouredShiningShaderProgram(context);
        entityColouredShiningShaderProgram = new EntityColouredShiningShaderProgram(context);
        entityTexturedShiningShaderProgram = new EntityTexturedShiningShaderProgram(context);
        entityTexturedNotShiningShaderProgram = new EntityTexturedNotShiningShaderProgram(context);

        attributeColorShaderProgram = new AttributeColorShaderProgram(context);
        simpleColorShaderProgram = new SimpleColorShaderProgram(context);

        particleRenderer = new ParticleRenderer(new ParticleShaderProgram(context));
        heightmapRenderer = new HeightmapRenderer(new HeightmapShaderProgram(context));
        skyboxRenderer = new SkyboxRenderer(new SkyboxShaderProgram(context));

        guiRenderer = new GuiRenderer(new GuiShaderProgram(context));

        this.light = light;
        this.camera = camera;
    }

    public void render(List<Entity> entities) {
        for(Entity entity:entities)
            render(entity);
    }

    public void render(Entity entity) {
        if(!isObjectInRenderArea(entity.getPos(), camera))
            return;

        ColorShaderProgram colorShaderProgram = null;
        if(Entity.Type.UNCOLOURED.equals(entity.getType()))
            colorShaderProgram = simpleColorShaderProgram;

        else if(Entity.Type.COLOURED.equals(entity.getType()))
            colorShaderProgram = attributeColorShaderProgram;

        entityRenderer.render(colorShaderProgram, entity, viewMatrix, projectionMatrix);
    }

    public void render(HeightMap heightMap) {
        heightmapRenderer.render(heightMap, viewMatrix, projectionMatrix);
    }

    public void createProjectionMatrix(int width, int height) {
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float)width / (float) height, 0.1f, 75f);
    }
    public void prepareCamera(Camera camera) {
        setIdentityM(viewMatrix, 0);
        rotateM(viewMatrix, 0, camera.getRotationY(), 1f, 0f, 0f);
        rotateM(viewMatrix, 0, camera.getRotationX(), 0f, 1f, 0f);
        translateM(viewMatrix, 0, -camera.getPosX(), -camera.getLookPosY(), -camera.getPosZ());
    }

    public void render(ComplexEntity complexEntity) {
        if(complexEntity instanceof Room)
            render((Room)complexEntity);
        else
            renderWithNormals(complexEntity.getEntities());
    }

    public void render(Room room) {
        render(room.getEntities());
    }

    public void render(AbstractPuzzle puzzle, float currentTime) {
        renderWithNormals(puzzle.getTip());
        if (puzzle instanceof TeleportPuzzle) {
            render((TeleportPuzzle) puzzle);
        }
        else if (puzzle instanceof DragonStatuePuzzle) {
            render((DragonStatuePuzzle) puzzle);
        }
        else if (puzzle instanceof MixColorPuzzle) {
            render((MixColorPuzzle) puzzle);
        }
        else if (puzzle instanceof ParticlesOrderPuzzle) {
            render((ParticlesOrderPuzzle) puzzle, currentTime);
        }
        else if (puzzle instanceof ParticlesWalkPuzzle) {
            render((ParticlesWalkPuzzle) puzzle, currentTime);
        }
    }

    public void render(TeleportPuzzle teleportPuzzle) {
        render(teleportPuzzle.getTeleports());
        for(Room r:teleportPuzzle.getRooms())
            render(r);
    }

    public void render(DragonStatuePuzzle dragonStatuePuzzle) {
        for(DragonStatue d:dragonStatuePuzzle.getStatues())
            render(d);
    }

    public void render(MixColorPuzzle mixColorPuzzle) {
        for(SimpleColorShaderCube c:mixColorPuzzle.getCubes())
            renderWithNormals(c);
        for(Lever l:mixColorPuzzle.getLevers())
            render(l);
    }

    public void render(ParticlesOrderPuzzle particlesOrderPuzzle, float currentTime){
        int notInArea = 0;
        for(ParticleShooter particleShooter:particlesOrderPuzzle.getParticleShooters())
            if(!isParticleInRenderArea(particleShooter.getPos(), camera))
                ++notInArea;
        if(notInArea == particlesOrderPuzzle.getParticleShooters().length)
            return;
        render(particlesOrderPuzzle.getParticleSystem(), currentTime);
    }

    public void render(ParticlesWalkPuzzle particlesWalkPuzzle, float currentTime) {
        if(!isParticleInRenderArea(particlesWalkPuzzle.getParticleShooter().getPos(), camera))
            return;
        render(particlesWalkPuzzle.getParticleSystem(), currentTime);
    }

    public void render(ParticleSystem particleSystem, float currentTime) {
        particleRenderer.render(particleSystem, viewMatrix, projectionMatrix, currentTime);
    }

    public void renderGuis(List<GuiEntity> guiEntities) {
        guiRenderer.render(guiEntities);
    }

    public void render(Skybox skybox) {
        setIdentityM(viewMatrix, 0);
        skybox.rotate();
        rotateM(viewMatrix, 0, camera.getRotationY(), 1f, 0f, 0f);
        rotateM(viewMatrix, 0, camera.getRotationX(), 0f, 1f, 0f);
        rotateM(viewMatrix, 0, skybox.getRotation(), 0f, 1f, 0f);
        skyboxRenderer.render(skybox, viewMatrix, projectionMatrix);
    }

    public void renderWithNormals(List<? extends Entity> entities) {
        for(Entity e:entities)
            renderWithNormals(e);
    }

    public void renderWithNormals(Entity entity) {
        if(!entity.isVisible() || !isObjectInRenderArea(entity.getPos(), camera))
            return;
        EntityShaderProgram entityShaderProgram = null;
        switch(entity.getType())
        {
            case UNCOLOURED:
                if(entity.isShining()) {
                    entityShaderProgram = entityUnColouredShiningShaderProgram;
                    break;
                }
                else {
                    entityShaderProgram = entityUnColouredNotShiningShaderProgram;
                    break;
                }
            case COLOURED:
                if(entity.isShining()) {
                    entityShaderProgram = entityColouredShiningShaderProgram;
                    break;
                }
                else {
                    entityShaderProgram = entityColouredNotShiningShaderProgram;
                    break;
                }
            case TEXTURED:
                if(entity.isShining()) {
                    entityShaderProgram = entityTexturedShiningShaderProgram;
                    break;
                }
                else {
                    entityShaderProgram = entityTexturedNotShiningShaderProgram;
                    break;
                }
        }
        entityRenderer.renderWithNormals(entityShaderProgram, entity, viewMatrix, projectionMatrix, light, camera);
    }

    private boolean isObjectInRenderArea(Point center, Camera camera) {
        return countDistance(center, camera) < RENDER_ENTITY_DISTANCE;
    }

    private boolean isParticleInRenderArea(Point center, Camera camera) {
        return countDistance(center, camera) < RENDER_PARTICLES_DISTANCE;
    }

    private float countDistance(Point center, Camera camera) {
        float diffX = camera.getPosX() - center.x;
        float diffY = camera.getPosY() - center.y;
        float diffZ = camera.getPosZ() - center.z;

        return (float) Math.sqrt(diffX*diffX + diffY*diffY + diffZ*diffZ);
    }
}
