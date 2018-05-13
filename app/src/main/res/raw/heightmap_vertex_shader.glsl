uniform mat4 u_ModelMatrix;
uniform mat4 u_ViewMatrix;
uniform mat4 u_ProjectionMatrix;
attribute vec3 a_Position;
attribute vec2 a_TextureCoordinates;

varying vec3 v_Color;
varying vec2 v_PassTextureCoordinates;

varying float v_Visibility;
const float density = 0.02f;
const float gradient = 6.5f;

void main()
{
    v_PassTextureCoordinates = a_TextureCoordinates;
    vec4 worldPosition = u_ModelMatrix * vec4(a_Position, 1.0);
    vec4 positionRelativeToCamera = u_ViewMatrix * worldPosition;
    gl_Position = u_ProjectionMatrix * positionRelativeToCamera;

    float distanceToCamera = length(positionRelativeToCamera.xyz);
    v_Visibility = exp(-pow((distanceToCamera*density), gradient));
    v_Visibility = clamp(v_Visibility, 0.0f, 1.0f);
}