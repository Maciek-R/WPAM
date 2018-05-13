package pl.android.puzzledepartment.managers;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.objects.EntityModel;
import pl.android.puzzledepartment.util.OBJLoader;

/**
 * Created by Maciek Ruszczyk on 2017-10-20.
 */

public class EntityManager {

    private static EntityManager instance = null;

    private Context context;
    private Map<Integer, EntityModel> entitiesModels;

    private EntityManager(Context context) {
        entitiesModels = new HashMap<>();
        this.context = context;
    }

    public static EntityManager getInstance(Context context) {
        if(instance == null) {
            instance = new EntityManager(context);
        }
        return instance;
    }

    public void cleanVBO() {
        for (Integer key : entitiesModels.keySet())
            entitiesModels.get(key).clean();
    }

    public EntityModel getEntityModel(int resourceId) {
        EntityModel entityModel = entitiesModels.get(resourceId);
        if(entityModel != null) {
            return entityModel;
        }
        else {
            entityModel = OBJLoader.loadObjModel(context, resourceId);
            entitiesModels.put(resourceId, entityModel);
            return entityModel;
        }
    }
}
