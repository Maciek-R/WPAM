package pl.android.puzzledepartment.objects;

import android.graphics.Color;

import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;


/**
 * Created by Maciek Ruszczyk on 2017-10-15.
 */

public class Dragon extends ModelEntity{

    public Dragon(Point pos, EntityModel entityModel) {
        this(pos, entityModel, new Vector3f(1f, 1f, 1f));
    }

    public Dragon(Point pos, EntityModel entityModel, Vector3f scale) {
        super(pos, 0.0f, new Vector3f(0.25f * scale.x, 0.25f * scale.y, 0.25f * scale.z), entityModel);
        this.color = Color.rgb(255, 0, 0);
    }

    @Override
    protected void initObjectProperties(){
        type = Type.UNCOLOURED;
        isShining = true;
    }
}
