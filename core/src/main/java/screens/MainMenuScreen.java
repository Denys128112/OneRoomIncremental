package screens;

import Services.Main;
import ui.SettingsWindow;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/** Start menu with the requested start, settings and exit actions. */
public class MainMenuScreen extends BaseScreen {
    public MainMenuScreen(Main game) {
        super(game);
        build();
    }

    private void build() {
        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(game.getSkin().getDrawable("dark"));
        stage.addActor(root);

        Stack logo = new Stack();
        logo.add(new Image(game.getSkin().getDrawable("logo-frame")));
        Table titleLayer = new Table();
        titleLayer.add(new Label("ОДИН В КІМНАТІ", game.getSkin(), "title")).row();
        titleLayer.add(new Label("ІНКРЕМЕНТАЛЬНА ГРА", game.getSkin(), "heading")).padTop(10f);
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
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                action.run();
            }
        });
        root.add(button).width(480f).height(82f).pad(9f).row();
    }
}
