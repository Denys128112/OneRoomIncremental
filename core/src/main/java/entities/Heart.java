package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Heart extends Entity {
    private final TextureRegion texture;

    public Heart(float x, float y, Texture image) {
        super(x, y, 8, 8, 0f, Color.RED);
        this.texture = new TextureRegion(image);
    }

    @Override
    public void update(float deltaTime) {
        // Залишаємо порожнім
    }

    @Override
    public void render(Batch batch) {
        batch.draw(texture, x, y, width, height);
    }
}
