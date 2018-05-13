uniform mat4 u_ModelMatrix;
uniform mat4 u_ViewMatrix;
uniform mat4 u_ProjectionMatrix;
uniform vec4 u_Color;

attribute vec4 a_Position;

varying vec4 v_Color;
varying float v_Visibility;
const float density = 0.02f;
const float gradient = 6.5f;

void main()
{
    v_Color = u_Color;
    vec4 worldPosition = u_ModelMatrix * a_Position;
    vec4 positionRelativeToCamera = u_ViewMatrix * worldPosition;
    gl_Position = u_ProjectionMatrix * positionRelativeToCamera;

    float distanceToCamera = length(positionRelativeToCamera.xyz);
    v_Visibility = exp(-pow((distanceToCamera*density), gradient));
    v_Visibility = clamp(v_Visibility, 0.0f, 1.0f);
}