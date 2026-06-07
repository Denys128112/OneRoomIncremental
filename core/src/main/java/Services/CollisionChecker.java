package Services;

import entities.Entity;

/**
 * Temporary compatibility stub required by the existing Player class.
 *
 * Replace this file when the combat team merges its collision implementation.
 * Keeping the same public methods means Player.java does not need UI-related edits.
 */
public final class CollisionChecker {
    private static final int TILE_SIZE = 16;

    private CollisionChecker() {
    }

    public static boolean isCollisionWithWall(Entity entity) {
        int minX = Math.max(0, (int) Math.floor(entity.bounds.x / TILE_SIZE));
        int minY = Math.max(0, (int) Math.floor(entity.bounds.y / TILE_SIZE));
        int maxX = Math.min(Map.SIZE - 1,
            (int) Math.floor((entity.bounds.x + entity.bounds.width - 0.001f) / TILE_SIZE));
        int maxY = Math.min(Map.SIZE - 1,
            (int) Math.floor((entity.bounds.y + entity.bounds.height - 0.001f) / TILE_SIZE));

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (Map.map[x][y] != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isCollision(Entity first, Entity second) {
        return first.bounds.overlaps(second.bounds);
    }
}
