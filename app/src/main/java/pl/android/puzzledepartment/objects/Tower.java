package pl.android.puzzledepartment.objects;

import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;


/**
 * Created by Maciek Ruszczyk on 2017-12-17.
 */

public class Tower extends ModelEntity{

    public Tower(Point pos, int color, EntityModel entityModel) {
        this(pos, color, entityModel, new Vector3f(1f, 1f, 1f));
    }

    public Tower(Point pos, int color, EntityModel entityModel, Vector3f scale) {
        super(pos, 0.0f, new Vector3f(0.5f*scale.x, 0.5f*scale.y, 0.5f*scale.z), entityModel);
        this.color = color;
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
