package pl.android.puzzledepartment.objects;

import java.util.ArrayList;
import java.util.List;


import static android.opengl.GLES30.GL_TRIANGLE_FAN;
import static android.opengl.GLES30.GL_TRIANGLE_STRIP;
import static android.opengl.GLES30.glDrawArrays;

/**
 * Created by Maciek Ruszczyk on 2017-10-07.
 */

public class ObjectBuilder {
    interface DrawCommand{
        void draw();
    }
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COORDINATES_COMPONENT_COUNT = 3;
    private static final int NORMAL_COMPONENT_COUNT = 3;
    private static final int FLOATS_PER_VERTEX = POSITION_COMPONENT_COUNT + COLOR_COORDINATES_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT;
    private final float[] vertexData;
    private int offset;
    private final List<DrawCommand> drawList;

    static class GeneratedVertexData{
        final float[] vertexData;
        final List<DrawCommand> drawList;

        GeneratedVertexData(float[] vertexData, List<DrawCommand> drawList) {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }
    }

    private ObjectBuilder(int numOfVertices) {
        vertexData = new float[numOfVertices * FLOATS_PER_VERTEX];
        drawList = new ArrayList<>();
    }
    private static int sizeOfCircleInVertices(int numPoints){
        return numPoints + 2;
    }
    private static int sizeOfWallOfCylinderInVertices(int numPoints){
        return (numPoints + 1)*2;
    }
    private GeneratedVertexData build(){
        return new GeneratedVertexData(vertexData, drawList);
    }

    static GeneratedVertexData createCylinder(int numPoints) {
        int size = sizeOfCircleInVertices(numPoints) * 2 + sizeOfWallOfCylinderInVertices(numPoints);

        ObjectBuilder builder = new ObjectBuilder(size);

        builder.appendCircle(numPoints, false);
        builder.appendCircle(numPoints, true);
        builder.appendWallOfCylinder(numPoints);

        return builder.build();
    }

    private void appendCircle(int numPoints, boolean isTop) {
        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfCircleInVertices(numPoints);
        final float normX = 0.0f, normZ = 0.0f;
        float normY, posY;

        if (isTop) {
            normY = 1.0f;
            posY = 0.5f;
        } else {
            normY = -1.0f;
            posY = -0.5f;
        }

        //vertices
        vertexData[offset++] = 0.0f;
        vertexData[offset++] = posY;
        vertexData[offset++] = 0.0f;
        //normals
        vertexData[offset++] = normX;
        vertexData[offset++] = normY;
        vertexData[offset++] = normZ;
        //colors
        vertexData[offset++] = 0f;
        vertexData[offset++] = 0.5f;
        vertexData[offset++] = 0.5f;

        for(int i=0; i<=numPoints; ++i){
            float angleInRadians = ((float)i / (float)numPoints) * ((float) Math.PI * 2f);
            float ccwAngleInRadians = ((float)(numPoints - i) / (float)numPoints) * ((float) Math.PI * 2f);
            float angleRad;

            if(isTop)
                angleRad = ccwAngleInRadians;
            else
                angleRad = angleInRadians;
            vertexData[offset++] = (float)Math.cos(angleRad);
            vertexData[offset++] = posY;
            vertexData[offset++] = (float)Math.sin(angleRad);

            vertexData[offset++] = normX;
            vertexData[offset++] = normY;
            vertexData[offset++] = normZ;

            vertexData[offset++] = 0f;
            vertexData[offset++] = (float) (Math.cos((double)angleInRadians))/2 + 0.5f;
            vertexData[offset++] = (float) (Math.sin((double)angleInRadians))/2 + 0.5f;
        }
        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices);
            }
        });
    }
    private void appendWallOfCylinder(int numPoints){
        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfWallOfCylinderInVertices(numPoints);
        final float yStart = -0.5f;
        final float yEnd = 0.5f;

        for(int i=0; i<=numPoints; ++i){
            float angleInRadians = ((float)i/(float)numPoints) * ((float) Math.PI*2f);
            float cosinus = (float)Math.cos(angleInRadians);
            float sinus = (float)Math.sin(angleInRadians);

            float xBottomPosition = cosinus;
            float xTopPosition = cosinus;
            float zBottomPosition = sinus;
            float zTopPosition = sinus;

            vertexData[offset++] = xBottomPosition;
            vertexData[offset++] = yStart;
            vertexData[offset++] = zBottomPosition;
            vertexData[offset++] = cosinus;
            vertexData[offset++] = 0f;
            vertexData[offset++] = sinus;
            vertexData[offset++] = 0f;
            vertexData[offset++] = (float) (Math.cos((double)angleInRadians))/2 + 0.5f;
            vertexData[offset++] = (float) (Math.sin((double)angleInRadians))/2 + 0.5f;

            vertexData[offset++] = xTopPosition;
            vertexData[offset++] = yEnd;
            vertexData[offset++] = zTopPosition;
            vertexData[offset++] = cosinus;
            vertexData[offset++] = 0f;
            vertexData[offset++] = sinus;
            vertexData[offset++] = 0f;
            vertexData[offset++] = (float) (Math.cos((double)angleInRadians))/2 + 0.5f;
            vertexData[offset++] = (float) (Math.sin((double)angleInRadians))/2 + 0.5f;
        }
        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);
            }
        });
    }
}
