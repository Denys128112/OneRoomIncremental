package entities;

import Services.CollisionChecker;
import Services.Map;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Werewolf extends Enemy {

    private float dashRange = 150f;
    private boolean isDashing = false;
    private float dashTimer = 0f;
    private float dashAngle = 0f;
    private final float DASH_SPEED = 350f;
    private final float DASH_DURATION = 0.3f;
    private float stunTimer = 0f;

    public Werewolf(float x, float y, Player player) {
        super(x, y, player, 100f, 140, 1, 0.2f, 0.5f);
        this.color = Color.ORANGE;
        EnemyAnimationFactory.attachLarge(
            this, "enemies/werewolf/black-werewolf-topdown.png", 42f, 56f
        );
    }

    @Override
    public void update(float deltaTime) {
        if (player == null) return;
        if (isDead()) {
            super.update(deltaTime);
            return;
        }
        float previousX = x;
        float previousY = y;
        if (stunTimer > 0) {
            stunTimer -= deltaTime;
            this.color = Color.GRAY;
            bounds.setPosition(x, y);
            return;
        }
        if (attackTimer > 0) {
            attackTimer -= deltaTime;
        }

        if (isDashing) {
            dashTimer -= deltaTime;
            this.color = Color.RED;
            float nextX = this.x + MathUtils.cos(dashAngle) * DASH_SPEED * deltaTime;
            float nextY = this.y + MathUtils.sin(dashAngle) * DASH_SPEED * deltaTime;

            if (!CollisionChecker.isCollisionWithWall(this)) {
                this.x = nextX;
            } else {
                stopDashWithStun();
            }
            if (!CollisionChecker.isCollisionWithWall(this)) {
                this.y = nextY;
            } else {
                stopDashWithStun();
            }

            if (dashTimer <= 0) {
                isDashing = false;
                attackTimer = attackCooldown;
            }

        } else {
            this.color = Color.ORANGE;
            float distanceToPlayer = Vector2.dst(this.x, this.y, player.getX(), player.getY());

            if (distanceToPlayer > dashRange) {
                super.update(deltaTime);
            } else {
                float angleRad = MathUtils.atan2(player.getY() - this.y, player.getX() - this.x);
                this.rotation = angleRad * MathUtils.radiansToDegrees;
                if (attackTimer <= 0) {
                    isDashing = true;
                    dashTimer = DASH_DURATION;
                    dashAngle = angleRad;
                    playAttackAnimation();
                }
            }
        }
        bounds.setPosition(x, y);
        updateAnimation(deltaTime, x - previousX, y - previousY);
    }


    private void stopDashWithStun() {
        isDashing = false;
        stunTimer = 0.8f;
        attackTimer = attackCooldown + 0.5f;
    }
}
