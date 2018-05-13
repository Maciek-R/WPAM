precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 textureCoords;

void main()
{
    gl_FragColor = texture2D(u_TextureUnit, textureCoords);
}