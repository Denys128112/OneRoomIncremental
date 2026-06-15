package skills;

import stub.GameStateStub;

public class PlayerSkills {
    public final TankSkills tank;
    public final PhantomSkills phantom;
    public final WarriorSkills warrior;
    public final RangerSkills ranger;
    public final MageSkills mage;
    public final SkillEvents events;
    public final SkillStatModifiers stats;

    private static final String[] TANK_IDS = {
        "regeneration", "vampirism", "rylai", "second_wind",
        "tough_skin", "invulnerability"
    };

    private static final String[] WARRIOR_IDS = {
        "attack_power", "sword_iron", "sword_tech", "energy_wave",
        "sword_size", "stun_strike", "parry", "vortex",
        "atk_speed", "crit_chance", "knockback", "berserk"
    };

    private static final String[] RANGER_IDS = {
        "bow_basic", "bow_iron", "bow_enchanted", "bow_explosive",
        "piercing", "cryo_arrows", "ricochet", "bullet_destroy", "crossfire",
        "pistol1", "pistol2", "pistol3", "pistol4", "turret"
    };

    private static final String[] MAGE_IDS = {
        "spark", "fireball", "infernal_explosion", "infernal_explosion_2",
        "freezing", "frosty_breath", "ice_storm",
        "wave", "whirlpool", "tsunami",
        "stone", "earthquake", "stone_wall"
    };

    public PlayerSkills(GameStateStub state) {
        tank = new TankSkills(state);
        phantom = new PhantomSkills(state);
        warrior = new WarriorSkills(state);
        ranger = new RangerSkills(state);
        mage = new MageSkills(state);
        mage.loadTextures();
        mage.initFrames();
        events = new SkillEvents(state, tank, phantom, warrior, mage);
        stats = new SkillStatModifiers(tank, phantom, warrior);
    }

    public void tick(float delta) {
        tank.tick(delta);
        phantom.tick(delta);
        warrior.tick(delta);
        ranger.tick(delta);
        mage.tick(delta);
    }

    public void unlock(String id) {
        for (String s : TANK_IDS) if (id.equals(s)) { tank.unlock(id); return; }
        if (id.startsWith("enemy") || id.startsWith("helper")) {
            tank.unlock(id);
            return;
        }
        for (String s : WARRIOR_IDS) if (id.equals(s)) { warrior.unlock(id); return; }
        for (String s : RANGER_IDS) if (id.equals(s)) { ranger.unlock(id); return; }
        for (String s : MAGE_IDS) if (id.equals(s)) { mage.unlock(id); return; }
        phantom.unlock(id);
    }

}
