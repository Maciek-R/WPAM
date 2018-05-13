package pl.android.puzzledepartment.objects.particles;

import pl.android.puzzledepartment.objects.Collisionable;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-11-24.
 */

public class ParticleCollideShooter extends ParticleShooter implements Collisionable{
    private Vector3f scale;

    public ParticleCollideShooter(Point pos, Vector3f direction, int color, float angleVariance, float speedVariance) {
        super(pos, direction, color, angleVariance, speedVariance);
        this.scale = new Vector3f(1.0f, 1.0f, 1.0f);
    }

    public Point getPos() {
        return pos;
    }
    public Vector3f getScale() {
        return scale;
    }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.CUBE;
    }
}
