package Services.screens;

import Services.Main;
import Services.stub.GameStateStub;
import Services.ui.FloatingText;
import Services.ui.GameHUD;
import Services.ui.SettingsWindow;
import Services.ui.UiSkinFactory;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import java.math.BigDecimal;

/**
 * UI integration sandbox for the combat screen.
 *
 * Replace the center placeholder with the team's world renderer. GameHUD remains a
 * separate actor above it, so combat code never needs to own HUD layout.
 */
public class GameScreen extends BaseScreen {
    private final GameStateStub state;
    private final GameHUD hud;
    private float hudRefresh;

    public GameScreen(Main game) {
        super(game);
        state = game.getGameState();
        buildArenaPlaceholder();
        hud = new GameHUD(game.getSkin(), state);
        stage.addActor(hud);
    }

    private void buildArenaPlaceholder() {
        Table arena = new Table();
        arena.setFillParent(true);
        arena.setBackground(game.getSkin().getDrawable("dark"));
        arena.padTop(180f);
        stage.addActor(arena);

        Table room = new Table();
        room.setBackground(game.getSkin().getDrawable("panel"));
        room.add(new Label("ДИНАМІЧНА SCI-FI КІМНАТА 64 x 64", game.getSkin(), "heading")).padBottom(18f).row();
        room.add(new Label("Тимчасова зона бойового рендерера", game.getSkin(), "small")).padBottom(42f).row();

        TextButton collect = actionButton("ЗІБРАТИ +1 КРЕДИТ", () -> {
            state.addCredits(BigDecimal.ONE);
            showFloating("+1 КРЕДИТ", collectAnchor);
        });
        collectAnchor = collect;
        room.add(collect).width(370f).height(72f).pad(8f).row();

        TextButton xp = actionButton("ОТРИМАТИ +10 ДОСВІДУ", () -> {
            state.addExperience(10);
            showFloating("+10 ДОСВІДУ", xpAnchor);
        });
        xpAnchor = xp;
        room.add(xp).width(370f).height(72f).pad(8f).row();

        Table lowerActions = new Table();
        lowerActions.add(actionButton("ОТРИМАТИ 1/4 ШКОДИ", state::damageOneQuarter))
            .width(280f).height(64f).pad(6f);
        lowerActions.add(actionButton("ВІДНОВИТИ 1/4", state::healOneQuarter))
            .width(240f).height(64f).pad(6f);
        room.add(lowerActions).row();

        room.add(actionButton("ЗАВЕРШИТИ ХВИЛЮ / ПОКРАЩЕННЯ", () -> {
            state.nextWave();
            game.showUpgrades();
        })).width(530f).height(76f).padTop(22f).row();

        Table navigation = new Table();
        navigation.add(actionButton("НАЛАШТУВАННЯ", () ->
            new SettingsWindow(game.getSkin(), state, hud::refresh).showCentered(stage)))
            .width(220f).height(60f).pad(6f);
        navigation.add(actionButton("ГОЛОВНЕ МЕНЮ", game::showMainMenu))
            .width(220f).height(60f).pad(6f);
        room.add(navigation).padTop(14f);
        arena.add(room).width(920f).height(650f);
    }

    private Actor collectAnchor;
    private Actor xpAnchor;

    private TextButton actionButton(String text, Runnable action) {
        TextButton button = new TextButton(text, game.getSkin());
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                action.run();
                hudRefresh = 1f;
            }
        });
        return button;
    }

    private void showFloating(String text, Actor source) {
        Vector2 position = source.localToStageCoordinates(
            new Vector2(source.getWidth() / 2f, source.getHeight()));
        FloatingText.show(stage, game.getSkin(), text, position.x, position.y, UiSkinFactory.GOLD);
    }

    @Override
    public void render(float delta) {
        state.update(delta);
        hudRefresh += delta;
        if (hudRefresh >= 0.1f) {
            hud.refresh();
            hudRefresh = 0f;
        }
        super.render(delta);
    }
}
