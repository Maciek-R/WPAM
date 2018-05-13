package pl.android.puzzledepartment.objects.complex_objects;

import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.objects.Cube;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-10-20.
 */

public class Room extends ComplexEntity{

    private static final float SPACE = 1.0f;
    private List<Entity> entities;

    public Room(Point center, float size, float height) {
        super(center);
        entities = new ArrayList<>();

        createWall(new Point(0.0f, -0.5f, 0.0f), new Vector3f(size*2+1f, 0.5f, size*2+1f));

        createWall(new Point(-size, 0.0f, 0.0f), new Vector3f(1.0f, height, size*2-1f));
        createWall(new Point(size, 0.0f, 0.0f), new Vector3f(1.0f, height, size*2-1f));
        createWall(new Point(0.0f, 0.0f, size), new Vector3f(size*2+1f, height, 1.0f));
        createWall(new Point(0.0f, 0.0f, -size), new Vector3f(size*2+1f, height, 1.0f));
    }

    private void createWall(Point point, Vector3f scale) {
        Cube cube = new Cube(new Point(point.x + pos.x, point.y + pos.y, point.z + pos.z), scale);
        entities.add(cube);
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
