package pl.android.puzzledepartment.objects;

import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;


/**
 * Created by Maciek Ruszczyk on 2017-12-17.
 */

public class Door extends ModelEntity{

    public Door(Point pos, int color, EntityModel entityModel) {
        this(pos, color, entityModel, new Vector3f(1.0f, 1.74f, 1.8f));
    }

    public Door(Point pos, int color, EntityModel entityModel, Vector3f scale) {
        super(pos, 0.0f, scale, entityModel);
        this.color = color;
        verAngle-=9f;
    }

    public void update(){
    }

    public void onCollisionNotify() {
    }

    @Override
    protected void initObjectProperties() {
        type = Entity.Type.UNCOLOURED;
        isShining = true;
    }
}
