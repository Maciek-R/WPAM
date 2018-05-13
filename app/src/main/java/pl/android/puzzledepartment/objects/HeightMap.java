package pl.android.puzzledepartment.objects;

import android.graphics.Bitmap;
import android.graphics.Color;

import pl.android.puzzledepartment.data.IndexBuffer;
import pl.android.puzzledepartment.data.VertexBuffer;
import pl.android.puzzledepartment.programs.HeightmapShaderProgram;
import pl.android.puzzledepartment.util.geometry.Maths;
import pl.android.puzzledepartment.util.geometry.Vector3f;
import pl.android.puzzledepartment.util.geometry.Vector2f;

import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glDrawElements;

/**
 * Created by Maciek Ruszczyk on 2017-10-07.
 */

public class HeightMap {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int TEXTURE_COMPONENT_COUNT = 2;

    private final int width;
    private final int height;
    private final int numElements;
    private final VertexBuffer vertexBuffer;
    private final VertexBuffer vertexTextureBuffer;
    private final IndexBuffer indexBuffer;
    private float[][] heights;
    private final Vector3f scale;
    private final TerrainTexturePack terrainTexturePack;
    private final TerrainTexture blendMap;

    public HeightMap(Bitmap bitmap, Vector3f scale, TerrainTexturePack terrainTexturePack, TerrainTexture blendMap) {
        this.terrainTexturePack = terrainTexturePack;
        this.blendMap = blendMap;
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        this.scale = scale;

        if (width * height > 65536)
            throw new RuntimeException("Heightmap is too large for indexBuffer.");

        heights = new float[height][width];
        numElements = calculateNumElements();
        vertexBuffer = new VertexBuffer(loadBitmapData(bitmap));
        vertexTextureBuffer = new VertexBuffer(loadTextureData());
        indexBuffer = new IndexBuffer(createIndexData());
    }

    public void bindData(HeightmapShaderProgram heightmapShaderProgram) {
        vertexBuffer.setVertexAttribPointer(0, heightmapShaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, 0);
        vertexTextureBuffer.setVertexAttribPointer(0, heightmapShaderProgram.getTextureCoordsAttributeLocation(), TEXTURE_COMPONENT_COUNT, 0);
    }

    public void draw() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer.getBufferId());
        glDrawElements(GL_TRIANGLES, numElements, GL_UNSIGNED_SHORT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private short[] createIndexData() {
        final short[] indexData = new short[numElements];
        int offset = 0;

        for(int row = 0; row < height - 1; ++row) {
            for(int col = 0; col < width - 1; ++col) {
                short topLeftIndex = (short) (row*width + col);
                short topRightIndex = (short) (row*width + col + 1);
                short bottomLeftIndex = (short) ((row+1)*width + col);
                short bottomRightIndex = (short) ((row+1)*width + col + 1);

                indexData[offset++] = topLeftIndex;
                indexData[offset++] = bottomLeftIndex;
                indexData[offset++] = topRightIndex;

                indexData[offset++] = topRightIndex;
                indexData[offset++] = bottomLeftIndex;
                indexData[offset++] = bottomRightIndex;
            }
        }
        return indexData;
    }

    private int calculateNumElements() {
        return (width - 1) * (height - 1) * 6;
    }

    private float[] loadBitmapData(Bitmap bitmap) {
        final int[] pixels = new int[height * width];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        bitmap.recycle();

        final float[] heightMapVertices = new float[width * height * POSITION_COMPONENT_COUNT];
        int offset = 0;
        for(int row = 0; row < height; ++row) {
            for(int col = 0; col < width; ++col) {
                final float xPosition = ((float)col / (float)(width - 1)) - 0.5f;
                final float yPosition = (float) Color.red(pixels[row*width + col]) / (float)255;
                final float zPosition = ((float)row / (float)(height - 1)) - 0.5f;

                heightMapVertices[offset++] = xPosition;
                heightMapVertices[offset++] = yPosition;
                heightMapVertices[offset++] = zPosition;
                heights[row][col] = yPosition;
            }
        }
        return heightMapVertices;
    }

    private float[] loadTextureData() {
        final float[] textureCoords = new float[width * height * TEXTURE_COMPONENT_COUNT];
        int offset = 0;

        for(int row = 0; row < height; ++row) {
            for(int col = 0; col < width; ++col) {
                textureCoords[offset++] = (float)col/((float)width - 1);
                textureCoords[offset++] = (float)row/((float)height - 1);
            }
        }
        return textureCoords;
    }

    public float getHeight(float worldX, float worldZ){
        float terrainX = worldX + scale.x;
        float terrainZ = worldZ + scale.z;

        float gridXSquareSize = scale.x / ((float)width-1);
        float gridZSquareSize = scale.z / ((float)height-1);

        int gridX = (int) Math.floor(worldX / gridXSquareSize) + ((width-1) / 2);
        int gridZ = (int) Math.floor(worldZ / gridZSquareSize) + ((height-1) / 2);

        if(gridX >= width-1 || gridZ >= height-1 || gridX < 0 || gridZ < 0){
            return 0;
        }

        float xCoord = (terrainX % gridXSquareSize) / gridXSquareSize;
        float zCoord = (terrainZ % gridZSquareSize) / gridZSquareSize;

        float height;
        if (xCoord <= (1-zCoord)) {
            height = Maths
                    .barryCentric(new Vector3f(0, heights[gridZ][gridX], 0), new Vector3f(1,
                            heights[gridZ][gridX + 1], 0), new Vector3f(0,
                            heights[gridZ + 1][gridX], 1), new Vector2f(xCoord, zCoord));
        } else {
            height = Maths
                    .barryCentric(new Vector3f(1, heights[gridZ][gridX + 1], 0), new Vector3f(1,
                            heights[gridZ + 1][gridX + 1], 1), new Vector3f(0,
                            heights[gridZ + 1][gridX], 1), new Vector2f(xCoord, zCoord));
        }
        return height*scale.y;
    }
    public Vector3f getScale() {
        return scale;
    }

    public TerrainTexturePack getTerrainTexturePack() {
        return terrainTexturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }
}
