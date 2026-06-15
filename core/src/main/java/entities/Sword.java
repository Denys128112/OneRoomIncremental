package entities;

import Services.AudioManager;
import Services.CollisionChecker;
import Services.GameManager;
import Services.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import skills.PlayerSkills;

public class Sword extends Weapon {
    private float hitRadius = 45f;
    private PlayerSkills skills;

    public Sword(Player owner) {
        super(owner, 0.4f, 50);
    }

    public void setSkills(PlayerSkills skills) {
        this.skills = skills;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && canAttack()) {
            owner.playAttackAnimation();
            attack();
        }
    }

    @Override
    public void attack() {

        AudioManager.playSound(AudioManager.weapSwordSwing);

        float px = owner.getX() + owner.width / 2;
        float py = owner.getY() + owner.height / 2;
        float angle = owner.getRotation();
        float radius = (skills != null && skills.warrior.isSwordSizeUnlocked()) ? hitRadius * 1.4f : hitRadius;

        for (Enemy enemy : GameManager.enemies) {
            if (enemy.isDead()) continue;

            float ex = enemy.getX() + enemy.width / 2;
            float ey = enemy.getY() + enemy.height / 2;
            float distance = (float) Math.hypot(ex - px, ey - py);

            if (distance <= radius) {
                if (skills != null) {skills.warrior.onSwordHit(enemy, px, py, angle);}
                int finalDamage = (skills != null) ? skills.warrior.modifySwordDamage(damage) : damage;
                enemy.takeDamage(finalDamage);
            }
        }
        Box box = null;
        for (Box b : Map.boxes) {
            float ex = b.getX() + b.width / 2;
            float ey = b.getY() + b.height / 2;
            float distance = (float) Math.hypot(ex - px, ey - py);
            if (distance <= hitRadius) {
                b.interact();
                box = b;
            }
        }
        cooldownTimer = attackCooldown;
    }
}
