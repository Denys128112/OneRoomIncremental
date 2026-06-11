package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import Services.CollisionChecker;
import entities.animation.SpriteSheetAnimator;
import entities.animation.SpriteSheetLayout;
import stub.GameStateStub;

public class Player extends Entity {
    private float stunTimer;
    private float poisonTimer;
    private float poisonTickTimer;
    private int poisonDamage;

    public Player(float x, float y) {
        super(x, y, 16, 16, 200f, Color.BLUE);
        setAnimator(new SpriteSheetAnimator(
            "heroes/hero-2-topdown.png",
            32,
            32,
            32f,
            32f,
            SpriteSheetLayout.threeDirectionsMirrored()
        ));
    }

    @Override
    public void update(float deltaTime) {
        updateStatusEffects(deltaTime);
        float startX = x;
        float startY = y;
        if (stunTimer > 0f) {
            updateAnimation(deltaTime, 0f, 0f);
            return;
        }
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
        updateAnimation(deltaTime, x - startX, y - startY);
    }

    public void lookAt(float targetX, float targetY) {
        float centerX = x + width / 2;
        float centerY = y + height / 2;
        float angleRad = MathUtils.atan2(targetY - centerY, targetX - centerX);
        this.rotation = angleRad * MathUtils.radiansToDegrees;
    }

    public void attack() {
        playAttackAnimation();
    }

    public void takeDamage(int damage) {
        playHurtAnimation();
        for (int i = 0; i < damage; i++) {
            GameStateStub.damageOneQuarter();
        }
    }

    public void applyStun(float duration) {
        stunTimer = Math.max(stunTimer, duration);
    }

    public void applyPoison(float duration, int damagePerTick) {
        poisonTimer = Math.max(poisonTimer, duration);
        poisonDamage = Math.max(poisonDamage, damagePerTick);
    }

    private void updateStatusEffects(float deltaTime) {
        stunTimer = Math.max(0f, stunTimer - deltaTime);
        if (poisonTimer <= 0f) return;
        poisonTimer -= deltaTime;
        poisonTickTimer -= deltaTime;
        if (poisonTickTimer <= 0f) {
            takeDamage(poisonDamage);
            poisonTickTimer = 1f;
        }
        if (poisonTimer <= 0f) poisonDamage = 0;
    }
}
