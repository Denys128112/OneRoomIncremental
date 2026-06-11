package screens;

import Services.Main;
import stub.GameStateStub;
import ui.GameHUD;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entities.Enemy;
import entities.Player;
import entities.Projectile;

import Services.Map;
import Services.CollisionChecker;
import Services.EnemyGenerator;

/**
 * Combat screen інтегрований з реальним ігровим рендерером.
 * HUD залишається окремим актором поверх ігрового світу.
 */
public class GameScreen extends BaseScreen {
    private final GameStateStub state;
    private final GameHUD hud;
    private float hudRefresh;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;
    private Player player;
    private Vector3 mousePos = new Vector3();
    private ArrayList<Projectile> projectiles;
    private EnemyGenerator enemyGenerator;
    private List<Enemy> enemies;

    public GameScreen(Main game) {
        super(game);
        this.state = game.getGameState();
        map = new TmxMapLoader().load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        Map.createVisualMap(map);
        player = new Player(400, 300);
        resolvePlayerCoordinates();
        enemyGenerator = new EnemyGenerator();
        enemies = enemyGenerator.enemyList;
        enemyGenerator.generate(Map.map, 1,player);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1F;
        camera.update();
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        projectiles = new ArrayList<>();

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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);
        player.lookAt(mousePos.x, mousePos.y);
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float startX = player.getX() + 16;
            float startY = player.getY() + 16;
            projectiles.add(new Projectile(startX, startY, player.getRotation(),20));
            player.attack();
        }
        player.update(delta);
        Iterator<Projectile> iter = projectiles.iterator();
        while (iter.hasNext()) {
            Projectile p = iter.next();
            p.update(delta);

            if (CollisionChecker.isCollisionWithWall(p)) {
                iter.remove();
            } else {
                Enemy e = checkWithEnemy(p);
                if (e != null) {
                    e.takeDamage(p.getDamage());
                    iter.remove();
                }
            }

        }
        camera.position.set(player.getX() + 16, player.getY() + 16, 0);
        camera.update();
        renderer.setView(camera);
        renderer.render();

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.update(delta);
            if (enemy.isReadyForRemoval()) {
                enemy.dispose();
                enemyIterator.remove();
            }
        }

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Projectile p : projectiles) {
            p.render(shapeRenderer);
        }
        player.render(shapeRenderer);
        enemyGenerator.render(shapeRenderer);
        shapeRenderer.end();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        player.render(spriteBatch);
        for (Enemy enemy : enemies) {
            enemy.render(spriteBatch);
        }
        spriteBatch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            game.showSkillTree();
        }
        if (stage != null) {
            stage.act(delta);
            stage.draw();
        }
    }
    private void resolvePlayerCoordinates(){
        float x= player.getX();
        float y= player.getY();
        int xGrid = (int) (x / 16);
        int yGrid = (int) (y / 16);
        if(Map.map[xGrid][yGrid] !=0){
            while(Map.map[xGrid][yGrid] !=0){
                xGrid--;
                yGrid--;
            }
            x=xGrid*16;
            y=yGrid*16;
            player.setX(x);
            player.setY(y);
        }
    }
    private Enemy checkWithEnemy(Projectile p) {
        for (Enemy e : enemies) {
            if (!e.isDead() && CollisionChecker.isCollision(p, e)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public void dispose() {
        if (map != null) map.dispose();
        if (renderer != null) renderer.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (spriteBatch != null) spriteBatch.dispose();
        if (player != null) player.dispose();
        if (enemies != null) {
            for (Enemy enemy : enemies) enemy.dispose();
        }
        if (enemyGenerator != null) enemyGenerator.dispose();
        super.dispose();
    }
}
