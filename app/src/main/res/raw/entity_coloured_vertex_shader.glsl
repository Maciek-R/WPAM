uniform mat4 u_ModelMatrix;
uniform mat4 u_ViewMatrix;
uniform mat4 u_ProjectionMatrix;
uniform mat4 u_IT_ModelMatrix;
uniform vec4 u_Color;
uniform vec3 u_LightPos;
uniform vec3 u_CameraPos;
uniform float u_Type;

attribute vec4 a_Position;
attribute vec4 a_Normal;
attribute vec4 a_Color;

varying vec4 v_Color;
varying vec3 v_Normal;
varying vec3 v_ToLightDir;
varying vec3 v_ToCameraDir;

varying float v_Visibility;
const float density = 0.02f;
const float gradient = 6.5f;

void main()
{
    vec4 worldPosition = u_ModelMatrix * a_Position;
    vec4 positionRelativeToCamera = u_ViewMatrix * worldPosition;
    v_Normal = vec3(u_IT_ModelMatrix * a_Normal);
    v_ToLightDir = u_LightPos - worldPosition.xyz;
    v_ToCameraDir = u_CameraPos - worldPosition.xyz;

    gl_Position = u_ProjectionMatrix * positionRelativeToCamera;
    v_Color = a_Color;

    float distanceToCamera = length(positionRelativeToCamera.xyz);
    v_Visibility = exp(-pow((distanceToCamera*density), gradient));
    v_Visibility = clamp(v_Visibility, 0.0f, 1.0f);
}
