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
import pl.android.puzzledepartment.objects.SimpleColorShaderCube;
import pl.android.puzzledepartment.objects.Tip;
import pl.android.puzzledepartment.objects.complex_objects.Lever;
import pl.android.puzzledepartment.util.geometry.Point;


/**
 * Created by Maciek Ruszczyk on 2017-11-26.
 */

public class MixColorPuzzle extends AbstractPuzzle{

    private int levelsCount;
    private List<SimpleColorShaderCube> cubes;
    private List<Integer> colors;
    private List<Lever> levers;

    private List<Round> rounds = new ArrayList<>();
    private int currentLevel = 0;

    public MixColorPuzzle(TextureManager textureManager, Point pos, EntityModel leverBaseModel, EntityModel leverHandleModel, HeightMap heightMap, Tip tip) {
        super(textureManager, pos, tip);
        colors = new ArrayList<>();
        cubes = new ArrayList<>();

        for(int i=0; i<3; ++i)
            cubes.add(new SimpleColorShaderCube(new Point(pos.x - i*2, pos.y, pos.z)));
        for(int i=0; i<2; ++i)
            colors.add(Integer.valueOf(0));

        levers = new ArrayList<>();
        levers.add(new Lever(new Point(pos.x, heightMap.getHeight(pos.x, pos.z - 5f), pos.z - 5f), leverBaseModel, leverHandleModel, cubes.get(0)));
        levers.add(new Lever(new Point(pos.x-2f, heightMap.getHeight(pos.x-2f, pos.z - 5f), pos.z - 5f), leverBaseModel, leverHandleModel, cubes.get(1)));

        if(!loadPuzzleFromFile(textureManager.getContext(), R.raw.mix_color_puzzle))
            initDefaultLevels();
        levelsCount = rounds.size();
        loadInitColors();
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
                if (line.startsWith("l ")) {
                    String firstColor = currentLine[1];
                    String secondColor = currentLine[2];
                    String thirdColor = currentLine[3];

                    rounds.add(new Round(stringToColor(firstColor), stringToColor(secondColor), stringToColor(thirdColor)));
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return !rounds.isEmpty();
    }

    private void initDefaultLevels() {
        rounds.clear();
        rounds.add(new Round(Color.GREEN, Color.RED, Color.YELLOW));
        rounds.add(new Round(Color.RED, Color.BLUE, Color.MAGENTA));
        rounds.add(new Round(Color.GREEN, Color.BLUE, Color.CYAN));
    }

    private void loadInitColors() {
        for(int i=0; i<rounds.get(currentLevel).getColorToMixCount(); ++i)
            cubes.get(i).setColor(Color.RED);

        loadNextFinalColor();
    }

    private void loadNextFinalColor() {
        int colorsCountToMix = rounds.get(currentLevel).getColorToMixCount();
        cubes.get(colorsCountToMix).setColor(rounds.get(currentLevel).getFinalColor());
    }

    private void nextLevel() {
        ++currentLevel;
        loadNextFinalColor();
    }

    public List<SimpleColorShaderCube> getCubes() {
        return cubes;
    }

    public List<Lever> getLevers() {
        return levers;
    }

    @Override
    public void update() {
        if(!checkIfColorsAreCorrect())
            return;

        if(currentLevel >= levelsCount - 1)
            isCompleted = true;
        else
            nextLevel();
    }

    private boolean checkIfColorsAreCorrect() {
        for(int i=0; i<2; ++i)
            colors.set(i, cubes.get(i).getColor());

        return rounds.get(currentLevel).checkCorrectColors(colors);
    }

    @Override
    public Point getKeySpawnPosition() {
        return new Point(pos.x-2f, pos.y, pos.z-2.5f);
    }

    @Override
    public Point getTipPosition() {
        return new Point(pos.x-2f, pos.y-1.5f, pos.z-2.5f);
    }

    @Override
    public int getKeyColor() {
        return Color.GREEN;
    }

    @Override
    protected int getKeyGuiTexturePath() {
        return R.drawable.greenkey;
    }

    private int stringToColor(String strColor){
        switch (strColor){
            case "GREEN": return Color.GREEN;
            case "RED": return Color.RED;
            case "YELLOW": return Color.YELLOW;
            case "BLUE": return Color.BLUE;
            case "MAGENTA": return Color.MAGENTA;
            case "CYAN": return Color.CYAN;
            case "BLACK": return Color.BLACK;
            case "GRAY": return Color.GRAY;
            case "WHITE": return Color.WHITE;
        }
        return -1;
    }

    @Override
    public void setInFinalStage() {
        for(int i=0; i<2; ++i)
            cubes.get(i).setColor(rounds.get(rounds.size()-1).colorsNeedToBeMixed.get(i));

        cubes.get(cubes.size()-1).setColor(rounds.get(rounds.size()-1).finalColor);
    }

    private class Round{
        List<Integer> colorsNeedToBeMixed;
        int finalColor;

        public Round(int... colors) {
            colorsNeedToBeMixed = new ArrayList<>();
            for (int c : colors)
                colorsNeedToBeMixed.add(Integer.valueOf(c));

            finalColor = colorsNeedToBeMixed.remove(colorsNeedToBeMixed.size()-1);
        }

        public boolean checkCorrectColors(List<Integer> colors) {
            return colorsNeedToBeMixed.containsAll(colors) && colors.containsAll(colorsNeedToBeMixed);
        }

        public int getFinalColor() {
            return finalColor;
        }
        public int getColorToMixCount() {
            return colorsNeedToBeMixed.size();
        }
    }
}
