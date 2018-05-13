package pl.android.puzzledepartment.managers;

import java.util.ArrayList;
import java.util.List;

import pl.android.puzzledepartment.action.Actionable;
import pl.android.puzzledepartment.objects.Camera;
import pl.android.puzzledepartment.puzzles.AbstractPuzzle;
import pl.android.puzzledepartment.puzzles.DragonStatuePuzzle;
import pl.android.puzzledepartment.puzzles.MixColorPuzzle;

/**
 * Created by Maciek Ruszczyk on 2017-10-28.
 */

public class ActionManager {

    private List<Actionable> actionables;
    private Actionable activeActionable;

    public ActionManager() {
        actionables = new ArrayList<>();
        activeActionable = null;
    }

    public void addPuzzle(List<? extends AbstractPuzzle> puzzles) {
        for (AbstractPuzzle puzzle : puzzles)
            addPuzzle(puzzle);
    }

    private void addPuzzle(AbstractPuzzle puzzle) {
        add(puzzle.getTip());
        if (puzzle instanceof DragonStatuePuzzle) {
            add(((DragonStatuePuzzle)puzzle).getStatues());

        } else if (puzzle instanceof MixColorPuzzle) {
            add(((MixColorPuzzle)puzzle).getLevers());
        }
    }

    private void add(List<? extends Actionable> actionables) {
        for(Actionable a:actionables)
            add(a);
    }

    public void add(Actionable actionable) {
        actionables.add(actionable);
    }

    public boolean isNearAnyActionableObject(Camera camera) {
        for (Actionable a : actionables)
            if (!a.isInAction() && extendedCollide(a, camera)) {
                activeActionable = a;
                return true;
            }

        activeActionable = null;
        return false;
    }

    private boolean extendedCollide(Actionable a, Camera camera) {
        final float scaleX = a.getScale().x;
        final float scaleY = a.getScale().y;
        final float scaleZ = a.getScale().z;

        final float entityLeftPosX = a.getPosition().x - scaleX;
        final float entityRightPosX = a.getPosition().x + scaleX;
        final float entityBottomPosY = a.getPosition().y - scaleY;
        final float entityTopPosY = a.getPosition().y + scaleY;
        final float entityLeftPosZ = a.getPosition().z - scaleZ;
        final float entityRightPosZ = a.getPosition().z + scaleZ;

        final float possibleCamLeftX = camera.getPossibleX() - Camera.WIDTH/2;
        final float possibleCamRightX = camera.getPossibleX() + Camera.WIDTH/2;
        final float possibleCamBottomY = camera.getPossibleY();
        final float possibleCamTopY = camera.getPossibleY() + Camera.WIDTH;
        final float possibleCamLeftZ = camera.getPossibleZ() - Camera.WIDTH/2;
        final float possibleCamRightZ = camera.getPossibleZ() + Camera.WIDTH/2;

        return !(possibleCamLeftX > entityRightPosX || possibleCamRightX < entityLeftPosX ||
                possibleCamBottomY > entityTopPosY || possibleCamTopY < entityBottomPosY ||
                possibleCamLeftZ > entityRightPosZ || possibleCamRightZ < entityLeftPosZ);
    }

    public void activate() {
        if(activeActionable != null)
            activeActionable.action();
    }

    public void moveInActionObjects() {
        for (Actionable a : actionables) {
            if(a.isInAction())
                a.updateAction();
        }
    }
}
