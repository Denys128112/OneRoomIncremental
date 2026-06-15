package ui;

import Services.AudioManager;
import stub.GameStateStub;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/** Modal settings dialog available from both the main menu and gameplay. */
public class SettingsWindow extends Dialog {
    private final GameStateStub state;
    private final TextButton soundButton;
    private final Label musicValue;
    private final Label soundValue;

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
                AudioManager.setEnabled(state.isSoundEnabled());
                refreshSoundText();
            }
        });
        getContentTable().add(soundButton).width(440f).height(74f).pad(10f).row();

        musicValue = new Label("", skin, "small");
        soundValue = new Label("", skin, "small");
        getContentTable().add(volumeRow(
            skin,
            "МУЗИКА",
            state.getRawMusicVolume(),
            musicValue,
            value -> {
                state.setMusicVolume(value);
                AudioManager.setMusicVolume(state.getRawMusicVolume());
                refreshVolumeLabels();
            }
        )).width(510f).padTop(8f).row();
        getContentTable().add(volumeRow(
            skin,
            "ЕФЕКТИ",
            state.getRawSoundVolume(),
            soundValue,
            value -> {
                state.setSoundVolume(value);
                AudioManager.setSoundVolume(state.getRawSoundVolume());
                refreshVolumeLabels();
            }
        )).width(510f).padTop(8f).row();
        refreshVolumeLabels();

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
        setSize(720f, 620f);
        setPosition((stage.getWidth() - getWidth()) / 2f, (stage.getHeight() - getHeight()) / 2f);
    }

    private void refreshSoundText() {
        soundButton.setText("ЗВУК: " + (state.isSoundEnabled() ? "УВІМКНЕНО" : "ВИМКНЕНО"));
    }

    private Table volumeRow(
        Skin skin,
        String title,
        float value,
        Label valueLabel,
        VolumeCallback callback
    ) {
        Table row = new Table();
        row.setBackground(skin.getDrawable("panel"));
        row.pad(14f);

        Label titleLabel = new Label(title, skin, "heading");
        titleLabel.setFontScale(0.56f);
        Slider slider = new Slider(0f, 1f, 0.05f, false, skin);
        slider.setValue(value);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                callback.changed(slider.getValue());
            }
        });
        valueLabel.setAlignment(Align.right);

        row.add(titleLabel).left().width(140f);
        row.add(slider).width(250f).height(34f).padLeft(12f);
        row.add(valueLabel).right().width(80f).padLeft(12f);
        return row;
    }

    private void refreshVolumeLabels() {
        musicValue.setText(Math.round(state.getRawMusicVolume() * 100f) + "%");
        soundValue.setText(Math.round(state.getRawSoundVolume() * 100f) + "%");
    }

    private void configureButton(TextButton button) {
        button.getLabel().setAlignment(Align.center);
        button.getLabel().setWrap(true);
        button.getLabel().setFontScale(0.8f);
    }

    private interface VolumeCallback {
        void changed(float value);
    }
}
