package pl.android.puzzledepartment.programs.entity_programs;

import android.content.Context;

import pl.android.puzzledepartment.R;

/**
 * Created by Maciek Ruszczyk on 2017-11-18.
 */

public class EntityColouredShiningShaderProgram extends EntityShaderProgram {
    public EntityColouredShiningShaderProgram(Context context) {
        super(context, R.raw.entity_coloured_vertex_shader, R.raw.entity_shining_fragment_shader);
    }
}
