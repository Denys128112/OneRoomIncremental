package entities;

public class SwordMan extends Enemy{


    public SwordMan(float x, float y, Player player,float speed,int hp,int attackDamage,float attackCooldown,float attackTimer) {
        super(x, y, player,speed,hp,attackDamage,attackCooldown,attackTimer);
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
