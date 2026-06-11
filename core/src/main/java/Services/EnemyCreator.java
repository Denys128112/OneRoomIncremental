package Services;

import com.badlogic.gdx.utils.Disposable;
import entities.Enemy;
import entities.Goblin;
import entities.Gorgon;
import entities.Kitsune;
import entities.Minotaur;
import entities.Necromant;
import entities.Player;
import entities.Rat;
import entities.Shaman;
import entities.SkeletArcher;
import entities.SkeletMage;
import entities.SkeletSwordman;
import entities.Tengu;
import entities.Werewolf;

public class EnemyCreator implements Disposable {
    public static final int RAT = 1;
    public static final int GOBLIN = 2;
    public static final int SKELETON_SWORDSMAN = 3;
    public static final int SKELETON_ARCHER = 4;
    public static final int WEREWOLF = 5;
    public static final int NECROMANCER = 6;
    public static final int SHAMAN = 7;
    public static final int SKELETON_MAGE = 8;
    public static final int MINOTAUR = 9;
    public static final int GORGON = 10;
    public static final int KARASU_TENGU = 11;
    public static final int YAMABUSHI_TENGU = 12;
    public static final int KITSUNE = 13;

    public Enemy createEnemy(float x, float y, int type, Player player) {
        switch (type) {
            case RAT:
                return new Rat(x, y, player);
            case GOBLIN:
                return new Goblin(x, y, player);
            case SKELETON_SWORDSMAN:
                return new SkeletSwordman(x, y, player);
            case SKELETON_ARCHER:
                return new SkeletArcher(x, y, player, GameManager.projectiles);
            case WEREWOLF:
                return new Werewolf(x, y, player);
            case NECROMANCER:
                return new Necromant(
                    x, y, player, GameManager.deadEnemies, GameManager.enemiesToAdd
                );
            case SHAMAN:
                return new Shaman(x, y, player, GameManager.projectiles);
            case SKELETON_MAGE:
                return new SkeletMage(x, y, player, GameManager.projectiles);
            case MINOTAUR:
                return new Minotaur(x, y, player);
            case GORGON:
                return new Gorgon(x, y, player, GameManager.projectiles);
            case KARASU_TENGU:
                return new Tengu(x, y, player, false);
            case YAMABUSHI_TENGU:
                return new Tengu(x, y, player, true);
            case KITSUNE:
                return new Kitsune(x, y, player, GameManager.projectiles);
            default:
                return new Rat(x, y, player);
        }
    }

    @Override
    public void dispose() {
        // Each animator owns and disposes its own texture.
    }
}
