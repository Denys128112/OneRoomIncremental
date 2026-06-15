package entities;

import Services.AudioManager;
import Services.GameManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Bow extends Weapon {
    private float chargeTime = 0f;
    private float maxCharge = 1.5f;
    private int minDamage = 5;
    private int maxDamage = 75;

    public Bow(Player owner) {
        super(owner, 0.3f, 0);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && canAttack()) {
            chargeTime += deltaTime;
            if (chargeTime > maxCharge) {
                chargeTime = maxCharge;
            }
        }
        else if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT) && chargeTime > 0) {
            owner.playAttackAnimation();
            attack();
        }
    }

    @Override
    public void attack() {
        float chargePercent = chargeTime / maxCharge;

        int finalDamage = minDamage + (int)((maxDamage - minDamage) * chargePercent);

        float startX = owner.getX() + owner.width / 2;
        float startY = owner.getY() + owner.height / 2;

        GameManager.projectiles.add(new Projectile(startX, startY, owner.getRotation(), finalDamage, 1, false, Color.BROWN, false));

        AudioManager.playSound(AudioManager.weapBowShoot);

        chargeTime = 0f;
        cooldownTimer = attackCooldown;
    }
    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if (chargeTime > 0) {
            float chargePercent = chargeTime / maxCharge;

            float barWidth = 32f;
            float barHeight = 4f;

            float barX = owner.getX() + (owner.width / 2) - (barWidth / 2);
            float barY = owner.getY() + owner.height + 8f;

            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect(barX, barY, barWidth, barHeight);

            shapeRenderer.setColor(Color.YELLOW.cpy().lerp(Color.RED, chargePercent));
            shapeRenderer.rect(barX, barY, barWidth * chargePercent, barHeight);
        }
    }
}
