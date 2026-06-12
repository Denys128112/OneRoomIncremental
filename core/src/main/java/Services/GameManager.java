package Services;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entities.Enemy;
import entities.Player;
import entities.Projectile;
import stub.GameStateStub;

public class GameManager {
    public static List<Enemy> enemiesToAdd=new ArrayList<>();
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
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.update(delta);
            if (enemy.isReadyForRemoval()) {
                state.addExperience(enemy.getExperienceReward());
                state.addCredits(BigDecimal.valueOf(enemy.getCreditReward()));
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
        player.render(batch);
        enemyGenerator.render(batch);
    }

    public void startNewWave() {
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
        int cx = (int) (player.getX() / 16);
        int cy = (int) (player.getY() / 16);
        if (Map.map[cx][cy] == 0) return;

        int radius = 1;
        outer:
        while (radius < 15) {
            int minX = Math.max(1, cx - radius);
            int maxX = Math.min(Map.SIZE - 2, cx + radius);
            int minY = Math.max(1, cy - radius);
            int maxY = Math.min(Map.SIZE - 2, cy + radius);
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

        float newX = cx * 16f;
        float newY = cy * 16f;
        player.setX(newX);
        player.setY(newY);
        player.bounds.x = newX + 2f;
        player.bounds.y = newY + 2f;
    }

    public void dispose() {
        player.dispose();
        for (Enemy enemy : enemies) enemy.dispose();
        for (Enemy enemy : deadEnemies) enemy.dispose();
        enemyGenerator.dispose();
    }
}
