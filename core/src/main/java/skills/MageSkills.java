package skills;

import Services.GameManager;
import entities.Enemy;
import entities.Projectile;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import stub.GameStateStub;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MageSkills {
    private final GameStateStub state;

    private static Texture fireSheet;
    private static Texture waterSheet;
    private static Texture greenSheet;

    public void loadTextures() {
        if (fireSheet  == null) fireSheet  = new Texture("heroes/mageSkills/FireEffectandBullet16x16.png");
        if (waterSheet == null) waterSheet = new Texture("heroes/mageSkills/WaterEffectandBullet16x16.png");
        if (greenSheet == null) greenSheet = new Texture("heroes/mageSkills/GreenEffectandBullet16x16.png");
    }

    private static TextureRegion[] extractAnim(Texture sheet, int col, int startRow, int frameCount) {
        TextureRegion[][] all = TextureRegion.split(sheet, 16, 16);
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++)
            frames[i] = all[startRow + i][col];
        return frames;
    }

    public static class VisualEffect {
        public float x, y;
        public float timer, maxTimer;
        public float drawSize;
        public TextureRegion[] frames;
        public float animTimer;
        public float frameTime;
        public boolean looping;
        public VisualEffect(float x, float y, float drawSize, float duration, float frameTime, boolean looping, TextureRegion[] frames) {
            this.x = x; this.y = y;
            this.drawSize = drawSize;
            this.timer = duration; this.maxTimer = duration;
            this.frameTime = frameTime;
            this.looping = looping;
            this.frames = frames;
        }

        public void render(Batch batch) {
            if (frames == null || frames.length == 0) return;
            int idx;
            if (looping) idx = (int)(animTimer / frameTime) % frames.length;
            else idx = Math.min((int)(animTimer / frameTime), frames.length - 1);

            float half = drawSize / 2f;
            batch.draw(frames[idx], x - half, y - half, half, half, drawSize, drawSize, 1f, 1f, 0f);
        }
    }

    public final List<VisualEffect> effects = new ArrayList<>();

    // fire
    private TextureRegion[] fx_fireballExplode;
    private TextureRegion[] fx_infernal;
    // ice
    private TextureRegion[] fx_frostyBreath;
    private TextureRegion[] fx_iceStorm;
    // water
    private TextureRegion[] fx_wave;
    private TextureRegion[] fx_whirlpool;
    private TextureRegion[] fx_tsunami;
    // earth
    private TextureRegion[] fx_earthquake;

    public void initFrames() {
        fx_fireballExplode = extractAnim(fireSheet, 4, 4, 4);
        fx_infernal = extractAnim(fireSheet, 3, 8, 4);

        fx_frostyBreath = extractAnim(waterSheet, 3, 3, 3);
        fx_iceStorm = extractAnim(waterSheet, 6, 8, 4);

        fx_wave = extractAnim(waterSheet, 2, 6, 4);
        fx_whirlpool = extractAnim(waterSheet, 5, 6, 4);
        fx_tsunami = extractAnim(waterSheet, 4, 10, 3);

        fx_earthquake = extractAnim(greenSheet, 3, 8, 4);
    }

    public enum Element { FIRE, ICE, WATER, EARTH }
    private Element currentElement = Element.FIRE;
    public void setElement(Element e) { currentElement = e; }
    public Element getElement() { return currentElement; }

    private boolean sparkUnlocked;
    private boolean fireballUnlocked;
    private boolean infernalExplosionUnlocked;
    private boolean infernalExplosion2Unlocked;

    private float fireballAutoTimer = 0f;
    private static final float FIREBALL_AUTO_INTERVAL = 20f;
    private static final int FIREBALL_DIRECT_DAMAGE = 50;
    private static final int FIREBALL_SPLASH_DAMAGE = 25;
    private static final float FIREBALL_RADIUS = 50f;

    private static final int INFERNAL_DAMAGE = 150;
    private static final int INFERNAL2_DAMAGE = 300;
    private static final float INFERNAL_RADIUS = 100f;
    private static final float INFERNAL_COOLDOWN = 40f;
    private static final float INFERNAL2_COOLDOWN = 20f;
    private float infernalCooldown = 0f;

    private boolean freezingUnlocked;
    private boolean frostyBreathUnlocked;
    private boolean iceStormUnlocked;

    private float freezingAutoTimer = 0f;
    private static final float FREEZING_AUTO_INTERVAL = 5f;
    private static final float FROSTY_DURATION = 5f;
    private static final float FROSTY_RADIUS = 50f;
    private static final float FROSTY_COOLDOWN = 15f;
    private float frostyCooldown = 0f;
    private static final float ICE_STORM_RADIUS = 70f;
    private static final float ICE_STORM_COOLDOWN = 10f;
    private float iceStormCooldown = 0f;

    private boolean waveUnlocked;
    private boolean whirlpoolUnlocked;
    private boolean tsunamiUnlocked;

    private static final float WAVE_KNOCKBACK = 50f;
    private static final float WAVE_COOLDOWN  = 10f;
    private float waveCooldown = 0f;

    private static final float WHIRLPOOL_RADIUS = 70f;
    private static final float WHIRLPOOL_PULL = 60f;
    private static final int WHIRLPOOL_DAMAGE = 8;
    private static final float WHIRLPOOL_COOLDOWN = 15f;
    private float whirlpoolCooldown = 0f;
    private float activeWhirlpoolX = -9999f;
    private float activeWhirlpoolY = -9999f;
    private float activeWhirlpoolTimer = 0f;
    private static final float WHIRLPOOL_DURATION = 3f;

    private static final float TSUNAMI_KNOCKBACK = 200f;
    private static final int TSUNAMI_DAMAGE = 200;
    private static final float TSUNAMI_RADIUS = 150f;
    private static final float TSUNAMI_COOLDOWN = 20f;
    private float tsunamiCooldown = 0f;

    private boolean stoneUnlocked;
    private boolean earthquakeUnlocked;
    private boolean stoneWallUnlocked;

    private static final int STONE_HP_BONUS = 8;
    private static final float EARTHQUAKE_RADIUS = 25f;
    private static final int EARTHQUAKE_DAMAGE = 25;
    private static final float EARTHQUAKE_STUN = 3f;
    private static final float EARTHQUAKE_COOLDOWN = 10f;
    private float earthquakeCooldown = 0f;

    private static final float STONE_WALL_COOLDOWN = 10f;
    private static final float STONE_WALL_DURATION = 5f;
    private float stoneWallCooldown = 0f;

    public enum WallMode { NONE, PLACING_HORIZONTAL, PLACING_VERTICAL }
    private WallMode wallMode = WallMode.NONE;

    public MageSkills(GameStateStub state) {
        this.state = state;
    }

    public void tick(float delta) {
        if (infernalCooldown > 0f) infernalCooldown -= delta;
        if (frostyCooldown > 0f) frostyCooldown -= delta;
        if (iceStormCooldown > 0f) iceStormCooldown -= delta;
        if (waveCooldown > 0f) waveCooldown -= delta;
        if (whirlpoolCooldown > 0f) whirlpoolCooldown -= delta;
        if (tsunamiCooldown > 0f) tsunamiCooldown -= delta;
        if (earthquakeCooldown > 0f) earthquakeCooldown -= delta;
        if (stoneWallCooldown > 0f) stoneWallCooldown -= delta;

        if (fireballUnlocked && currentElement == Element.FIRE) {
            fireballAutoTimer += delta;
            if (fireballAutoTimer >= FIREBALL_AUTO_INTERVAL) {
                fireballAutoTimer = 0f;
                autoFireball();
            }
        }
        if (freezingUnlocked && currentElement == Element.ICE) {
            freezingAutoTimer += delta;
            if (freezingAutoTimer >= FREEZING_AUTO_INTERVAL) {
                freezingAutoTimer = 0f;
                autoFreezingShot();
            }
        }
        if (activeWhirlpoolTimer > 0f) {
            activeWhirlpoolTimer -= delta;
            tickWhirlpool(delta);
            if (activeWhirlpoolTimer <= 0f) activeWhirlpoolX = -9999f;
        }

        Iterator<VisualEffect> it = effects.iterator();
        while (it.hasNext()) {
            VisualEffect fx = it.next();
            fx.timer -= delta;
            fx.animTimer += delta;
            if (fx.timer <= 0f) it.remove();
        }
    }

    public void onStaffHit(Enemy enemy) {
        if (sparkUnlocked && currentElement == Element.FIRE) enemy.applyBurn(10f, 5);
    }

    private void autoFireball() {
        Enemy nearest = findNearest(9999f);
        if (nearest == null) return;
        entities.Player player = skills.PlayerSkillsHolder.player;
        if (player == null) return;
        float cx = player.getX() + player.getWidth() / 2f;
        float cy = player.getY() + player.getHeight() / 2f;
        float angle = MathUtils.atan2Deg(nearest.getY() - cy, nearest.getX() - cx);
        GameManager.projectiles.add(new Projectile(cx, cy, angle, FIREBALL_DIRECT_DAMAGE, 2, false, null, false, fireSheet, 1, 4));
    }

    public void onFireballHit(float hitX, float hitY) {
        if (!fireballUnlocked) return;
        for (Enemy e : GameManager.enemies) {
            if (e.isDead()) continue;
            float dx = e.getX() - hitX, dy = e.getY() - hitY;
            if (dx * dx + dy * dy <= FIREBALL_RADIUS * FIREBALL_RADIUS) e.takeDamage(FIREBALL_SPLASH_DAMAGE);
        }
        effects.add(new VisualEffect(hitX, hitY, 64f, 0.4f, 0.08f, false, fx_fireballExplode));
    }

    public boolean tryInfernalExplosion(float playerX, float playerY) {
        if (!infernalExplosionUnlocked || infernalCooldown > 0f) return false;
        float cooldown = infernalExplosion2Unlocked ? INFERNAL2_COOLDOWN : INFERNAL_COOLDOWN;
        int damage = infernalExplosion2Unlocked ? INFERNAL2_DAMAGE : INFERNAL_DAMAGE;
        infernalCooldown = cooldown;
        for (Enemy e : GameManager.enemies) {
            if (e.isDead()) continue;
            float dx = e.getX() - playerX, dy = e.getY() - playerY;
            if (dx * dx + dy * dy <= INFERNAL_RADIUS * INFERNAL_RADIUS) {
                int dmg = Math.min(damage, Math.max(0, e.getHp() - 1));
                if (dmg > 0) e.takeDamage(dmg);
            }
        }
        effects.add(new VisualEffect(playerX, playerY, 200f, 0.6f, 0.1f, false, fx_infernal));
        return true;
    }

    private void autoFreezingShot() {
        entities.Player player = skills.PlayerSkillsHolder.player;
        if (player == null) return;
        float cx = player.getX() + player.getWidth() / 2f;
        float cy = player.getY() + player.getHeight() / 2f;
        for (Enemy target : findNearest3(9999f)) {
            float angle = MathUtils.atan2Deg(target.getY() - cy, target.getX() - cx);
            GameManager.projectiles.add(new Projectile(
                cx, cy, angle, 5, 1, false,
                null, false, waterSheet, 0, 3));
        }
    }

    public boolean tryFrostyBreath(float playerX, float playerY, float mouseX, float mouseY) {
        if (!frostyBreathUnlocked || frostyCooldown > 0f) return false;
        frostyCooldown = FROSTY_COOLDOWN;
        float angle = MathUtils.atan2Deg(mouseY - playerY, mouseX - playerX);
        for (Enemy e : GameManager.enemies) {
            if (e.isDead()) continue;
            float dx = e.getX() - playerX, dy = e.getY() - playerY;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            if (dist > FROSTY_RADIUS) continue;
            float enemyAngle = MathUtils.atan2Deg(dy, dx);
            if (Math.abs(angleDiff(angle, enemyAngle)) <= 45f) e.applyStun(FROSTY_DURATION);
        }
        float ex = playerX + MathUtils.cosDeg(angle) * FROSTY_RADIUS;
        float ey = playerY + MathUtils.sinDeg(angle) * FROSTY_RADIUS;
        effects.add(new VisualEffect(ex, ey, 80f, 0.5f, 0.1f, false, fx_frostyBreath));
        return true;
    }

    public boolean tryIceStorm(float playerX, float playerY) {
        if (!iceStormUnlocked || iceStormCooldown > 0f) return false;
        iceStormCooldown = ICE_STORM_COOLDOWN;
        for (Enemy e : GameManager.enemies) {
            if (e.isDead()) continue;
            float dx = e.getX() - playerX, dy = e.getY() - playerY;
            if (dx * dx + dy * dy <= ICE_STORM_RADIUS * ICE_STORM_RADIUS) e.applyStun(FROSTY_DURATION);
        }
        effects.add(new VisualEffect(playerX, playerY, 140f, 0.6f, 0.1f, false, fx_iceStorm));
        return true;
    }

    public boolean tryWave(float playerX, float playerY, float mouseX, float mouseY) {
        if (!waveUnlocked || waveCooldown > 0f) return false;
        waveCooldown = WAVE_COOLDOWN;
        float angle = MathUtils.atan2Deg(mouseY - playerY, mouseX - playerX);
        for (Enemy e : GameManager.enemies) {
            if (e.isDead()) continue;
            float dx = e.getX() - playerX, dy = e.getY() - playerY;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            if (dist > 80f) continue;
            float enemyAngle = MathUtils.atan2Deg(dy, dx);
            if (Math.abs(angleDiff(angle, enemyAngle)) <= 50f) {
                float nx = dx / dist, ny = dy / dist;
                e.setX(e.getX() + nx * WAVE_KNOCKBACK);
                e.setY(e.getY() + ny * WAVE_KNOCKBACK);
            }
        }
        float ex = playerX + MathUtils.cosDeg(angle) * 40f;
        float ey = playerY + MathUtils.sinDeg(angle) * 40f;
        effects.add(new VisualEffect(ex, ey, 96f, 0.4f, 0.08f, false, fx_wave));
        return true;
    }

    public boolean tryWhirlpool(float mouseX, float mouseY) {
        if (!whirlpoolUnlocked || whirlpoolCooldown > 0f) return false;
        whirlpoolCooldown = WHIRLPOOL_COOLDOWN;
        activeWhirlpoolX = mouseX;
        activeWhirlpoolY = mouseY;
        activeWhirlpoolTimer = WHIRLPOOL_DURATION;
        effects.add(new VisualEffect(mouseX, mouseY, 140f, WHIRLPOOL_DURATION, 0.1f, true, fx_whirlpool));
        return true;
    }

    private void tickWhirlpool(float delta) {
        for (Enemy e : GameManager.enemies) {
            if (e.isDead()) continue;
            float dx = e.getX() - activeWhirlpoolX, dy = e.getY() - activeWhirlpoolY;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            if (dist > 0 && dist <= WHIRLPOOL_RADIUS) {
                float nx = dx / dist, ny = dy / dist;
                e.setX(e.getX() - nx * WHIRLPOOL_PULL * delta);
                e.setY(e.getY() - ny * WHIRLPOOL_PULL * delta);
                e.takeDamage(Math.round(WHIRLPOOL_DAMAGE * delta));
            }
        }
    }

    public boolean tryTsunami(float playerX, float playerY) {
        if (!tsunamiUnlocked || tsunamiCooldown > 0f) return false;
        tsunamiCooldown = TSUNAMI_COOLDOWN;
        for (Enemy e : GameManager.enemies) {
            if (e.isDead()) continue;
            float dx = e.getX() - playerX, dy = e.getY() - playerY;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            if (dist > 0 && dist <= TSUNAMI_RADIUS) {
                float nx = dx / dist, ny = dy / dist;
                e.setX(e.getX() + nx * TSUNAMI_KNOCKBACK);
                e.setY(e.getY() + ny * TSUNAMI_KNOCKBACK);
                e.takeDamage(TSUNAMI_DAMAGE);
            }
        }
        effects.add(new VisualEffect(playerX, playerY, 300f, 0.5f, 0.1f, false, fx_tsunami));
        return true;
    }

    public void onStonePurchased() {
        state.addMaxHealthQuarters(STONE_HP_BONUS);
    }

    public boolean tryEarthquake(float playerX, float playerY) {
        if (!earthquakeUnlocked || earthquakeCooldown > 0f) return false;
        earthquakeCooldown = EARTHQUAKE_COOLDOWN;
        for (Enemy e : GameManager.enemies) {
            if (e.isDead()) continue;
            float dx = e.getX() - playerX, dy = e.getY() - playerY;
            if (dx * dx + dy * dy <= EARTHQUAKE_RADIUS * EARTHQUAKE_RADIUS) {
                e.takeDamage(EARTHQUAKE_DAMAGE);
                e.applyStun(EARTHQUAKE_STUN);
            }
        }
        effects.add(new VisualEffect(playerX, playerY, 160f, 0.5f, 0.08f, false, fx_earthquake));
        return true;
    }

    public boolean startStoneWall() {
        if (!stoneWallUnlocked || stoneWallCooldown > 0f) return false;
        if (wallMode == WallMode.NONE) wallMode = WallMode.PLACING_HORIZONTAL;
        return true;
    }

    public boolean confirmStoneWall(float mouseX, float mouseY) {
        if (wallMode == WallMode.NONE) return false;
        stoneWallCooldown = STONE_WALL_COOLDOWN;
        boolean horizontal = wallMode == WallMode.PLACING_HORIZONTAL;
        wallMode = WallMode.NONE;
        float wallW = horizontal ? 48f : 16f;
        float wallH = horizontal ? 16f : 48f;
        GameManager.addTemporaryWall(mouseX - wallW / 2f, mouseY - wallH / 2f, wallW, wallH, STONE_WALL_DURATION);
        return true;
    }

    public void rotateWall() {
        if (wallMode == WallMode.PLACING_HORIZONTAL) wallMode = WallMode.PLACING_VERTICAL;
        else if (wallMode == WallMode.PLACING_VERTICAL) wallMode = WallMode.PLACING_HORIZONTAL;
    }

    public void cancelStoneWall() { wallMode = WallMode.NONE; }
    public WallMode getWallMode() { return wallMode; }

    public boolean isFireballUnlocked() { return fireballUnlocked; }
    public boolean isInfernalReady() { return infernalExplosionUnlocked && infernalCooldown <= 0f; }
    public float getInfernalCooldown() { return infernalCooldown; }
    public boolean isFrostyBreathReady() { return frostyBreathUnlocked && frostyCooldown <= 0f; }
    public boolean isIceStormReady() { return iceStormUnlocked && iceStormCooldown <= 0f; }
    public boolean isWaveReady(){ return waveUnlocked && waveCooldown <= 0f; }
    public boolean isWhirlpoolReady()  { return whirlpoolUnlocked && whirlpoolCooldown <= 0f; }
    public boolean isTsunamiReady(){ return tsunamiUnlocked && tsunamiCooldown <= 0f; }
    public boolean isEarthquakeReady(){ return earthquakeUnlocked && earthquakeCooldown <= 0f; }
    public boolean isStoneWallReady() { return stoneWallUnlocked && stoneWallCooldown <= 0f; }
    public boolean isFrostyBreathUnlocked(){ return frostyBreathUnlocked; }
    public boolean isIceStormUnlocked() { return iceStormUnlocked; }
    public boolean isWaveUnlocked() { return waveUnlocked; }
    public boolean isWhirlpoolUnlocked() { return whirlpoolUnlocked; }
    public boolean isTsunamiUnlocked(){ return tsunamiUnlocked; }
    public boolean isEarthquakeUnlocked() { return earthquakeUnlocked; }
    public boolean isStoneWallUnlocked() { return stoneWallUnlocked; }
    public float getStoneDamageReduction() { return 1f; }

    private Enemy findNearest(float maxDist) {
        entities.Player player = skills.PlayerSkillsHolder.player;
        if (player == null) return null;
        float cx = player.getX() + player.getWidth() / 2f;
        float cy = player.getY() + player.getHeight() / 2f;
        Enemy nearest = null;
        float minDist = maxDist * maxDist;
        for (Enemy e : GameManager.enemies) {
            if (e.isDead()) continue;
            float dx = e.getX() - cx, dy = e.getY() - cy;
            float d = dx * dx + dy * dy;
            if (d < minDist) { minDist = d; nearest = e; }
        }
        return nearest;
    }

    private List<Enemy> findNearest3(float maxDist) {
        entities.Player player = skills.PlayerSkillsHolder.player;
        List<Enemy> result = new ArrayList<>();
        if (player == null) return result;
        float cx = player.getX() + player.getWidth() / 2f;
        float cy = player.getY() + player.getHeight() / 2f;
        float maxD2 = maxDist * maxDist;
        List<float[]> candidates = new ArrayList<>();
        for (Enemy e : GameManager.enemies) {
            if (e.isDead()) continue;
            float dx = e.getX() - cx, dy = e.getY() - cy;
            float d = dx * dx + dy * dy;
            if (d <= maxD2) candidates.add(new float[]{d, GameManager.enemies.indexOf(e)});
        }
        candidates.sort((a, b) -> Float.compare(a[0], b[0]));
        for (int i = 0; i < Math.min(3, candidates.size()); i++) result.add(GameManager.enemies.get((int) candidates.get(i)[1]));
        return result;
    }

    private float angleDiff(float a, float b) {
        float d = (b - a) % 360f;
        if (d > 180f) d -= 360f;
        if (d < -180f) d += 360f;
        return d;
    }

    public void unlock(String id) {
        switch (id) {
            case "spark": sparkUnlocked = true; break;
            case "fireball": fireballUnlocked = true; break;
            case "infernal_explosion": infernalExplosionUnlocked = true; break;
            case "infernal_explosion_2": infernalExplosion2Unlocked = true; break;
            case "freezing": freezingUnlocked = true; break;
            case "frosty_breath": frostyBreathUnlocked = true; break;
            case "ice_storm": iceStormUnlocked = true; break;
            case "wave": waveUnlocked = true; break;
            case "whirlpool": whirlpoolUnlocked = true; break;
            case "tsunami": tsunamiUnlocked = true; break;
            case "stone": stoneUnlocked = true; onStonePurchased(); break;
            case "earthquake": earthquakeUnlocked = true; break;
            case "stone_wall": stoneWallUnlocked = true; break;
        }
    }
}
