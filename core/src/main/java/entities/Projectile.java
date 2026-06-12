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

    public int getDamage() {
        return damage;
    }

    @Override
    public void update(float deltaTime) {
        x += dx * speed * deltaTime;
        y += dy * speed * deltaTime;
        bounds.setPosition(x - width/2, y - height/2);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        if (type == 2) {
            shapeRenderer.circle(x, y, width / 2);
        } else {
            shapeRenderer.rect(x - width/2, y - height/2, width / 2, height / 2, width, height, 1f, 1f, rotation);
        }
    }
}
