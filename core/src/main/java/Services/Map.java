package Services;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.MathUtils;
import entities.Box;
import entities.Chest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Map {
    public static final int SIZE = 62;
    public static int[][] map = new int[SIZE][SIZE];
    public static float barellChance=0.01f;
    public static float chestChance=0.001f;
    public static ArrayList<Chest> chests=new ArrayList<>();
    public static ArrayList<Box> boxes=new ArrayList<>();
    public static void generateMap() {
        if (MathUtils.randomBoolean(0.5f))
            digers();
        else
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
        fillWithWalls();
    }
    public static void fillWithWalls() {
            boolean[][] reachable = new boolean[SIZE][SIZE];
            Queue<int[]> queue = new LinkedList<>();
            queue.add(new int[]{1, 1});
            reachable[1][1] = true;

            while (!queue.isEmpty()) {
                int[] cell = queue.poll();
                int cx = cell[0], cy = cell[1];

                int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
                for (int[] d : dirs) {
                    int nx = cx + d[0], ny = cy + d[1];
                    if (nx >= 0 && nx < SIZE && ny >= 0 && ny < SIZE
                        && !reachable[nx][ny] && map[nx][ny] == 0) {
                        reachable[nx][ny] = true;
                        queue.add(new int[]{nx, ny});
                    }
                }
            }
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    if (map[x][y] == 0 && !reachable[x][y]) {
                        map[x][y] = 1;
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
        TiledMapTileSet tileset = tiledMap.getTileSets().getTileSet("tileset2");
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("walls");
        TiledMapTile roofTile = tileset.getTile(200);
        TiledMapTile frontWallTile = tileset.getTile(234);
        TiledMapTile backWallTile = tileset.getTile(183);
        TiledMapTile cornerTopLeft = tileset.getTile(182);
        TiledMapTile cornerBottomLeft = tileset.getTile(233);


        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x+1, y+1);
                if (cell == null) {
                    cell = new TiledMapTileLayer.Cell();
                }
                cell.setFlipHorizontally(false);
                cell.setFlipVertically(false);
                cell.setRotation(0);
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
                layer.setCell(x+1, y+1, cell);
            }
        }
        addProps();
    }
    public static void addProps(){
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (map[x][y]==0) {
                    if (MathUtils.randomBoolean(chestChance)) {
                        map[x][y]=1;
                        Chest chest=new Chest((x+1)*16,(y+1)*16,new Texture("tiles\\Chests.png"));
                        chests.add(chest);
                    } else if (MathUtils.randomBoolean(barellChance)) {
                        map[x][y]=1;
                        Box box=new Box((x+1)*16,(y+1)*16,new Texture("tiles\\boxes.png"));
                        boxes.add(box);
                    }
                }
            }
        }
        }
    }

