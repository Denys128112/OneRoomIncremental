package entities.animation;

/**
 * Describes where directional rows are located in a sprite sheet.
 * A negative right row means that LEFT must be mirrored horizontally.
 */
public final class SpriteSheetLayout {
    public final int downRow;
    public final int leftRow;
    public final int rightRow;
    public final int upRow;

    public SpriteSheetLayout(int downRow, int leftRow, int rightRow, int upRow) {
        this.downRow = downRow;
        this.leftRow = leftRow;
        this.rightRow = rightRow;
        this.upRow = upRow;
    }

    /** Layout used by the generated 12x4 enemy sheets. */
    public static SpriteSheetLayout fourDirections() {
        return new SpriteSheetLayout(0, 1, 2, 3);
    }

    /**
     * Layout for sheets containing DOWN, LEFT and UP only.
     * RIGHT is produced by mirroring LEFT.
     */
    public static SpriteSheetLayout threeDirectionsMirrored() {
        return new SpriteSheetLayout(0, 1, -1, 2);
    }
}
