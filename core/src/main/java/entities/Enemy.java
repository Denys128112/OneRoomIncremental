package entities;

import Services.Map;
import Services.PathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class Enemy extends Entity {

    protected Player player;

    private List<Vector2> currentPath;
    private int currentWaypointIndex = 0;
    private final float TILE_SIZE = 16f;

    private int lastKnownPlayerGridX = -1;
    private int lastKnownPlayerGridY = -1;
    private int hp;
    public Enemy(float x, float y, Player player, int hp) {
        super(x, y, 16, 16, 50f, Color.RED);
        this.player = player;
        this.hp = hp;
    }

    @Override
    public void update(float deltaTime) {
        if (player == null) return;
        if (isDead()) {
            updateAnimation(deltaTime, 0f, 0f);
            return;
        }
        float previousX = x;
        float previousY = y;
        if (pathNeedsRecalculation()) {
            int startX = (int) (this.x / TILE_SIZE);
            int startY = (int) (this.y / TILE_SIZE);
            int targetX = (int) (player.getX() / TILE_SIZE);
            int targetY = (int) (player.getY() / TILE_SIZE);
            currentPath = PathFinder.findPath(Map.map, startX, startY, targetX, targetY);
            currentWaypointIndex = 0;
        }
        if (currentPath != null && currentWaypointIndex < currentPath.size()) {
            Vector2 waypoint = currentPath.get(currentWaypointIndex);

            float targetWorldX = waypoint.x * TILE_SIZE;
            float targetWorldY = waypoint.y * TILE_SIZE;

            moveTowards(targetWorldX, targetWorldY, deltaTime);

            if (Math.abs(this.x - targetWorldX) < 1f && Math.abs(this.y - targetWorldY) < 1f) {
                currentWaypointIndex++;
            }
        }

        bounds.setPosition(x, y);
        updateAnimation(deltaTime, x - previousX, y - previousY);
    }

    private void moveTowards(float targetX, float targetY, float deltaTime) {
        float dx = targetX - this.x;
        float dy = targetY - this.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float moveAmount = speed * deltaTime;

        if (distance <= moveAmount) {
            this.x = targetX;
            this.y = targetY;
            return;
        }

        float dirX = dx / distance;
        float dirY = dy / distance;

        this.x += dirX * moveAmount;
        this.y += dirY * moveAmount;
    }

    private boolean pathNeedsRecalculation() {
        if (currentPath == null || currentWaypointIndex >= currentPath.size()) {
            return true;
        }

        int currentPlayerGridX = (int) (player.getX() / TILE_SIZE);
        int currentPlayerGridY = (int) (player.getY() / TILE_SIZE);

        if (currentPlayerGridX != lastKnownPlayerGridX || currentPlayerGridY != lastKnownPlayerGridY) {
            lastKnownPlayerGridX = currentPlayerGridX;
            lastKnownPlayerGridY = currentPlayerGridY;
            return true;
        }
        return false;
    }
    public void takeDamage(int damage) {
        if (isDead()) return;
        hp -= damage;
        if (hp <= 0) {
            playDeathAnimation();
        } else {
            playHurtAnimation();
        }
    }
    public int getHp() { return hp; }

    public boolean isDead() {
        return hp <= 0;
    }

    public boolean isReadyForRemoval() {
        return isDead() && isAnimationFinished();
    }
}
