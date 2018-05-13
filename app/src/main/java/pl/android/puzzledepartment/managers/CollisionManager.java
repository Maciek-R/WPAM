package pl.android.puzzledepartment.managers;


import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Collisionable;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.Key;
import pl.android.puzzledepartment.objects.Teleport;
import pl.android.puzzledepartment.objects.complex_objects.DragonStatue;
import pl.android.puzzledepartment.objects.complex_objects.EndTower;
import pl.android.puzzledepartment.objects.complex_objects.Lever;
import pl.android.puzzledepartment.objects.particles.ParticleCollideShooter;
import pl.android.puzzledepartment.puzzles.AbstractPuzzle;
import pl.android.puzzledepartment.puzzles.DragonStatuePuzzle;
import pl.android.puzzledepartment.puzzles.MixColorPuzzle;
import pl.android.puzzledepartment.puzzles.ParticlesOrderPuzzle;
import pl.android.puzzledepartment.puzzles.TeleportPuzzle;
import pl.android.puzzledepartment.objects.complex_objects.Room;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-10-20.
 */

public class CollisionManager {
    private GameManager gameManager;

    private List<Collisionable> entities;
    private CollisionDescription collisionDescription;

    private List<Entity> keys;

    private TeleportPuzzle teleportPuzzle;
    private List<Entity> teleports;

    private ParticlesOrderPuzzle particlesOrderPuzzle;

    private Teleport endTeleport;

    public CollisionManager() {
        entities = new ArrayList<>();
        keys = new ArrayList<>();
        collisionDescription = new CollisionDescription();

        teleports = new ArrayList<>();
    }

