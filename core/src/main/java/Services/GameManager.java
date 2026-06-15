package Services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entities.*;
import skills.MageSkills;
import skills.PlayerSkillsHolder;
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

    public static List<Drone> drones = new ArrayList<>();
    public static List<Turret> turrets = new ArrayList<>();

    public static List<com.badlogic.gdx.math.Rectangle> temporaryWalls = new ArrayList<>();
    public static List<float[]> temporaryWallTimers = new ArrayList<>();
    public GameManager(TiledMap map, GameStateStub state) {
        this.map = map;
        this.state = state;
        this.player = new Player(400, 300);
        skills.PlayerSkills playerSkills = new skills.PlayerSkills(state);
        player.setSkills(playerSkills);
        PlayerSkillsHolder.player = player;
        PlayerSkillsHolder.instance = playerSkills;
        this.projectiles = new ArrayList<>();
        this.deadEnemies = new ArrayList<>();
        this.enemyGenerator = new EnemyGenerator(state.getLevelManager());
        Enemy.loadStatusTextures(
            new com.badlogic.gdx.graphics.Texture("heroes/mageSkills/Spark.png"),
            new com.badlogic.gdx.graphics.Texture("heroes/mageSkills/FrostyBreath.png")
        );
        resolvePlayerCoordinates();
        this.enemies = enemyGenerator.enemyList;
        enemyGenerator.generate(Map.map, GameStateStub.wave, player);
    }

    public void update(float delta) {
        player.update(delta);

        Iterator<Drone> droneIter = drones.iterator();
        while (droneIter.hasNext()) {droneIter.next().update(delta);}

        for (int i = temporaryWalls.size() - 1; i >= 0; i--) {
            temporaryWallTimers.get(i)[0] -= delta;
            if (temporaryWallTimers.get(i)[0] <= 0f) {
                temporaryWalls.remove(i);
                temporaryWallTimers.remove(i);
            }
        }

        Iterator<Turret> turretIter = turrets.iterator();
        while (turretIter.hasNext()) {
            Turret t = turretIter.next();
            t.update(delta);
            if (t.isDead()) {
                t.dispose();
                turretIter.remove();
            }
        }

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.update(delta);
            if (enemy.isReadyForRemoval()) {
                state.addExperience(enemy.getExperienceReward());
                state.addCredits(BigDecimal.valueOf(enemy.getCreditReward()));
                if (player.skills != null) player.skills.events.onEnemyKilled(enemy);
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
                boolean ricochetUnlocked = player.skills != null
                    && player.skills.ranger.isRicochetUnlocked();

                if (ricochetUnlocked
                    && !p.isEnemyProjectile
                    && p.ricochetCount < Projectile.MAX_RICOCHET) {

                    CollisionChecker.WallHitSide side =
                        CollisionChecker.getWallHitSide(p);

                    if (side == CollisionChecker.WallHitSide.VERTICAL) {
                        p.reflectX();
                    } else {
                        p.reflectY();
                    }

                    p.setX(p.getX() + p.getDx() * 2f);
                    p.setY(p.getY() + p.getDy() * 2f);
                    p.bounds.setPosition(p.getX() - p.bounds.width / 2f,
                        p.getY() - p.bounds.height / 2f);

                    p.ricochetCount++;
                    p.hitEnemies.clear();
                } else {
                    iter.remove();
                }
                continue;
            }

            if (p.isEnemyProjectile) {
                boolean destroyed = false;
                if (player.skills != null && player.skills.ranger.isBulletDestroyUnlocked()) {
                    Iterator<Projectile> inner = projectiles.iterator();
                    while (inner.hasNext()) {
                        Projectile other = inner.next();
                        if (!other.isEnemyProjectile && other.bounds.overlaps(p.bounds)) {
                            inner.remove();
                            destroyed = true;
                            break;
                        }
                    }
                }
                if (destroyed) { iter.remove(); continue; }

                if (p.bounds.overlaps(player.bounds)) {
                    boolean invul = player.skills != null && player.skills.tank.isInvulnerable();
                    boolean dashInvul = player.isInvulnerableDash();
                    if (!invul && !dashInvul) player.takeDamage(p.getDamage(), null);
                    iter.remove();
                }
            } else {
                boolean bulletDestroyed = false;

                for (Enemy e : enemies) {
                    if (e.isDead()) continue;
                    if (!CollisionChecker.isCollision(p, e)) continue;
                    if (p.hitEnemies.contains(e)) continue;

                    e.takeDamage(p.getDamage());
                    p.hitEnemies.add(e);

                    if (player.inventory[1] instanceof Bow
                        && player.selectedSlot == 1
                        && player.skills != null) {
                        player.skills.ranger.onArrowHit(e, p.getX(), p.getY());
                    }

                    if (player.inventory[2] instanceof Staff) {
                        Staff staff = (Staff) player.inventory[2];
                        staff.onProjectileHit(e, p.getX(), p.getY());
                    }

                    if (p.hitEnemies.size() >= p.maxHits) {
                        iter.remove();
                        bulletDestroyed = true;
                        break;
                    }
                }
                if (bulletDestroyed) continue;
            }
        }
    }


    public void drawEntities(ShapeRenderer shapeRenderer) {
        for (Projectile p : projectiles) {
            if (!p.hasSprite()) p.render(shapeRenderer);
        }
        for (Drone d : drones) d.render(shapeRenderer);
        for (Turret t : turrets) t.render(shapeRenderer);
        player.render(shapeRenderer);
        enemyGenerator.render(shapeRenderer);

        if (player.skills != null && player.skills.tank.isInvulnerable()) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            float cx = player.getX() + player.getWidth() / 2f;
            float cy = player.getY() + player.getHeight() / 2f;
            float radius = player.skills.tank.getShieldPulse();

            shapeRenderer.setColor(0.3f, 0.6f, 1f, 0.2f);
            shapeRenderer.circle(cx, cy, radius);

            shapeRenderer.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0.5f, 0.9f, 1f, 0.9f);
            shapeRenderer.circle(cx, cy, radius);
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        if (player.skills != null && player.skills.warrior.isVortexVisualActive()) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            float cx = player.getX() + player.getWidth() / 2f;
            float cy = player.getY() + player.getHeight() / 2f;
            float r = player.skills.warrior.getVortexVisualRadius();

            shapeRenderer.setColor(1f, 0.8f, 0f, 0.35f); // золотистий
            shapeRenderer.circle(cx, cy, r);

            shapeRenderer.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1f, 0.9f, 0f, 0.9f);
            shapeRenderer.circle(cx, cy, r);
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setColor(0.2f, 0.8f, 0.2f, 0.7f);
        for (com.badlogic.gdx.math.Rectangle wall : GameManager.temporaryWalls) shapeRenderer.rect(wall.x, wall.y, wall.width, wall.height);
        if (player.skills != null
            && player.skills.mage.getWallMode() != MageSkills.WallMode.NONE) {
            boolean horiz = player.skills.mage.getWallMode() == MageSkills.WallMode.PLACING_HORIZONTAL;
            float pw = horiz ? 48f : 16f;
            float ph = horiz ? 16f : 48f;
            float mx = screens.GameScreen.mouseWorldX - pw / 2f;
            float my = screens.GameScreen.mouseWorldY - ph / 2f;
            shapeRenderer.setColor(0.4f, 1f, 0.4f, 0.4f);
            shapeRenderer.rect(mx, my, pw, ph);
        }
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void drawSprites(Batch batch) {
        player.render(batch);
        enemyGenerator.render(batch);
        for (Drone d : drones) d.render(batch);
        for (Turret t : turrets) t.render(batch);
        for (Projectile p : projectiles) if (p.hasSprite()) p.render(batch);

        com.badlogic.gdx.graphics.GL20 gl = com.badlogic.gdx.Gdx.gl;
        gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
        gl.glBlendFunc(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA);
        for (Enemy e : enemies) e.renderEffects(batch);

        if (player.skills != null) {
            for (MageSkills.VisualEffect fx : player.skills.mage.effects) {
                fx.render(batch);
            }
        }
    }

    public void startNewWave() {
        state.nextWave();
        for (Enemy enemy : deadEnemies) enemy.dispose();
        deadEnemies.clear();
        isWaveCleared = false;
        Map.createVisualMap(map);
        resolvePlayerCoordinates();
        enemyGenerator.generate(Map.map, state.getWave(), player);
        turrets.clear();
    }

    public void shootProjectile() {
        player.attack();
    }

    public static void addTemporaryWall(float x, float y, float w, float h, float duration) {
        temporaryWalls.add(new com.badlogic.gdx.math.Rectangle(x, y, w, h));
        temporaryWallTimers.add(new float[]{duration});
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
