package entities;

import Services.Map;
import Services.PathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import java.util.List;

public class Enemy extends Entity {
    private static final float TILE_SIZE = 16f;

    protected Player player;
    protected int attackDamage;
    protected float attackCooldown;
    protected float attackTimer;

    private List<Vector2> currentPath;
    private int currentWaypointIndex;
    private int lastKnownPlayerGridX = -1;
    private int lastKnownPlayerGridY = -1;
    private int hp;
    private final int experienceReward;
    private final int creditReward;

    public Enemy(
        float x,
        float y,
        Player player,
        float speed,
        int hp,
        int attackDamage,
        float attackCooldown,
        float attackTimer
    ) {
        this(x, y, player, speed, hp, attackDamage, attackCooldown, attackTimer, 5, 2);
    }

    public Enemy(
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
        super(x, y, 16, 16, speed, Color.RED);
        this.player = player;
        this.hp = hp;
        this.attackDamage = attackDamage;
        this.attackCooldown = attackCooldown;
        this.attackTimer = attackTimer;
        this.experienceReward = experienceReward;
        this.creditReward = creditReward;
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
            int startX = (int) ((x + 8f) / TILE_SIZE);
            int startY = (int) ((y + 8f) / TILE_SIZE);
            int targetX = (int) ((player.getX() + 8f) / TILE_SIZE);
            int targetY = (int) ((player.getY() + 8f) / TILE_SIZE);
            int[] validTarget = nearestOpenTile(targetX, targetY);
            currentPath = PathFinder.findPath(
                Map.map, startX, startY, validTarget[0], validTarget[1]
            );
            currentWaypointIndex = 0;
        }

        if (currentPath != null && currentWaypointIndex < currentPath.size()) {
            Vector2 waypoint = currentPath.get(currentWaypointIndex);
            float targetWorldX = waypoint.x * TILE_SIZE;
            float targetWorldY = waypoint.y * TILE_SIZE;
            moveTowards(targetWorldX, targetWorldY, deltaTime);
            if (Math.abs(x - targetWorldX) < 1f && Math.abs(y - targetWorldY) < 1f) {
                currentWaypointIndex++;
            }
        }

        bounds.setPosition(x, y);
        updateAnimation(deltaTime, x - previousX, y - previousY);
    }

    protected void moveDirectlyTowards(float targetX, float targetY, float deltaTime) {
        float previousX = x;
        float previousY = y;
        moveTowards(targetX, targetY, deltaTime);
        bounds.setPosition(x, y);
        updateAnimation(deltaTime, x - previousX, y - previousY);
    }

    private void moveTowards(float targetX, float targetY, float deltaTime) {
        float dx = targetX - x;
        float dy = targetY - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        if (distance < 0.001f) return;

        float moveAmount = speed * deltaTime;
        if (distance <= moveAmount) {
            x = targetX;
            y = targetY;
            return;
        }
        x += dx / distance * moveAmount;
        y += dy / distance * moveAmount;
    }

    private int[] nearestOpenTile(int targetX, int targetY) {
        if (isOpenTile(targetX, targetY)) return new int[] {targetX, targetY};
        int[][] neighbors = {
            {0, 1}, {0, -1}, {1, 0}, {-1, 0},
            {1, 1}, {-1, -1}, {1, -1}, {-1, 1}
        };
        for (int[] offset : neighbors) {
            int checkX = targetX + offset[0];
            int checkY = targetY + offset[1];
            if (isOpenTile(checkX, checkY)) return new int[] {checkX, checkY};
        }
        return new int[] {targetX, targetY};
    }

    private boolean isOpenTile(int gridX, int gridY) {
        return gridX >= 0
            && gridX < Map.map.length
            && gridY >= 0
            && gridY < Map.map[0].length
            && Map.map[gridX][gridY] == 0;
    }

    private boolean pathNeedsRecalculation() {
        if (currentPath == null || currentWaypointIndex >= currentPath.size()) return true;
        int playerGridX = (int) ((player.getX() + 8f) / TILE_SIZE);
        int playerGridY = (int) ((player.getY() + 8f) / TILE_SIZE);
        if (playerGridX != lastKnownPlayerGridX || playerGridY != lastKnownPlayerGridY) {
            lastKnownPlayerGridX = playerGridX;
            lastKnownPlayerGridY = playerGridY;
            return true;
        }
        return false;
    }

    public void takeDamage(int damage) {
        if (isDead()) return;
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            playDeathAnimation();
        } else {
            playHurtAnimation();
        }
    }

    public int getHp() {
        return hp;
    }

    protected void setHp(int hp) {
        this.hp = hp;
        if (hp > 0) resetAnimation();
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public boolean isReadyForRemoval() {
        return isDead() && isAnimationFinished();
    }

    public int getExperienceReward() {
        return experienceReward;
    }

    public int getCreditReward() {
        return creditReward;
    }
}
