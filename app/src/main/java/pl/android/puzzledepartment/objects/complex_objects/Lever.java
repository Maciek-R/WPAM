package pl.android.puzzledepartment.objects.complex_objects;

import android.graphics.Color;

import pl.android.puzzledepartment.action.Actionable;
import pl.android.puzzledepartment.objects.EntityModel;
import pl.android.puzzledepartment.objects.LeverBase;
import pl.android.puzzledepartment.objects.LeverHandle;
import pl.android.puzzledepartment.objects.SimpleColorShaderCube;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;


/**
 * Created by Maciek Ruszczyk on 2017-11-26.
 */

public class Lever extends ComplexEntity implements Actionable{
    public enum Direction{LEFT, RIGHT}
    private Direction direction = Direction.LEFT;

    private Vector3f scale;

    private LeverBase leverBase;
    private LeverHandle leverHandle;
    private final SimpleColorShaderCube simpleColorShaderCube;

    private boolean isInAction = false;
    private float targetRotation;

    public Lever(Point pos, EntityModel leverBaseModel, EntityModel leverHandModel, SimpleColorShaderCube simpleColorShaderCube) {
        this(pos, leverBaseModel, leverHandModel, new Vector3f(1f, 1f, 1f), simpleColorShaderCube);
    }

    public Lever(Point pos, EntityModel leverBaseModel, EntityModel leverHandModel, Vector3f scale, SimpleColorShaderCube simpleColorShaderCube) {
        super(pos);
        this.scale = scale;
        this.simpleColorShaderCube = simpleColorShaderCube;

        leverBase = new LeverBase(pos, Color.GREEN, leverBaseModel);
        leverHandle = new LeverHandle(pos, Color.GREEN, leverHandModel);
        leverHandle.singleHorRotate(30f);
        leverHandle.singleVerRotate(90f);

        add(leverBase);
        add(leverHandle);
    }

    public LeverBase getLeverBase() {
        return leverBase;
    }

    public LeverHandle getLeverHandle() {
        return leverHandle;
    }

    @Override
    public void action() {
        isInAction = true;
        if(Direction.LEFT.equals(direction))
            targetRotation = -30f;
        else
            targetRotation = 30f;
    }

    @Override
    public Point getPosition() {
        return pos;
    }

    @Override
    public Vector3f getScale() {
        return scale;
    }

    @Override
    public boolean isInAction() {
        return isInAction;
    }

    @Override
    public void updateAction() {
        if(Direction.LEFT.equals(direction)) {
            leverHandle.horRotate(-60f);
            if(leverHandle.getHorRotation() < targetRotation)
                changeState();
        }
        else {
            leverHandle.horRotate(60f);
            if(leverHandle.getHorRotation() > targetRotation)
                changeState();
        }
    }

    private void changeState() {
        leverHandle.setHorRotation(targetRotation);
        isInAction = false;
        simpleColorShaderCube.setNextColor();
        setNextDirection();
    }

    private void setNextDirection() {
        switch(direction){
            case RIGHT: direction = Direction.LEFT; break;
            case LEFT: direction = Direction.RIGHT; break;
        }
    }
}
