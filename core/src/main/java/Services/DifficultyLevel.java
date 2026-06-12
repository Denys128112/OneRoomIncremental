package Services;

import com.badlogic.gdx.graphics.Color;

public enum DifficultyLevel {
    EXPLORER(
        "ДОСЛІДНИК",
        "Менше ворогів, вони повільніші та мають менше здоров'я. "
            + "Підійде для знайомства з грою.",
        0.75f,
        0.8f,
        0.85f,
        0.75f,
        0.9f,
        Color.valueOf("63E6FF")
    ),
    FIGHTER(
        "БОЄЦЬ",
        "Стандартна кількість і сила ворогів. "
            + "Збалансоване проходження для більшості гравців.",
        1f,
        1f,
        1f,
        1f,
        1f,
        Color.valueOf("FFC857")
    ),
    NIGHTMARE(
        "КОШМАР",
        "Більше ворогів, вони швидші, витриваліші та небезпечніші. "
            + "За перемоги нараховується більше нагород.",
        1.35f,
        1.45f,
        1.18f,
        1.5f,
        1.35f,
        Color.valueOf("FF4F70")
    );

    public final String title;
    public final String description;
    public final float enemyCountMultiplier;
    public final float enemyHealthMultiplier;
    public final float enemySpeedMultiplier;
    public final float enemyDamageMultiplier;
    public final float rewardMultiplier;
    public final Color accentColor;

    DifficultyLevel(
        String title,
        String description,
        float enemyCountMultiplier,
        float enemyHealthMultiplier,
        float enemySpeedMultiplier,
        float enemyDamageMultiplier,
        float rewardMultiplier,
        Color accentColor
    ) {
        this.title = title;
        this.description = description;
        this.enemyCountMultiplier = enemyCountMultiplier;
        this.enemyHealthMultiplier = enemyHealthMultiplier;
        this.enemySpeedMultiplier = enemySpeedMultiplier;
        this.enemyDamageMultiplier = enemyDamageMultiplier;
        this.rewardMultiplier = rewardMultiplier;
        this.accentColor = accentColor;
    }
}
