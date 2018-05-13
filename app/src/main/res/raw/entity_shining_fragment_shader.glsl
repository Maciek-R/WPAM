precision mediump float;

uniform vec3 u_CameraPos;
uniform vec3 u_LightColor;

uniform float u_Damper;
uniform float u_Reflectivity;
uniform float u_IsShining;
uniform vec3 u_SkyColour;

varying vec4 v_Color;
varying vec3 v_Normal;
varying vec3 v_ToLightDir;
varying vec3 v_ToCameraDir;
varying float v_Visibility;

void main()
{
    vec3 unitNormal = normalize(v_Normal);
    vec3 unitToLightDir = normalize(v_ToLightDir);
    vec3 unitToCameraDir = normalize(v_ToCameraDir);

    float diffuseFactor = max(dot(unitNormal, unitToLightDir), 0.0);
    vec3 diffuse = diffuseFactor * u_LightColor;

    vec3 lightDir = -unitToLightDir;
    vec3 reflectedLightDirection = reflect(lightDir, unitNormal);

    float specularFactor = max(dot(reflectedLightDirection, unitToCameraDir), 0.0);
    float damperFactor = pow(specularFactor, u_Damper);
    vec3 finalSpecular = damperFactor * u_Reflectivity * u_LightColor;

    vec4 totalColour = v_Color * vec4(diffuse + vec3(0.3f, 0.3f, 0.3f), 1.0) + vec4(finalSpecular, 1.0);
    gl_FragColor = mix(vec4(u_SkyColour, 1.0), totalColour, v_Visibility);
}