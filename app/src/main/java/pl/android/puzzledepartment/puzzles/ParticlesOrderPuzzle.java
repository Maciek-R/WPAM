package pl.android.puzzledepartment.puzzles;

import android.content.Context;
import android.graphics.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.managers.TextureManager;
import pl.android.puzzledepartment.objects.Tip;
import pl.android.puzzledepartment.objects.particles.ParticleCollideShooter;
import pl.android.puzzledepartment.objects.particles.ParticleSystem;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-11-24.
 */

public class ParticlesOrderPuzzle extends AbstractPuzzle{

    private int particlesShootersCount = 5;
    private float radius = 3.0f;

    private ParticleSystem particleSystem;
    private ParticleCollideShooter particleShooters[];

    private List<ParticleCollideShooter> particleShootersOrderLevel;
    private List<ParticleCollideShooter> particleShootersOrderLevelAlreadyPicked;
    private int currentLevel = 0;

    public ParticlesOrderPuzzle(TextureManager textureManager, Point pos, int particleTexture, Tip tip) {
        super(textureManager, pos, tip);
        particleSystem = new ParticleSystem(10000, particleTexture);
        loadPuzzleFromFile(textureManager.getContext(), R.raw.particle_order_puzzle);
        particleShooters = new ParticleCollideShooter[particlesShootersCount];
        for(int i=0; i<particlesShootersCount; ++i) {
            float angleInRadians = ((float)i / (float)particlesShootersCount) * ((float) Math.PI * 2f);
            float transX = (float)Math.cos(angleInRadians) * radius;
            float transZ = (float)Math.sin(angleInRadians) * radius;
            particleShooters[i] = new ParticleCollideShooter(new Point(pos.x + transX, pos.y, pos.z + transZ), new Vector3f(0f, 0.5f, 0f), Color.rgb(255, 50, 5), 360f, 0.5f);
        }
        randomParticlesOrderToPick();
    }

    private void randomParticlesOrderToPick() {
        particleShootersOrderLevelAlreadyPicked = new ArrayList<>();
        particleShootersOrderLevel = new ArrayList<>(particlesShootersCount);
        for(int i=0; i<particlesShootersCount; ++i)
            particleShootersOrderLevel.add(particleShooters[i]);

        Collections.shuffle(particleShootersOrderLevel);
    }

    public void checkIfChoseCorrectParticle(ParticleCollideShooter particleCollideShooter) {
        if(currentLevel>=particlesShootersCount){
            return;
        }

        if (particleCollideShooter == particleShootersOrderLevel.get(currentLevel)) {
            particleCollideShooter.changeColorToGreen();
            particleShootersOrderLevelAlreadyPicked.add(particleCollideShooter);
            if(++currentLevel == particlesShootersCount)
                isCompleted = true;
        }
        else if(!isAlreadyPicked(particleCollideShooter))
            reset();
    }

    private boolean loadPuzzleFromFile(Context context, int resourceId) {
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line;

        try {
            while (true) {
                line = bufferedReader.readLine();
                if(line == null)
                    break;

                String[] currentLine = line.split(" ");
                if (line.startsWith("count ")) {
                    particlesShootersCount = Integer.parseInt(currentLine[1]);
                }
                else if(line.startsWith("radius ")){
                    radius = Float.parseFloat(currentLine[1]);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean isAlreadyPicked(ParticleCollideShooter particleCollideShooter) {
        return particleShootersOrderLevelAlreadyPicked.contains(particleCollideShooter);
    }

    private void reset() {
        for (ParticleCollideShooter particleCollideShooter : particleShooters) {
            particleCollideShooter.changeColorToRed();
        }
        particleShootersOrderLevelAlreadyPicked.clear();
        currentLevel = 0;
    }

    @Override
    public void update(float elapsedTime) {
        for(int i=0; i<particlesShootersCount; ++i) {
            particleShooters[i].addParticles(particleSystem, elapsedTime, 5);
        }
    }

    public ParticleSystem getParticleSystem() {
        return particleSystem;
    }

    public ParticleCollideShooter[] getParticleShooters() {
        return particleShooters;
    }

    @Override
    public Point getKeySpawnPosition() {
        return new Point(pos.x, pos.y+1f, pos.z);
    }

    @Override
    public Point getTipPosition() {
        return new Point(pos.x, pos.y-0.2f, pos.z);
    }

    @Override
    public int getKeyColor() {
        return Color.YELLOW;
    }

    @Override
    protected int getKeyGuiTexturePath() {
        return R.drawable.yellowkey;
    }

    @Override
    public void setInFinalStage() {
        for(ParticleCollideShooter p:particleShootersOrderLevel)
            p.changeColorToGreen();
    }
}
