package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class Necromant extends Enemy {

    private Player realPlayer;
    private List<Enemy> deadEnemies;
    private List<Enemy> enemiesToAdd;
    private Vector2 fleeTarget = new Vector2();
    private float fleeTimer = 0;
    private float reviveCooldown = 4.0f;
    private float reviveTimer = 0f;

    public Necromant(float x, float y, Player player, List<Enemy> deadEnemies, List<Enemy> enemiesToAdd) {
        super(x, y, player, 80f, 150, 0, 0.2f, 0.5f);
        this.color = Color.PURPLE;
        this.player = new Player(player.getX(), player.getY());
        this.realPlayer = player;
        this.deadEnemies = deadEnemies;
        this.enemiesToAdd = enemiesToAdd;
        EnemyAnimationFactory.attachSmall(this, "enemies/base/enemy-4-topdown.png");
    }

    @Override
    public void update(float deltaTime) {
        if (isDead()) {
            super.update(deltaTime);
            return;
        }
        fleeTimer += deltaTime;
        if (fleeTimer >= 0.5f) {
            fleeTimer = 0;
            updateFleeTarget();
        }

        this.player.setX(fleeTarget.x);
        this.player.setY(fleeTarget.y);

        reviveTimer -= deltaTime;
        if (reviveTimer <= 0) {
            revive();
            reviveTimer = reviveCooldown;
        }
        super.update(deltaTime);
    }

    private void updateFleeTarget() {
        float bestDist = -1;
        float[] angles = {0, 45, 90, 135, 180, 225, 270, 315};
        float checkRadius = 200f;

        for (float angle : angles) {
            float rad = (float) Math.toRadians(angle);
            float cx = x + (float) Math.cos(rad) * checkRadius;
            float cy = y + (float) Math.sin(rad) * checkRadius;

            float dist = Vector2.dst(cx, cy, realPlayer.getX(), realPlayer.getY());
            if (dist > bestDist) {
                bestDist = dist;
                fleeTarget.set(cx, cy);
            }
        }
    }

    private void revive() {
        if (deadEnemies.isEmpty()) return;
        Enemy target = deadEnemies.stream()
            .min((e1, e2) -> Float.compare(
                Vector2.dst(this.x, this.y, e1.getX(), e1.getY()),
                Vector2.dst(this.x, this.y, e2.getX(), e2.getY())
            ))
            .orElse(null);
        if (target != null) {

            deadEnemies.remove(target);

            target.setHp(20);

            target.color = Color.TEAL;
            enemiesToAdd.add(target);
        }
    }
}
