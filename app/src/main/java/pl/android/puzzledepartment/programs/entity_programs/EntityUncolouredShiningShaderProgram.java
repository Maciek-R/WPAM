package pl.android.puzzledepartment.programs.entity_programs;

import android.content.Context;

import pl.android.puzzledepartment.R;

/**
 * Created by Maciek Ruszczyk on 2017-11-18.
 */

public class EntityUncolouredShiningShaderProgram extends EntityShaderProgram {
    public EntityUncolouredShiningShaderProgram(Context context) {
        super(context, R.raw.entity_uncoloured_vertex_shader, R.raw.entity_shining_fragment_shader);
    }
}
