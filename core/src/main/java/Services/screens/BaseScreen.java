package Services.screens;

import Services.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Shared adaptive Stage.
 *
 * The starter project had no Viewport. FitViewport keeps the 1600x900 UI aspect
 * ratio on any window without stretching; letterboxing is preferable to distortion.
 */
abstract class BaseScreen extends ScreenAdapter {
    protected static final float UI_WIDTH = 1600f;
    protected static final float UI_HEIGHT = 900f;

    protected final Main game;
    protected final Stage stage;

    protected BaseScreen(Main game) {
        this.game = game;
        stage = new Stage(new FitViewport(UI_WIDTH, UI_HEIGHT));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.02f, 0.035f, 0.075f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(delta, 1f / 20f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
