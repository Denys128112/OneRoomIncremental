package entities;

public class Rat extends SwordMan{
    public Rat(float x, float y, Player player) {
        super(x, y, player, 150f, 20, 1, 0.6f, 0.5f, 2, 1);
        EnemyAnimationFactory.attachSmall(this, "enemies/base/enemy-1-topdown.png");
    }
}
