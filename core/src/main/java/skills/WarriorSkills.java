package skills;

import Services.GameManager;
import entities.Enemy;
import entities.Projectile;
import com.badlogic.gdx.graphics.Color;
import stub.GameStateStub;

public class WarriorSkills {
    private final GameStateStub state;

    private boolean attackPowerUnlocked;
    private boolean swordIronUnlocked;
    private boolean swordTechUnlocked;
    private boolean energyWaveUnlocked;

    private boolean swordSizeUnlocked;
    private boolean stunStrikeUnlocked;
    private boolean parryUnlocked;
    private boolean vortexUnlocked;

    private boolean parryReady = false;
    private float vortexCooldown;
    private static final float VORTEX_COOLDOWN = 10f;
    private static final float VORTEX_RADIUS = 20f;
    private static final int VORTEX_DAMAGE = 25;

    private boolean atkSpeedUnlocked;
    private boolean critChanceUnlocked;
    private boolean knockbackUnlocked;
    private boolean berserkUnlocked;

    private float berserkTimer;
    private float berserkCooldown;
    private static final float BERSERK_DURATION = 5f;
    private static final float BERSERK_COOLDOWN = 10f;

    private int attackCount = 0;
    private int energyWaveCounter = 0;
    private int stunCounter = 0;
    private int critCounter = 0;
    private int parryCounter = 0;

    private float vortexVisualTimer = 0f;
    private static final float VORTEX_VISUAL_DURATION = 0.4f;

    public WarriorSkills(GameStateStub state) {
        this.state = state;
    }

    public void tick(float delta) {
        if (berserkTimer > 0f) berserkTimer -= delta;
        if (berserkCooldown > 0f) berserkCooldown -= delta;
        if (vortexCooldown > 0f) vortexCooldown -= delta;
        if (vortexVisualTimer > 0f) vortexVisualTimer -= delta;
    }

    public void onSwordHit(Enemy enemy, float fromX, float fromY, float angle) {
        energyWaveCounter++;
        stunCounter++;
        critCounter++;

        if (energyWaveUnlocked && energyWaveCounter >= 5) {
            energyWaveCounter = 0;
            GameManager.projectiles.add(
                new Projectile(fromX, fromY, angle, 50, 3, true, Color.CYAN, false)
            );
        }

        if (stunStrikeUnlocked && stunCounter >= 3) {
            stunCounter = 0;
            enemy.applyStun(3f);
        }

        if (critChanceUnlocked && critCounter >= 5) critCounter = 0;

        if (knockbackUnlocked && Math.random() < 0.15) {
            float dx = enemy.getX() - fromX;
            float dy = enemy.getY() - fromY;
            float len = (float) Math.sqrt(dx * dx + dy * dy);
            if (len > 0) {
                enemy.setX(enemy.getX() + dx / len * 15f);
                enemy.setY(enemy.getY() + dy / len * 15f);
            }
        }

        if (parryUnlocked) {
            parryCounter++;
            if (parryCounter >= 5) {
                parryCounter = 0;
                parryReady = true;
            }
        }
    }

    public int modifySwordDamage(int base) {
        float mult = 1f;
        if (attackPowerUnlocked) mult += 0.15f;
        if (swordIronUnlocked) mult += 0.20f;
        if (swordTechUnlocked) mult += 0.50f;
        if (isBerserkActive()) mult += 0.50f;

        if (critChanceUnlocked && critCounter == 0) mult *= 2f;

        return Math.round(base * mult);
    }

    public void onDamageTaken() {
        if (berserkUnlocked && berserkCooldown <= 0f) {
            berserkTimer = BERSERK_DURATION;
            berserkCooldown = BERSERK_COOLDOWN;
        }
    }

    public boolean consumeParry() {
        if (!parryReady) return false;
        parryReady = false;
        return true;
    }

    public void applyParryKnockback(Enemy enemy, float fromX, float fromY) {
        float dx = enemy.getX() - fromX;
        float dy = enemy.getY() - fromY;
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len > 0) {
            enemy.setX(enemy.getX() + dx / len * 20f);
            enemy.setY(enemy.getY() + dy / len * 20f);
        }
    }

    public void tryVortex(float playerX, float playerY) {
        if (!vortexUnlocked || vortexCooldown > 0f) return;
        vortexCooldown = VORTEX_COOLDOWN;
        vortexVisualTimer = VORTEX_VISUAL_DURATION;
        for (Enemy e : GameManager.enemies) {
            if (e.isDead()) continue;
            float dx = e.getX() - playerX;
            float dy = e.getY() - playerY;
            if (dx * dx + dy * dy <= VORTEX_RADIUS * VORTEX_RADIUS) e.takeDamage(VORTEX_DAMAGE);
        }
    }

    public boolean isBerserkActive() { return berserkTimer > 0f; }
    public boolean isSwordSizeUnlocked() { return swordSizeUnlocked; }
    public boolean isSwordTechUnlocked() { return swordTechUnlocked; }
    public boolean isEnergyWaveUnlocked() { return energyWaveUnlocked; }
    public boolean isParryUnlocked() { return parryUnlocked; }
    public boolean isVortexReady() { return vortexUnlocked && vortexCooldown <= 0f; }
    public float getVortexCooldown() { return vortexCooldown; }
    public boolean isVortexVisualActive() { return vortexVisualTimer > 0f; }
    public float getVortexVisualRadius() { return VORTEX_RADIUS; }

    public float getAttackCooldownMultiplier() {
        float mult = 1f;
        if (atkSpeedUnlocked) mult *= 0.90f;
        if (isBerserkActive()) mult *= 0.50f;
        if (swordSizeUnlocked) mult *= 0.85f;
        return mult;
    }

    public void unlock(String id) {
        switch (id) {
            case "attack_power": attackPowerUnlocked = true; break;
            case "sword_iron": swordIronUnlocked = true; break;
            case "sword_tech": swordTechUnlocked = true; break;
            case "energy_wave": energyWaveUnlocked = true; break;
            case "sword_size": swordSizeUnlocked = true; break;
            case "stun_strike": stunStrikeUnlocked = true; break;
            case "parry": parryUnlocked = true; break;
            case "vortex": vortexUnlocked = true; break;
            case "atk_speed": atkSpeedUnlocked = true; break;
            case "crit_chance": critChanceUnlocked = true; break;
            case "knockback": knockbackUnlocked = true; break;
            case "berserk": berserkUnlocked = true; break;
        }
    }
}
