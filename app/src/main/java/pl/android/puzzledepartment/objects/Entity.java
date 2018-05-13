package pl.android.puzzledepartment.objects;

import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.managers.TimeManager;
import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-10-08.
 */

public abstract class Entity implements Collisionable{
    protected Point pos;
    protected float verAngle;
    private float horAngle;
    private Vector3f scale;
    private boolean isVisible = true;
    protected List<ObjectBuilder.DrawCommand> drawList;

    protected boolean isShining = false;
    private float damper = 10;
    private float reflectivity = 1;

    public enum Type{UNCOLOURED, COLOURED, TEXTURED, COMPLEX}
    protected Type type = Type.UNCOLOURED;

    private int textureId;
    protected int color;

    private float targetPositionY;
    private boolean isMoving = false;

    protected Entity(Point pos) {
        this(pos, 0f, new Vector3f(1f, 1f, 1f));
    }
    protected Entity(Point pos, float angle) {
        this(pos, angle, new Vector3f(1f, 1f, 1f));
    }
    protected Entity(Point pos, Vector3f scale) {
        this(pos, 0f, scale);
    }
    protected Entity(Point pos, float angle, Vector3f scale, int textureId) {
        this(pos, angle, scale);
        this.textureId = textureId;
    }
    protected Entity(Point pos, float angle, Vector3f scale) {
        this.pos = pos;
        this.verAngle = angle;
        this.horAngle = 0;
        this.scale = scale;
        drawList = new ArrayList<>();
        initObjectProperties();
    }

    protected abstract void initObjectProperties();

    public void moveToY(float aimY){
        isMoving = true;
        targetPositionY = aimY;
    }

    public void move() {

        long time = SystemClock.currentThreadTimeMillis();

        float x = 3*(float) Math.sin((float)time / 1000);
        float z = 3*(float) Math.cos((float)time / 1000) - 3;

        this.pos = new Point(x, pos.y, z);
    }
    public void setPos(Point p){
        this.pos = p;
    }
    public void move2() {

        long time = SystemClock.currentThreadTimeMillis();
        float x = 3*(float) Math.sin((float)time/1000f);
        this.pos = new Point(x, pos.y, pos.z);
    }
    public void rotate(float angleInSeconds) {
        this.verAngle+= TimeManager.getDeltaTimeInSeconds() * angleInSeconds;
    }
    public void horRotate(float angleInSeconds) {
        this.horAngle+= TimeManager.getDeltaTimeInSeconds() * angleInSeconds;
    }
    public void singleHorRotate(float angleInDeg) {
        this.horAngle+=angleInDeg;
    }
    public void singleVerRotate(float angleInDeg) { this.verAngle+=angleInDeg; }
    abstract public void bindData(ShaderProgram shaderProgram);
    abstract public void draw();

    public Point getPos() {
        return pos;
    }
    public float getVerRotation() { return verAngle; }
    public float getHorRotation() { return horAngle; }
    public void setVerRotation(float rotation) { this.verAngle = rotation; }
    public void setHorRotation(float rotation) { this.horAngle = rotation; }
    public Vector3f getScale() { return scale; }

    public void setShining(boolean shining) {
        isShining = shining;
    }
    public void setDamper(float damper) {
        this.damper = damper;
    }
    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public boolean isShining() {
        return isShining;
    }
    public float getDamper() {
        return damper;
    }
    public float getReflectivity() {
        return reflectivity;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
    public int getColor() {
        return color;
    }
    public int getTextureId(){ return textureId; }
    public void setColor(int color) {
        this.color = color;
    }
    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
    public boolean isVisible() {
        return isVisible;
    }
    public void onCollisionNotify() {}
    public void update(){
        if(!isMoving)
            return;

        boolean reached = false;
        float trans = TimeManager.getDeltaTimeInSeconds();
        float nextY;

            if (targetPositionY - pos.y > 0) {
                nextY = pos.y + trans;
                if (targetPositionY - nextY < 0)
                    reached = true;

            } else {
                nextY = pos.y - trans;
                if (targetPositionY - nextY > 0)
                    reached = true;
            }

        if(reached)
            isMoving = false;

        this.pos = new Point(pos.x, nextY, pos.z);
    }
    @Override
    public CollisionType getCollisionType() {
        return CollisionType.CUBE;
    }
}
