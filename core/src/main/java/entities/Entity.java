package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public abstract class Entity {
    protected float x, y;
    protected float width, height;
    protected float speed;
    protected Color color;
    public Rectangle bounds;
    protected float rotation = 0;

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
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, width / 2, height / 2, width, height, 1f, 1f, rotation);
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

}
