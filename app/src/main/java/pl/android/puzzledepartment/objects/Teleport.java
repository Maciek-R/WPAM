package pl.android.puzzledepartment.objects;

import pl.android.puzzledepartment.util.geometry.Point;

/**
 * Created by Maciek Ruszczyk on 2017-12-28.
 */

public class Teleport extends Cylinder{
    private Point teleportPlace;

    public Teleport(Point point, Point teleportPlace) {
        super(point);
        this.teleportPlace = teleportPlace;
    }

    public Point getTeleportPlace(){
        return teleportPlace;
    }
}
