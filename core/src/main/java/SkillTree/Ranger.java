package SkillTree;

import com.badlogic.gdx.graphics.Texture;

public class Ranger {
    public static Char build(float headerX, float screenH, float scale) {
        float cy = screenH - 80f * scale;
        float startY = cy - 75f * scale;
        float step = 75f * scale;

        float bowX = headerX - 55f * scale;
        float pistolX = headerX + 55f * scale;
        float sharedX = headerX;

        Skill[] skills = {
            new Skill("bow_basic", "Wooden Bow",
                "Basic bow. Longer charge = more damage.",
                new Texture("skillTree/hitRange.png"),
                bowX, startY, null),

            new Skill("bow_iron", "Iron Bow",
                "More base damage and range.",
                new Texture("skillTree/Sniper.png"),
                bowX, startY - step, "bow_basic"),

            new Skill("bow_enchanted", "Enchanted Bow",
                "Arrows have auto-aim on the nearest enemy.",
                new Texture("skillTree/Sniper.png"),
                bowX, startY - step * 2, "bow_iron"),

            new Skill("bow_explosive", "Explosive Bow",
                "Arrows explode on impact — AoE damage and burning effect.",
                new Texture("skillTree/BurstingArrow.png"),
                bowX, startY - step * 3, "bow_enchanted"),

            new Skill("piercing", "Piercing Shot",
                "Arrows pierce through 1 enemy. Upgrade: pierce infinite enemies.",
                new Texture("skillTree/PiercingShot.png"),
                bowX, startY - step * 4, "bow_explosive"),

            new Skill("cryo_arrows", "Cryo Arrows",
                "15% chance to freeze an enemy for 2 seconds on hit.",
                new Texture("skillTree/Freezing.png"),
                bowX, startY - step * 5, "piercing"),

            new Skill("ricochet", "Ricochet",
                "Bullets bounce off walls.",
                new Texture("skillTree/Ricochet.png"),
                sharedX, startY - step, null),

            new Skill("bullet_destroy", "Bullet Breaker",
                "Your bullets can destroy enemy projectiles.",
                new Texture("skillTree/BulletBreaker.png"),
                sharedX, startY - step * 2, "ricochet"),

            new Skill("crossfire", "Crossfire",
                "Also fires one projectile behind the player. Upgrade: fire in all 4 directions.",
                new Texture("skillTree/Crossfire.png"),
                sharedX, startY - step * 3, "bullet_destroy"),

            new Skill("pistol1", "Pistol MK-1",
                "Very low damage but extremely high fire rate.",
                new Texture("skillTree/poison.png"),
                pistolX, startY, null),

            new Skill("pistol2", "Pistol MK-2",
                "More damage, knocks back enemies. Lower fire rate.",
                new Texture("skillTree/poison.png"),
                pistolX, startY - step, "pistol1"),

            new Skill("pistol3", "Pistol MK-3",
                "Fires multiple projectiles in a cone.",
                new Texture("skillTree/DoubleShot.png"),
                pistolX, startY - step * 2, "pistol2"),

            new Skill("pistol4", "Pistol MK-4",
                "Multiple projectiles with auto-aim.",
                new Texture("skillTree/DoubleShot.png"),
                pistolX, startY - step * 3, "pistol3"),

            new Skill("turret", "Auto Turret",
                "Deploy a turret with auto-aim. Increases fire rate.",
                new Texture("skillTree/showerofarrows.png"),
                pistolX, startY - step * 4, "pistol4"),
        };
        return new Char("RANGER", "skillTree/ranger.png", headerX, cy, skills, false);
    }
}
