package SkillTree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import skills.PlayerSkills;
import skills.PlayerSkillsHolder;
import Services.AudioManager;

public class SkillTreeUpdater {

    private final float SKILL_ICON = 32f;
    private Skill hoveredSkill = null;

    private Skill previousHoveredSkill = null;

    public Skill getHoveredSkill() {
        return hoveredSkill;
    }

    public void update(Char[] classes) {
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();
        float half = (32f * Math.min(Gdx.graphics.getWidth()  / 640f, Gdx.graphics.getHeight() / 480f)) / 2f + 7f;

        hoveredSkill = null;

        for (Char characterClass : classes) {
            for (Skill skill : characterClass.skills) {
                if (mx >= skill.x - half && mx <= skill.x + half &&
                    my >= skill.y - half && my <= skill.y + half) {

                    hoveredSkill = skill;

                    if (hoveredSkill != previousHoveredSkill) {
                        AudioManager.playSound(AudioManager.uiHover);
                    }
                    previousHoveredSkill = hoveredSkill;

                    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                        tryUnlock(skill, characterClass);
                    }
                    return;
                }
            }
        }

        previousHoveredSkill = null;
    }

    private void tryUnlock(Skill skill, Char owner) {
        if (skill.unlocked || !skill.available) {
            AudioManager.playSound(AudioManager.uiError);
            return;
        }

        skill.unlocked = true;
        if (PlayerSkillsHolder.instance != null) PlayerSkillsHolder.instance.unlock(skill.id);
        AudioManager.playSound(AudioManager.uiUpgrade);

        for (Skill child : owner.skills) {
            if (skill.id.equals(child.requiresId)) child.available = true;
        }
    }
}
