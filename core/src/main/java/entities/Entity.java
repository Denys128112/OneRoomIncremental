package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import entities.animation.SpriteSheetAnimator;

public abstract class Entity implements Disposable {
    protected float x, y;
    protected float width, height;
    protected float speed;
    protected Color color;
    public Rectangle bounds;
    protected float rotation = 0;
    private SpriteSheetAnimator animator;

    private static com.badlogic.gdx.graphics.Texture whitePixelTex;
    private float spriteRenderWidth;
    private float spriteRenderHeight;

    public Entity(float x, float y, float width, float height, float speed, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.color = color;
        this.bounds = new Rectangle(x, y, width, height);

    }

    public abstract void update(float deltaTime);

    public void render(ShapeRenderer shapeRenderer) {
        if (animator != null) return;
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, width / 2, height / 2, width, height, 1f, 1f, rotation);
    }

    public void render(Batch batch) {
        if (animator != null) animator.draw(batch, x, y, width);
    }

    public boolean hasSpriteAnimation() {
        return animator != null;
    }

    protected void setAnimator(SpriteSheetAnimator animator, float renderWidth, float renderHeight) {
        if (this.animator != null) this.animator.dispose();
        this.animator = animator;
        this.spriteRenderWidth = renderWidth;
        this.spriteRenderHeight = renderHeight;
    }

    protected void setAnimator(SpriteSheetAnimator animator) {
        setAnimator(animator, width, height);
    }

    protected void updateAnimation(float deltaTime, float movementX, float movementY) {
        if (animator != null) animator.update(deltaTime, movementX, movementY);
    }

    protected static com.badlogic.gdx.graphics.Texture getWhitePixel() {
        if (whitePixelTex == null) {
            com.badlogic.gdx.graphics.Pixmap pm = new com.badlogic.gdx.graphics.Pixmap(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
            pm.setColor(1, 1, 1, 1);
            pm.fill();
            whitePixelTex = new com.badlogic.gdx.graphics.Texture(pm);
            pm.dispose();
        }
        return whitePixelTex;
    }

    protected void playAttackAnimation() {
        if (animator != null) animator.playAttack();
    }

    protected void playHurtAnimation() {
        if (animator != null) animator.playHurt();
    }

    protected void playDeathAnimation() {
        if (animator != null) animator.playDeath();
    }

    protected boolean isAnimationFinished() {
        return animator == null || animator.isFinished();
    }

    protected void resetAnimation() {
        if (animator != null) animator.reset();
    }

    public float getX() { return x; }
    public float getY() { return y; }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRotation() {
        return rotation;
    }

    public float getSpriteRenderWidth()  { return spriteRenderWidth; }
    public float getSpriteRenderHeight() { return spriteRenderHeight; }

    @Override
    public void dispose() {
        if (animator != null) {
            animator.dispose();
            animator = null;
        }
    }
}
