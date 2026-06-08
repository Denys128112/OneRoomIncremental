package SkillTree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SkillTooltip {
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;

    public SkillTooltip(SpriteBatch batch, ShapeRenderer shapeRenderer, BitmapFont font) {
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        this.font = font;
    }

    public void render(Skill skill, float scale) {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        float tooltipWidth = 240f * scale;
        float tooltipHeight = 90f  * scale;
        float tooltipX = mouseX + 14f;
        float tooltipY = mouseY + 10f;

        if (tooltipX + tooltipWidth > Gdx.graphics.getWidth()  - 5f) tooltipX = mouseX - tooltipWidth - 14f;
        if (tooltipY + tooltipHeight > Gdx.graphics.getHeight() - 5f) tooltipY = mouseY - tooltipHeight - 10f;
        if (tooltipY < 5f) tooltipY = 5f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.08f, 0.08f, 0.16f, 0.97f);
        shapeRenderer.rect(tooltipX, tooltipY, tooltipWidth, tooltipHeight);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.35f, 0.35f, 0.55f, 1f);
        shapeRenderer.rect(tooltipX, tooltipY, tooltipWidth, tooltipHeight);
        shapeRenderer.end();

        batch.begin();

        font.getData().setScale(0.95f * scale);
        font.setColor(Color.WHITE);
        font.draw(batch, skill.name, tooltipX + 10f, tooltipY + tooltipHeight - 8f * scale);

        font.getData().setScale(0.72f * scale);
        font.setColor(Color.LIGHT_GRAY);
        font.draw(batch, skill.description, tooltipX + 10f, tooltipY + tooltipHeight - 26f * scale, tooltipWidth - 20f, -1, true);

        String status;
        if (skill.unlocked) status = "[Unlocked]";
        else if (skill.available) status = "[Click to unlock]";
        else status = "[Locked]";

        Color statusColor;

        if (skill.unlocked) statusColor = new Color(0.35f, 0.85f, 1.00f, 1f);
        else if (skill.available) statusColor = new Color(0.85f, 0.85f, 0.25f, 1f);
        else statusColor = new Color(0.50f, 0.50f, 0.55f, 1f);
        font.setColor(statusColor);

        font.getData().setScale(0.72f * scale);
        font.draw(batch, status, tooltipX + 10f, tooltipY + 16f * scale);

        font.getData().setScale(1f);
        font.setColor(Color.WHITE);
        batch.end();
    }
}
