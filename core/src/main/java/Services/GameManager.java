package Services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entities.*;
import stub.GameStateStub;

import static Services.Map.boxes;
import static Services.Map.chests;

public class GameManager {
    public static List<Enemy> enemiesToAdd=new ArrayList<>();
    public static List<Coin> coins=new ArrayList<>();
    public static List<Heart> hearts=new ArrayList<>();
    public Player player;
    public static List<Projectile> projectiles;
    public static List<Enemy> enemies;
    public static List<Enemy> deadEnemies;

    private EnemyGenerator enemyGenerator;
    private TiledMap map;
    private final GameStateStub state;
    public boolean isWaveCleared = false;

    public GameManager(TiledMap map, GameStateStub state) {
        this.map = map;
        this.state = state;
        this.player = new Player(400, 300);
        this.projectiles = new ArrayList<>();
        this.deadEnemies = new ArrayList<>();
        this.enemyGenerator = new EnemyGenerator(state.getLevelManager());
        resolvePlayerCoordinates();
        this.enemies = enemyGenerator.enemyList;
        enemyGenerator.generate(Map.map, GameStateStub.wave, player);
    }

    public void update(float delta) {
        player.update(delta);
        handlePickups(delta);
        for (Box b : Map.boxes) {
            b.update(delta);
        }
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.update(delta);
            if (enemy.isReadyForRemoval()) {
                state.addExperience(enemy.getExperienceReward());
                enemy.dropLoot();
                deadEnemies.add(enemy);
                enemyIterator.remove();
            }
        }
        handleCollisions(delta);
        if (!enemiesToAdd.isEmpty()) {
            enemies.addAll(enemiesToAdd);
            enemiesToAdd.clear();
        }
        if (enemies.isEmpty()) {
            isWaveCleared = true;
        }
    }



    private void handleCollisions(float delta) {
        Iterator<Projectile> iter = projectiles.iterator();
        while (iter.hasNext()) {
            Projectile p = iter.next();
            p.update(delta);

            if (CollisionChecker.isCollisionWithWall(p)) {
                iter.remove();
                continue;
            }

            if (p.isEnemyProjectile) {
                if (p.bounds.overlaps(player.bounds)) {
                    player.takeDamage(p.getDamage());
                    iter.remove();
                }
            }
            else {
                boolean bulletDestroyed = false;
                for (Enemy e : enemies) {
                    if (e.isDead()) continue;
                    if (CollisionChecker.isCollision(p, e) && !p.hitEnemies.contains(e)) {
                        e.takeDamage(p.getDamage());
                        p.hitEnemies.add(e);

                        if (!p.isPiercing) {
                            iter.remove();
                            bulletDestroyed = true;
                            break;
                        }
                    }
                }

                if (bulletDestroyed) continue;

                for (Box b : Map.boxes) {
                    if (!b.isBroken && !b.isBreaking && CollisionChecker.isCollision(p, b)) {
                        b.interact();
                        if (!p.isPiercing) {
                            iter.remove();
                            bulletDestroyed = true;
                            break;
                        }
                    }
                }
                if (bulletDestroyed) continue;
            }
        }
    }

    public void drawEntities(ShapeRenderer shapeRenderer) {
        for (Projectile p : projectiles) {
            p.render(shapeRenderer);
        }
        player.render(shapeRenderer);
        enemyGenerator.render(shapeRenderer);
    }

    public void drawSprites(Batch batch) {
        for (Chest chest : chests) {
            chest.render(batch);
        }
        for(Box b:Map.boxes){
            b.render(batch);
        }
        enemyGenerator.render(batch);
        for (Coin c : coins) {
            c.render(batch);
        }
        for (Heart h : hearts) {
            h.render(batch);
        }
        player.render(batch);
    }

    public void startNewWave() {
        Map.boxes.clear();
        Map.chests.clear();
        coins.clear();
        hearts.clear();
        state.nextWave();
        for (Enemy enemy : deadEnemies) enemy.dispose();
        deadEnemies.clear();
        isWaveCleared = false;
        Map.createVisualMap(map);
        resolvePlayerCoordinates();
        enemyGenerator.generate(Map.map, state.getWave(), player);

    }

    public void shootProjectile() {
        player.attack();
    }


    private void resolvePlayerCoordinates() {
        int cx = (int) (player.getX() / 16f) - 1;
        int cy = (int) (player.getY() / 16f) - 1;
        if (cx < 0) cx = 0;
        if (cy < 0) cy = 0;
        if (cx >= Map.SIZE) cx = Map.SIZE - 1;
        if (cy >= Map.SIZE) cy = Map.SIZE - 1;
        if (Map.map[cx][cy] == 0) return;
        int radius = 1;
        outer:
        while (radius < 20) {
            int minX = Math.max(0, cx - radius);
            int maxX = Math.min(Map.SIZE - 1, cx + radius);
            int minY = Math.max(0, cy - radius);
            int maxY = Math.min(Map.SIZE - 1, cy + radius);

            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    if (Map.map[x][y] == 0) {
                        cx = x;
                        cy = y;
                        break outer;
                    }
                }
            }
            radius++;
        }
        float newX = (cx + 1) * 16f;
        float newY = (cy + 1) * 16f;
        player.setX(newX);
        player.setY(newY);
        player.bounds.x = newX + 2f;
        player.bounds.y = newY + 2f;
    }
    public void handleChests(float delta) {
        for (Chest c : chests) {
            c.update(delta);
            if (CollisionChecker.isCollision(player, c) && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                c.interact();
            }
        }
    }
    private void handlePickups(float delta) {
        float magnetRadius = 70f; // Відстань, на якій починає діяти магніт (в пікселях)
        float magnetSpeed = 180f; // Швидкість польоту монетки до гравця


        Iterator<Coin> coinIter = coins.iterator();

        while (coinIter.hasNext()) {
            Coin c = coinIter.next();
            c.update(delta);
            float playerCenterX = player.getX() + 8f;
            float playerCenterY = player.getY() + 8f;
            float coinCenterX = c.getX() + 8f;
            float coinCenterY = c.getY() + 8f;

            float dx = playerCenterX - coinCenterX;
            float dy = playerCenterY - coinCenterY;

            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance < magnetRadius && distance > 0) {

                float moveX = (dx / distance) * magnetSpeed * delta;
                float moveY = (dy / distance) * magnetSpeed * delta;

                c.setX(c.getX() + moveX);
                c.setY(c.getY() + moveY);
                c.bounds.x = c.getX();
                c.bounds.y = c.getY();
            }

            if (CollisionChecker.isCollision(player, c)) {
                state.addCredits(BigDecimal.valueOf(5));
                coinIter.remove();
            }
        }
        Iterator<Heart> heartIter = hearts.iterator();
        while (heartIter.hasNext()) {
            Heart h = heartIter.next();
            h.update(delta);

            float playerCenterX = player.getX() + 8f;
            float playerCenterY = player.getY() + 8f;
            float heartCenterX = h.getX() + 4f;
            float heartCenterY = h.getY() + 4f;
            float dx = playerCenterX - heartCenterX;
            float dy = playerCenterY - heartCenterY;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance < magnetRadius && distance > 0) {
                float moveX = (dx / distance) * magnetSpeed * delta;
                float moveY = (dy / distance) * magnetSpeed * delta;
                h.setX(h.getX() + moveX);
                h.setY(h.getY() + moveY);
                h.bounds.x = h.getX();
                h.bounds.y = h.getY();
            }

            if (CollisionChecker.isCollision(player, h)) {
                state.healOneQuarter();
                heartIter.remove();
            }
        }
    }
    public void dispose() {
        player.dispose();
        for (Enemy enemy : enemies) enemy.dispose();
        for (Enemy enemy : deadEnemies) enemy.dispose();
        enemyGenerator.dispose();
    }
}
