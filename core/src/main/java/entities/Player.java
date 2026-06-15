package entities;

import Services.GameManager;
import Services.AudioManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import Services.CollisionChecker;
import entities.animation.SpriteSheetAnimator;
import entities.animation.SpriteSheetLayout;
import skills.MageSkills;
import skills.PlayerSkills;
import stub.GameStateStub;

public class Player extends Entity {
    private float stunTimer;
    private float poisonTimer;
    private float poisonTickTimer;
    private int poisonDamage;

    public Weapon[] inventory = new Weapon[4];
    public int selectedSlot = 0;
    public PlayerSkills skills;

    private static final float DASH_DISTANCE = 80f;
    private static final float DASH_DURATION = 0.15f;
    private float dashTimer = 0f;
    private float dashDx, dashDy;
    private boolean isDashing = false;
    private float dashCooldown = 0f;

    public float getWidth() { return width; }
    public float getHeight() { return height; }

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

        inventory[0] = new Sword(this);
        inventory[1] = new Bow(this);
        inventory[2] = new Staff(this);
        inventory[3] = new Pistol(this);

    }

    public void setSkills(PlayerSkills skills) {
        this.skills = skills;
        ((Sword) inventory[0]).setSkills(skills);
        ((Bow) inventory[1]).setSkills(skills);
        ((Staff) inventory[2]).setSkills(skills);
        ((Pistol) inventory[3]).setSkills(skills);
    }

    @Override
    public void update(float deltaTime) {
        if (skills != null) skills.tick(deltaTime);
        updateStatusEffects(deltaTime);

        if (Gdx.input.isKeyJustPressed(Keys.NUM_1)) selectedSlot = 0;
        if (Gdx.input.isKeyJustPressed(Keys.NUM_2)) selectedSlot = 1;
        if (Gdx.input.isKeyJustPressed(Keys.NUM_3)) selectedSlot = 2;
        if (Gdx.input.isKeyJustPressed(Keys.NUM_4)) selectedSlot = 3;

        if (skills != null) handleAbilityKeys();

        Weapon activeWeapon = inventory[selectedSlot];
        if (activeWeapon != null) {
            activeWeapon.update(deltaTime);
        }

        if (isDashing) {
            updateDash(deltaTime);
            if (dashCooldown > 0f) dashCooldown -= deltaTime;
            return;
        }
        if (dashCooldown > 0f) dashCooldown -= deltaTime;

        float startX = x;
        float startY = y;
        if (stunTimer > 0f) {
            updateAnimation(deltaTime, 0f, 0f);
            return;
        }
        float currentSpeed = speed;
        if (skills != null) {
            currentSpeed *= skills.stats.getSpeedMultiplier();
            currentSpeed *= skills.phantom.getSpeedMultiplier();
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

    private void handleAbilityKeys() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) startDash();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
            skills.phantom.trySprint();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            skills.tank.tryActivateInvulnerability();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB) && selectedSlot == 2) {
            MageSkills.Element current = skills.mage.getElement();
            MageSkills.Element[] elements = MageSkills.Element.values();
            int next = (current.ordinal() + 1) % elements.length;
            skills.mage.setElement(elements[next]);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F) && skills.phantom.tryEcho()) {
            GameManager.projectiles.removeIf(p -> p.isEnemyProjectile);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.T) && skills.phantom.tryTeleport()) {
            x = screens.GameScreen.mouseWorldX - width / 2f;
            y = screens.GameScreen.mouseWorldY - height / 2f;
            bounds.x = x + 2f;
            bounds.y = y + 2f;
        }

        switch (selectedSlot) {
            case 0:
                if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) skills.warrior.tryVortex(x + width / 2f, y + height / 2f);
                break;

            case 1:
                if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                    skills.ranger.tryDeployTurret(x, y);
                }
                break;

            case 2:
                if (skills == null) break;
                MageSkills.Element el = skills.mage.getElement();

                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    if (skills.mage.getWallMode() == MageSkills.WallMode.NONE) {
                        skills.mage.startStoneWall();
                    } else {
                        skills.mage.confirmStoneWall(screens.GameScreen.mouseWorldX, screens.GameScreen.mouseWorldY);
                    }
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                    if (skills.mage.getWallMode() != MageSkills.WallMode.NONE) skills.mage.rotateWall();
                    else if (el == MageSkills.Element.FIRE) skills.mage.tryInfernalExplosion(x + width / 2f, y + height / 2f);
                    else if (el == MageSkills.Element.ICE) {
                        if (skills.mage.isIceStormReady()) {
                            skills.mage.tryIceStorm(x + width / 2f, y + height / 2f);
                        } else {
                            skills.mage.tryFrostyBreath(
                                x + width / 2f, y + height / 2f, screens.GameScreen.mouseWorldX, screens.GameScreen.mouseWorldY);
                        }
                    } else if (el == MageSkills.Element.WATER) skills.mage.tryWave(x + width / 2f, y + height / 2f, screens.GameScreen.mouseWorldX, screens.GameScreen.mouseWorldY);
                    else if (el == MageSkills.Element.EARTH) skills.mage.tryEarthquake(x + width / 2f, y + height / 2f);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                    if (skills.mage.getWallMode() != MageSkills.WallMode.NONE) {
                        skills.mage.cancelStoneWall();
                    } else if (el == MageSkills.Element.WATER) {
                        skills.mage.tryWhirlpool(
                            screens.GameScreen.mouseWorldX,
                            screens.GameScreen.mouseWorldY);
                    } else if (el == MageSkills.Element.WATER) {
                        skills.mage.tryTsunami(x + width / 2f, y + height / 2f);
                    }
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.Z)
                    && el == MageSkills.Element.WATER) {
                    skills.mage.tryTsunami(x + width / 2f, y + height / 2f);
                }
                break;

            case 3:
                if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) skills.ranger.tryDeployTurret(x, y);
                break;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            skills.ranger.tryDeployTurret(screens.GameScreen.mouseWorldX, screens.GameScreen.mouseWorldY);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.V) && skills.phantom.tryEcho()) {
            GameManager.projectiles.removeIf(p -> p.isEnemyProjectile);
        }
    }

    public void lookAt(float targetX, float targetY) {
        float centerX = x + width / 2;
        float centerY = y + height / 2;
        float angleRad = MathUtils.atan2(targetY - centerY, targetX - centerX);
        this.rotation = angleRad * MathUtils.radiansToDegrees;
    }

    public void attack() {
        Weapon activeWeapon = inventory[selectedSlot];

        if (activeWeapon != null && activeWeapon.canAttack()) {
            playAttackAnimation();
            activeWeapon.attack();
        }
    }

    public void takeDamage(int damage, Enemy attacker) {
        if (skills != null && skills.tank.isInvulnerable()) return;
        if (skills != null && skills.warrior.consumeParry()) {
            if (attacker != null) skills.warrior.applyParryKnockback(attacker, x + width/2f, y + height/2f);
            return;
        }
        if (skills != null) {
            damage = skills.events.modifyIncomingDamage(damage);
            if (damage <= 0) return;
            skills.events.onDamageTaken();
        }
        if (skills != null && GameStateStub.getHealthQuartersStatic() <= GameStateStub.getMaxHealthQuartersStatic() / 2) {
            skills.events.onLowHealth();
        }
        AudioManager.playSound(AudioManager.playerHurt);
        playHurtAnimation();
        for (int i = 0; i < damage; i++) GameStateStub.damageOneQuarter();
    }

    public void takeDamage(int damage) {
        takeDamage(damage, null);
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
            takeDamage(poisonDamage, null);
            poisonTickTimer = 1f;
        }
        if (poisonTimer <= 0f) poisonDamage = 0;
    }

    private float getDashCooldown() {
        return (skills != null && skills.phantom.isDashDamageUnlocked()) ? 10f : 15f;
    }

    private void startDash() {
        if (dashCooldown > 0f) return;
        if (!skills.phantom.isDashUnlocked()) return;

        float dx = 0, dy = 0;
        if (Gdx.input.isKeyPressed(Keys.A)) dx -= 1;
        if (Gdx.input.isKeyPressed(Keys.D)) dx += 1;
        if (Gdx.input.isKeyPressed(Keys.W)) dy += 1;
        if (Gdx.input.isKeyPressed(Keys.S)) dy -= 1;

        if (dx == 0 && dy == 0) {
            dx = MathUtils.cosDeg(rotation);
            dy = MathUtils.sinDeg(rotation);
        }

        float len = (float) Math.sqrt(dx * dx + dy * dy);
        dashDx = dx / len;
        dashDy = dy / len;

        isDashing = true;
        dashTimer = DASH_DURATION;
        dashCooldown = getDashCooldown();
    }

    private void updateDash(float deltaTime) {
        dashTimer -= deltaTime;
        float dashSpeed = DASH_DISTANCE / DASH_DURATION;

        float moveX = dashDx * dashSpeed * deltaTime;
        float moveY = dashDy * dashSpeed * deltaTime;

        x += moveX;
        bounds.x = x + 2f;
        if (CollisionChecker.isCollisionWithWall(this)) {
            x -= moveX;
            bounds.x = x + 2f;
            isDashing = false;
        }

        y += moveY;
        bounds.y = y + 2f;
        if (CollisionChecker.isCollisionWithWall(this)) {
            y -= moveY;
            bounds.y = y + 2f;
            isDashing = false;
        }

        if (skills != null && skills.phantom.isDashDamageUnlocked()) {
            for (Enemy e : GameManager.enemies) {
                if (e.isDead()) continue;
                if (bounds.overlaps(e.bounds)) {
                    skills.events.onDashThroughEnemy(e);
                }
            }
        }

        updateAnimation(deltaTime, moveX, moveY);

        if (dashTimer <= 0f) {
            isDashing = false;
        }
    }

    public boolean isInvulnerableDash() {
        return isDashing && skills != null && skills.phantom.isInvulDashUnlocked();
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        super.render(shapeRenderer);

        Weapon activeWeapon = inventory[selectedSlot];
        if (activeWeapon != null) {
            activeWeapon.render(shapeRenderer);
        }
    }

}
