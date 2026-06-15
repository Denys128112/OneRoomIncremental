package entities;

public class SwordMan extends Enemy {
    public SwordMan(
        float x,
        float y,
        Player player,
        float speed,
        int hp,
        int attackDamage,
        float attackCooldown,
        float attackTimer
    ) {
        super(x, y, player, speed, hp, attackDamage, attackCooldown, attackTimer);
    }

    public SwordMan(
        float x,
        float y,
        Player player,
        float speed,
        int hp,
        int attackDamage,
        float attackCooldown,
        float attackTimer,
        int experienceReward,
        int creditReward
    ) {
        super(
            x, y, player, speed, hp, attackDamage, attackCooldown, attackTimer,
            experienceReward, creditReward
        );
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (isDead()) return;
        attackTimer -= deltaTime;
        if (bounds.overlaps(player.bounds) && attackTimer <= 0f) {
            playAttackAnimation();
            player.takeDamage(attackDamage, this);
            attackTimer = attackCooldown;
        }
    }
}
