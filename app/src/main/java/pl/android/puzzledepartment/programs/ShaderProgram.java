package pl.android.puzzledepartment.programs;

import android.content.Context;

import pl.android.puzzledepartment.util.ShaderHelper;
import pl.android.puzzledepartment.util.TextResourceReader;

import static android.opengl.GLES30.glUseProgram;

/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */


public abstract class ShaderProgram {
    //Uniform
    protected static final String U_MODEL_MATRIX = "u_ModelMatrix";
    protected static final String U_VIEW_MATRIX = "u_ViewMatrix";
    protected static final String U_PROJECTION_MATRIX = "u_ProjectionMatrix";
    protected static final String U_IT_MODEL_VIEW_MATRIX = "u_IT_ModelMatrix";

    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_BACKGROUND_TEXTURE_UNIT = "u_BackgroundTextureUnit";
    protected static final String U_RED_TEXTURE_UNIT = "u_RedTextureUnit";
    protected static final String U_GREEN_TEXTURE_UNIT = "u_GreenTextureUnit";
    protected static final String U_BLUE_TEXTURE_UNIT = "u_BlueTextureUnit";
    protected static final String U_BLENDMAP_TEXTURE_UNIT = "u_BlendMapTextureUnit";

    protected static final String U_COLOR = "u_Color";
    protected static final String U_CAMERA_POS = "u_CameraPos";
    protected static final String U_LIGHT_POS = "u_LightPos";
    protected static final String U_LIGHT_COLOR = "u_LightColor";
    protected static final String U_TIME = "u_Time";
    protected static final String U_DAMPER = "u_Damper";
    protected static final String U_REFLECTIVITY = "u_Reflectivity";
    protected static final String U_IS_SHINING = "u_IsShining";
    protected static final String U_SKY_COLOUR = "u_SkyColour";

    //Attribute
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_NORMAL = "a_Normal";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    protected static final String A_DIRECTION_VECTOR = "a_DirectionVector";
    protected static final String A_PARTICLE_START_TIME = "a_ParticleStartTime";
    protected final int program;

    protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {

        program = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId));
    }
    public void useProgram() {
        glUseProgram(program);
    }
    public void stopProgram() {glUseProgram(0);}

    public int getPositionAttributeLocation() {
       return -1;
    }
    public int getColorAttributeLocation() { return -1;}
    public int getNormalAttributeLocation() { return -1; }
    public int getTextureCoordsAttributeLocation() { return -1; }
    public int getDirectionVectorAttributeLocation() { return -1; }
    public int getParticleStartTimeAttributeLocation() {
        return -1;
    }
}
