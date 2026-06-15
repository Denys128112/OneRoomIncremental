package Services;

import com.badlogic.gdx.math.Rectangle;
import entities.Entity;

public class CollisionChecker {
    public enum WallHitSide { NONE, HORIZONTAL, VERTICAL }
    public static boolean isCollision(Entity e1, Entity e2){
        return e1.bounds.overlaps(e2.bounds);
    }
    public static boolean isCollisionWithWall(Entity e1) {
        int startX = (int) (e1.bounds.x / 16f);
        int endX = (int) ((e1.bounds.x + e1.bounds.width) / 16f);
        int startY = (int) (e1.bounds.y / 16f);
        int endY = (int) ((e1.bounds.y + e1.bounds.height) / 16f);
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (x < 1 || x >= Map.SIZE || y < 1 || y >= Map.SIZE) {
                    return true;
                }
                int tileId = Map.map[x][y];
                if (tileId != 0) {
                    float wallX = x * 16f;
                    float wallY = y * 16f;
                    float wallWidth = 16f;
                    float wallHeight = 16f;

                    if (tileId == 14) {
                        wallY += 8f;
                        wallHeight = 8f;
                    }
                    else if (tileId == 12 || tileId == 13) {
                        wallWidth = 8f;
                    }
                    else if (tileId == 18 || tileId == 19) {
                        wallX += 8f;
                        wallWidth = 8f;
                    }
                    else if (tileId == 16) {
                        wallWidth = 8f;
                        wallY += 8f;
                        wallHeight = 8f;
                    }
                    else if (tileId == 22) {
                        wallX += 8f;
                        wallWidth = 8f;
                        wallY += 8f;
                        wallHeight = 8f;
                    }
                    Rectangle wallBounds = new Rectangle(wallX, wallY, wallWidth, wallHeight);
                    if (e1.bounds.overlaps(wallBounds)) {
                        return true;
                    }
                }
            }
        }
        for (com.badlogic.gdx.math.Rectangle wall : Services.GameManager.temporaryWalls) {if (e1.bounds.overlaps(wall)) return true;}
        return false;
    }

    public static WallHitSide getWallHitSide(Entity e1) {
        int startX = (int) (e1.bounds.x / 16f);
        int endX = (int) ((e1.bounds.x + e1.bounds.width) / 16f);
        int startY = (int) (e1.bounds.y / 16f);
        int endY = (int) ((e1.bounds.y + e1.bounds.height) / 16f);

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (x < 1 || x >= Map.SIZE || y < 1 || y >= Map.SIZE)
                    return WallHitSide.VERTICAL;

                int tileId = Map.map[x][y];
                if (tileId == 0) continue;

                float wallX = x * 16f, wallY = y * 16f;
                float wallW = 16f, wallH = 16f;

                if (tileId == 12 || tileId == 13) { wallW = 8f; }
                else if (tileId == 18 || tileId == 19) { wallX += 8f; wallW = 8f; }
                else if (tileId == 16) { wallW = 8f; wallY += 8f; wallH = 8f; }
                else if (tileId == 22) { wallX += 8f; wallW = 8f; wallY += 8f; wallH = 8f; }
                else if (tileId == 14) { wallY += 8f; wallH = 8f; }

                Rectangle wb = new Rectangle(wallX, wallY, wallW, wallH);
                if (!e1.bounds.overlaps(wb)) continue;

                if (wallW < wallH) return WallHitSide.VERTICAL;
                else return WallHitSide.HORIZONTAL;
            }
        }
        return WallHitSide.NONE;
    }
}
