package pl.android.puzzledepartment.puzzles;





import pl.android.puzzledepartment.managers.TextureManager;
import pl.android.puzzledepartment.objects.Tip;
import pl.android.puzzledepartment.util.geometry.Point;

/**
 * Created by Maciek Ruszczyk on 2017-12-15.
 */

public abstract class AbstractPuzzle{
    protected Point pos;
    protected boolean isCompleted = false;
    private boolean wasKeySpawned = false;
    private int guiKeyTexture;

    private Tip tip;

    protected AbstractPuzzle(TextureManager textureManager, Point pos, Tip tip) {
        this.pos = pos;
        guiKeyTexture = textureManager.getTextureId(getKeyGuiTexturePath());
        this.tip = tip;
        this.tip.setPos(getTipPosition());
    }
    public Point getPosition(){
        return pos;
    }
    public abstract Point getKeySpawnPosition();
    protected abstract Point getTipPosition();
    public boolean isCompleted(){
        return isCompleted;
    }
    public boolean wasKeySpawned(){
        return wasKeySpawned;
    }
    public void setWasKeySpawned(boolean wasKeySpawned) {
        this.wasKeySpawned = wasKeySpawned;
    }
    public abstract int getKeyColor();
    public void update() {
    }
    public void update(float elapsedTime){
    }
    protected abstract int getKeyGuiTexturePath();
    public int getKeyGuiTexture() {
        return guiKeyTexture;
    }
    public Tip getTip() {
        return tip;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    public abstract void setInFinalStage();
}
