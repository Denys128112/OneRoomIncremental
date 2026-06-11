package entities;

import com.badlogic.gdx.graphics.Texture;
import entities.animation.SpriteSheetAnimator;
import entities.animation.SpriteSheetLayout;

public class swordMan extends Enemy{
    private int attackDamage = 1;
    private float attackCooldown = 1f;
    private float attackTimer = 0f;

    public swordMan(float x, float y, Player player, Texture texture) {
        super(x, y, player,100);
        setAnimator(new SpriteSheetAnimator(
            texture,
            48,
            64,
            36f,
            48f,
            SpriteSheetLayout.fourDirections()
        ));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (isDead()) return;
        attackTimer -= deltaTime;
        if (bounds.overlaps(player.bounds) && attackTimer <= 0f) {
            playAttackAnimation();
            player.takeDamage(attackDamage);
            attackTimer = attackCooldown;
        }
    }
}
