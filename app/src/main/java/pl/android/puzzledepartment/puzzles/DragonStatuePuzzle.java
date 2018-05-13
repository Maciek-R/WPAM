package pl.android.puzzledepartment.puzzles;

import android.content.Context;
import android.graphics.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.R;
import pl.android.puzzledepartment.managers.TextureManager;
import pl.android.puzzledepartment.objects.EntityModel;
import pl.android.puzzledepartment.objects.HeightMap;
import pl.android.puzzledepartment.objects.Tip;
import pl.android.puzzledepartment.objects.complex_objects.DragonStatue;
import pl.android.puzzledepartment.util.geometry.Point;

/**
 * Created by Maciek Ruszczyk on 2017-11-26.
 */

public class DragonStatuePuzzle extends AbstractPuzzle{
    private final static int DRAGONS_COUNT = 4;

    private List<DragonStatue> statues;
    private List<DragonStatue.Direction> correctDirections = new ArrayList<>();

    public DragonStatuePuzzle(TextureManager textureManager, Point pos, EntityModel dragonModel, EntityModel vaseModel, HeightMap heightMap, Tip tip) {
        super(textureManager, pos, tip);
        statues = new ArrayList<>();
        statues.add(new DragonStatue(new Point(pos.x, heightMap.getHeight(pos.x, pos.z-3f)+0.5f, pos.z-3f), dragonModel, vaseModel));
        statues.add(new DragonStatue(new Point(pos.x, heightMap.getHeight(pos.x, pos.z-1f)+0.5f, pos.z-1f), dragonModel, vaseModel));
        statues.add(new DragonStatue(new Point(pos.x, heightMap.getHeight(pos.x, pos.z+1f)+0.5f, pos.z+1f), dragonModel, vaseModel));
        statues.add(new DragonStatue(new Point(pos.x, heightMap.getHeight(pos.x, pos.z+3f)+0.5f, pos.z+3f), dragonModel, vaseModel));

        if(!loadPuzzleFromFile(textureManager.getContext(), R.raw.dragon_statue_puzzle))
            initDefaultValues();
    }

    private void initDefaultValues() {
        correctDirections.clear();
        correctDirections.add(DragonStatue.Direction.RIGHT);
        correctDirections.add(DragonStatue.Direction.LEFT);
        correctDirections.add(DragonStatue.Direction.RIGHT);
        correctDirections.add(DragonStatue.Direction.LEFT);
    }

    public List<DragonStatue> getStatues() {
        return statues;
    }

    @Override
    public void update() {
        if(checkStatues())
            isCompleted = true;
    }

    private boolean checkStatues() {
        for(int i=0; i<DRAGONS_COUNT; ++i)
            if(!correctDirections.get(i).equals(statues.get(i).getDirection()))
                return false;

        return true;
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

                if (line.startsWith("RIGHT"))
                    correctDirections.add(DragonStatue.Direction.RIGHT);
                else if(line.startsWith("LEFT"))
                    correctDirections.add(DragonStatue.Direction.LEFT);
                else if(line.startsWith("BACKWARD"))
                    correctDirections.add(DragonStatue.Direction.BACKWARD);
                else if(line.startsWith("FORWARD"))
                    correctDirections.add(DragonStatue.Direction.FORWARD);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return correctDirections.size() == DRAGONS_COUNT;
    }

    @Override
    public Point getKeySpawnPosition() {
        return new Point(pos.x-3f, pos.y+1f, pos.z);
    }

    @Override
    public Point getTipPosition() {
        return new Point(pos.x-3f, pos.y, pos.z);
    }

    @Override
    public int getKeyColor() {
        return Color.CYAN;
    }

    @Override
    protected int getKeyGuiTexturePath() {
        return R.drawable.cyankey;
    }

    @Override
    public void setInFinalStage() {
        for(int i=0; i<DRAGONS_COUNT; ++i)
            statues.get(i).setDirection(correctDirections.get(i));
    }
}
