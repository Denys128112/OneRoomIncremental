package entities.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

/**
 * Shared directional animator for heroes and enemies.
 *
 * Expected columns:
 * 0-1 idle, 2-5 movement, 6-8 attack, 9 hurt, 10-11 death.
 */
public final class SpriteSheetAnimator implements Disposable {
    private static final float MOVE_FRAME_TIME = 0.11f;
    private static final float IDLE_FRAME_TIME = 0.28f;
    private static final float ACTION_FRAME_TIME = 0.10f;
    private static final float WORLD_UNITS_PER_MOVE_FRAME = 6f;
    private static final float DIRECTION_CHANGE_DELAY = 0.06f;

    private final Texture texture;
    private final boolean ownsTexture;
    private final TextureRegion[][] frames;
    private final SpriteSheetLayout layout;
    private final float renderWidth;
    private final float renderHeight;

    private AnimationState state = AnimationState.IDLE;
    private Direction direction = Direction.DOWN;
    private Direction pendingDirection = Direction.DOWN;
    private float pendingDirectionTime;
    private float stateTime;
    private boolean finished;

    public SpriteSheetAnimator(
        String texturePath,
        int frameWidth,
        int frameHeight,
        float renderWidth,
        float renderHeight,
        SpriteSheetLayout layout
    ) {
        this(new Texture(texturePath), true, frameWidth, frameHeight, renderWidth, renderHeight, layout);
    }

    public SpriteSheetAnimator(
        Texture texture,
        int frameWidth,
        int frameHeight,
        float renderWidth,
        float renderHeight,
        SpriteSheetLayout layout
    ) {
        this(texture, false, frameWidth, frameHeight, renderWidth, renderHeight, layout);
    }

    private SpriteSheetAnimator(
        Texture texture,
        boolean ownsTexture,
        int frameWidth,
        int frameHeight,
        float renderWidth,
        float renderHeight,
        SpriteSheetLayout layout
    ) {
        this.texture = texture;
        this.ownsTexture = ownsTexture;
        this.renderWidth = renderWidth;
        this.renderHeight = renderHeight;
        this.layout = layout;
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        frames = TextureRegion.split(texture, frameWidth, frameHeight);
    }

    public void update(float delta, float movementX, float movementY) {
        if (state == AnimationState.DEATH && finished) return;

        boolean moving = isMoving(movementX, movementY);
        updateDirection(delta, movementX, movementY);

        if (isOneShotState()) {
            stateTime += delta;
        } else {
            setState(moving ? AnimationState.MOVE : AnimationState.IDLE);
            if (state == AnimationState.MOVE) {
                float distance = (float) Math.sqrt(
                    movementX * movementX + movementY * movementY
                );
                stateTime += distance / WORLD_UNITS_PER_MOVE_FRAME * MOVE_FRAME_TIME;
            } else {
                stateTime += delta;
            }
        }

        if (isOneShotState() && stateTime >= getFrameCount(state) * ACTION_FRAME_TIME) {
            if (state == AnimationState.DEATH) {
                finished = true;
            } else {
                setState(moving ? AnimationState.MOVE : AnimationState.IDLE);
            }
        }
    }

    public void playAttack() {
        if (state != AnimationState.DEATH) setState(AnimationState.ATTACK);
    }

    public void playHurt() {
        if (state != AnimationState.DEATH) setState(AnimationState.HURT);
    }

    public void playDeath() {
        setState(AnimationState.DEATH);
    }

    public boolean isFinished() {
        return finished;
    }

    public void draw(Batch batch, float entityX, float entityY, float entityWidth) {
        int row = rowFor(direction);
        boolean mirror = direction == Direction.RIGHT && layout.rightRow < 0;
        TextureRegion frame = currentFrame(row);

        float drawX = Math.round(entityX + (entityWidth - renderWidth) * 0.5f);
        float drawY = Math.round(entityY);
        if (mirror) {
            batch.draw(frame, drawX + renderWidth, drawY, -renderWidth, renderHeight);
        } else {
            batch.draw(frame, drawX, drawY, renderWidth, renderHeight);
        }
    }

    private TextureRegion currentFrame(int row) {
        int start = startColumn(state);
        int count = getFrameCount(state);
        float frameTime = state == AnimationState.IDLE ? IDLE_FRAME_TIME
            : state == AnimationState.MOVE ? MOVE_FRAME_TIME : ACTION_FRAME_TIME;
        int index = (int) (stateTime / frameTime);
        if (state == AnimationState.DEATH || state == AnimationState.HURT) {
            index = Math.min(index, count - 1);
        } else {
            index %= count;
        }
        return frames[row][start + index];
    }

    private void updateDirection(float delta, float movementX, float movementY) {
        if (!isMoving(movementX, movementY)) return;
        Direction candidate;
        if (Math.abs(movementX) > Math.abs(movementY)) {
            candidate = movementX < 0f ? Direction.LEFT : Direction.RIGHT;
        } else {
            candidate = movementY < 0f ? Direction.DOWN : Direction.UP;
        }

        if (candidate == direction) {
            pendingDirection = direction;
            pendingDirectionTime = 0f;
            return;
        }
        if (candidate != pendingDirection) {
            pendingDirection = candidate;
            pendingDirectionTime = 0f;
            return;
        }

        pendingDirectionTime += delta;
        if (pendingDirectionTime >= DIRECTION_CHANGE_DELAY) {
            direction = candidate;
            pendingDirectionTime = 0f;
        }
    }

    private void setState(AnimationState next) {
        if (state == next) return;
        state = next;
        stateTime = 0f;
        finished = false;
    }

    private boolean isOneShotState() {
        return state == AnimationState.ATTACK
            || state == AnimationState.HURT
            || state == AnimationState.DEATH;
    }

    private static boolean isMoving(float x, float y) {
        return Math.abs(x) > 0.001f || Math.abs(y) > 0.001f;
    }

    private int rowFor(Direction value) {
        switch (value) {
            case LEFT:
                return layout.leftRow;
            case RIGHT:
                return layout.rightRow >= 0 ? layout.rightRow : layout.leftRow;
            case UP:
                return layout.upRow;
            case DOWN:
            default:
                return layout.downRow;
        }
    }

    private static int startColumn(AnimationState value) {
        switch (value) {
            case MOVE:
                return 2;
            case ATTACK:
                return 6;
            case HURT:
                return 9;
            case DEATH:
                return 10;
            case IDLE:
            default:
                return 0;
        }
    }

    private static int getFrameCount(AnimationState value) {
        switch (value) {
            case MOVE:
                return 4;
            case ATTACK:
                return 3;
            case HURT:
                return 1;
            case DEATH:
            case IDLE:
            default:
                return 2;
        }
    }

    @Override
    public void dispose() {
        if (ownsTexture) texture.dispose();
    }
}
