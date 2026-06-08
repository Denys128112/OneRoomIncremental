package entities;

public class swordMan extends Enemy{
    private float attackDamage = 10f;
    private float attackCooldown = 1f;
    private float attackTimer = 0f;
    public swordMan(float x, float y, Player player) {
        super(x, y, player);
    }
    public void update(float deltaTime) {
        super.update(deltaTime);
        attackTimer -= deltaTime;
        if (bounds.overlaps(player.bounds) && attackTimer <= 0f) {
            player.takeDamage(attackDamage);
            attackTimer = attackCooldown;
        }
    }
}
