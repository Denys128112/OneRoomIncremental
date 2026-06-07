package Services.ui;

import Services.stub.GameStateStub;
import Services.stub.LargeNumberFormatter;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

/** HUD overlay intended to be added above the combat world's renderer. */
public class GameHUD extends Table {
    private final GameStateStub state;
    private final HeartMeter hearts;
    private final ProgressBar experienceBar;
    private final Label experienceLabel;
    private final Label waveLabel;
    private final Label timerLabel;
    private final Label creditsLabel;

    public GameHUD(Skin skin, GameStateStub state) {
        this.state = state;
        setFillParent(true);
        top();
        pad(22f);

        Table healthPanel = new Table();
        healthPanel.setBackground(skin.getDrawable("panel-strong"));
        healthPanel.pad(16f);
        healthPanel.add(new Label("ЗДОРОВ’Я", skin, "small")).left().row();
        hearts = new HeartMeter(skin);
        healthPanel.add(hearts).left().padTop(8f).row();

        Table xpStack = new Table();
        experienceBar = new ProgressBar(0f, 1f, 0.001f, false, skin);
        experienceLabel = new Label("", skin, "small");
        experienceLabel.setAlignment(Align.center);
        com.badlogic.gdx.scenes.scene2d.ui.Stack stack = new com.badlogic.gdx.scenes.scene2d.ui.Stack();
        stack.add(experienceBar);
        stack.add(experienceLabel);
        xpStack.add(stack).width(390f).height(34f);
        healthPanel.add(xpStack).left().padTop(12f);
        add(healthPanel).width(520f).left();

        add().expandX();

        Table statusPanel = new Table();
        statusPanel.setBackground(skin.getDrawable("panel-strong"));
        statusPanel.pad(16f);
        waveLabel = new Label("", skin, "heading");
        timerLabel = new Label("", skin);
        creditsLabel = new Label("", skin, "gold");
        Image creditsIcon = new Image(skin.getDrawable("credits-icon"));
        statusPanel.add(waveLabel).right().colspan(2).row();
        statusPanel.add(timerLabel).right().colspan(2).padTop(4f).row();
        statusPanel.add(creditsIcon).size(38f).padTop(8f).padRight(12f);
        statusPanel.add(creditsLabel).right().padTop(8f);
        add(statusPanel).width(390f).right();
        refresh();
    }

    public void refresh() {
        hearts.setHealth(state.getHealthQuarters(), state.getMaxHealthQuarters());
        experienceBar.setRange(0f, state.getExperienceToNextLevel());
        experienceBar.setValue(state.getExperience());
        experienceLabel.setText("ДОСВІД " + state.getExperience() + " / " + state.getExperienceToNextLevel());
        waveLabel.setText("ХВИЛЯ " + state.getWave());
        int seconds = (int) state.getWaveSeconds();
        timerLabel.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
        creditsLabel.setText(LargeNumberFormatter.format(state.getCredits()));
    }
}
