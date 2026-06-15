package entities;

import Services.AudioManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import java.util.List;

public class Archer extends Enemy {

    float attackRange = 150f;
    private List<Projectile> projectileList;

    public Archer(float x, float y, Player player, float speed, int hp, int attackDamage, float attackCooldown, float attackTimer, List<Projectile> projectileList) {
        super(x, y, player, speed, hp, attackDamage, attackCooldown, attackTimer);
        this.color = Color.GREEN;
        this.projectileList = projectileList;
    }

    @Override
    public void update(float deltaTime) {
        if (isDead()) {
            super.update(deltaTime);
            return;
        }
        if (attackTimer > 0) {
            attackTimer -= deltaTime;
        }

        float distanceToPlayer = Vector2.dst(this.x, this.y, player.getX(), player.getY());

        if (distanceToPlayer > attackRange) {
            super.update(deltaTime);
        } else {
            float angleRad = MathUtils.atan2(player.getY() - this.y, player.getX() - this.x);
            this.rotation = angleRad * MathUtils.radiansToDegrees;

            if (attackTimer <= 0) {
                shoot();
                attackTimer = attackCooldown;
            }
            updateAnimation(deltaTime, 0f, 0f);
        }

        bounds.setPosition(x, y);
    }

    void shoot() {
        playAttackAnimation();
        float startX = this.x + this.width / 2;
        float startY = this.y + this.height / 2;
        Projectile arrow = new Projectile(startX, startY, this.rotation, this.attackDamage, 1, false, Color.RED, true);
        AudioManager.playSound(AudioManager.enemyShoot);
        projectileList.add(arrow);
    }
}
