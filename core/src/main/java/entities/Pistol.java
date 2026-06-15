package entities;

import Services.GameManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import skills.PlayerSkills;

public class Pistol extends Weapon {
    private PlayerSkills skills;

    private static final float BASE_COOLDOWN = 0.15f;
    private static final int   BASE_DAMAGE   = 8;

    public Pistol(Player owner) {
        super(owner, BASE_COOLDOWN, BASE_DAMAGE);
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
        float startX = owner.getX() + owner.width / 2f;
        float startY = owner.getY() + owner.height / 2f;

        if (skills != null && skills.ranger.getPistolLevel() > 0) {
            skills.ranger.firePistol(startX, startY, owner.getRotation());
            cooldownTimer = skills.ranger.getPistolCooldown();
        } else {
            GameManager.projectiles.add(
                new Projectile(startX, startY, owner.getRotation(),
                    BASE_DAMAGE, 1, false, Color.YELLOW, false)
            );
            cooldownTimer = BASE_COOLDOWN;
        }
    }
}
