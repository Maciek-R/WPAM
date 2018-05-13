precision mediump float;

uniform vec3 u_SkyColour;

varying vec4 v_Color;
varying float v_Visibility;

void main()
{
    gl_FragColor = mix(vec4(u_SkyColour, 1.0), v_Color, v_Visibility);
}