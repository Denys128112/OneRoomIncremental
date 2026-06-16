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
import ui.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

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
    private final EnemyTrackerPanel enemyTracker;

    private float hudRefresh;
    private float transitionTimer;
    private ScreenState currentState = ScreenState.PLAYING;
    private InstructionsWindow instructionsWindow;
    private boolean instructionsOpen;
    private boolean resultOpen;

    private SkillHotbarPanel skillHotbar;

    public static float mouseWorldX, mouseWorldY;

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
        enemyTracker = new EnemyTrackerPanel(game.getSkin(), GameManager.enemies);
        stage.addActor(enemyTracker);
        skillHotbar = new SkillHotbarPanel(game.getSkin(), stage);
        addInstructionsButton();

        checkMusic();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            toggleInstructions();
        }

        if (!isOverlayOpen()) {
            state.update(delta);
        }
        updateHud(delta);

        skills.MageSkills.Element el = skills.MageSkills.Element.FIRE;
        if (gameManager.player.skills != null) el = gameManager.player.skills.mage.getElement();
        skillHotbar.refresh(gameManager.player.selectedSlot, el);

        if (!isOverlayOpen()) updateAimAndInput();

        if (!isOverlayOpen() && currentState == ScreenState.PLAYING) {
            gameManager.update(delta);
            if (state.isPlayerDead()) {
                showRunResult(false);
                drawWorld();
                stage.act(delta);
                stage.draw();
                return;
            }
            if (game.areAllSkillsUnlocked()) {
                showRunResult(true);
                drawWorld();
                stage.act(delta);
                stage.draw();
                return;
            }
            if (gameManager.isWaveCleared) {
                currentState = ScreenState.FADING_OUT;
                transitionTimer = 0f;
            }
        }
        gameManager.handleChests(delta);
        if (!isOverlayOpen() && Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            game.showSkillTree();
            return;
        }

        drawWorld();
        if (!isOverlayOpen()) {
            updateAndDrawTransition(delta);
        }
        drawInventory();
        stage.act(delta);
        stage.draw();

    }

    private void addInstructionsButton() {
        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.bottom().right().pad(22f);

        TextButton button = new TextButton("ІНСТРУКЦІЯ [H]", game.getSkin());
        button.getLabel().setAlignment(Align.center);
        button.getLabel().setFontScale(0.68f);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleInstructions();
            }
        });
        overlay.add(button).width(255f).height(58f);
        stage.addActor(overlay);
    }

    private void toggleInstructions() {
        if (instructionsOpen) {
            instructionsWindow.close();
            return;
        }
        instructionsOpen = true;
        instructionsWindow = new InstructionsWindow(
            game.getSkin(),
            state.getLevelManager().getDifficulty(),
            "ПОВЕРНУТИСЯ ДО ГРИ",
            () -> {instructionsOpen = false;
                instructionsWindow = null;
            }
        );
        instructionsWindow.showCentered(stage);
    }

    private void updateHud(float delta) {
        hudRefresh += delta;
        if (hudRefresh >= 0.1f) {
            hud.refresh();
            enemyTracker.refresh();
            hudRefresh = 0f;
        }
    }

    private boolean isOverlayOpen() {
        return resultOpen || instructionsOpen || enemyTracker.isDetailsOpen();
    }

    private void showRunResult(boolean victory) {
        if (resultOpen) return;
        resultOpen = true;
        if (victory) {
            Services.AudioManager.playSound(Services.AudioManager.sysLevelUp);
        } else {
            Services.AudioManager.playSound(Services.AudioManager.playerDeath);
        }
        new RunResultWindow(
            game.getSkin(),
            victory,
            state.getWave(),
            state.getWaveSeconds(),
            state.getLevelManager().getDifficulty(),
            game::restartCurrentRun,
            game::showMainMenu
        ).showCentered(stage);
    }

    private void updateAimAndInput() {
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);
        GameScreen.mouseWorldX = mousePos.x;
        GameScreen.mouseWorldY = mousePos.y;
        gameManager.player.lookAt(mousePos.x, mousePos.y);
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
            checkMusic();
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


    private void drawInventory() {
        int slotSize = 45;
        int spacing = 8;
        int totalWidth = (slotSize * 4) + (spacing * 3);

        float worldWidth = stage.getViewport().getWorldWidth();
        float worldHeight = stage.getViewport().getWorldHeight();

        float startX = (worldWidth - totalWidth) / 2f;
        float startY = 20f;

        shapeRenderer.setProjectionMatrix(stage.getViewport().getCamera().combined);
        spriteBatch.setProjectionMatrix(stage.getViewport().getCamera().combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        for (int i = 0; i < 4; i++) {
            float slotX = startX + i * (slotSize + spacing);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            if (gameManager.player.inventory[i] != null) {
                shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 0.7f);
            } else {
                shapeRenderer.setColor(0.08f, 0.08f, 0.08f, 0.6f);
            }
            shapeRenderer.rect(slotX, startY, slotSize, slotSize);
            shapeRenderer.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            if (gameManager.player.selectedSlot == i) {
                shapeRenderer.setColor(1f, 0.8f, 0f, 1f);
                shapeRenderer.rect(slotX, startY, slotSize, slotSize);
                shapeRenderer.rect(slotX + 1, startY + 1, slotSize - 2, slotSize - 2);
            } else {
                shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 0.8f);
                shapeRenderer.rect(slotX, startY, slotSize, slotSize);
            }
            shapeRenderer.end();
        }
        Gdx.gl.glDisable(GL20.GL_BLEND);

        spriteBatch.begin();
        com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle labelStyle = game.getSkin().get(com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle.class);
        com.badlogic.gdx.graphics.g2d.BitmapFont font = labelStyle.font;
        font.setColor(com.badlogic.gdx.graphics.Color.WHITE);

        for (int i = 0; i < 4; i++) {
            float slotX = startX + i * (slotSize + spacing);
            String text = String.valueOf(i + 1);

            font.draw(spriteBatch, text, slotX + 19f, startY + 29f);
        }
        spriteBatch.end();
    }


    private void checkMusic() {
        if (GameStateStub.wave > 0 && GameStateStub.wave % 5 == 0) {
            Services.AudioManager.playMusic(Services.AudioManager.bossMusic);
        } else {
            Services.AudioManager.playMusic(Services.AudioManager.battleMusic);
        }
    }

    @Override
    public void dispose() {
        enemyTracker.dispose();
        gameManager.dispose();
        map.dispose();
        renderer.dispose();
        shapeRenderer.dispose();
        spriteBatch.dispose();
        skillHotbar.dispose();
        super.dispose();
    }
}
