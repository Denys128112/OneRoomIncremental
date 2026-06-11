package entities;

public class SkeletSwordman extends SwordMan{
    public SkeletSwordman(float x, float y, Player player) {
        super(x, y, player, 80f, 140, 1, 1f, 0.5f, 8, 5);
        EnemyAnimationFactory.attachSmall(this, "enemies/base/enemy-3-topdown.png");
    }
}
