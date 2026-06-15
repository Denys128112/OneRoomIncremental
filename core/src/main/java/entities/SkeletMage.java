package entities;

import Services.Map;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class SkeletMage extends Archer {
    private float teleportCooldown = 3f;
    private float teleportTimer = teleportCooldown;
    public SkeletMage(float x, float y, Player player, List<Projectile> projectileList) {
        super(x, y, player, 80f, 110, 1, 1f, 0.5f, projectileList);
        EnemyAnimationFactory.attachSmall(this, "enemies/generated-small/skeleton-mage-topdown.png");
        this.setLootAmount(3);
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
        teleportTimer -= deltaTime;
        if (teleportTimer <= 0) {
            teleport();
            playAttackAnimation();
            teleportTimer = teleportCooldown;
        } else {

            float angleRad = MathUtils.atan2(player.getY() - this.y, player.getX() - this.x);
            this.rotation = angleRad * MathUtils.radiansToDegrees;

            if (attackTimer <= 0) {
                shoot();
                attackTimer = attackCooldown;
            }
        }

        bounds.setPosition(x, y);
        updateAnimation(deltaTime, 0f, 0f);
    }

    @Override
    public int getExperienceReward() {
        return 3;
    }

    private void teleport() {
        int pGridX = (int) (player.getX() / 16f);
        int pGridY = (int) (player.getY() / 16f);
        int maxRange = (int) (attackRange / 16f);
        for (int r = maxRange; r > 0; r--) {
            int[][] offsets = {
                {r, 0}, {-r, 0}, {0, r}, {0, -r},
                {r, r}, {-r, -r}, {r, -r}, {-r, r}
            };

            for (int[] offset : offsets) {
                int checkX = pGridX + offset[0];
                int checkY = pGridY + offset[1];

                if (checkX > 0 && checkX < Map.map.length && checkY > 0 && checkY < Map.map[0].length) {

                    if (Map.map[checkX][checkY] == 0) {
                        this.x = checkX * 16f;
                        this.y = checkY * 16f;
                        bounds.setPosition(this.x, this.y);
                        return;
                    }
                }
            }
        }
    }
}
