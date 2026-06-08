package SkillTree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SkillTreeRenderer {
    private final OrthographicCamera camera;
    private final ScreenViewport viewport;

    static final Color COL_UNLOCKED = Color.WHITE;
    static final Color COL_AVAILABLE = new Color(0.60f, 0.60f, 0.65f, 1f);
    static final Color COL_LOCKED = new Color(0.20f, 0.20f, 0.25f, 1f);

    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final GlyphLayout layout = new GlyphLayout();
    private final SkillTooltip tooltip;

    public SkillTreeRenderer() {
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        tooltip = new SkillTooltip(batch, shapeRenderer, font);
        font.setColor(Color.WHITE);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private float CLASS_ICON = 40f;
    private float SKILL_ICON = 32f;
    private float scale = 1f;

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        scale = Math.min(width / 1400f, height / 1000f);
        CLASS_ICON = 40f * scale;
        SKILL_ICON = 32f * scale;
    }

    public void render(Char[] classes, Skill hoveredSkill) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        drawBackground();
        drawAllLines(classes);
        drawAllCircles(classes, hoveredSkill);
        drawAllText(classes, hoveredSkill);
        if (hoveredSkill != null) tooltip.render(hoveredSkill, scale);
    }

    private void drawBackground() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.05f, 0.05f, 0.10f, 1f);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
    }

    private void drawAllLines(Char[] classes) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.30f, 0.30f, 0.35f, 1f);

        for (Char characterClass : classes) {
            if (characterClass .skills.length == 0) continue;

            for (Skill skill : characterClass .skills) {
                if (skill.requiresId == null) {
                    shapeRenderer.rectLine(characterClass .cx, characterClass .cy - CLASS_ICON / 2f - 2f,
                        skill.x,  skill.y  + SKILL_ICON / 2f + 8f, 1.5f);
                }
            }

            for (Skill child : characterClass .skills) {
                if (child.requiresId == null) continue;
                for (Skill parent : characterClass .skills) {
                    if (parent.id.equals(child.requiresId)) {
                        shapeRenderer.rectLine(parent.x, parent.y, child.x, child.y, 1.5f);
                        break;
                    }
                }
            }
        }
        shapeRenderer.end();
    }

    private void drawAllCircles(Char[] classes, Skill hoveredSkill) {
        for (Char characterClass : classes) {
            Color classColor;
            if (characterClass.skills.length == 0) classColor = new Color(0.10f, 0.10f, 0.14f, 1f);
            else classColor = new Color(0.18f, 0.18f, 0.26f, 1f);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(classColor);

            shapeRenderer.circle(characterClass .cx, characterClass .cy, CLASS_ICON / 2f + 8f, 32);
            shapeRenderer.end();

            for (Skill skill : characterClass .skills) {
                float half = SKILL_ICON / 2f;
                boolean hov = (skill == hoveredSkill);

                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                if (skill.unlocked) shapeRenderer.setColor(0.30f, 0.65f, 1.00f, 1f);
                else if (skill.available) shapeRenderer.setColor(0.50f, 0.50f, 0.60f, 1f);
                else shapeRenderer.setColor(0.18f, 0.18f, 0.22f, 1f);
                if (hov) shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.rect(skill.x - half - 3f, skill.y - half - 3f, SKILL_ICON + 6f, SKILL_ICON + 6f);
                shapeRenderer.end();
            }
        }
    }

    private void drawAllText(Char[] classes, Skill hoveredSkill) {
        batch.begin();

        font.getData().setScale(1.4f * scale);
        font.setColor(Color.WHITE);
        drawCentered("SKILL TREE", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() - 15f * scale);

        for (Char characterClass : classes) {
            float half = CLASS_ICON / 2f;

            Color classIconColor;
            if (characterClass.skills.length == 0) classIconColor = COL_LOCKED;
            else classIconColor = COL_UNLOCKED;
            batch.setColor(classIconColor);

            batch.draw(characterClass.icon, characterClass.cx - half, characterClass.cy - half, CLASS_ICON, CLASS_ICON);
            batch.setColor(Color.WHITE);

            Color classNameColor;
            if (characterClass.skills.length == 0) classNameColor = COL_LOCKED;else classNameColor = new Color(0.85f, 0.85f, 1.00f, 1f);
            font.getData().setScale(0.70f * scale);
            font.setColor(classNameColor);

            drawCentered(characterClass.name, characterClass.cx, characterClass.cy + half + 18f * scale);

            for (Skill skill : characterClass.skills) {
                float shalf = SKILL_ICON / 2f;

                Color skillIconColor;
                if (skill.unlocked) skillIconColor = COL_UNLOCKED;
                else if (skill.available) skillIconColor = COL_AVAILABLE;
                else skillIconColor = COL_LOCKED;
                batch.setColor(skillIconColor);
                batch.draw(skill.icon, skill.x - shalf, skill.y - shalf, SKILL_ICON, SKILL_ICON);
                batch.setColor(Color.WHITE);

                Color skillTextColor;
                if (skill.unlocked) skillTextColor = Color.WHITE;
                else if (skill.available) skillTextColor = COL_AVAILABLE;
                else skillTextColor = COL_LOCKED;
                font.getData().setScale(0.60f * scale);
                font.setColor(skillTextColor);drawCentered(skill.name, skill.x, skill.y - shalf - 12f * scale);
            }
        }

        font.getData().setScale(1f);
        font.setColor(Color.WHITE);
        batch.end();
    }

    private void drawCentered(String text, float x, float y) {
        layout.setText(font, text);
        font.draw(batch, text, x - layout.width / 2f, y);
    }

    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
