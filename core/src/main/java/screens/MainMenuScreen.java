package screens;

import Services.AudioManager;
import Services.Main;
import com.badlogic.gdx.graphics.Color;
import ui.SettingsWindow;
import ui.InstructionsWindow;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

/** Start menu with the requested start, settings and exit actions. */
public class MainMenuScreen extends BaseScreen {
    public MainMenuScreen(Main game) {
        super(game);
        build();
        Services.AudioManager.playMusic(Services.AudioManager.menuMusic);
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
        dimLayer.setColor(1f, 1f, 1f, 0.62f);
        screen.add(dimLayer);

        Table root = new Table();
        screen.add(root);

        // 1. КОНТЕЙНЕР ЛОГОТИПУ
        Stack logo = new Stack();
        logo.add(new Image(game.getSkin().getDrawable("logo-frame")));

        Table titleLayer = new Table();
        titleLayer.top().padTop(90f); // Фіксуємо верхні написи трохи вище центру

        Label title = new Label("ОДИН В КІМНАТІ", game.getSkin(), "title");
        title.setAlignment(Align.center);
        title.setWrap(true);

        Label subtitle = new Label("ІНКРЕМЕНТАЛЬНА ГРА", game.getSkin(), "heading");
        subtitle.setColor(Color.BLUE);
        subtitle.setAlignment(Align.center);
        subtitle.setWrap(true);

        // Наш напис розробників
        Label credits = new Label("Розробники: Самойлич Софія, Колісник Денис, Коротков Андрій, Топчий Віолетта", game.getSkin(), "small");
        credits.setColor(Color.LIGHT_GRAY);
        credits.setAlignment(Align.center);
        credits.setWrap(true);
        credits.setFontScale(1f); // Акуратний розмір, щоб ідеально вписатися в нижнє звуження

        // Збираємо таблицю
        titleLayer.add(title).width(900f).row();
        titleLayer.add(subtitle).width(860f).padTop(30f).row();

        // Великий відступ padTop(60f) штовхає цей рядок вниз, прямо у намальовану ручкою зону трапеції
        titleLayer.add(credits).width(860f).padTop(35f);

        logo.add(titleLayer);

        // Тримаємо базові розміри рамки, щоб вона не деформувалася
        root.add(logo).width(970f).height(320f).padBottom(24f).row();

        // 2. КНОПКИ МЕНЮ
        addButton(root, "ПОЧАТИ ГРУ", game::showDifficultySelection);
        addButton(root, "ІНСТРУКЦІЯ", () ->
            new InstructionsWindow(
                game.getSkin(),
                game.getGameState().getLevelManager().getDifficulty(),
                "ЗАКРИТИ",
                () -> { }
            ).showCentered(stage));
        addButton(root, "НАЛАШТУВАННЯ", () ->
            new SettingsWindow(game.getSkin(), game.getGameState(), () -> { }).showCentered(stage));
        addButton(root, "ВИХІД", Gdx.app::exit);
    }

    private void addButton(Table root, String text, Runnable action) {
        TextButton button = new TextButton(text, game.getSkin());
        button.getLabel().setAlignment(Align.center);
        button.getLabel().setWrap(true);
        button.getLabel().setFontScale(0.9f);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound(AudioManager.uiClick);
                action.run();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if (pointer == -1) {
                    AudioManager.playSound(AudioManager.uiHover);
                }
            }
        });

        root.add(button).width(480f).height(82f).pad(9f).row();
    }

}
