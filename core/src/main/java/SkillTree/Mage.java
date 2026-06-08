package SkillTree;

import com.badlogic.gdx.graphics.Texture;

public class Mage {
    public static Char build(float headerX, float screenH, float scale) {
        float cy = screenH - 80f * scale;
        float startY = cy - 75f * scale;
        float step = 75f * scale;

        float fireX = headerX - 90f * scale;
        float iceX = headerX - 30f * scale;
        float waterX = headerX + 30f * scale;
        float earthX = headerX + 90f * scale;

        Skill[] skills = {
            //FIRE
            new Skill("spark", "Spark",
                "Ignites enemies with a burst of fire magic, increasing fire damage dealt.",
                new Texture("skillTree/Spark.png"),
                fireX, startY, null),

            new Skill("fireball", "Fireball",
                "Launches a ball of fire that explodes on impact, damaging all enemies in the blast area.",
                new Texture("skillTree/Fireball.png"),
                fireX, startY - step, "spark"),

            new Skill("infernal_explosion", "Infernal Explosion",
                "Triggers a massive fire explosion across a wide area, dealing heavy damage without fully killing enemies.",
                new Texture("skillTree/InfernalExplosion.png"),
                fireX, startY - step * 2, "fireball"),

            new Skill("infernal_explosion_2", "Infernal Explosion II",
                "Empowers Infernal Explosion — the blast now annihilates groups of enemies caught in its radius.",
                new Texture("skillTree/InfernalExplosion.png"),
                fireX, startY - step * 3, "infernal_explosion"),

            //ICE
            new Skill("freezing", "Freezing Touch",
                "Chills nearby enemies on hit, reducing their movement speed significantly.",
                new Texture("skillTree/Freezing.png"),
                iceX, startY, null),

            new Skill("frosty_breath", "Frosty Breath",
                "Exhales a freezing blast that locks enemies in place for 5 seconds.",
                new Texture("skillTree/FrostyBreath.png"),
                iceX, startY - step, "freezing"),

            new Skill("ice_storm", "Ice Storm",
                "Summons a blizzard that freezes all enemies within a set radius around the hero.",
                new Texture("skillTree/IceStorm.png"),
                iceX, startY - step * 2, "frosty_breath"),

            //WATER
            new Skill("wave", "Wave",
                "Releases a wave of water that pushes enemies back a set distance.",
                new Texture("skillTree/Wave.png"),
                waterX, startY, null),

            new Skill("whirlpool", "Whirlpool",
                "Creates a vortex that pulls all nearby enemies to the center and deals continuous damage.",
                new Texture("skillTree/Whirlpool.png"),
                waterX, startY - step, "wave"),

            new Skill("tsunami", "Tsunami",
                "Unleashes a massive wave in all directions around the hero, knocking back enemies and dealing heavy damage.",
                new Texture("skillTree/Tsunami.png"),
                waterX, startY - step * 2, "whirlpool"),

            //EARTH
            new Skill("stone", "Stone Skin",
                "Hardens the hero's body with earth magic, permanently increasing armor and reducing incoming damage.",
                new Texture("skillTree/Stone.png"),
                earthX, startY, null),

            new Skill("earthquake", "Earthquake",
                "Slams the ground to trigger a shockwave that stuns all enemies in a wide area around the hero.",
                new Texture("skillTree/Earthquake.png"),
                earthX, startY - step, "stone"),

            new Skill("stone_wall", "Stone Wall",
                "Raises a wall of stone that blocks enemy movement and pushes them back.",
                new Texture("skillTree/StoneWall.png"),
                earthX, startY - step * 2, "earthquake"),
        };
        return new Char("MAGE", "skillTree/mage.png", headerX, cy, skills, false);
    }
}
