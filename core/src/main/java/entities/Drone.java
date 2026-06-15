package entities;

import Services.GameManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Drone extends Entity {

    private float attackCooldown = 1.5f;
    private float attackTimer = 0f;
    private int damage = 15;
    private float detectionRadius = 150f;

    private float  orbitAngle;
    private float  orbitRadius;
    private Player owner;

    private static Texture sharedTexture;
    private static int instanceCount = 0;
    private TextureRegion region;

    private static final float RENDER_W = 16f;
    private static final float RENDER_H = 16f;

    public Drone(Player owner, float orbitAngle, float orbitRadius) {
        super(owner.getX(), owner.getY(), (int) RENDER_W, (int) RENDER_H, 0f, Color.CYAN);
        this.owner = owner;
        this.orbitAngle = orbitAngle;
        this.orbitRadius = orbitRadius;

        if (sharedTexture == null) {
            sharedTexture = new Texture("enemies/drone_dog.png");
            sharedTexture.setFilter(
                Texture.TextureFilter.Nearest,
                Texture.TextureFilter.Nearest
            );
        }
        instanceCount++;
        region = new TextureRegion(sharedTexture, 0, 0, sharedTexture.getWidth(), sharedTexture.getHeight());
    }

    @Override
    public void update(float deltaTime) {
        orbitAngle += 90f * deltaTime;
        if (orbitAngle >= 360f) orbitAngle -= 360f;

        float rad = (float) Math.toRadians(orbitAngle);
        x = owner.getX() + owner.getWidth()  / 2f + (float) Math.cos(rad) * orbitRadius - RENDER_W / 2f;
        y = owner.getY() + owner.getHeight() / 2f + (float) Math.sin(rad) * orbitRadius - RENDER_H / 2f;
        bounds.setPosition(x, y);

        attackTimer += deltaTime;
        if (attackTimer >= attackCooldown) {
            Enemy nearest = findNearest();
            if (nearest != null) {
                shoot(nearest);
                attackTimer = 0f;
            }
        }
    }

    private Enemy findNearest() {
        float cx = x + RENDER_W / 2f;
        float cy = y + RENDER_H / 2f;
        Enemy nearest = null;
        float minDist = detectionRadius * detectionRadius;
        for (Enemy e : GameManager.enemies) {
            if (e.isDead()) continue;
            float dx = e.getX() - cx;
            float dy = e.getY() - cy;
            float dist = dx * dx + dy * dy;
            if (dist < minDist) { minDist = dist; nearest = e; }
        }
        return nearest;
    }

    private void shoot(Enemy target) {
        float cx = x + RENDER_W / 2f;
        float cy = y + RENDER_H / 2f;
        float angle = (float) Math.toDegrees(Math.atan2(target.getY() - cy, target.getX() - cx));
        GameManager.projectiles.add(new Projectile(cx, cy, angle, damage, 1, false, Color.CYAN, false));
    }

    public void setDamage(int damage) { this.damage = damage; }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
    }

    @Override
    public void render(Batch batch) {
        if (region != null) batch.draw(region, x, y, RENDER_W, RENDER_H);
    }

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
