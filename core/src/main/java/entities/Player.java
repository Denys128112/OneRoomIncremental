package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class Player extends Entity {

    public Player(float x, float y) {
        super(x, y, 32, 32, 200f, Color.BLUE);
    }

    @Override
    public void update(float deltaTime) {
        float currentSpeed = speed;

        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            currentSpeed = speed * 3f;
        }

        if (Gdx.input.isKeyPressed(Keys.W)) y += currentSpeed * deltaTime;
        if (Gdx.input.isKeyPressed(Keys.S)) y -= currentSpeed * deltaTime;
        if (Gdx.input.isKeyPressed(Keys.A)) x -= currentSpeed * deltaTime;
        if (Gdx.input.isKeyPressed(Keys.D)) x += currentSpeed * deltaTime;

        bounds.setPosition(x, y);
    }

    public void lookAt(float targetX, float targetY) {
        float centerX = x + width / 2;
        float centerY = y + height / 2;

        float angleRad = MathUtils.atan2(targetY - centerY, targetX - centerX);

        this.rotation = angleRad * MathUtils.radiansToDegrees;
    }
}
