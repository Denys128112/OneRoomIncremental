package screens;

import Services.Main;
import com.badlogic.gdx.graphics.Color;
import ui.SettingsWindow;
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

        Stack logo = new Stack();
        logo.add(new Image(game.getSkin().getDrawable("logo-frame")));
        Table titleLayer = new Table();
        Label title = new Label("ОДИН В КІМНАТІ", game.getSkin(), "title");
        title.setAlignment(Align.center);
        title.setWrap(true);
        Label subtitle = new Label("ІНКРЕМЕНТАЛЬНА ГРА", game.getSkin(), "heading");
        subtitle.setColor(Color.BLUE);
        subtitle.setAlignment(Align.center);
        subtitle.setWrap(true);
        titleLayer.add(title).width(860f).row();
        titleLayer.add(subtitle).width(860f).padTop(20f);
        logo.add(titleLayer);
        root.add(logo).width(970f).height(320f).padBottom(24f).row();

        addButton(root, "ПОЧАТИ ГРУ", game::showGame);
        addButton(root, "НАЛАШТУВАННЯ", () ->
            new SettingsWindow(game.getSkin(), game.getGameState(), () -> { }).showCentered(stage));
        addButton(root, "ВИХІД", Gdx.app::exit);
        root.add(new Label("SCI-FI АРЕНА-РОГАЛИК // ПРОТОТИП UI", game.getSkin(), "small"))
            .padTop(30f);
    }

    private void addButton(Table root, String text, Runnable action) {
        TextButton button = new TextButton(text, game.getSkin());
        button.getLabel().setAlignment(Align.center);
        button.getLabel().setWrap(true);
        button.getLabel().setFontScale(0.9f);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                action.run();
            }
        });
        root.add(button).width(480f).height(82f).pad(9f).row();
    }
}
