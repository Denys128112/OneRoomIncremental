package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import java.util.ArrayList;
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
    private Enemy testEnemy;
    private Vector3 mousePos = new Vector3();
    private ArrayList<Projectile> projectiles;

    @Override
    public void create() {
        map = new TmxMapLoader().load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        Map.createVisualMap(map);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();





        shapeRenderer = new ShapeRenderer();
        player = new Player(400, 300);
        testEnemy = new Enemy(200, 200);
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
        testEnemy.update(deltaTime);
        for (Projectile p : projectiles) {
            p.update(deltaTime);
        }



        camera.position.set(player.getX() + 16, player.getY() + 16, 0);





        camera.update();
        renderer.setView(camera);
        renderer.render();



        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        testEnemy.render(shapeRenderer);
        for (Projectile p : projectiles) {
            p.render(shapeRenderer);
        }
        player.render(shapeRenderer);

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();



        shapeRenderer.dispose();


    }
}
