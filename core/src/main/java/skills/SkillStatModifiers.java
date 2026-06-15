package skills;

public class SkillStatModifiers {
    private final TankSkills tank;
    private final PhantomSkills phantom;
    private final WarriorSkills warrior;

    public SkillStatModifiers(TankSkills tank, PhantomSkills phantom, WarriorSkills warrior) {
        this.tank = tank;
        this.phantom = phantom;
        this.warrior = warrior;
    }

    public float getSpeedMultiplier() {
        return phantom.getSpeedMultiplier();
    }

    public float getAttackCooldownMultiplier() {
        float mult = 1f;
        mult *= phantom.getAttackCooldownMultiplier();
        mult *= warrior.getAttackCooldownMultiplier();
        return mult;
    }

    public float getCounterDamageMultiplier() {
        return phantom.getCounterDamageMultiplier();
    }

    public float getEnemyHealthMultiplier() {
        if (tank.isEnemy3Unlocked()) return 0.85f;
        return 1f;
    }

    public float getEnemySpeedMultiplier() {
        if (tank.isEnemy2Unlocked()) return 0.90f;
        return 1f;
    }

    public float getRewardMultiplier() {
        if (tank.isEnemy4Unlocked()) return 1.20f;
        return 1f;
    }
}
