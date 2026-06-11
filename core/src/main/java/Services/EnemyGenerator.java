package Services;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Array;
import entities.Enemy;
import entities.Player;

public class EnemyGenerator implements Disposable {
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
            int type = chooseEnemyType(wave, i);
            enemyList.add(creator.createEnemy(spawnX, spawnY, type, player));
        }
    }

    private int chooseEnemyType(int wave, int index) {
        // Every fifth wave starts with a guaranteed minotaur boss.
        if (wave >= 5 && wave % 5 == 0 && index == 0) {
            return EnemyCreator.MINOTAUR;
        }
        int maxType;
        if (wave <= 2) maxType = EnemyCreator.GOBLIN;
        else if (wave <= 4) maxType = EnemyCreator.SKELETON_ARCHER;
        else if (wave <= 6) maxType = EnemyCreator.WEREWOLF;
        else if (wave <= 8) maxType = EnemyCreator.SKELETON_MAGE;
        else if (wave <= 10) maxType = EnemyCreator.GORGON;
        else if (wave <= 12) maxType = EnemyCreator.YAMABUSHI_TENGU;
        else maxType = EnemyCreator.KITSUNE;
        return MathUtils.random(EnemyCreator.RAT, maxType);
    }
    public void render(ShapeRenderer shapeRenderer) {
        for (Enemy e:enemyList)
            e.render(shapeRenderer);
    }

    public void render(Batch batch) {
        for (Enemy enemy : enemyList) {
            enemy.render(batch);
        }
    }

    @Override
    public void dispose() {
        creator.dispose();
    }
}
