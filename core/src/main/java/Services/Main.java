package Services;

import screens.GameScreen;
import screens.DifficultyScreen;
import screens.MainMenuScreen;
import screens.UpgradeScreen;
import stub.GameStateStub;
import ui.UiSkinFactory;
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
    private GameScreen gameScreen;

    @Override
    public void create() {
        AudioManager.load();
        skin = UiSkinFactory.create();
        gameState = new GameStateStub();
        AudioManager.setEnabled(gameState.isSoundEnabled());
        AudioManager.setMusicVolume(gameState.getRawMusicVolume());
        AudioManager.setSoundVolume(gameState.getRawSoundVolume());
        showMainMenu();
    }

    public void showMainMenu() {
        gameScreen = null;
        skillTreeScreen = null;
        switchTo(new MainMenuScreen(this));
    }

    public void showGame() {
        if (gameScreen == null) gameScreen = new GameScreen(this);
        switchTo(gameScreen);
    }

    public void showDifficultySelection() {
        switchTo(new DifficultyScreen(this));
    }

    public void startGame(DifficultyLevel difficulty) {
        gameState.startNewRun(difficulty);
        gameScreen = null;
        skillTreeScreen = null;
        showGame();
    }

    public void restartCurrentRun() {
        startGame(gameState.getLevelManager().getDifficulty());
    }

    public void showUpgrades() {
        switchTo(new UpgradeScreen(this));
    }

    public void showSkillTree() {
        if (skillTreeScreen == null) skillTreeScreen = new SkillTreeScreen(this);
        switchTo(skillTreeScreen);
    }

    public boolean areAllSkillsUnlocked() {
        return skillTreeScreen != null && skillTreeScreen.areAllSkillsUnlocked();
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
        if (previous != null && previous != gameScreen && previous != skillTreeScreen) {
            previous.dispose();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        if (skillTreeScreen != null) skillTreeScreen.dispose();
        AudioManager.dispose();
    }
}
