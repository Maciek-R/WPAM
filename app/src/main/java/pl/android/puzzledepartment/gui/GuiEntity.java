package pl.android.puzzledepartment.gui;

import pl.android.puzzledepartment.managers.TimeManager;
import pl.android.puzzledepartment.util.geometry.Vector2f;

/**
 * Created by Maciek Ruszczyk on 2017-10-28.
 */

public class GuiEntity {

    private final int textureId;
    private Vector2f position;
    private Vector2f scale;
    private boolean isVisible = false;

    private boolean isVisibleFewSeconds = false;
    private float startVisibleTime;
    private float visibleInSeconds;

    public GuiEntity(int textureId, Vector2f position) {
        this(textureId, position, new Vector2f(1.0f, 1.0f));
    }
    public GuiEntity(int textureId) {
        this(textureId, new Vector2f(0.0f, 0.0f), new Vector2f(1.0f, 1.0f));
    }
    public GuiEntity(int textureId, Vector2f position, Vector2f scale) {
        this.textureId = textureId;
        this.position = position;
        this.scale = scale;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public int getTextureId() {
        return textureId;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }

    public boolean pressed(float normalizedX, float normalizedY) {
        return normalizedX >= position.x-scale.x && normalizedX <= position.x+scale.x &&
                normalizedY >= position.y-scale.y && normalizedY <= position.y+scale.y;
    }

    public void setVisibleForFewSeconds(float seconds) {
        startVisibleTime = TimeManager.getElapsedTimeFromBeginningInSeconds();
        isVisibleFewSeconds = true;
        visibleInSeconds = seconds;
        isVisible = true;
    }

    public void update(){
        if(isVisibleFewSeconds){
            if(TimeManager.getElapsedTimeFromBeginningInSeconds() - startVisibleTime > visibleInSeconds){
                isVisibleFewSeconds = false;
                isVisible = false;
            }
        }
    }
}
