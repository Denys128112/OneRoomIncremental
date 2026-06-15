package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils; // Не забудь цей імпорт

public class Chest extends Entity {
    private final Animation<TextureRegion> openAnimation;
    private float stateTime;

    public boolean isOpening = false;
    public boolean isOpen = false;
    private boolean hasDroppedLoot = false; // Запобіжник від нескінченного луту

    private final float renderSize = 32f;
    private final float offset = -8f;

    public Chest(float x, float y, Texture sheet) {
        super(x-2f, y-2f, 32, 32, 0f, Color.BROWN);

        int frameWidth = (sheet.getWidth() / 9);
        int frameHeight = sheet.getHeight() / 4;

        TextureRegion[][] tmp = TextureRegion.split(sheet, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = tmp[i][7];
        }
        openAnimation = new Animation<>(0.15f, frames);
        stateTime = 0f;
    }

    @Override
    public void update(float deltaTime) {
        if (isOpening && !isOpen) {
            stateTime += deltaTime;
            if (openAnimation.isAnimationFinished(stateTime)) {
                isOpen = true;

                // Коли скриня повністю відкрилася - спавнимо лут
                if (!hasDroppedLoot) {
                    spawnLoot();
                    hasDroppedLoot = true;
                }
            }
        }
    }

    private void spawnLoot() {
        if (MathUtils.randomBoolean(0.6f)) {
            float coinX = x + 5f;
            float coinY = y + 6f;

            Services.GameManager.coins.add(new Coin(coinX, coinY, new Texture("tiles\\coin.png")));
        } else {
            float heartX = x + 4f;
            float heartY = y + 6f;

            Services.GameManager.hearts.add(new Heart(heartX, heartY, new Texture("tiles\\heart.png")));
        }
    }

    @Override
    public void render(Batch batch) {
        TextureRegion currentFrame = openAnimation.getKeyFrame(stateTime, false);
        batch.draw(currentFrame, x + offset, y + offset, renderSize, renderSize);
    }

    public void interact() {
        if (!isOpening && !isOpen) {
            isOpening = true;
            stateTime = 0f;
        }
    }
}
