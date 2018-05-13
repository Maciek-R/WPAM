package pl.android.puzzledepartment.programs.color_programs;

import android.content.Context;

import pl.android.puzzledepartment.R;

import static android.opengl.GLES30.glGetAttribLocation;

/**
 * Created by Maciek Ruszczyk on 2017-11-19.
 */

public class AttributeColorShaderProgram extends ColorShaderProgram {
    private final int aColorLocation;

    public AttributeColorShaderProgram(Context context) {
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);

        aColorLocation = glGetAttribLocation(program, A_COLOR);
    }

    @Override
    public void loadColour(int color) {
    }

    @Override
    public int getColorAttributeLocation() {
        return aColorLocation;
    }
}
