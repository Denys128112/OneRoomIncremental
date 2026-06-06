package entities;

import com.badlogic.gdx.graphics.Color;

public class Enemy extends Entity {

    public Enemy(float x, float y) {
        super(x, y, 16, 16, 0f, Color.RED);
    }

    @Override
    public void update(float deltaTime) {







        // тут ші





        bounds.setPosition(x, y);
    }
}
