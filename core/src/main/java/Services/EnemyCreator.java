package Services;

import entities.*;
import screens.GameScreen;

public class EnemyCreator {
    public Enemy createEnemy(float x, float y, int type, Player player) {
        if (type == 1) return new Goblin(x, y, player);
        else if (type == 2) return new Rat(x, y, player);
        else if (type == 3) return new SkeletSwordman(x, y, player);
        else if (type == 4) return new SkeletArcher(x, y, player, GameManager.projectiles);
        else if (type == 5) return new Werewolf(x, y, player);
        else if (type == 6) return new Necromant(x, y, player, GameManager.deadEnemies, GameManager.enemiesToAdd);
        else if (type == 7) return new Shaman(x, y, player, GameManager.projectiles);
        return new SkeletMage(x, y, player, GameManager.projectiles);
    }
}
