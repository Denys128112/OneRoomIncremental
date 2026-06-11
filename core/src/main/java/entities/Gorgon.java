package entities;

import com.badlogic.gdx.math.Vector2;
import java.util.List;

public class Gorgon extends Archer {
    private float petrifyTimer = 2.5f;
    private float poisonTimer = 1f;

    public Gorgon(float x, float y, Player player, List<Projectile> projectiles) {
        super(x, y, player, 58f, 280, 1, 1.4f, 0.7f, projectiles);
        attackRange = 130f;
        EnemyAnimationFactory.attachLarge(
            this, "enemies/gorgon/gorgon-1-topdown.png", 42f, 56f
        );
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (isDead()) return;

        float distance = Vector2.dst(x, y, player.getX(), player.getY());
        if (distance > attackRange) return;

        poisonTimer -= deltaTime;
        petrifyTimer -= deltaTime;
        if (poisonTimer <= 0f) {
            player.applyPoison(3f, 1);
            poisonTimer = 4f;
        }
        if (petrifyTimer <= 0f) {
            playAttackAnimation();
            player.applyStun(0.7f);
            petrifyTimer = 6f;
        }
    }
}
