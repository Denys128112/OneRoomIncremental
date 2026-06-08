package SkillTree;

import com.badlogic.gdx.graphics.Texture;

public class Phantom {
    public static Char build(float headerX, float screenH, float scale) {
        float cy = screenH - 80f * scale;
        float startY = cy - 75f * scale;
        float step = 75f * scale;
        float leftX = headerX - 35f * scale;
        float rightX = headerX + 35f * scale;

        Skill[] skills = {
            new Skill("sprint", "Sprint",
                "Short burst of extra movement speed.",
                new Texture("skillTree/sprint.png"),
                leftX, startY, null),

            new Skill("speed_up", "Speed Up",
                "Permanently increases movement speed.",
                new Texture("skillTree/speed-up.png"),
                leftX, startY - step, "sprint"),

            new Skill("swift_step", "Swift Step",
                "Further increases movement speed.",
                new Texture("skillTree/speed-up.png"),
                leftX, startY - step * 2, "speed_up"),

            new Skill("dash", "Dash",
                "Quick dash in any direction.",
                new Texture("skillTree/Dash.png"),
                leftX, startY - step * 3, "swift_step"),

            new Skill("invul_dash", "Invulnerable Dash",
                "Become invincible during the entire dash.",
                new Texture("skillTree/Dash.png"),
                leftX, startY - step * 4, "dash"),

            new Skill("dash_damage", "Dash Strike",
                "Dashing through an enemy deals damage to them.",
                new Texture("skillTree/DashUltra.png"),
                leftX, startY - step * 5, "invul_dash"),

            new Skill("teleport", "Teleport",
                "Once per minute: teleport to cursor position.",
                new Texture("skillTree/Teleport.png"),
                leftX, startY - step * 6, "dash_damage"),

            new Skill("attack_speed", "Attack Speed",
                "Reduces cooldown between attacks.",
                new Texture("skillTree/attackSpeed.png"),
                rightX, startY, null),

            new Skill("counterattack", "Counterattack",
                "Bonus damage after dodging.",
                new Texture("skillTree/counterattack.png"),
                rightX, startY - step, "attack_speed"),

            new Skill("echo", "Echo",
                "Instantly destroys all enemy projectiles on screen.",
                new Texture("skillTree/shield.png"),
                rightX, startY - step * 2, "counterattack"),
        };
        return new Char("PHANTOM", "skillTree/phantom.png", headerX, cy, skills, false);
    }
}
