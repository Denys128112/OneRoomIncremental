package screens;

import Services.DifficultyLevel;
import Services.Main;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

public class DifficultyScreen extends BaseScreen {
    public DifficultyScreen(Main game) {
        super(game);
        build();
    }

    private void build() {
        Stack screen = new Stack();
        screen.setFillParent(true);
        stage.addActor(screen);

        Image background = new Image(game.getSkin().getDrawable("main-menu-background"));
        background.setScaling(Scaling.fill);
        screen.add(background);

        Table dimLayer = new Table();
        dimLayer.setBackground(game.getSkin().getDrawable("dark"));
        dimLayer.setColor(1f, 1f, 1f, 0.78f);
        screen.add(dimLayer);

        Table root = new Table();
        root.pad(36f);
        screen.add(root);

        Label title = new Label("ОБЕРИ РІВЕНЬ СКЛАДНОСТІ", game.getSkin(), "title");
        title.setAlignment(Align.center);
        title.setWrap(true);
        title.setFontScale(0.78f);
        root.add(title).colspan(3).width(1350f).padBottom(12f).row();

        Label subtitle = new Label(
            "Складність визначає кількість, силу та швидкість ворогів у кожній хвилі.",
            game.getSkin(),
            "default"
        );
        subtitle.setAlignment(Align.center);
        subtitle.setWrap(true);
        subtitle.setColor(Color.valueOf("B8C7D9"));
        root.add(subtitle).colspan(3).width(1120f).padBottom(30f).row();

        DifficultyLevel[] levels = DifficultyLevel.values();
        for (int i = 0; i < levels.length; i++) {
            root.add(buildCard(levels[i]))
                .width(440f)
                .height(505f)
                .padLeft(i == 0 ? 0f : 12f)
                .padRight(i == levels.length - 1 ? 0f : 12f);
        }
        root.row();

        TextButton back = button("ПОВЕРНУТИСЯ В МЕНЮ", game::showMainMenu);
        back.getLabel().setFontScale(0.72f);
        root.add(back).colspan(3).width(390f).height(68f).padTop(26f);
    }

    private Table buildCard(DifficultyLevel difficulty) {
        Table card = new Table();
        card.setBackground(game.getSkin().getDrawable("panel-strong"));
        card.pad(18f);
        card.top();

        Label marker = new Label(difficulty == DifficultyLevel.FIGHTER
            ? "РЕКОМЕНДОВАНО"
            : "РЕЖИМ", game.getSkin(), "small");
        marker.setAlignment(Align.center);
        marker.setColor(difficulty.accentColor);
        card.add(marker).growX().height(24f).row();

        Label title = new Label(difficulty.title, game.getSkin(), "heading");
        title.setAlignment(Align.center);
        title.setWrap(true);
        title.setColor(difficulty.accentColor);
        title.setFontScale(0.92f);
        card.add(title).width(380f).height(58f).padTop(4f).row();

        Label description = new Label(difficulty.description, game.getSkin());
        description.setAlignment(Align.center);
        description.setWrap(true);
        description.setFontScale(0.86f);
        card.add(description).width(365f).height(105f).padTop(6f).row();

        Table stats = new Table();
        stats.setBackground(game.getSkin().getDrawable("panel"));
        stats.pad(12f);
        addStat(stats, "Кількість ворогів", difficulty.enemyCountMultiplier);
        addStat(stats, "Здоров'я ворогів", difficulty.enemyHealthMultiplier);
        addStat(stats, "Швидкість ворогів", difficulty.enemySpeedMultiplier);
        addStat(stats, "Шкода ворогів", difficulty.enemyDamageMultiplier);
        addStat(stats, "Нагороди", difficulty.rewardMultiplier);
        card.add(stats).width(365f).height(155f).padTop(8f).row();

        TextButton choose = button("ОБРАТИ", () -> game.startGame(difficulty));
        choose.getLabel().setFontScale(0.82f);
        card.add(choose).width(300f).height(62f).padTop(12f);
        return card;
    }

    private void addStat(Table table, String name, float multiplier) {
        Label nameLabel = new Label(name, game.getSkin(), "small");
        Label valueLabel = new Label(formatPercent(multiplier), game.getSkin(), "small");
        valueLabel.setColor(multiplier > 1f
            ? Color.valueOf("FF8A9D")
            : multiplier < 1f ? Color.valueOf("73F3C0") : Color.WHITE);
        table.add(nameLabel).left().expandX();
        table.add(valueLabel).right().row();
    }

    private String formatPercent(float multiplier) {
        int percent = Math.round((multiplier - 1f) * 100f);
        if (percent == 0) return "СТАНДАРТ";
        return (percent > 0 ? "+" : "") + percent + "%";
    }

    private TextButton button(String text, Runnable action) {
        TextButton button = new TextButton(text, game.getSkin());
        button.getLabel().setAlignment(Align.center);
        button.getLabel().setWrap(true);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                action.run();
            }
        });
        return button;
    }
}
