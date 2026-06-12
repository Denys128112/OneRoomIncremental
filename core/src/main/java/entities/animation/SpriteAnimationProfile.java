package entities.animation;

public final class SpriteAnimationProfile {
    public static final SpriteAnimationProfile COMPACT = new SpriteAnimationProfile(
        new int[] {0, 2, 6, 9, 10},
        new int[] {2, 4, 3, 1, 2}
    );

    public static final SpriteAnimationProfile ORIGINAL_HERO = new SpriteAnimationProfile(
        new int[] {0, 4, 10, 14, 16},
        new int[] {4, 6, 4, 2, 8}
    );

    private final int[] startColumns;
    private final int[] frameCounts;

    private SpriteAnimationProfile(int[] startColumns, int[] frameCounts) {
        this.startColumns = startColumns;
        this.frameCounts = frameCounts;
    }

    int startColumn(AnimationState state) {
        return startColumns[indexOf(state)];
    }

    int frameCount(AnimationState state) {
        return frameCounts[indexOf(state)];
    }

    private int indexOf(AnimationState state) {
        switch (state) {
            case MOVE:
                return 1;
            case ATTACK:
                return 2;
            case HURT:
                return 3;
            case DEATH:
                return 4;
            case IDLE:
            default:
                return 0;
        }
    }
}
