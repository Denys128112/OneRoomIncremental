package entities;

import com.badlogic.gdx.graphics.Color;

public class Enemy extends Entity {

    //HP
    public float hp = 60f;
    public float maxHp = 60f;
    public boolean dead = false;
    //player
    protected Player player;

    public Enemy(float x, float y, Player player) {
        super(x, y, 16, 16, 50f, Color.RED);
        this.player = player;
    }

    @Override
    public void update(float deltaTime) {
        if (player != null)
            moveTowardsPlayer(deltaTime);
        bounds.setPosition(x, y);
    }

    private void moveTowardsPlayer(float delta) {
        float dx = (player.getX() + player.width / 2f) - (x + width / 2f);
        float dy = (player.getY() + player.height / 2f) - (y + height / 2f);
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        if (length > 0) {
            x += (dx / length) * speed * delta;
            y += (dy / length) * speed * delta;
        }
    }

    public void takeDamage(float amount) {
        hp -= amount;
        if (hp <= 0) dead = true;
    }
}
