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
import pl.android.puzzledepartment.managers.EntityManager;
import pl.android.puzzledepartment.managers.TextureManager;
import pl.android.puzzledepartment.objects.Cylinder;
import pl.android.puzzledepartment.objects.Department;
import pl.android.puzzledepartment.objects.Entity;
import pl.android.puzzledepartment.objects.EntityModel;
import pl.android.puzzledepartment.objects.Tip;
import pl.android.puzzledepartment.objects.complex_objects.Room;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector2f;
import pl.android.puzzledepartment.util.geometry.Vector3f;

/**
 * Created by Maciek Ruszczyk on 2017-10-21.
 */

public class TeleportPuzzle extends AbstractPuzzle{
    private enum TeleportType{WRONG, RIGHT}
    private List<Vector2f> teleportPositions = new ArrayList<>();
    private int numberOfLevels = 4;

    private List<Entity> teleports = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Entity> correctTeleportPerLevel;
    private int currentLevel = 0;

    private final EntityManager entityManager;

    public TeleportPuzzle(Context context, TextureManager textureManager, Point pos, EntityManager entityManager, Tip tip) {
        super(textureManager, pos, tip);
        this.entityManager = entityManager;
        if(!loadPuzzleFromFile(context, R.raw.teleportpuzzle))
            initFields();

        createScene();
    }

    private void initFields() {
        teleportPositions.add(new Vector2f(-3.0f, 0.0f));
        teleportPositions.add(new Vector2f(-2.0f, -3.0f));
        teleportPositions.add(new Vector2f(2.0f, -3.0f));
        teleportPositions.add(new Vector2f(3.0f, 0.0f));
    }

    private void nextLevel() {
        currentLevel ++;
    }

    private void reset() {
        currentLevel = 0;
    }

    private void createScene() {
        correctTeleportPerLevel = new ArrayList<>();
        List<TeleportType> teleportTypes = new ArrayList<>();
        teleportTypes.add(TeleportType.WRONG);
        teleportTypes.add(TeleportType.WRONG);
        teleportTypes.add(TeleportType.WRONG);
        teleportTypes.add(TeleportType.RIGHT);

        for(int i=0; i<numberOfLevels; ++i) {
            Collections.shuffle(teleportTypes);
            int l = 0;
            for (Vector2f teleportPosition : teleportPositions) {

                TeleportType teleportType = teleportTypes.get(l);
                Entity d = new Cylinder(new Point(teleportPosition.x + pos.x, i*10 + pos.y+0.5f, teleportPosition.y + pos.z), new Vector3f(1.0f, 0.5f, 1.0f));
                teleports.add(d);
                if(TeleportType.RIGHT.equals(teleportType))
                    correctTeleportPerLevel.add(d);
                ++l;
            }
        }
        for(int i=0; i<=numberOfLevels; ++i){
            rooms.add(new Room(new Point(0 + pos.x, i*10 + pos.y, 0 + pos.z), 5f, 1f));
        }
    }

    /*private Entity getDepartment(DepartmentType departmentType, Vector2f teleportPosition, int i) {
        int color = Color.BLUE;
        EntityModel entityModel = null;
        switch(departmentType){
            case ELKA:
                color = Color.BLUE;
                entityModel = entityManager.getDepartmentElka();
                break;
            case MINI:
                color = Color.BLUE;
                entityModel = entityManager.getDepartmentMini();
                break;
            case MECH:
                color = Color.BLUE;
                entityModel = entityManager.getDepartmentMech();
                break;
            case MEL:
                color = Color.BLUE;
                entityModel = entityManager.getDepartmentMel();
                break;
        }
        return new Department(new Point(teleportPosition.x + pos.x, i*10 + pos.y+0.5f, teleportPosition.y + pos.z), color, entityModel);
    }*/

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
                    if (line.startsWith("p ")) {
                        Vector2f teleportPosition = new Vector2f(Float.parseFloat(currentLine[1]),
                                Float.parseFloat(currentLine[2]));
                        teleportPositions.add(teleportPosition);
                    }
                    else if(line.startsWith("levels ")){
                        numberOfLevels = Integer.parseInt(currentLine[1]);
                    }
                }
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
    }

    public List<Entity> getTeleports() {
        return teleports;
    }
    public List<Room> getRooms() {
        return rooms;
    }

    public boolean checkCorrectTeleport(Entity e) {
        if(currentLevel >= numberOfLevels){
            reset();
            return false;
        }
        if (e == correctTeleportPerLevel.get(currentLevel)) {
                nextLevel();
            if(currentLevel >= numberOfLevels){
                isCompleted = true;
            }
            return true;
        }
        else {
            reset();
            return false;
        }
    }

    public Vector3f getPositionOnCurrentFloor() {
        return new Vector3f(0.0f + pos.x, currentLevel*10f + pos.y, 0.0f + pos.z);
    }

    @Override
    public Point getKeySpawnPosition() {
        return new Point(pos.x, numberOfLevels * 10f + pos.y + 3f, pos.z);
    }

    @Override
    public Point getTipPosition() {
        return new Point(pos.x, pos.y, pos.z-2f);
    }

    @Override
    public int getKeyColor() {
        return Color.BLUE;
    }
    @Override
    protected int getKeyGuiTexturePath() {
        return R.drawable.bluekey;
    }

    @Override
    public void setInFinalStage() {
    }
}
