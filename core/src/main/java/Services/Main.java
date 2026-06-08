package Services;

import Services.screens.GameScreen;
import Services.screens.MainMenuScreen;
import Services.screens.UpgradeScreen;
import Services.stub.GameStateStub;
import Services.ui.UiSkinFactory;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import SkillTree.SkillTreeScreen;

/**
 * Screen-oriented application root.
 *
 * The combat team can keep its world rendering inside GameScreen and leave this
 * class responsible only for shared UI resources and screen transitions.
 */
public class Main extends Game {
    private Skin skin;
    private GameStateStub gameState;
    private SkillTreeScreen skillTreeScreen;

    @Override
    public void create() {
        skin = UiSkinFactory.create();
        gameState = new GameStateStub();
        showMainMenu();
    }

    public void showMainMenu() {
        switchTo(new MainMenuScreen(this));
    }

    public void showGame() {
        switchTo(new GameScreen(this));
    }

    public void showUpgrades() {
        switchTo(new UpgradeScreen(this));
    }

    public void showSkillTree() {
        if (skillTreeScreen == null) skillTreeScreen = new SkillTreeScreen(this);
        switchTo(skillTreeScreen);
    }
    public Skin getSkin() {
        return skin;
    }

    public GameStateStub getGameState() {
        return gameState;
    }

    private void switchTo(Screen next) {
        Screen previous = getScreen();
        setScreen(next);
        if (previous != null && previous != skillTreeScreen) {
            previous.dispose();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
    }
}
