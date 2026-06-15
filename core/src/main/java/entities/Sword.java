package entities;

import Services.AudioManager;
import Services.CollisionChecker;
import Services.GameManager;
import Services.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Sword extends Weapon {
    private float hitRadius = 45f;

    public Sword(Player owner) {
        super(owner, 0.4f, 50);
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

        for (Enemy enemy : GameManager.enemies) {
            if (enemy.isDead()) continue;

            float ex = enemy.getX() + enemy.width / 2;
            float ey = enemy.getY() + enemy.height / 2;

            float distance = (float) Math.hypot(ex - px, ey - py);

            if (distance <= hitRadius) {
                enemy.takeDamage(damage);
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
