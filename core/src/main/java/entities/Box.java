package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils; // Додаємо імпорт для рандому

public class Box extends Entity {
    private final Animation<TextureRegion> breakAnimation;
    private final TextureRegion idleFrame;
    private float stateTime;

    public boolean isBreaking = false;
    public boolean isBroken = false;
    private boolean hasDroppedLoot = false; // Запобіжник, щоб не спавнити лут двічі

    private final float renderSize = 48f;
    private final float offset = (16f - renderSize) / 2f;

    public Box(float x, float y, Texture sheet) {
        super(x - 2f, y - 2f, 20, 20, 0f, Color.BROWN);

        int frameWidth = sheet.getWidth() / 7;
        int frameHeight = sheet.getHeight() / 12;

        TextureRegion[][] tmp = TextureRegion.split(sheet, frameWidth, frameHeight);
        int crop = 1;
        idleFrame = new TextureRegion(tmp[2][0], crop, crop, frameWidth - crop * 2, frameHeight - crop * 2);
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(tmp[3][3 + i], crop, crop, frameWidth - crop * 2, frameHeight - crop * 2);
        }
        breakAnimation = new Animation<>(0.20f, frames);
        stateTime = 0f;
    }

    @Override
    public void update(float deltaTime) {
        if (isBreaking && !isBroken) {
            stateTime += deltaTime;
            if (breakAnimation.isAnimationFinished(stateTime)) {
                isBroken = true;
                if (!hasDroppedLoot) {
                    trySpawnLoot();
                    hasDroppedLoot = true;
                }
            }
        }
    }

    private void trySpawnLoot() {
        if (MathUtils.randomBoolean(0.10f)) {

            float tileX = x + 2f;
            float tileY = y + 2f;

            if (MathUtils.randomBoolean(0.50f)) {
                float coinX = tileX + 5f;
                float coinY = tileY + 5f;
                Services.GameManager.coins.add(new Coin(coinX, coinY, new Texture("tiles\\coin.png")));
            } else {
                float heartX = tileX + 4f;
                float heartY = tileY + 4f;
                Services.GameManager.hearts.add(new Heart(heartX, heartY, new Texture("tiles\\heart.png")));
            }
        }
    }

    @Override
    public void render(Batch batch) {
        TextureRegion frameToDraw;
        if (!isBreaking && !isBroken) {
            frameToDraw = idleFrame;
        } else {
            frameToDraw = breakAnimation.getKeyFrame(stateTime, false);
        }
        batch.draw(frameToDraw, x + offset, y + offset, renderSize, renderSize);
    }

    public void interact() {
        if (!isBreaking && !isBroken) {
            isBreaking = true;
            Services.AudioManager.playSound(Services.AudioManager.envBoxBreak);
            stateTime = 0f;

            int gridX = (int) (x / 16f);
            int gridY = (int) (y / 16f);

            if (gridX >= 0 && gridX < Services.Map.SIZE && gridY >= 0 && gridY < Services.Map.SIZE) {
                Services.Map.map[gridX][gridY] = 0;
            }
        }
    }
}
