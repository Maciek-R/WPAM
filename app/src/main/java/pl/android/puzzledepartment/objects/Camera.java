package pl.android.puzzledepartment.objects;


import pl.android.puzzledepartment.managers.CollisionManager;
import pl.android.puzzledepartment.managers.TimeManager;
import pl.android.puzzledepartment.util.geometry.Vector2f;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public class Camera {

    public static final float WIDTH = 0.7f;
    private static final float JUMP_POWER = 20;
    private static final float GRAVITY = -50;

    private float posX;
    private float posY;
    private float lookPosY;
    private float posZ;

    private float rotationX;
    private float rotationY;

    private float possiblePosX;
    private float possiblePosY;
    private float possiblePosZ;

    private boolean isInAir = false;
    private float flySpeed = 0f;

    private float deltaX = 0;
    private float deltaZ = 0;

    public Camera() {
        this(0, 0, 0);
    }

    public Camera(float x, float y, float z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.lookPosY = posY + 1.5f;
    }

    private void moveXZ() {
        this.posX = possiblePosX;
        this.posZ = possiblePosZ;

    }

    public void jump() {
        if (!isInAir) {
            flySpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    public void update(HeightMap heightMap, CollisionManager collisionManager) {
        //System.out.println("X: " + this.posX + "Z:  " + this.posZ);
        countNextPossiblePosition();
        clampHeightMapBorder(heightMap);

        collisionManager.checkWithKeyCollision(this);
        collisionManager.checkParticlesCollision(this);

        if(collisionManager.checkTeleportCollision(this)){
            isInAir = false;
            flySpeed = 0;
            return;
        }
        if (collisionManager.checkEndTeleportCollision(this)) {
            isInAir = false;
            flySpeed = 0;
            return;
        }

        flySpeed += GRAVITY * TimeManager.getDeltaTimeInSeconds();
        this.possiblePosY = this.posY + flySpeed * TimeManager.getDeltaTimeInSeconds();

        serveCollisionsWithEntities(collisionManager);
        serveCollisionWithHeightmap(heightMap);
    }

    private void serveCollisionWithHeightmap(HeightMap heightMap) {
        float heightY = heightMap.getHeight(posX, posZ);
        if (posY < heightY) {
            isInAir = false;
            flySpeed = 0;
            posY = heightY;
            lookPosY = posY + 1.5f;
        }
    }

    private void serveCollisionsWithEntities(CollisionManager collisionManager) {
        CollisionManager.CollisionDescription collisionDescription = collisionManager.checkCollision(this);
        if (!collisionDescription.isCollision()) {
            moveXZ();
            this.posY = possiblePosY;
            this.lookPosY = posY + 1.5f;
        }
        else{
            if (collisionDescription.isCollisionOverEntity() && collisionDescription.isCollisionSideEntity()) {
                this.posY = collisionDescription.getCollisionPosY();
                this.lookPosY = posY + 1.5f;
                isInAir = false;
                flySpeed = 0;
            }
            else if (collisionDescription.isCollisionOverEntity()) {
                moveXZ();
                this.posY = collisionDescription.getCollisionPosY();
                this.lookPosY = posY + 1.5f;
                isInAir = false;
                flySpeed = 0;
            } else if (collisionDescription.isCollisionSideEntity()) {
                this.posY = possiblePosY;
                this.lookPosY = posY + 1.5f;
            }
        }
    }

    private void clampHeightMapBorder(HeightMap heightMap) {
        possiblePosX = Math.max(-heightMap.getScale().x/2+1, Math.min(heightMap.getScale().x/2-1, possiblePosX));
        possiblePosZ = Math.max(-heightMap.getScale().z/2+1, Math.min(heightMap.getScale().z/2-1, possiblePosZ));
    }

    private void countNextPossiblePosition() {
        possiblePosX = posX;
        possiblePosZ = posZ;

        float translationX = -(float) (Math.sin(Math.toRadians(rotationX)) * deltaZ);
        float translationZ = (float) (Math.cos(Math.toRadians(rotationX)) * deltaZ);

        possiblePosX += translationX;
        possiblePosZ += translationZ;

        translationX = (float) (Math.sin(Math.toRadians(rotationX + 90)) * deltaX);
        translationZ = -(float) (Math.cos(Math.toRadians(rotationX + 90)) * deltaX);

        possiblePosX += translationX;
        possiblePosZ += translationZ;
    }

    public void rotateX(float angle) {
        rotationX+=angle;
    }
    public void rotateY(float angle) {
        rotationY+=angle;

        if(rotationY < -90)
            rotationY = -90;
        else if(rotationY > 90)
            rotationY = 90;
    }

    public float getPosX() {return posX;}
    public float getPosY() {return posY;}
    public float getPosZ() {return posZ;}

    public float getRotationX() {return rotationX;}
    public float getRotationY() {return rotationY;}

    public float getLookPosY() {return lookPosY;}

    public float getPossibleX() {return possiblePosX;}
    public float getPossibleY() {return possiblePosY;}
    public float getPossibleZ() {return possiblePosZ;}

    public void setDirection(float deltaX, float deltaZ) {
        this.deltaX = deltaX;
        this.deltaZ = deltaZ;
    }

    public void goTo(Vector3f position) {
        this.posX = position.x;
        this.posY = position.y;
        this.posZ = position.z;
        this.lookPosY = posY + 1.5f;
    }
    public float getDeltaX() {
        return deltaX;
    }
    public float getDeltaZ() {
        return deltaZ;
    }
    public float getFlySpeed() { return flySpeed; }
    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setPosZ(float posZ) {
        this.posZ = posZ;
    }

    public void setRotationX(float rotationX) {
        this.rotationX = rotationX;
    }

    public void setRotationY(float rotationY) {
        this.rotationY = rotationY;
    }
}
