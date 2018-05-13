package pl.android.puzzledepartment.puzzles;

import android.graphics.Color;

import java.util.Random;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.managers.TextureManager;
import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.objects.Tip;
import pl.android.puzzledepartment.objects.particles.ParticleShooter;
import pl.android.puzzledepartment.objects.particles.ParticleSystem;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;


/**
 * Created by Maciek Ruszczyk on 2017-11-25.
 */

public class ParticlesWalkPuzzle extends AbstractPuzzle{

    public final static float MAX_DISTANCE = 1500f;

    private ParticleSystem particleSystem;
    private ParticleShooter particleShooter;

    private Camera camera;
    private final static float MAX_CAMERA_SPEED = 0.02f;
    private final static int COLOR_NUMBERS = 3;
    private int colors[];
    private final Random random;

    public ParticlesWalkPuzzle(TextureManager textureManager, Point pos, int particleTexture, Camera camera, Tip tip) {
        super(textureManager, pos, tip);
        this.camera = camera;
        particleSystem = new ParticleSystem(10000, particleTexture);
        random = new Random();
        colors = new int[COLOR_NUMBERS];
        colors[0] = Color.rgb(255, 50, 5);
        colors[1] = Color.rgb(50, 205, 10);
        colors[2] = Color.rgb(10, 50, 255);
        particleShooter = new ParticleShooter(new Point(pos.x, pos.y, pos.z), new Vector3f(0f, 0.5f, 0f), colors[0], 360f, 1.5f);
    }

    @Override
    public void update(float elapsedTime) {
        particleShooter.changeColor(colors[random.nextInt(COLOR_NUMBERS)]);
        //Log.v("DELTAX", new Float(Math.abs(camera.getDeltaX() + camera.getDeltaZ())).toString());
        if (Math.abs(camera.getDeltaX() + camera.getDeltaZ() + camera.getFlySpeed()) > MAX_CAMERA_SPEED)
            particleShooter.increaseSpeedMultiplier();
        else
            particleShooter.decreaseSpeedMultiplier();

        boolean calm = particleShooter.areParticleCalm();
        if(calm && isCameraClose())
            isCompleted = true;
        particleShooter.addParticles(particleSystem, elapsedTime, 5);
    }

    private boolean isCameraClose() {
        return (camera.getPosX()-pos.x)*(camera.getPosX()-pos.x)+
                (camera.getPosY()-pos.y)*(camera.getPosY()-pos.y)+
                (camera.getPosZ()-pos.z)*(camera.getPosZ()-pos.z) <= MAX_DISTANCE;
    }

    public ParticleShooter getParticleShooter() {return particleShooter;}

    public ParticleSystem getParticleSystem() {
        return particleSystem;
    }

    @Override
    public Point getKeySpawnPosition() {
        return new Point(pos.x, pos.y, pos.z);
    }

    @Override
    public Point getTipPosition() {
        return new Point(pos.x+2f, pos.y-2f, pos.z+5f);
    }

    @Override
    public int getKeyColor() {
        return Color.RED;
    }
    @Override
    protected int getKeyGuiTexturePath() {
        return R.drawable.redkey;
    }

    @Override
    public void setInFinalStage() {
    }
}
