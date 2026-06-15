package skills;

import Services.GameManager;
import entities.Enemy;
import entities.Projectile;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import entities.Turret;
import stub.GameStateStub;

public class RangerSkills {
    private final GameStateStub state;

    private boolean bowBasicUnlocked;
    private boolean bowIronUnlocked;
    private boolean bowEnchantedUnlocked;
    private boolean bowExplosiveUnlocked;
    private boolean piercingUnlocked;
    private boolean cryoArrowsUnlocked;

    private boolean ricochetUnlocked;
    private boolean bulletDestroyUnlocked;
    private int crossfireLevel;

    private int pistolLevel;
    private boolean turretUnlocked;

    private float turretCooldown;
    private static final float TURRET_DEPLOY_COOLDOWN = 30f;

    private static final float CRYO_CHANCE = 0.15f;
    private static final float CRYO_DURATION = 2f;
    private static final int EXPLOSION_DAMAGE = 15;
    private static final float EXPLOSION_RADIUS = 60f;

    public RangerSkills(GameStateStub state) {
        this.state = state;
    }

    public void tick(float delta) {
        if (turretCooldown > 0f) turretCooldown -= delta;
    }

    public int modifyBowDamage(int base) {
        float mult = 1f;
        if (bowIronUnlocked) mult += 0.20f;
        if (bowEnchantedUnlocked) mult += 0.15f;
        if (bowExplosiveUnlocked) mult += 0.10f;
        return Math.round(base * mult);
    }

    public boolean isBowPiercing() {
        return piercingUnlocked;
    }

    public void onArrowHit(Enemy enemy, float hitX, float hitY) {
        if (cryoArrowsUnlocked && MathUtils.random() < CRYO_CHANCE) enemy.applyStun(CRYO_DURATION);
        if (bowExplosiveUnlocked) {
            for (Enemy e : GameManager.enemies) {
                if (e.isDead()) continue;
                float dx = e.getX() - hitX;
                float dy = e.getY() - hitY;
                if (dx * dx + dy * dy <= EXPLOSION_RADIUS * EXPLOSION_RADIUS)
                    e.takeDamage(EXPLOSION_DAMAGE);
            }
        }
    }

    public float resolveArrowAngle(float baseAngle, float fromX, float fromY) {
        if (!bowEnchantedUnlocked && pistolLevel < 4) return baseAngle;
        Enemy nearest = null;
        float minDist = Float.MAX_VALUE;
        for (Enemy e : GameManager.enemies) {
            if (e.isDead()) continue;
            float dx = e.getX() - fromX;
            float dy = e.getY() - fromY;
            float dist = dx * dx + dy * dy;
            if (dist < minDist) {
                minDist = dist;
                nearest = e;
            }
        }
        if (nearest == null) return baseAngle;
        return MathUtils.atan2Deg(nearest.getY() - fromY, nearest.getX() - fromX);
    }

    public boolean isRicochetUnlocked() {
        return ricochetUnlocked;
    }

    public boolean isBulletDestroyUnlocked() {
        return bulletDestroyUnlocked;
    }

    public void fireCrossfire(float fromX, float fromY, float mainAngle, int damage, boolean piercing) {
        if (crossfireLevel == 0) return;
        if (crossfireLevel >= 1) {
            GameManager.projectiles.add(new Projectile(fromX, fromY, mainAngle + 180f, damage, 1, piercing, Color.ORANGE, false));
        }
        if (crossfireLevel >= 2) {
            GameManager.projectiles.add(new Projectile(fromX, fromY, mainAngle + 90f,  damage, 1, piercing, Color.ORANGE, false));
            GameManager.projectiles.add(new Projectile(fromX, fromY, mainAngle + 270f, damage, 1, piercing, Color.ORANGE, false));
        }
    }

    public int getPistolDamage() {
        switch (pistolLevel) {
            case 1: return 5;
            case 2: return 12;
            case 3: return 10;
            case 4: return 10;
            default: return 5;
        }
    }

    public float getPistolCooldown() {
        switch (pistolLevel) {
            case 1: return 0.08f;
            case 2: return 0.25f;
            case 3: return 0.20f;
            case 4: return 0.20f;
            default: return 0.15f;
        }
    }

    public boolean firePistol(float fromX, float fromY, float angle) {
        if (pistolLevel == 0) return false;

        float finalAngle = resolveArrowAngle(angle, fromX, fromY);
        boolean knockback = pistolLevel >= 2;

        if (pistolLevel <= 2) {
            Projectile p = new Projectile(
                fromX, fromY, finalAngle, getPistolDamage(), 1, false, Color.YELLOW, false
            );
            GameManager.projectiles.add(p);
        } else {
            for (int i = -1; i <= 1; i++) {
                float spreadAngle = finalAngle + i * 15f;
                GameManager.projectiles.add(new Projectile(
                    fromX, fromY, spreadAngle, getPistolDamage(), 1, false, Color.YELLOW, false
                ));
            }
        }
        return true;
    }

    public boolean tryDeployTurret(float x, float y) {
        if (!turretUnlocked || turretCooldown > 0f) return false;
        turretCooldown = TURRET_DEPLOY_COOLDOWN;
        GameManager.turrets.add(new Turret(x, y));
        return true;
    }

    public int getPistolLevel() { return pistolLevel; }
    public boolean isTurretReady() { return turretUnlocked && turretCooldown <= 0f; }
    public float getTurretCooldown() { return turretCooldown; }
    public boolean isBowBasicUnlocked() { return bowBasicUnlocked; }
    public boolean isBowExplosiveUnlocked() { return bowExplosiveUnlocked; }

    public void unlock(String id) {
        switch (id) {
            case "bow_basic": bowBasicUnlocked = true; break;
            case "bow_iron": bowIronUnlocked = true; break;
            case "bow_enchanted": bowEnchantedUnlocked = true; break;
            case "bow_explosive": bowExplosiveUnlocked = true; break;
            case "piercing": piercingUnlocked = true; break;
            case "cryo_arrows": cryoArrowsUnlocked = true; break;
            case "ricochet": ricochetUnlocked = true; break;
            case "bullet_destroy": bulletDestroyUnlocked = true; break;
            case "crossfire": crossfireLevel = Math.min(crossfireLevel + 1, 2);break;
            case "pistol1": pistolLevel = 1; break;
            case "pistol2": pistolLevel = 2; break;
            case "pistol3": pistolLevel = 3; break;
            case "pistol4": pistolLevel = 4; break;
            case "turret": turretUnlocked = true; break;
            case "crossfire2": crossfireLevel = 2; break;
        }
    }
}
