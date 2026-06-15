package skills;

import Services.GameManager;
import com.badlogic.gdx.Gdx;
import entities.Drone;
import entities.Enemy;
import stub.GameStateStub;

public class TankSkills {
    private final GameStateStub state;

    // Regeneration
    private boolean regenUnlocked;
    private float regenTimer;
    private static final float REGEN_INTERVAL = 1f;

    // Vampirism
    private boolean vampirismUnlocked;

    // Rylai
    private boolean rylaiUnlocked;
    private float rylaiCooldown;
    private static final float RYLAI_COOLDOWN = 180f;

    // Second Wind
    private boolean secondWindUnlocked;
    private boolean secondWindUsed;

    // Tough Skin
    private boolean toughSkinUnlocked;
    private static final int BASE_MAX_QUARTERS = 24;
    private static final float TOUGH_SKIN_BONUS = 0.15f;

    // Invulnerability
    private boolean invulnerabilityUnlocked;
    private float invulnerabilityTimer;
    private float invulnerabilityCooldown;
    private static final float INVULN_DURATION = 30f;
    private static final float INVULN_COOLDOWN = 60f;

    // Enemy Study
    private boolean enemy1Unlocked;
    private boolean enemy2Unlocked;
    private boolean enemy3Unlocked;
    private boolean enemy4Unlocked;

    private int droneCount = 0;

    private float shieldPulseTimer = 0f;

    public TankSkills(GameStateStub state) {
        this.state = state;
    }

    public void tick(float delta) {
        if (regenUnlocked) {
            regenTimer += delta;
            if (regenTimer >= REGEN_INTERVAL) {
                regenTimer = 0f;
                state.healOneQuarter();
            }
        }
        if (rylaiCooldown > 0f) rylaiCooldown -= delta;
        if (invulnerabilityTimer > 0f) invulnerabilityTimer -= delta;
        if (invulnerabilityCooldown > 0f) invulnerabilityCooldown -= delta;
    }

    public void onEnemyKilled(Enemy enemy) {
        if (!vampirismUnlocked) return;
        int missing = state.getMaxHealthQuarters() - state.getHealthQuarters();
        int heal = Math.max(1, (int)(missing * 0.05f));
        for (int i = 0; i < heal; i++) state.healOneQuarter();
    }

    public void onLowHealth() {
        if (!secondWindUnlocked || secondWindUsed) return;
        secondWindUsed = true;
        int heal = state.getMaxHealthQuarters() / 2;
        for (int i = 0; i < heal; i++) state.healOneQuarter();
    }

    public boolean tryRylai() {
        if (!rylaiUnlocked || rylaiCooldown > 0f) return false;
        rylaiCooldown = RYLAI_COOLDOWN;
        int heal = (int)(state.getMaxHealthQuarters() * 0.3f);
        for (int i = 0; i < heal; i++) state.healOneQuarter();
        return true;
    }

    public boolean tryActivateInvulnerability() {
        if (!invulnerabilityUnlocked || invulnerabilityCooldown > 0f) return false;
        invulnerabilityTimer = INVULN_DURATION;
        invulnerabilityCooldown = INVULN_COOLDOWN;
        return true;
    }

    public boolean isInvulnerable() { return invulnerabilityTimer > 0f; }
    public boolean isEnemy1Unlocked() { return enemy1Unlocked; }
    public boolean isEnemy2Unlocked() { return enemy2Unlocked; }
    public boolean isEnemy3Unlocked() { return enemy3Unlocked; }
    public boolean isEnemy4Unlocked() { return enemy4Unlocked; }
    public int getDroneCount() { return droneCount; }
    public boolean isInvulnReady() { return invulnerabilityUnlocked && invulnerabilityCooldown <= 0f; }
    public float getInvulnCooldown() { return invulnerabilityCooldown; }
    public float getShieldPulse() {
        shieldPulseTimer += Gdx.graphics.getDeltaTime();
        return 18f + (float) Math.sin(shieldPulseTimer * 4f) * 3f;
    }

    public void unlock(String id) {
        switch (id) {
            case "regeneration":
                regenUnlocked = true;
                break;
            case "vampirism":
                vampirismUnlocked = true;
                break;
            case "rylai":
                rylaiUnlocked = true;
                break;
            case "second_wind":
                secondWindUnlocked = true;
                break;
            case "tough_skin":
                if (!toughSkinUnlocked) {
                    toughSkinUnlocked = true;
                    int bonus = Math.round(BASE_MAX_QUARTERS * TOUGH_SKIN_BONUS);
                    state.addMaxHealthQuarters(bonus);
                    for (int i = 0; i < bonus; i++) state.healOneQuarter();
                }
                break;
            case "invulnerability":
                invulnerabilityUnlocked = true;
                break;
            case "enemy1": enemy1Unlocked = true; break;
            case "enemy2": enemy2Unlocked = true; break;
            case "enemy3": enemy3Unlocked = true; break;
            case "enemy4": enemy4Unlocked = true; break;
            case "helper1":
                droneCount = 1;
                rebuildDrones(40f, 15, new float[]{0f});
                break;
            case "helper2":
                droneCount = 2;
                rebuildDrones(45f, 18, new float[]{0f, 180f});
                break;
            case "helper3":
                droneCount = 3;
                rebuildDrones(50f, 22, new float[]{0f, 180f, 270f});
                break;
        }
    }

    private void rebuildDrones(float orbit, int damage, float[] angles) {
        if (PlayerSkillsHolder.player == null) return;
        GameManager.drones.clear();
        for (float angle : angles) {
            Drone d = new Drone(PlayerSkillsHolder.player, angle, orbit);
            d.setDamage(damage);
            GameManager.drones.add(d);
        }
    }
}
