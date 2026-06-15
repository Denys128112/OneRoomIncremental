package entities;

import Services.GameManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Turret extends Entity {

    private static final float MAX_LIFETIME = 15f;
    private static final float DETECTION_RADIUS = 200f;
    private static final int DAMAGE = 20;
    private static final float FIRE_RATE  = 0.5f;

    private static final int COLS = 4;
    private static final int ROWS = 4;
    private static final int ROW_DOWN = 0;
    private static final int ROW_LEFT = 1;
    private static final int ROW_RIGHT = 2;
    private static final int ROW_UP = 3;

    private static final float RENDER_W = 40f;
    private static final float RENDER_H = 40f;

    private static Texture sharedTexture;
    private static int instanceCount = 0;

    private TextureRegion[] directionFrames;
    private int currentRow = ROW_DOWN;

    private float lifetime = MAX_LIFETIME;
    private float fireTimer = 0f;

    public Turret(float x, float y) {
        super(x, y, (int) RENDER_W, (int) RENDER_H, 0f, Color.YELLOW);

        if (sharedTexture == null) {
            sharedTexture = new Texture("enemies/turel.png");
            sharedTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
        instanceCount++;

        int frameW = sharedTexture.getWidth() / COLS;
        int frameH = sharedTexture.getHeight() / ROWS;

        directionFrames = new TextureRegion[ROWS];
        for (int row = 0; row < ROWS; row++) {
            directionFrames[row] = new TextureRegion(
                sharedTexture,
                40, row * frameH,
                frameW, frameH
            );
        }
    }

    @Override
    public void update(float deltaTime) {
        if (isDead()) return;
        lifetime -= deltaTime;
        if (isDead()) return;

        fireTimer += deltaTime;
        if (fireTimer >= FIRE_RATE) {
            Enemy nearest = findNearest();
            if (nearest != null) {
                float cx = x + RENDER_W / 2f;
                float cy = y + RENDER_H / 2f;
                float angle = (float) Math.toDegrees(Math.atan2(nearest.getY() - cy, nearest.getX() - cx));
                currentRow = angleToRow(angle);
                GameManager.projectiles.add(new Projectile(cx, cy, angle, DAMAGE, 1, false, Color.YELLOW, false));
                fireTimer = 0f;
            }
        }
    }

    private int angleToRow(float angle) {
        float a = ((angle % 360f) + 360f) % 360f;
        if (a >= 45f  && a < 135f) return ROW_UP;
        if (a >= 135f && a < 225f) return ROW_LEFT;
        if (a >= 225f && a < 315f) return ROW_DOWN;
        return ROW_RIGHT;
    }

    private Enemy findNearest() {
        float cx = x + RENDER_W / 2f;
        float cy = y + RENDER_H / 2f;
        Enemy nearest = null;
        float minDist = DETECTION_RADIUS * DETECTION_RADIUS;
        for (Enemy e : GameManager.enemies) {
            if (e.isDead()) continue;
            float dx = e.getX() - cx;
            float dy = e.getY() - cy;
            float dist = dx * dx + dy * dy;
            if (dist < minDist) { minDist = dist; nearest = e; }
        }
        return nearest;
    }

    @Override
    public void render(Batch batch) {
        if (isDead()) return;
        batch.draw(directionFrames[currentRow], x, y, RENDER_W, RENDER_H);
    }

    @Override
    public void render(ShapeRenderer sr) {
        if (isDead()) return;
        sr.setColor(0.15f, 0.15f, 0.15f, 0.8f);
        sr.rect(x, y + RENDER_H + 3f, RENDER_W, 3f);
        sr.setColor(0.1f, 0.85f, 0.2f, 1f);
        sr.rect(x, y + RENDER_H + 3f, RENDER_W * (lifetime / MAX_LIFETIME), 3f);
    }

    public boolean isDead() { return lifetime <= 0f; }
    public float getLifetime() { return lifetime; }

    @Override
    public void dispose() {
        instanceCount--;
        if (instanceCount <= 0 && sharedTexture != null) {
            sharedTexture.dispose();
            sharedTexture = null;
            instanceCount = 0;
        }
        super.dispose();
    }
}
