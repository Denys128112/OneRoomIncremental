package entities;

import entities.animation.SpriteSheetAnimator;
import entities.animation.SpriteSheetLayout;

final class EnemyAnimationFactory {
    private EnemyAnimationFactory() {
    }

    static void attachSmall(Enemy enemy, String texturePath) {
        enemy.setAnimator(new SpriteSheetAnimator(
            texturePath,
            32,
            32,
            32f,
            32f,
            SpriteSheetLayout.threeDirectionsMirrored()
        ));
    }

    static void attachLarge(Enemy enemy, String texturePath, float width, float height) {
        enemy.setAnimator(new SpriteSheetAnimator(
            texturePath,
            48,
            64,
            width,
            height,
            SpriteSheetLayout.fourDirections()
        ));
    }
}
