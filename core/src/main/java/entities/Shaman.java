package entities;

import java.util.List;

public class Shaman extends Archer{
    public Shaman(float x, float y, Player player, List<Projectile> projectileList){
        super(x, y, player, 60f, 160, 1, 0.8f, 0.5f, projectileList);
        EnemyAnimationFactory.attachSmall(this, "enemies/base/enemy-4-topdown.png");
    }
}
