precision mediump float;

uniform sampler2D u_BackgroundTextureUnit;
uniform sampler2D u_RedTextureUnit;
uniform sampler2D u_GreenTextureUnit;
uniform sampler2D u_BlueTextureUnit;
uniform sampler2D u_BlendMapTextureUnit;
uniform vec3 u_SkyColour;

varying vec3 v_Color;
varying vec2 v_PassTextureCoordinates;
varying float v_Visibility;

void main()
{
    vec2 tiledTextureCoordinates = v_PassTextureCoordinates * 32.0;

    vec4 blendTextureColour = texture2D(u_BlendMapTextureUnit, v_PassTextureCoordinates, 2.0f);

    float restColour = 1.0f - (blendTextureColour.r + blendTextureColour.g + blendTextureColour.b);

    vec4 background = texture2D(u_BackgroundTextureUnit, tiledTextureCoordinates, 2.0f) * restColour;
    vec4 redTexture = texture2D(u_RedTextureUnit, tiledTextureCoordinates, 2.0f) * blendTextureColour.r;
    vec4 greenTexture = texture2D(u_GreenTextureUnit, tiledTextureCoordinates, 2.0f) * blendTextureColour.g;
    vec4 blueTexture = texture2D(u_BlueTextureUnit, tiledTextureCoordinates, 2.0f) * blendTextureColour.b;

    vec4 totalColour = background + redTexture + greenTexture + blueTexture;

    gl_FragColor = mix(vec4(u_SkyColour, 1.0), totalColour, v_Visibility);
}