package ui;

import Services.DifficultyLevel;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class RunResultWindow extends Window {
    public RunResultWindow(
        Skin skin,
        boolean victory,
        int wave,
        float seconds,
        DifficultyLevel difficulty,
        Runnable onRestart,
        Runnable onMenu
    ) {
        super(victory ? "ФІНАЛ" : "GAME OVER", skin);
        setModal(true);
        setMovable(false);
        setResizable(false);
        padTop(70f);
        padLeft(30f);
        padRight(30f);
        padBottom(26f);

        Label title = new Label(
            victory ? "ЗАПОВІТНИЙ КЛЮЧ ЗНАЙДЕНО" : "ГЕРОЙ НЕ ВИТРИМАВ",
            skin,
            "title"
        );
        title.setAlignment(Align.center);
        title.setWrap(true);
        title.setFontScale(0.72f);
        title.setColor(victory ? UiSkinFactory.CYAN : UiSkinFactory.RED);
        add(title).width(760f).padBottom(14f).row();

        Label body = new Label(
            victory
                ? "Після довгого шляху герой підняв ключ, переможно зробив крок назад, послизнувся на банановій шкірці й епічно загинув. Кінець."
                : "Здоров'я впало до нуля. Забіг завершено, але схема прокачок збережена для наступної спроби.",
            skin
        );
        body.setAlignment(Align.center);
        body.setWrap(true);
        body.setFontScale(0.88f);
        add(body).width(720f).padBottom(20f).row();

        Table stats = new Table();
        stats.setBackground(skin.getDrawable("panel-strong"));
        stats.pad(16f);
        addStat(stats, skin, "ХВИЛЯ", String.valueOf(wave));
        addStat(stats, skin, "ЧАС", formatTime(seconds));
        addStat(stats, skin, "СКЛАДНІСТЬ", difficulty.title);
        add(stats).width(620f).padBottom(22f).row();

        Table buttons = new Table();
        buttons.add(button(skin, "НОВИЙ ЗАБІГ", onRestart)).width(285f).height(66f).padRight(14f);
        buttons.add(button(skin, "ГОЛОВНЕ МЕНЮ", onMenu)).width(285f).height(66f);
        add(buttons).row();
    }

    public void showCentered(Stage stage) {
        stage.addActor(this);
        setSize(860f, 560f);
        setPosition(
            (stage.getWidth() - getWidth()) * 0.5f,
            (stage.getHeight() - getHeight()) * 0.5f
        );
        stage.setKeyboardFocus(this);
    }

    private void addStat(Table table, Skin skin, String label, String value) {
        Label name = new Label(label, skin, "small");
        name.setColor(UiSkinFactory.MUTED);
        Label val = new Label(value, skin, "heading");
        val.setColor(UiSkinFactory.GOLD);
        val.setFontScale(0.62f);
        val.setAlignment(Align.right);
        table.add(name).left().expandX().pad(5f);
        table.add(val).right().width(260f).pad(5f).row();
    }

    private TextButton button(Skin skin, String text, Runnable action) {
        TextButton button = new TextButton(text, skin);
        button.getLabel().setAlignment(Align.center);
        button.getLabel().setWrap(true);
        button.getLabel().setFontScale(0.76f);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (action != null) action.run();
            }
        });
        return button;
    }

    private String formatTime(float seconds) {
        int total = Math.max(0, (int) seconds);
        return String.format("%02d:%02d", total / 60, total % 60);
    }
}
