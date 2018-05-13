package pl.android.puzzledepartment.objects;


import pl.android.puzzledepartment.data.IntegerIndexBuffer;
import pl.android.puzzledepartment.data.VertexBuffer;

/**
 * Created by Maciek Ruszczyk on 2017-10-15.
 */

public class EntityModel {
    private final float[] verticesArray;
    private final float[] normalsArray;
    private final float[] texturesArray;
    final int[] indicesArray;

    private VertexBuffer vertexBuffer = null;
    private IntegerIndexBuffer intIndexBuffer = null;

    public EntityModel(float[] verticesArray, float[] texturesArray, float[] normalsArray, int[] indicesArray) {
        this.verticesArray = verticesArray;
        this.texturesArray = texturesArray;
        this.normalsArray = normalsArray;
        this.indicesArray = indicesArray;
    }

    /**
     * Order: x, y, z, nx, ny, nz
     *
     * @return VertexArray
     */
    public VertexBuffer getNormalVertexBuffer() {
        if(vertexBuffer != null)
            return vertexBuffer;

        float[] vertexData = new float[verticesArray.length + normalsArray.length];

        int verticesOffset = 0;
        int normalsOffset = 0;
        int offset = 0;
        while (verticesOffset < verticesArray.length) {

            vertexData[offset++] = verticesArray[verticesOffset++];
            vertexData[offset++] = verticesArray[verticesOffset++];
            vertexData[offset++] = verticesArray[verticesOffset++];

            vertexData[offset++] = normalsArray[normalsOffset++];
            vertexData[offset++] = normalsArray[normalsOffset++];
            vertexData[offset++] = normalsArray[normalsOffset++];
        }
        vertexBuffer = new VertexBuffer(vertexData);
        return vertexBuffer;
    }

    public IntegerIndexBuffer getIntIndexBuffer() {
        if(intIndexBuffer!=null)
            return intIndexBuffer;

        intIndexBuffer = new IntegerIndexBuffer(indicesArray);
        return intIndexBuffer;
    }

    public int getIndicesArrayLength() {
        return indicesArray.length;
    }

    public void clean(){
        if(vertexBuffer!=null) {
            vertexBuffer.clean();
            vertexBuffer = null;
        }
        if (intIndexBuffer != null) {
            intIndexBuffer.clean();
            intIndexBuffer = null;
        }
    }
}
