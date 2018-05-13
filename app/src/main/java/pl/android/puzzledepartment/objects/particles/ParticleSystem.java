package pl.android.puzzledepartment.objects.particles;

import android.graphics.Color;

import pl.android.puzzledepartment.data.VertexArray;
import pl.android.puzzledepartment.programs.ShaderProgram;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static pl.android.puzzledepartment.util.Constants.BYTES_PER_FLOAT;

/**
 * Created by Maciek Ruszczyk on 2017-10-21.
 */

public class ParticleSystem {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int VECTOR_COMPONENT_COUNT = 3;
    private static final int PARTICLE_START_TIME_COMPONENT_COUNT = 1;
    private static final int TOTAL_COMPONENT_COUNT =
            POSITION_COMPONENT_COUNT +
                    COLOR_COMPONENT_COUNT +
                    VECTOR_COMPONENT_COUNT +
                    PARTICLE_START_TIME_COMPONENT_COUNT;
    private static final int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;

    private final float[] particles;
    private final VertexArray vertexArray;
    private final int maxNumberOfParticles;
    private int currentParticleCount;
    private int nextParticle;
    private int particleTexture;

    public ParticleSystem(int maxNumberOfParticles, int particleTexture) {
        particles = new float[maxNumberOfParticles * TOTAL_COMPONENT_COUNT];
        vertexArray = new VertexArray(particles);
        this.maxNumberOfParticles = maxNumberOfParticles;
        this.particleTexture = particleTexture;
    }

    public void addParticle(Point pos, int color, Vector3f direction, float particleStartTime) {

        final int particleOffset = nextParticle * TOTAL_COMPONENT_COUNT;
        int offset = particleOffset;
        nextParticle++;
        if(currentParticleCount < maxNumberOfParticles)
            currentParticleCount++;
        if(nextParticle == maxNumberOfParticles)
            nextParticle = 0;

        particles[offset++] = pos.x;
        particles[offset++] = pos.y;
        particles[offset++] = pos.z;
        particles[offset++] = Color.red(color) / 255f;
        particles[offset++] = Color.green(color) / 255f;
        particles[offset++] = Color.blue(color) / 255f;
        particles[offset++] = direction.x;
        particles[offset++] = direction.y;
        particles[offset++] = direction.z;
        particles[offset++] = particleStartTime;

        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COMPONENT_COUNT);
    }

    public void bindData(ShaderProgram particleShaderProgram) {
        int offset = 0;
        vertexArray.setVertexAttribPointer(offset, particleShaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        offset += POSITION_COMPONENT_COUNT;
        vertexArray.setVertexAttribPointer(offset, particleShaderProgram.getColorAttributeLocation(), COLOR_COMPONENT_COUNT, STRIDE);
        offset += COLOR_COMPONENT_COUNT;
        vertexArray.setVertexAttribPointer(offset, particleShaderProgram.getDirectionVectorAttributeLocation(), VECTOR_COMPONENT_COUNT, STRIDE);
        offset += VECTOR_COMPONENT_COUNT;
        vertexArray.setVertexAttribPointer(offset, particleShaderProgram.getParticleStartTimeAttributeLocation(), PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE);
    }
    public void draw() {
        glDrawArrays(GL_POINTS, 0, currentParticleCount);
    }

    public int getTexture() {
        return particleTexture;
    }
}
