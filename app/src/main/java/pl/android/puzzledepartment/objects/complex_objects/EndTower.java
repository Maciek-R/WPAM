package pl.android.puzzledepartment.objects.complex_objects;

import android.graphics.Color;

import pl.android.puzzledepartment.action.Actionable;
import pl.android.puzzledepartment.managers.GameManager;
import pl.android.puzzledepartment.objects.Collisionable;
import pl.android.puzzledepartment.objects.Door;
import pl.android.puzzledepartment.objects.EntityModel;
import pl.android.puzzledepartment.objects.Tower;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;


/**
 * Created by Maciek Ruszczyk on 2017-12-17.
 */

public class EndTower extends ComplexEntity implements Actionable, Collisionable{
    private GameManager gameManager;

    private Vector3f scale;

    private Tower tower;
    private Door door;

    private boolean isInAction = false;
    private float targetRotation;

    private boolean isDoorOpen = false;

    public EndTower(Point pos, EntityModel towerModel, EntityModel doorModel) {
        super(pos);
        this.scale = new Vector3f(3.1f, 30f, 3.1f);
        this.tower = new Tower(pos, Color.GREEN, towerModel);
        this.door = new Door(new Point(pos.x+2.41f, pos.y, pos.z+0.62f), Color.RED, doorModel);

        add(tower);
        add(door);
    }

    public void addObserver(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public Tower getTower(){
        return tower;
    }
    public Door getDoor(){
        return door;
    }

    public boolean isDoorOpen() {
        return isDoorOpen;
    }

    @Override
    public void action() {
        if(gameManager.isAllKeyTaken()) {
            isInAction = true;
            targetRotation = door.getVerRotation() + 90f;
        }
        else
            gameManager.notEnoughKeysMessage();
    }

    @Override
    public void updateAction() {
        if(isDoorOpen)
            return;
        door.rotate(30f);
        if(door.getVerRotation() > targetRotation) {
            door.setVerRotation(targetRotation);
            isDoorOpen = true;
        }
    }

    @Override
    public Point getPosition() {
        return door.getPos();
    }

    @Override
    public Point getPos() {
        return pos;
    }

    @Override
    public Vector3f getScale() {
        return scale;
    }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.CYLINDER_WALL;
    }

    @Override
    public boolean isInAction() {
        return isInAction;
    }
}
