package entities;

import Services.GameManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import skills.PlayerSkills;

public class Staff extends Weapon {
    private PlayerSkills skills;

    public enum Element { FIRE, ICE, WATER, EARTH }
    private Element currentElement = Element.FIRE;

    private static Texture fireSheet;
    private static Texture waterSheet;
    private static Texture greenSheet;

    public Staff(Player owner) {
        super(owner, 0.25f, 20);
        if (fireSheet  == null) fireSheet  = new Texture("heroes/mageSkills/FireEffectandBullet16x16.png");
        if (waterSheet == null) waterSheet = new Texture("heroes/mageSkills/WaterEffectandBullet16x16.png");
        if (greenSheet == null) greenSheet = new Texture("heroes/mageSkills/GreenEffectandBullet16x16.png");
    }

    public void setSkills(PlayerSkills skills) {
        this.skills = skills;
    }

    public Element getCurrentElement() {
        return currentElement;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            cycleElement();
            if (skills != null) skills.mage.cancelStoneWall();
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && canAttack()) {
            owner.playAttackAnimation();
            attack();
        }
    }

    private void cycleElement() {
        switch (currentElement) {
            case FIRE: currentElement = Element.ICE; break;
            case ICE: currentElement = Element.WATER; break;
            case WATER: currentElement = Element.EARTH; break;
            case EARTH: currentElement = Element.FIRE; break;
        }
    }

    @Override
    public void attack() {
        float startX = owner.getX() + owner.getWidth() / 2f;
        float startY = owner.getY() + owner.getHeight() / 2f;
        float angle = owner.getRotation();

        switch (currentElement) {
            case FIRE:
                GameManager.projectiles.add(new Projectile(startX, startY, angle, 20, 2, false, null, false, fireSheet, 0, 3));
                break;

            case ICE:
                GameManager.projectiles.add(new Projectile(startX, startY, angle, 20, 2, false, null, false, waterSheet, 0, 3));
                break;

            case WATER:
                GameManager.projectiles.add(new Projectile(startX, startY, angle, 20, 2, false, null, false, waterSheet, 3, 3));
                break;

            case EARTH:
                GameManager.projectiles.add(new Projectile(startX, startY, angle, 20, 2, false, null, false, greenSheet, 0, 3));
                break;
        }
        cooldownTimer = attackCooldown;
    }

    public void onProjectileHit(Enemy enemy, float hitX, float hitY) {
        if (skills == null) return;
        switch (currentElement) {
            case FIRE:
                skills.mage.onStaffHit(enemy);
                skills.mage.onFireballHit(hitX, hitY);
                break;
            case ICE:
                enemy.applyStun(2f);
                break;
            case WATER:
                enemy.setX(enemy.getX() + (enemy.getX() - hitX) * 0.3f);
                enemy.setY(enemy.getY() + (enemy.getY() - hitY) * 0.3f);
                break;
            case EARTH:
                break;
        }
    }
}
