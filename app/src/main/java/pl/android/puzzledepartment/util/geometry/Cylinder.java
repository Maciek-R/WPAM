package pl.android.puzzledepartment.util.geometry;

/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public class Cylinder {
    public final Circle circle;
    public final float height;

    public Cylinder(Circle circle, float height){
        this.circle = circle;
        this.height = height;
    }
}
