package Services;

import entities.Enemy;
import entities.Player;
import entities.swordMan;

public class EnemyCreator {
    public Enemy createEnemy(float x, float y, int type, Player player){
        return new swordMan(x, y, player);
    }
}
