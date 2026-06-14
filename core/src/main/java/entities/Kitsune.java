package entities;

import Services.Map;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import java.util.List;

public class Kitsune extends Archer {
    private final List<Projectile> projectiles;
    private float illusionTimer = 2f;

    public Kitsune(float x, float y, Player player, List<Projectile> projectiles) {
        super(x, y, player, 105f, 240, 2, 1.2f, 0.5f, projectiles);
        this.projectiles = projectiles;
        attackRange = 180f;
        EnemyAnimationFactory.attachLarge(
            this, "enemies/kitsune/kitsune-topdown.png", 42f, 56f
        );
        this.setLootAmount(5);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (isDead()) return;
        illusionTimer -= deltaTime;
        if (illusionTimer <= 0f) {
            createIllusionVolley();
            blinkToNearbyTile();
            illusionTimer = 5f;
        }
    }

    private void createIllusionVolley() {
        playAttackAnimation();
        float angle = MathUtils.atan2(player.getY() - y, player.getX() - x)
            * MathUtils.radiansToDegrees;
        for (float offset : new float[] {-18f, 0f, 18f}) {
            Projectile foxFire = new Projectile(x + 8f, y + 8f, angle + offset, attackDamage, 2, false, Color.FIREBRICK, true);
            projectiles.add(foxFire);
        }
    }

    private void blinkToNearbyTile() {
        int playerX = (int) (player.getX() / 16f);
        int playerY = (int) (player.getY() / 16f);
        for (int attempt = 0; attempt < 12; attempt++) {
            int gridX = playerX + MathUtils.random(-8, 8);
            int gridY = playerY + MathUtils.random(-8, 8);
            if (gridX > 1 && gridY > 1
                && gridX < Map.map.length - 1
                && gridY < Map.map[0].length - 1
                && Map.map[gridX][gridY] == 0) {
                x = gridX * 16f;
                y = gridY * 16f;
                bounds.setPosition(x, y);
                return;
            }
        }
    }
}
