package pl.android.puzzledepartment.objects;

import pl.android.puzzledepartment.action.Actionable;
import pl.android.puzzledepartment.gui.GuiEntity;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;


/**
 * Created by Maciek Ruszczyk on 2017-12-21.
 */

public class Tip extends ModelEntity implements Actionable, Collisionable{

    private GuiEntity guiEntity;

    public Tip(int color, EntityModel entityModel, GuiEntity guiEntity) {
        this(new Point(0f, 0f, 0f), color, entityModel, new Vector3f(1f, 1f, 1f), guiEntity);
    }

    public Tip(Point pos, int color, EntityModel entityModel, GuiEntity guiEntity) {
        this(pos, color, entityModel, new Vector3f(1f, 1f, 1f), guiEntity);
    }

    public Tip(Point pos, int color, EntityModel entityModel, Vector3f scale, GuiEntity guiEntity) {
        super(pos, 0.0f, scale, entityModel);
        this.color = color;
        this.guiEntity = guiEntity;
    }

    @Override
    protected void initObjectProperties() {
        type = Entity.Type.UNCOLOURED;
        isShining = true;
    }

    @Override
    public void action() {
        guiEntity.setVisibleForFewSeconds(3f);
    }

    @Override
    public Point getPosition() {
        return pos;
    }

    @Override
    public boolean isInAction() {
        return false;
    }

    @Override
    public void updateAction() {
    }
}
