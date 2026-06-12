package entities;

import Services.GameManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;

public class Staff extends Weapon {

    public Staff(Player owner) {
        super(owner, 0.25f, 20);
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
        float startX = owner.getX() + 8f;
        float startY = owner.getY() + 8f;

        GameManager.projectiles.add(new Projectile(startX, startY, owner.getRotation(), damage, 2, false, Color.CYAN, false));
        cooldownTimer = attackCooldown;
    }
}
