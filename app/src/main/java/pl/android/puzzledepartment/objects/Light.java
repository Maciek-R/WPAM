package pl.android.puzzledepartment.objects;

import pl.android.puzzledepartment.util.geometry.Point;

/**
 * Created by Maciek Ruszczyk on 2017-10-13.
 */

public class Light extends Cube
{
    public Light(Point pos, int lightColor) {
        super(pos);
        this.color = lightColor;
    }

    @Override
    protected void initObjectProperties() {
        this.type = Type.UNCOLOURED;
    }
}
