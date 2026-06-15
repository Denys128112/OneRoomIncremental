package skills;

import entities.Enemy;
import stub.GameStateStub;

public class SkillEvents {
    private final GameStateStub state;
    private final TankSkills tank;
    private final PhantomSkills phantom;
    private final WarriorSkills warrior;
    private final MageSkills mage;

    public SkillEvents(GameStateStub state, TankSkills tank, PhantomSkills phantom, WarriorSkills warrior, MageSkills mage) {
        this.state = state;
        this.tank = tank;
        this.phantom = phantom;
        this.warrior = warrior;
        this.mage = mage;
    }

    public void onEnemyKilled(Enemy enemy) {
        tank.onEnemyKilled(enemy);
    }

    public int modifyIncomingDamage(int raw) {
        if (tank.isInvulnerable()) return 0;
        float mult = 1f;
        if (tank.isEnemy1Unlocked()) mult *= 0.9f;
        mult *= mage.getStoneDamageReduction();
        return Math.max(1, Math.round(raw * mult));
    }

    public void onDamageTaken() {
        warrior.onDamageTaken();
    }

    public void onLowHealth() {
        tank.onLowHealth();
    }

    public boolean onPlayerDeath() {
        return tank.tryRylai();
    }

    public void onDodge() {
        phantom.onDodge();
    }

    public void onDashThroughEnemy(Enemy enemy) {
        phantom.onDashThroughEnemy(enemy);
    }
}