    public void addObserver(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public CollisionDescription checkCollision(Camera camera) {
        collisionDescription.isCollision = false;
        collisionDescription.isCollisionOverEntity = false;
        collisionDescription.isCollisionSideEntity = false;
        for (Collisionable c : entities)
            checkCollision(c, camera);

        return collisionDescription;
    }

    private boolean checkCollision(Collisionable c, Camera camera) {

        final float scaleY = c.getScale().y/2;

        if (collide(c, camera)) {

            collisionDescription.isCollision = true;
            if (camera.getPosY() >= (c.getPos().y + scaleY)) {
                collisionDescription.collisionPosY = c.getPos().y + scaleY;
                collisionDescription.isCollisionOverEntity = true;
            } else {
                collisionDescription.isCollisionSideEntity = true;
            }
            return true;
        } else
            return false;
    }

    private boolean collide(Collisionable c, Camera camera) {
        if(Collisionable.CollisionType.CUBE.equals(c.getCollisionType()))
            return collideCube(c, camera);
        else
            return collideCylinderWall(c, camera);
    }

    private boolean collideCylinderWall(Collisionable c, Camera camera) {
        final float scaleX = c.getScale().x;
        final float scaleY = c.getScale().y;
        final float scaleZ = c.getScale().z;

        final float possibleCamPosX = camera.getPossibleX();
        final float possibleCamPosZ = camera.getPossibleZ();
        final float possibleCamPosY = camera.getLookPosY();

        if(c.getPos().y > possibleCamPosY || c.getPos().y + scaleY< possibleCamPosY)
            return false;

        final float radius = (scaleX + scaleZ) / 2f;
        float distance = new Vector3f(Math.abs(c.getPos().x-possibleCamPosX),
                                        0f,
                                        Math.abs(c.getPos().z-possibleCamPosZ)).length();

        boolean isCollision =  (distance < radius) && (distance > radius - 0.8f);
        if(!isCollision)
            return false;

        if(c instanceof EndTower){

            isCollision = collide(((EndTower)c).getDoor(), camera);
            if(isCollision && ((EndTower)c).isDoorOpen())
                return false;
        }
        return true;
    }

    private boolean collideCube(Collisionable e, Camera camera) {
        final float scaleX = e.getScale().x/2;
        final float scaleY = e.getScale().y/2;
        final float scaleZ = e.getScale().z/2;

        final float entityLeftPosX = e.getPos().x - scaleX;
        final float entityRightPosX = e.getPos().x + scaleX;
        final float entityBottomPosY = e.getPos().y - scaleY;
        final float entityTopPosY = e.getPos().y + scaleY;
        final float entityLeftPosZ = e.getPos().z - scaleZ;
        final float entityRightPosZ = e.getPos().z + scaleZ;

        final float possibleCamLeftX = camera.getPossibleX() - Camera.WIDTH/2;
        final float possibleCamRightX = camera.getPossibleX() + Camera.WIDTH/2;
        final float possibleCamBottomY = camera.getPossibleY();
        final float possibleCamTopY = camera.getPossibleY() + Camera.WIDTH;
        final float possibleCamLeftZ = camera.getPossibleZ() - Camera.WIDTH/2;
        final float possibleCamRightZ = camera.getPossibleZ() + Camera.WIDTH/2;

        return !(possibleCamLeftX > entityRightPosX || possibleCamRightX < entityLeftPosX ||
                possibleCamBottomY > entityTopPosY || possibleCamTopY < entityBottomPosY ||
                possibleCamLeftZ > entityRightPosZ || possibleCamRightZ < entityLeftPosZ);
    }

    public void add(Collisionable c) {
        if(c instanceof Key)
            keys.add((Key)c);
        else if(c instanceof Teleport)
            endTeleport = (Teleport) c;
        else
            entities.add(c);
    }
    public void add(Room room) {
        for(Entity e:room.getEntities())
            add(e);
    }
    public void add(List<? extends AbstractPuzzle> puzzles) {
        for(AbstractPuzzle puzzle:puzzles)
            add(puzzle);
    }

    private void add (AbstractPuzzle puzzle) {
        add(puzzle.getTip());

        if (puzzle instanceof TeleportPuzzle) {
            addTeleportPuzzle((TeleportPuzzle) puzzle);

        } else if (puzzle instanceof ParticlesOrderPuzzle) {
            addParticlesOrderPuzzle((ParticlesOrderPuzzle) puzzle);

        }
        else if (puzzle instanceof MixColorPuzzle) {
            addMixColorPuzzle((MixColorPuzzle) puzzle);
        }
        else if (puzzle instanceof DragonStatuePuzzle) {
            addDragonStatuePuzzle((DragonStatuePuzzle) puzzle);
        }
    }

    private void addDragonStatuePuzzle(DragonStatuePuzzle puzzle) {
        for(DragonStatue d:puzzle.getStatues()){
            add(d.getVase());
            add(d.getDragon());
        }
    }

    private void addMixColorPuzzle(MixColorPuzzle puzzle) {
        for(Entity e:puzzle.getCubes())
            add(e);
        for(Lever l:puzzle.getLevers()){
            add(l.getLeverBase());
        }
    }

    private void addTeleportPuzzle(TeleportPuzzle teleportPuzzle) {
        this.teleportPuzzle = teleportPuzzle;
        for(Room r:teleportPuzzle.getRooms())
            add(r);
        for(Entity e:teleportPuzzle.getTeleports())
            addTeleport(e);
    }
    private void addParticlesOrderPuzzle(ParticlesOrderPuzzle particlesOrderPuzzle) {
        this.particlesOrderPuzzle = particlesOrderPuzzle;
    }

    private void addTeleport(Entity entity) {
        teleports.add(entity);
    }

    public static class CollisionDescription {
        boolean isCollision;
        boolean isCollisionOverEntity;
        boolean isCollisionSideEntity;
        float collisionPosY;

        public boolean isCollisionOverEntity() {
            return isCollisionOverEntity;
        }

        public boolean isCollisionSideEntity() {
            return isCollisionSideEntity;
        }

        public boolean isCollision() {
            return isCollision;
        }
        public float getCollisionPosY() {
            return collisionPosY;
        }
    }

    public boolean checkTeleportCollision(Camera camera) {
        for (Entity t : teleports)
            if (checkCollision(t, camera)) {
                teleportPuzzle.checkCorrectTeleport(t);
                camera.goTo(teleportPuzzle.getPositionOnCurrentFloor());
                return true;
            }
        return false;
    }

    public void checkParticlesCollision(Camera camera) {
        for (ParticleCollideShooter particleCollideShooter : particlesOrderPuzzle.getParticleShooters()) {
            if (checkCollision(particleCollideShooter, camera)) {
                particlesOrderPuzzle.checkIfChoseCorrectParticle(particleCollideShooter);
                return;
            }
        }
    }

    public void checkWithKeyCollision(Camera camera) {
        for (Entity k : keys)
            if (checkCollision(k, camera)) {
                gameManager.onCollisionNotify(k);
                keys.remove(k);
                return;
            }
    }

    public boolean checkEndTeleportCollision(Camera camera) {
        if(endTeleport == null)
            return false;

        if(checkCollision(endTeleport, camera)){
            Point p = endTeleport.getTeleportPlace();
            camera.goTo(new Vector3f(p.x, p.y, p.z));
            gameManager.gameCompletedMessage();
            return true;
        }
        return false;
    }
}
