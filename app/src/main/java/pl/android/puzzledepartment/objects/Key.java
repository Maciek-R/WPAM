package pl.android.puzzledepartment.objects;

import android.graphics.Color;

import pl.android.puzzledepartment.gui.GuiEntity;
import pl.android.puzzledepartment.managers.GameManager;
import pl.android.puzzledepartment.util.geometry.Point;
import pl.android.puzzledepartment.util.geometry.Vector2f;
import pl.android.puzzledepartment.util.geometry.Vector3f;


/**
 * Created by Maciek Ruszczyk on 2017-12-08.
 */

public class Key extends ModelEntity {
    private GameManager gameManager;

    private final int guiTextureId;
    private GuiEntity guiEntity;
    private String colorStr;

    public Key(Point pos, int color, int guiTextureId, EntityModel entityModel) {
        this(pos, color, entityModel, guiTextureId, new Vector3f(1f, 1f, 1f));
    }

    public Key(Point pos, int color, EntityModel entityModel, int guiTextureId, Vector3f scale) {
        super(pos, 0.0f, new Vector3f(0.5f*scale.x, 0.5f*scale.y, 0.5f*scale.z), entityModel);
        this.color = color;
        this.colorStr = getColorStr(color);
        this.guiTextureId = guiTextureId;
    }

    public void addObserver(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void update(){
        rotate(30.0f);
    }

    public void onCollisionNotify() {
        setIsVisible(false);
        guiEntity = new GuiEntity(guiTextureId, new Vector2f(-0.9f+0.18f* gameManager.getKeysTakenCount(), 0.9f), new Vector2f(0.08f, 0.08f));
        guiEntity.setIsVisible(true);
    }

    public GuiEntity getGuiEntity() {
        return guiEntity;
    }

    @Override
    protected void initObjectProperties() {
        type = Type.UNCOLOURED;
        isShining = true;
    }

    public String getColorStr() {
        return colorStr;
    }

    public static String[] getColorValues(){
        return new String[]{getColorStr(Color.RED), getColorStr(Color.GREEN), getColorStr(Color.BLUE),
                getColorStr(Color.YELLOW), getColorStr(Color.MAGENTA), getColorStr(Color.CYAN)};
    }

    public static String getColorStr(int color) {
        switch (color) {
            case Color.RED: return "Red";
            case Color.GREEN: return "Green";
            case Color.BLUE: return "Blue";
            case Color.YELLOW: return "Yellow";
            case Color.MAGENTA: return "Magenta";
            case Color.CYAN: return "Cyan";
        }
        return "";
    }
}
