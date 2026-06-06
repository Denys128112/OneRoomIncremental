package Services;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import java.util.ArrayList;
import java.util.List;

import entities.Enemy;
import entities.Player;
import entities.Projectile;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    private ShapeRenderer shapeRenderer;
    private Player player;
    private Vector3 mousePos = new Vector3();
    private ArrayList<Projectile> projectiles;
    private EnemyGenerator enemyGenerator;
    private List<Enemy> enemies;
    @Override
    public void create() {
        map = new TmxMapLoader().load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        Map.createVisualMap(map);
        enemyGenerator=new EnemyGenerator();
        enemies=enemyGenerator.enemyList;
        enemyGenerator.generate(Map.map,1);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.4F;
        camera.update();
        shapeRenderer = new ShapeRenderer();
        player = new Player(400, 300);
        projectiles = new ArrayList<>();


    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        float deltaTime = Gdx.graphics.getDeltaTime();



        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);
        player.lookAt(mousePos.x, mousePos.y);

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float startX = player.getX() + 16;
            float startY = player.getY() + 16;
            projectiles.add(new Projectile(startX, startY, player.getRotation()));
        }
        player.update(deltaTime);
        java.util.Iterator<Projectile> iter = projectiles.iterator();
        while (iter.hasNext()) {
            Projectile p = iter.next();
            p.update(deltaTime);
            if (CollisionChecker.isCollisionWithWall(p)) {
                iter.remove();
                System.out.println("collision");
            }else {
                Enemy e = checkWithEnemy(p);
                if (e != null) {
                    enemies.remove(e);
                    iter.remove();
                }
            }
        }
        camera.position.set(player.getX() + 16, player.getY() + 16, 0);
        camera.update();
        renderer.setView(camera);
        renderer.render();


        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Projectile p : projectiles) {
            p.render(shapeRenderer);
        }
        player.render(shapeRenderer);
        enemyGenerator.render(shapeRenderer);
        shapeRenderer.end();
    }

    private Enemy checkWithEnemy(Projectile p) {
        for(Enemy e:enemies) {
            if (CollisionChecker.isCollision(p, e)) {
                return e;
            }
        }
        System.out.println(p);
        return null;
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        shapeRenderer.dispose();
    }
}
