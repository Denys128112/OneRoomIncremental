package entities;

public class Minotaur extends SwordMan {
    public Minotaur(float x, float y, Player player) {
        super(x, y, player, 48f, 650, 3, 1.4f, 0.8f, 40, 30);
        EnemyAnimationFactory.attachLarge(
            this, "enemies/minotaur/minotaur-1-topdown.png", 54f, 72f
        );
        this.setLootAmount(6);
        bounds.setSize(28f, 28f);
    }
}
