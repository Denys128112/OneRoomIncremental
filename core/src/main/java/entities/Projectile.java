package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class Projectile extends Entity {
    private float dx;
    private float dy;

    public Projectile(float x, float y, float angle) {
        super(x, y, 8, 8, 500f, Color.YELLOW);
        this.rotation = angle;

        this.dx = MathUtils.cosDeg(angle);
        this.dy = MathUtils.sinDeg(angle);
    }

    @Override
    public void update(float deltaTime) {
        x += dx * speed * deltaTime;
        y += dy * speed * deltaTime;

        bounds.setPosition(x, y);
    }
}
