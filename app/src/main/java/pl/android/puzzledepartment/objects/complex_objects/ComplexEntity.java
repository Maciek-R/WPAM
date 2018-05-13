package pl.android.puzzledepartment.objects.complex_objects;

import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.util.geometry.Point;

/**
 * Created by Maciek Ruszczyk on 2018-01-10.
 */

public class ComplexEntity {

    protected Point pos;
    private List<Entity> entities = new ArrayList<>();

    public ComplexEntity(Point pos) {
        this.pos = pos;
    }

    public void add(Entity entity) {
        entities.add(entity);
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
