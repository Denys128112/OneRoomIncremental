package screens;

import Services.Main;
import Services.GameManager; // Імпортуємо наш новий клас
import Services.Map;
import stub.GameStateStub;
import ui.GameHUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

public class GameScreen extends BaseScreen {
    private final GameStateStub state;
    private final GameHUD hud;
    private float hudRefresh;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private Vector3 mousePos = new Vector3();
    private GameManager gameManager;
    private enum ScreenState { PLAYING, FADING_OUT, FADING_IN }
    private ScreenState currentState = ScreenState.PLAYING;
    private float transitionTimer = 0f;
    private final float TRANSITION_DURATION = 1.0f;

    public GameScreen(Main game) {
        super(game);
        this.state = game.getGameState();
        map = new TmxMapLoader().load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        Map.createVisualMap(map);
        gameManager = new GameManager(map);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.45F;
        camera.update();

        shapeRenderer = new ShapeRenderer();

        hud = new GameHUD(game.getSkin(), state);
        stage.addActor(hud);
    }

    @Override
    public void render(float delta) {
        state.update(delta);
        hudRefresh += delta;
        if (hudRefresh >= 0.1f) {
            hud.refresh();
            hudRefresh = 0f;
        }
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);
        gameManager.player.lookAt(mousePos.x, mousePos.y);

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            gameManager.shootProjectile();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            game.showSkillTree();
        }

        if (currentState == ScreenState.PLAYING) {
            gameManager.update(delta);

            if (gameManager.isWaveCleared) {
                currentState = ScreenState.FADING_OUT;
                transitionTimer = 0f;
            }
        }


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(gameManager.player.getX() + 16, gameManager.player.getY() + 16, 0);
        camera.update();
        renderer.setView(camera);
        renderer.render();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        gameManager.drawEntities(shapeRenderer);

        shapeRenderer.end();

        fadeOut(delta);

        if (stage != null) {
            stage.act(delta);
            stage.draw();
        }
    }

    private void fadeOut(float delta) {
        if (currentState == ScreenState.PLAYING) return;

        transitionTimer += delta;

        if (currentState == ScreenState.FADING_OUT && transitionTimer >= TRANSITION_DURATION) {
            gameManager.startNewWave();
            currentState = ScreenState.FADING_IN;
            transitionTimer = 0f;
        } else if (currentState == ScreenState.FADING_IN && transitionTimer >= TRANSITION_DURATION) {
            currentState = ScreenState.PLAYING;
        }

        if (currentState != ScreenState.PLAYING) {
            float alpha = Math.min(transitionTimer / TRANSITION_DURATION, 1f);
            float currentAlpha = (currentState == ScreenState.FADING_OUT) ? alpha : (1f - alpha);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, currentAlpha);
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    @Override
    public void dispose() {
        if (map != null) map.dispose();
        if (renderer != null) renderer.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        super.dispose();
    }
}
