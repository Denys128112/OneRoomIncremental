package entities;

import java.util.List;

public class SkeletArcher extends Archer{
    public SkeletArcher(float x, float y, Player player, List<Projectile> projectileList) {
        super(x, y, player, 80f, 140, 1, 1f, 0.5f, projectileList);
        EnemyAnimationFactory.attachSmall(this, "enemies/base/enemy-4-topdown.png");
    }
}
