package pl.android.puzzledepartment.programs.entity_programs;

import android.content.Context;

import pl.android.puzzledepartment.R;

/**
 * Created by Maciek Ruszczyk on 2017-12-22.
 */

public class EntityTexturedShiningShaderProgram extends EntityShaderProgram {
    public EntityTexturedShiningShaderProgram(Context context) {
        super(context, R.raw.entity_textured_vertex_shader, R.raw.entity_textured_shining_fragment_shader);
    }
}
