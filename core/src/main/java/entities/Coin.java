package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Coin extends Entity {
    private final TextureRegion texture;

    public Coin(float x, float y, Texture image) {
        super(x, y, 6, 6, 0f, Color.YELLOW);
        this.texture = new TextureRegion(image);
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void render(Batch batch) {
        batch.draw(texture, x, y, width, height);
    }
}
