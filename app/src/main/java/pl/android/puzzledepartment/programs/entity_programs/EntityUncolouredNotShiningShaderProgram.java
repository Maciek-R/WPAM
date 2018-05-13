package pl.android.puzzledepartment.programs.entity_programs;

import android.content.Context;

import pl.android.puzzledepartment.R;

/**
 * Created by Maciek Ruszczyk on 2017-11-18.
 */

public class EntityUncolouredNotShiningShaderProgram extends EntityShaderProgram {
    public EntityUncolouredNotShiningShaderProgram(Context context) {
        super(context, R.raw.entity_uncoloured_vertex_shader, R.raw.entity_not_shining_fragment_shader);
    }
}
