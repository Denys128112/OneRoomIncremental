package Services;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.utils.Array;
import entities.Enemy;
import entities.Player;

public class EnemyGenerator {
    EnemyCreator creator= new EnemyCreator();
    public List<Enemy> enemyList= new ArrayList<>();
    private static final int TILE_SIZE = 16;
    private static final int WALL_THICKNESS = 2;
    public void generate(int[][] map, int wave, Player player) {
        int targetEnemyCount = (int) (10 + 0.565f * Math.pow((wave - 1), 1.3));

        Array<GridPoint2> freeTiles = new Array<>();
        for (int x = WALL_THICKNESS; x < map.length - WALL_THICKNESS; x++) {
            for (int y = WALL_THICKNESS; y < map[0].length - WALL_THICKNESS; y++) {
                if (map[x][y] == 0) {
                    freeTiles.add(new GridPoint2(x, y));
                }
            }
        }
        if (freeTiles.isEmpty()) return;
        freeTiles.shuffle();

        int enemiesToSpawn = Math.min(targetEnemyCount, freeTiles.size);
        for (int i = 0; i < enemiesToSpawn; i++) {
            GridPoint2 spawnPoint = freeTiles.get(i);
            float spawnX = spawnPoint.x * TILE_SIZE;
            float spawnY = spawnPoint.y * TILE_SIZE;
            enemyList.add(creator.createEnemy(spawnX, spawnY, MathUtils.random(1, 10), player));
        }
    }
    public void render(ShapeRenderer shapeRenderer) {
        for (Enemy e:enemyList)
            e.render(shapeRenderer);
    }
}
