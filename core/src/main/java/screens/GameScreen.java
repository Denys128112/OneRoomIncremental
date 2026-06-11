package screens;

import Services.GameManager;
import Services.Main;
import Services.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import stub.GameStateStub;
import ui.GameHUD;

public class GameScreen extends BaseScreen {
    private static final float TRANSITION_DURATION = 1f;

    private enum ScreenState {
        PLAYING,
        FADING_OUT,
        FADING_IN
    }

    private final GameStateStub state;
    private final GameHUD hud;
    private final Vector3 mousePos = new Vector3();
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;
    private final OrthographicCamera camera;
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private final GameManager gameManager;

    private float hudRefresh;
    private float transitionTimer;
    private ScreenState currentState = ScreenState.PLAYING;

    public GameScreen(Main game) {
        super(game);
        state = game.getGameState();
        map = new TmxMapLoader().load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        Map.createVisualMap(map);
        gameManager = new GameManager(map, state);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.45f;
        camera.update();

        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        hud = new GameHUD(game.getSkin(), state);
        stage.addActor(hud);
    }

    @Override
    public void render(float delta) {
        state.update(delta);
        updateHud(delta);
        updateAimAndInput();

        if (currentState == ScreenState.PLAYING) {
            gameManager.update(delta);
            if (gameManager.isWaveCleared) {
                currentState = ScreenState.FADING_OUT;
                transitionTimer = 0f;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            game.showSkillTree();
        }

        drawWorld();
        updateAndDrawTransition(delta);
        stage.act(delta);
        stage.draw();
    }

    private void updateHud(float delta) {
        hudRefresh += delta;
        if (hudRefresh >= 0.1f) {
            hud.refresh();
            hudRefresh = 0f;
        }
    }

    private void updateAimAndInput() {
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);
        gameManager.player.lookAt(mousePos.x, mousePos.y);
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            gameManager.shootProjectile();
            gameManager.player.attack();
        }
    }

    private void drawWorld() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(
            gameManager.player.getX() + 16f,
            gameManager.player.getY() + 16f,
            0f
        );
        camera.update();
        renderer.setView(camera);
        renderer.render();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        gameManager.drawEntities(shapeRenderer);
        shapeRenderer.end();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        gameManager.drawSprites(spriteBatch);
        spriteBatch.end();
    }

    private void updateAndDrawTransition(float delta) {
        if (currentState == ScreenState.PLAYING) return;
        transitionTimer += delta;

        if (currentState == ScreenState.FADING_OUT
            && transitionTimer >= TRANSITION_DURATION) {
            gameManager.startNewWave();
            currentState = ScreenState.FADING_IN;
            transitionTimer = 0f;
        } else if (currentState == ScreenState.FADING_IN
            && transitionTimer >= TRANSITION_DURATION) {
            currentState = ScreenState.PLAYING;
            transitionTimer = 0f;
            return;
        }

        float alpha = Math.min(transitionTimer / TRANSITION_DURATION, 1f);
        if (currentState == ScreenState.FADING_IN) alpha = 1f - alpha;
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.getProjectionMatrix().setToOrtho2D(
            0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()
        );
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, alpha);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void dispose() {
        gameManager.dispose();
        map.dispose();
        renderer.dispose();
        shapeRenderer.dispose();
        spriteBatch.dispose();
        super.dispose();
    }
}
