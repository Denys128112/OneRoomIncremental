package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import Services.CollisionChecker;
public class Player extends Entity {

    public Player(float x, float y) {
        super(x, y, 16, 16, 200f, Color.BLUE);
    }

    @Override
    public void update(float deltaTime) {
        float currentSpeed = speed;
        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            currentSpeed = speed * 3f;
        }
        bounds.width = 12f;
        bounds.height = 12f;
        float offsetX = 2f;
        float offsetY = 2f;
        float oldX = x;
        if (Gdx.input.isKeyPressed(Keys.A)) x -= currentSpeed * deltaTime;
        if (Gdx.input.isKeyPressed(Keys.D)) x += currentSpeed * deltaTime;
        bounds.x = x + offsetX;
        bounds.y = y + offsetY;

        if (CollisionChecker.isCollisionWithWall(this)) {
            x = oldX;
            bounds.x = x + offsetX;
        }
        float oldY = y;
        if (Gdx.input.isKeyPressed(Keys.W)) y += currentSpeed * deltaTime;
        if (Gdx.input.isKeyPressed(Keys.S)) y -= currentSpeed * deltaTime;

        bounds.y = y + offsetY;

        if (CollisionChecker.isCollisionWithWall(this)) {
            y = oldY;
            bounds.y = y + offsetY;
        }
    }

    public void lookAt(float targetX, float targetY) {
        float centerX = x + width / 2;
        float centerY = y + height / 2;
        float angleRad = MathUtils.atan2(targetY - centerY, targetX - centerX);
        this.rotation = angleRad * MathUtils.radiansToDegrees;
    }

    public void takeDamage(float amount) {}
}
