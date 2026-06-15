package skills;

import entities.Enemy;
import stub.GameStateStub;

public class PhantomSkills {
    private final GameStateStub state;

    // Speed
    private boolean sprintUnlocked;
    private boolean speedUpUnlocked;
    private boolean swiftStepUnlocked;

    private float sprintTimer = 0f;
    private float sprintCooldown = 0f;
    private static final float SPRINT_DURATION = 5f;
    private static final float SPRINT_COOLDOWN = 20f;
    private boolean sprintActive = false;

    // Dash
    private boolean dashUnlocked;
    private boolean invulDashUnlocked;
    private boolean dashDamageUnlocked;
    private static final int DASH_DAMAGE = 30;

    // Teleport
    private boolean teleportUnlocked;
    private float teleportCooldown;
    private static final float TELEPORT_COOLDOWN = 60f;

    // Attack speed
    private boolean attackSpeedUnlocked;

    // Counterattack
    private boolean counterattackUnlocked;
    private boolean justDodged = false;
    private static final float COUNTER_MULTIPLIER = 1.5f;

    // Echo
    private boolean echoUnlocked;
    private float echoCooldown;
    private static final float ECHO_COOLDOWN = 30f;

    public PhantomSkills(GameStateStub state) {
        this.state = state;
    }

    public void tick(float delta) {
        if (sprintCooldown > 0f) sprintCooldown -= delta;
        if (sprintActive) {
            sprintTimer -= delta;
            if (sprintTimer <= 0f) sprintActive = false;
        }
        if (echoCooldown > 0f) echoCooldown -= delta;
        if (teleportCooldown > 0f) teleportCooldown -= delta;
    }

    public void onDodge() {
        if (counterattackUnlocked) justDodged = true;
    }

    public void onDashThroughEnemy(Enemy enemy) {
        if (dashDamageUnlocked) enemy.takeDamage(DASH_DAMAGE);
    }

    public boolean tryTeleport() {
        if (!teleportUnlocked || teleportCooldown > 0f) return false;
        teleportCooldown = TELEPORT_COOLDOWN;
        return true;
    }

    public boolean tryEcho() {
        if (!echoUnlocked || echoCooldown > 0f) return false;
        echoCooldown = ECHO_COOLDOWN;
        return true;
    }

    public float getSpeedMultiplier() {
        float m = 1f;
        if (sprintActive) m += 0.25f;
        if (speedUpUnlocked) m += 0.15f;
        if (swiftStepUnlocked) m += 0.30f;
        return m;
    }

    public float getAttackCooldownMultiplier() {
        return attackSpeedUnlocked ? 0.75f : 1f;
    }

    public float getCounterDamageMultiplier() {
        if (counterattackUnlocked && justDodged) {
            justDodged = false;
            return COUNTER_MULTIPLIER;
        }
        return 1f;
    }

    public boolean trySprint() {
        if (!sprintUnlocked || sprintCooldown > 0f) return false;
        sprintActive = true;
        sprintTimer = SPRINT_DURATION;
        sprintCooldown = SPRINT_COOLDOWN;
        return true;
    }

    public boolean isDashUnlocked() { return dashUnlocked; }
    public boolean isInvulDashUnlocked() { return invulDashUnlocked; }
    public boolean isDashDamageUnlocked() { return dashDamageUnlocked; }
    public boolean isTeleportReady() { return teleportUnlocked && teleportCooldown <= 0f; }
    public boolean isEchoReady() { return echoUnlocked && echoCooldown <= 0f; }
    public float   getTeleportCooldown() { return teleportCooldown; }

    public void unlock(String id) {
        switch (id) {
            case "sprint": sprintUnlocked = true; break;
            case "speed_up": speedUpUnlocked = true; break;
            case "swift_step": swiftStepUnlocked = true; break;
            case "dash": dashUnlocked = true; break;
            case "invul_dash": invulDashUnlocked = true; break;
            case "dash_damage": dashDamageUnlocked = true; break;
            case "teleport": teleportUnlocked = true; break;
            case "attack_speed": attackSpeedUnlocked = true; break;
            case "counterattack": counterattackUnlocked = true; break;
            case "echo": echoUnlocked = true; break;
        }
    }
}
