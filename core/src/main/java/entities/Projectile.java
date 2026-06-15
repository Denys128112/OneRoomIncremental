package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.List;

public class Projectile extends Entity {
    private float dx;
    private float dy;
    private int damage;

    public boolean isPiercing;
    public boolean isEnemyProjectile;
    public List<Enemy> hitEnemies;

    public int ricochetCount = 0;
    public static final int MAX_RICOCHET = 2;
    public float travelDistance = 0f;
    public int maxHits = 1;

    private com.badlogic.gdx.graphics.g2d.TextureRegion[] animFrames;
    private int animFrameCount = 1;
    private float animTimer = 0f;
    private static final float ANIM_FRAME_TIME = 0.08f;

    private int type;

    public Projectile(float x, float y, float angle, int damage, int type, boolean isPiercing, Color color, boolean isEnemyProjectile) {
        super(x, y, 8, 8, 500f, color);
        this.rotation = angle;
        this.damage = damage;
        this.type = type;
        this.isPiercing = isPiercing;
        this.isEnemyProjectile = isEnemyProjectile;
        this.hitEnemies = new ArrayList<>();

        if (type == 1) {
            width = 16f; height = 4f; speed = 600f;
        } else if (type == 2) {
            width = 12f; height = 12f; speed = 400f;
        } else if (type == 3) {
            width = 28f; height = 6f; speed = 700f;
        }

        this.bounds.width = width;
        this.bounds.height = height;

        this.dx = MathUtils.cosDeg(angle);
        this.dy = MathUtils.sinDeg(angle);
    }

    public Projectile(float x, float y, float angle, int damage, int type, boolean isPiercing, Color color, boolean isEnemyProjectile, com.badlogic.gdx.graphics.Texture spriteSheet, int sheetRow, int frameCount) {
        this(x, y, angle, damage, type, isPiercing, color, isEnemyProjectile);
        com.badlogic.gdx.graphics.g2d.TextureRegion[][] all = com.badlogic.gdx.graphics.g2d.TextureRegion.split(spriteSheet, 16, 16);
        animFrames = new com.badlogic.gdx.graphics.g2d.TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++)
            animFrames[i] = all[sheetRow][i];
        animFrameCount = frameCount;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public void update(float deltaTime) {
        float moveX = dx * speed * deltaTime;
        float moveY = dy * speed * deltaTime;
        x += moveX;
        y += moveY;

        travelDistance += Math.sqrt(moveX * moveX + moveY * moveY);
        bounds.setPosition(x - width/2, y - height/2);

        if (animFrames != null) {
            animTimer += deltaTime;
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if (animFrames != null) return;
        shapeRenderer.setColor(color);
        if (type == 2) {
            shapeRenderer.circle(x, y, width / 2);
        } else {
            shapeRenderer.rect(x - width/2, y - height/2, width / 2, height / 2, width, height, 1f, 1f, rotation);
        }
    }

    public void render(com.badlogic.gdx.graphics.g2d.Batch batch) {
        if (animFrames == null) return;
        int idx = (int)(animTimer / ANIM_FRAME_TIME) % animFrameCount;
        com.badlogic.gdx.graphics.g2d.TextureRegion frame = animFrames[idx];
        float drawSize = 24f;
        batch.draw(frame,
            x - drawSize / 2f, y - drawSize / 2f,
            drawSize / 2f, drawSize / 2f,
            drawSize, drawSize,
            1f, 1f,
            rotation);
    }

    public boolean hasSprite() { return animFrames != null; }

    public void reflectX() { dx = -dx; }
    public void reflectY() { dy = -dy; }
    public float getDx() { return dx; }
    public float getDy() { return dy; }
}
