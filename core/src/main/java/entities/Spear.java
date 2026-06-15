package entities;

import Services.AudioManager;
import Services.GameManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;

public class Spear extends Weapon {

    public Spear(Player owner) {
        super(owner, 0.8f, 35);
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
        float startX = owner.getX() + owner.width / 2;
        float startY = owner.getY() + owner.height / 2;

        GameManager.projectiles.add(new Projectile(startX, startY, owner.getRotation(), damage, 3, true, Color.LIGHT_GRAY, false));
        AudioManager.playSound(AudioManager.weapSpearThrow);
        cooldownTimer = attackCooldown;
    }
}
