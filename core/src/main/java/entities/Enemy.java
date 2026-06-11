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
    protected int attackDamage;
    protected float attackCooldown;
    protected float attackTimer;
    private int lastKnownPlayerGridX = -1;
    private int lastKnownPlayerGridY = -1;
    private int hp;
    public Enemy(float x, float y, Player player, float speed,int hp,int attackDamage,float attackCooldown,float attackTimer) {
        super(x, y, 16, 16, speed, Color.RED);
        this.player = player;
        this.hp = hp;
        this.attackDamage=attackDamage;
        this.attackCooldown=attackCooldown;
        this.attackTimer=attackTimer;
    }

    @Override
    public void update(float deltaTime) {
        if (player == null) return;
        if (pathNeedsRecalculation()) {
            int startX = (int) ((this.x + 8f) / TILE_SIZE);
            int startY = (int) ((this.y + 8f) / TILE_SIZE);
            int targetX = (int) ((player.getX() + 8f) / TILE_SIZE);
            int targetY = (int) ((player.getY() + 8f) / TILE_SIZE);
            if (targetX >= 0 && targetX < Map.map.length && targetY >= 0 && targetY < Map.map[0].length) {
                if (Map.map[targetX][targetY] != 0) {
                    int[][] neighbors = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};
                    for (int[] offset : neighbors) {
                        int checkX = targetX + offset[0];
                        int checkY = targetY + offset[1];
                        if (checkX >= 0 && checkX < Map.map.length && checkY >= 0 && checkY < Map.map[0].length) {
                            if (Map.map[checkX][checkY] == 0) {
                                targetX = checkX;
                                targetY = checkY;
                                break;
                            }
                        }
                    }
                }
            }
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

        int currentPlayerGridX = (int) ((player.getX()+8f) / TILE_SIZE);
        int currentPlayerGridY = (int) ((player.getY()+8f) / TILE_SIZE);

        if (currentPlayerGridX != lastKnownPlayerGridX || currentPlayerGridY != lastKnownPlayerGridY) {
            lastKnownPlayerGridX = currentPlayerGridX;
            lastKnownPlayerGridY = currentPlayerGridY;
            return true;
        }
        return false;
    }
    public void takeDamage(int damage) {
        hp -= damage;
    }
    public int getHp() { return hp; }

    protected void setHp(int i) {
        hp = i;
    }
}
