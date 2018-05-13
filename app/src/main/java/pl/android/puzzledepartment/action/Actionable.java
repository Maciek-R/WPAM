package pl.android.puzzledepartment.action;

import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-10-28.
 */

public interface Actionable {
    void action();
    Point getPosition();
    Vector3f getScale();
    boolean isInAction();
    void updateAction();
}
