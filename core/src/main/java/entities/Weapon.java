package entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Weapon {
    protected Player owner;
    protected float attackCooldown;
    protected float cooldownTimer;
    protected int damage;

    public Weapon(Player owner, float attackCooldown, int damage) {
        this.owner = owner;
        this.attackCooldown = attackCooldown;
        this.damage = damage;
        this.cooldownTimer = 0f;
    }

    public void update(float deltaTime) {
        if (cooldownTimer > 0) {
            cooldownTimer -= deltaTime;
        }
    }

    public boolean canAttack() {
        return cooldownTimer <= 0;
    }

    public abstract void attack();

    public void render(ShapeRenderer shapeRenderer) {
    }

}
