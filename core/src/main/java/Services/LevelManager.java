package Services;

import entities.Enemy;

public class LevelManager {
    private DifficultyLevel difficulty = DifficultyLevel.FIGHTER;

    public void selectDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty == null ? DifficultyLevel.FIGHTER : difficulty;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public int getEnemyCount(int wave) {
        int baseCount = (int) (10 + 0.565f * Math.pow((wave - 1), 1.3));
        return Math.max(1, Math.round(baseCount * difficulty.enemyCountMultiplier));
    }

    public void configureEnemy(Enemy enemy) {
        enemy.applyDifficulty(
            difficulty.enemyHealthMultiplier,
            difficulty.enemySpeedMultiplier,
            difficulty.enemyDamageMultiplier,
            difficulty.rewardMultiplier
        );
    }
}
