package SkillTree;

import com.badlogic.gdx.graphics.Texture;

public class Tank {
    public static Char build(float headerX, float screenH, float scale) {
        float cy = screenH - 80f * scale;
        float startY = cy - 75f * scale;
        float step = 75f * scale;
        float leftX = headerX - 70f * scale;
        float centerX = headerX;
        float rightX = headerX + 70f * scale;

        Skill[] skills = {
            new Skill("regeneration", "Regeneration",
                "Restores HP every second passively.",
                new Texture("skillTree/Regeneration.png"),
                leftX, startY, null),

            new Skill("vampirism", "Vampirism",
                "Killing restores 5% of missing HP.",
                new Texture("skillTree/Vampirism.png"),
                leftX, startY - step, "regeneration"),

            new Skill("rylai", "Rylai",
                "Resurrect with 30% HP on death. 3-min cooldown.",
                new Texture("skillTree/Rylai.png"),
                leftX, startY - step * 2, "vampirism"),

            new Skill("second_wind", "Second Wind",
                "One-time HP burst when HP drops below 50%.",
                new Texture("skillTree/AdditionalHP.png"),
                rightX, startY, null),

            new Skill("tough_skin", "Tough Skin",
                "Permanently increases max HP.",
                new Texture("skillTree/maxHP.png"),
                rightX, startY - step, "second_wind"),

            new Skill("invulnerability", "Invulnerability",
                "30-second damage shield absorbing all hits.",
                new Texture("skillTree/shield.png"),
                rightX, startY - step * 2, "tough_skin"),

            new Skill("enemy1", "Enemy Study I",
                "Enemies deal -10% damage to the hero.",
                new Texture("skillTree/EnemyStudy.png"),
                centerX, startY, null),

            new Skill("enemy2", "Enemy Study II",
                "Enemies move 10% slower.",
                new Texture("skillTree/EnemyStudy.png"),
                centerX, startY - step, "enemy1"),

            new Skill("enemy3", "Enemy Study III",
                "Enemies have -15% HP.",
                new Texture("skillTree/EnemyStudy.png"),
                centerX, startY - step * 2, "enemy2"),

            new Skill("enemy4", "Enemy Study IV",
                "Enemies drop +20% more resources.",
                new Texture("skillTree/EnemyStudy.png"),
                centerX, startY - step * 3, "enemy3"),

            new Skill("helper1", "Companion I",
                "Deploy a combat drone that attacks enemies.",
                new Texture("skillTree/Companion.png"),
                centerX, startY - step * 4, "enemy4"),

            new Skill("helper2", "Companion II",
                "Second drone. Both drones become faster.",
                new Texture("skillTree/Companion.png"),
                centerX, startY - step * 5, "helper1"),

            new Skill("helper3", "Companion III",
                "Third drone. All drones deal +30% damage.",
                new Texture("skillTree/CompanionUltra.png"),
                centerX, startY - step * 6, "helper2"),

            new Skill("helper_class", "Class Companion",
                "Drones gain abilities from your active class.",
                new Texture("skillTree/CompanionUltraClass.png"),
                centerX, startY - step * 7, "helper3"),
        };
        return new Char("GUARDIAN", "skillTree/guardian.png", headerX, cy, skills, false);
    }
}
