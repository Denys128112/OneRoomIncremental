package entities;

/** Strength tier shared by enemies that have three visual versions. */
public enum EnemyVariant {
    STANDARD(1, 1f, 1f, 1f, 1f),
    VETERAN(2, 1.30f, 1.08f, 1.25f, 1.35f),
    ELITE(3, 1.70f, 1.15f, 1.50f, 1.80f);

    public final int number;
    final float healthMultiplier;
    final float speedMultiplier;
    final float damageMultiplier;
    final float rewardMultiplier;

    EnemyVariant(
        int number,
        float healthMultiplier,
        float speedMultiplier,
        float damageMultiplier,
        float rewardMultiplier
    ) {
        this.number = number;
        this.healthMultiplier = healthMultiplier;
        this.speedMultiplier = speedMultiplier;
        this.damageMultiplier = damageMultiplier;
        this.rewardMultiplier = rewardMultiplier;
    }

    void applyTo(Enemy enemy) {
        enemy.applyDifficulty(
            healthMultiplier,
            speedMultiplier,
            damageMultiplier,
            rewardMultiplier
        );
    }
}
