package Services;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import entities.Enemy;
import entities.Player;
import entities.swordMan;

public class EnemyCreator implements Disposable {
    private final Texture enemyTexture =
        new Texture("enemies/gorgon/gorgon-1-topdown.png");

    public Enemy createEnemy(float x, float y, int type, Player player){
        return new swordMan(x, y, player, enemyTexture);
    }

    @Override
    public void dispose() {
        enemyTexture.dispose();
    }
}
