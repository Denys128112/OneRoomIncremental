package ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

/**
 * Heart display where every heart consists of four independently filled quadrants.
 * GameStateStub stores health in these quarter-heart units.
 */
public class HeartMeter extends Table {
    private final Skin skin;
    private final Array<Image> quarters = new Array<>();
    private int maxQuarters = -1;

    public HeartMeter(Skin skin) {
        this.skin = skin;
        left();
    }

    public void setHealth(int healthQuarters, int maximumQuarters) {
        if (maximumQuarters != maxQuarters) {
            rebuild(maximumQuarters);
        }
        for (int i = 0; i < quarters.size; i++) {
            quarters.get(i).setDrawable(skin.getDrawable(
                i < healthQuarters ? "heart-full" : "heart-empty"));
        }
    }

    private void rebuild(int maximumQuarters) {
        clearChildren();
        quarters.clear();
        maxQuarters = maximumQuarters;
        int heartCount = (maximumQuarters + 3) / 4;
        for (int heart = 0; heart < heartCount; heart++) {
            Table heartTable = new Table();
            for (int quadrant = 0; quadrant < 4; quadrant++) {
                Image image = new Image(skin.getDrawable("heart-empty"));
                quarters.add(image);
                heartTable.add(image).size(15f).pad(1f);
                if (quadrant == 1) {
                    heartTable.row();
                }
            }
            add(heartTable).size(38f).padRight(8f);
        }
    }
}
