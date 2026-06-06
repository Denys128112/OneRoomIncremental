package Services;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.MathUtils;

public class Map {
    public static final int SIZE = 63;

    static int[][] map = new int[SIZE][SIZE];

    public static void generateMap() {
        if (MathUtils.randomBoolean(0.5f))
            digers();
        blocks();
    }

    private static void digers() {
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    map[x][y] = 1;
                }
            }
            int diggersCount = 6;
            int corridorsPerDigger = 35;
            for (int d = 0; d < diggersCount; d++) {
                int diggerX = SIZE / 2;
                int diggerY = SIZE / 2;
                int direction = MathUtils.random(3);
                for (int i = 0; i < corridorsPerDigger; i++) {
                    int corridorLength = MathUtils.random(10, 22);

                    for (int step = 0; step < corridorLength; step++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            for (int dy = -1; dy <= 1; dy++) {
                                int clearX = diggerX + dx;
                                int clearY = diggerY + dy;
                                if (clearX >= 0 && clearX < SIZE && clearY >= 0 && clearY < SIZE) {
                                    map[clearX][clearY] = 0;
                                }
                            }
                        }
                        if (direction == 0 && diggerY < SIZE - 2) {
                            diggerY++;
                        } else if (direction == 1 && diggerY > 1) {
                            diggerY--;
                        } else if (direction == 2 && diggerX > 1) {
                            diggerX--;
                        } else if (direction == 3 && diggerX < SIZE - 2) {
                            diggerX++;
                        }
                    }
                    direction = MathUtils.random(3);
                }
            }
    }
    public static void blocks() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                map[x][y] = 0;
            }
        }
        int blocksCount = 100;
        for (int i = 0; i < blocksCount; i++) {
            int sizeX = MathUtils.random(2, 6);
            int sizeY = MathUtils.random(2, 6);

            int x = MathUtils.random(2, SIZE - sizeX - 2);
            int y = MathUtils.random(2, SIZE - sizeY - 2);

            for (int rx = x; rx < x + sizeX; rx++) {
                for (int ry = y; ry < y + sizeY; ry++) {
                    if (rx < SIZE && ry < SIZE) {
                        map[rx][ry] = 1;
                    }
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            int[][] tempMap = new int[SIZE][SIZE];

            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    tempMap[x][y] = map[x][y];
                    if (map[x][y] == 0 && x > 0 && x < SIZE - 1 && y > 0 && y < SIZE - 1) {
                        int surroundingWalls = 0;
                        if (map[x - 1][y] == 1) surroundingWalls++;
                        if (map[x + 1][y] == 1) surroundingWalls++;
                        if (map[x][y - 1] == 1) surroundingWalls++;
                        if (map[x][y + 1] == 1) surroundingWalls++;
                        if (surroundingWalls >= 3) {
                            tempMap[x][y] = 1;
                        }
                    }
                }
            }
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    map[x][y] = tempMap[x][y];
                }
            }
        }
    }

    public static void resolveToRender() {
        int[][] newMap = new int[SIZE][SIZE];
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                newMap[x][y] = map[x][y];

                if (map[x][y] == 1) {
                    boolean floorTop    = (y < SIZE - 1) && map[x][y + 1] == 0;
                    boolean floorRight  = (x < SIZE - 1) && map[x + 1][y] == 0;
                    boolean floorBottom = (y > 0)        && map[x][y - 1] == 0;
                    boolean floorLeft   = (x > 0)        && map[x - 1][y] == 0;

                    int bitmask = 0;
                    if (floorTop)    bitmask += 1;
                    if (floorRight)  bitmask += 2;
                    if (floorBottom) bitmask += 4;
                    if (floorLeft)   bitmask += 8;

                    newMap[x][y] = 10 + bitmask;
                }
            }
        }
        map = newMap;
    }

    public static void createVisualMap(TiledMap tiledMap) {
        generateMap();
        resolveToRender();
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                System.out.print(map[x][y] + " ");
            }
            System.out.println();
        }
        TiledMapTileSet tileset = tiledMap.getTileSets().getTileSet("tileset2");
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("walls");
        TiledMapTile roofTile = tileset.getTile(200);
        TiledMapTile frontWallTile = tileset.getTile(234);
        TiledMapTile backWallTile = tileset.getTile(183);
        TiledMapTile cornerTopLeft = tileset.getTile(182);
        TiledMapTile cornerBottomLeft = tileset.getTile(233);


        for (int x = 1; x < SIZE; x++) {
            for (int y = 1; y < SIZE; y++) {

                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell == null) {
                    cell = new TiledMapTileLayer.Cell();
                }
                cell.setFlipHorizontally(false);
                cell.setFlipVertically(false);
                int tileId = map[x][y];
                switch (tileId) {
                    case 0:
                        cell.setTile(null);
                        break;
                    case 14:
                        cell.setTile(frontWallTile);
                        break;
                    case 12:
                    case 13:
                        cell.setTile(backWallTile);
                        cell.setRotation(TiledMapTileLayer.Cell.ROTATE_270);
                        break;
                    case 18:
                    case 19:
                        cell.setTile(backWallTile);
                        cell.setRotation(TiledMapTileLayer.Cell.ROTATE_90);
                        break;
                    case 22:
                        cell.setTile(cornerBottomLeft);
                        break;
                    case 16:
                        cell.setTile(cornerBottomLeft);
                        cell.setFlipHorizontally(true);
                        break;
                    default:
                        cell.setTile(roofTile);
                        break;
                }
                layer.setCell(x, y, cell);
            }
        }
    }
}
