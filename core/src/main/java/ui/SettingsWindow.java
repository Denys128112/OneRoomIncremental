package ui;

import stub.GameStateStub;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/** Modal settings dialog available from both the main menu and gameplay. */
public class SettingsWindow extends Dialog {
    private final GameStateStub state;
    private final TextButton soundButton;

    public SettingsWindow(Skin skin, GameStateStub state, Runnable afterPrestige) {
        super("СИСТЕМНІ НАЛАШТУВАННЯ", skin);
        this.state = state;
        setModal(true);
        setMovable(false);
        padTop(74f);
        padLeft(42f);
        padRight(42f);
        padBottom(36f);

        soundButton = new TextButton("", skin);
        configureButton(soundButton);
        refreshSoundText();
        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                state.setSoundEnabled(!state.isSoundEnabled());
                refreshSoundText();
            }
        });
        getContentTable().add(soundButton).width(440f).height(74f).pad(10f).row();

        TextButton prestigeButton = new TextButton("ПРЕСТИЖ: СКИНУТИ ПРОГРЕС", skin);
        configureButton(prestigeButton);
        prestigeButton.getLabel().setColor(UiSkinFactory.RED);
        prestigeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                state.prestigeReset();
                afterPrestige.run();
                hide();
            }
        });
        getContentTable().add(prestigeButton).width(440f).height(74f).pad(10f).row();
        Label explanation = new Label(
            "Скидає кредити й покращення та підвищує рівень престижу.", skin, "small");
        explanation.setWrap(true);
        explanation.setAlignment(Align.center);
        getContentTable().add(explanation).width(500f).padTop(8f);

        TextButton close = new TextButton("ЗАКРИТИ", skin);
        configureButton(close);
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });
        getButtonTable().add(close).width(220f).height(64f).padTop(24f);
    }

    public void showCentered(Stage stage) {
        show(stage);
        setSize(700f, 460f);
        setPosition((stage.getWidth() - getWidth()) / 2f, (stage.getHeight() - getHeight()) / 2f);
    }

    private void refreshSoundText() {
        soundButton.setText("ЗВУК: " + (state.isSoundEnabled() ? "УВІМКНЕНО" : "ВИМКНЕНО"));
    }

    private void configureButton(TextButton button) {
        button.getLabel().setAlignment(Align.center);
        button.getLabel().setWrap(true);
        button.getLabel().setFontScale(0.8f);
    }
}
