package entities;

public class Goblin extends SwordMan{
    public Goblin(float x, float y, Player player) {
        super(x, y, player, 100f, 90, 1, 0.8f, 0.5f, 5, 3);
        EnemyAnimationFactory.attachSmall(this, "enemies/base/enemy-2-topdown.png");
    }
}
