package Services;

import entities.Enemy;
import entities.swordMan;

public class EnemyCreator {
    public Enemy createEnemy(float x, float y, int type){
        return new swordMan(x,y);
    }
}
