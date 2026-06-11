package Services;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
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
    public List<Enemy> enemies;
    public static List<Enemy> deadEnemies;

    private EnemyGenerator enemyGenerator;
    private TiledMap map;
    public boolean isWaveCleared = false;

    public GameManager(TiledMap map) {
        this.map = map;
        this.player = new Player(400, 300);
        this.projectiles = new ArrayList<>();
        this.deadEnemies = new ArrayList<>();
        this.enemyGenerator = new EnemyGenerator();
        resolvePlayerCoordinates();
        this.enemies = enemyGenerator.enemyList;
        enemyGenerator.generate(Map.map, GameStateStub.wave, player);
    }

    public void update(float delta) {
        player.update(delta);
        for (Enemy e : enemies) {
            e.update(delta);
        }
        handleCollisions(delta);
        if (!enemiesToAdd.isEmpty()) {
            enemies.addAll(enemiesToAdd);
            enemiesToAdd.clear();
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
            } else {
                Enemy e = checkWithEnemy(p);
                if (e != null) {
                    e.takeDamage(p.getDamage());
                    if (e.getHp() <= 0) {
                        enemies.remove(e);
                        deadEnemies.add(e);
                        if (enemies.isEmpty()) {
                            isWaveCleared = true;
                        }
                    }
                    iter.remove();
                }
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

    public void startNewWave() {
        GameStateStub.wave++;
        deadEnemies.clear();
        isWaveCleared = false;
        Map.createVisualMap(map);
        resolvePlayerCoordinates();
        enemyGenerator.generate(Map.map, GameStateStub.wave, player);

    }

    public void shootProjectile() {
        float startX = player.getX() + 16;
        float startY = player.getY() + 16;
        projectiles.add(new Projectile(startX, startY, player.getRotation(), 20));
    }

    private Enemy checkWithEnemy(Projectile p) {
        for (Enemy e : enemies) {
            if (CollisionChecker.isCollision(p, e)) {
                return e;
            }
        }
        return null;
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
}
