package pl.android.puzzledepartment.objects;

import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-11-24.
 */

public interface Collisionable {

    enum CollisionType{CUBE, CYLINDER_WALL}
    Point getPos();
    Vector3f getScale();
    CollisionType getCollisionType();
}
