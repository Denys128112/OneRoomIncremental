package SkillTree;

import com.badlogic.gdx.graphics.Texture;

public class Warrior {
    public static Char build(float headerX, float screenH, float scale) {
        float cy = screenH - 80f * scale;
        float startY = cy - 75f * scale;
        float step = 75f * scale;

        float leftX = headerX - 70f * scale;
        float centerX = headerX;
        float rightX = headerX + 70f * scale;

        Skill[] skills = {
            new Skill("attack_power", "Attack Power",
                "Increases base attack damage.",
                new Texture("skillTree/attackDamage.png"),
                leftX, startY, null),

            new Skill("sword_iron", "Iron Sword",
                "Replaces wooden sword with iron blade. +20% damage.",
                new Texture("skillTree/attackDamage.png"),
                leftX, startY - step, "attack_power"),

            new Skill("sword_tech", "Tech Sword",
                "Plasma blade. +50% damage, pierces armor.",
                new Texture("skillTree/attackDamagelUltra.png"),
                leftX, startY - step * 2, "sword_iron"),

            new Skill("energy_wave", "Energy Wave",
                "Sword strikes fire an energy projectile that pierces enemies.",
                new Texture("skillTree/attackDamagelUltra.png"),
                leftX, startY - step * 3, "sword_tech"),

            new Skill("sword_size", "Sword Size",
                "Increases sword size and attack range.",
                new Texture("skillTree/swordSize.png"),
                centerX, startY, null),

            new Skill("stun_strike", "Stun Strike",
                "Periodically stuns enemies on hit.",
                new Texture("skillTree/stunAttack.png"),
                centerX, startY - step, "sword_size"),

            new Skill("parry", "Parry",
                "Blocks next enemy attack and knocks them back.",
                new Texture("skillTree/parry.png"),
                centerX, startY - step * 2, "stun_strike"),

            new Skill("vortex", "Vortex",
                "Hero spins dealing AoE damage to all nearby enemies.",
                new Texture("skillTree/vortex.png"),
                centerX, startY - step * 3, "parry"),

            new Skill("atk_speed", "Attack Speed",
                "Reduces cooldown between attacks.",
                new Texture("skillTree/attackSpeed.png"),
                rightX, startY, null),

            new Skill("crit_chance", "Critical Hit",
                "Chance to deal +100% of base damage.",
                new Texture("skillTree/attackSpeed.png"),
                rightX, startY - step, "atk_speed"),

            new Skill("knockback", "Knockback",
                "Chance to knock back enemy on hit.",
                new Texture("skillTree/berserk.png"),
                rightX, startY - step * 2, "crit_chance"),

            new Skill("berserk", "Berserk Mode",
                "After taking damage: +50% attack speed and +50% damage for 5 seconds.",
                new Texture("skillTree/berserk.png"),
                rightX, startY - step * 3, "knockback"),
        };
        return new Char("WARRIOR", "skillTree/warrior.png", headerX, cy, skills, false);
    }
}
