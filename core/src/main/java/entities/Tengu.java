package entities;

import com.badlogic.gdx.math.Vector2;

public class Tengu extends Enemy {
    private final boolean yamabushi;
    private float strikeCooldown;

    public Tengu(float x, float y, Player player, boolean yamabushi) {
        super(
            x, y, player,
            yamabushi ? 135f : 175f,
            yamabushi ? 300 : 190,
            yamabushi ? 3 : 2,
            yamabushi ? 1.5f : 1f,
            0.5f,
            yamabushi ? 24 : 18,
            yamabushi ? 18 : 12
        );
        this.yamabushi = yamabushi;
        EnemyAnimationFactory.attachLarge(
            this,
            yamabushi
                ? "enemies/yamabushi-tengu/yamabushi-tengu-topdown.png"
                : "enemies/karasu-tengu/karasu-tengu-topdown.png",
            42f,
            56f
        );
    }

    public boolean isYamabushi() {
        return yamabushi;
    }

    @Override
    public void update(float deltaTime) {
        if (isDead()) {
            super.update(deltaTime);
            return;
        }

        strikeCooldown -= deltaTime;
        float distance = Vector2.dst(x, y, player.getX(), player.getY());
        if (distance > 22f) {
            // Tengu fly over obstacles and pursue the player directly.
            moveDirectlyTowards(player.getX(), player.getY(), deltaTime);
        } else {
            updateAnimation(deltaTime, 0f, 0f);
            if (strikeCooldown <= 0f) {
                playAttackAnimation();
                player.takeDamage(attackDamage);
                strikeCooldown = attackCooldown;
            }
        }
    }
}
