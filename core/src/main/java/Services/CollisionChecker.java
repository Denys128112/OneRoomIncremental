package Services;

import com.badlogic.gdx.math.Rectangle;
import entities.Chest;
import entities.Entity;

public class CollisionChecker {
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
                int mapX = x - 1;
                int mapY = y - 1;
                if (mapX < 0 || mapX >= Map.SIZE || mapY < 0 || mapY >= Map.SIZE) {
                    return true;
                }

                int tileId = Map.map[mapX][mapY];
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
        return false;
    }

}
