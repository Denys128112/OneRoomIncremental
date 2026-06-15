package SkillTree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import Services.Main;
import com.badlogic.gdx.Screen;

import java.util.HashMap;
import java.util.Map;

public class SkillTreeScreen implements Screen{
    private final Main main;
    private Char[] classes;
    private final SkillTreeUpdater updater;
    private final SkillTreeRenderer renderer;

    public SkillTreeScreen(Main main) {
        this.main = main;
        updater = new SkillTreeUpdater();
        renderer = new SkillTreeRenderer();
        classes = buildClasses();
    }

    private Char[] buildClasses() {
        Map<String, Boolean> unlockedState = new HashMap<>();
        if (classes != null) {
            for (Char characterClass : classes) {
                for (Skill skill : characterClass.skills) {
                    unlockedState.put(skill.id, skill.unlocked);
                }
                characterClass.dispose();
            }
        }

        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        float scale = Math.min(screenW / 900f, screenH / 900f);
        float margin = screenW * 0.08f;
        float usable = screenW - margin * 2f;
        float step = usable / 4f;

        Char[] newClasses = new Char[]{
            Warrior.build(margin + step * 0, screenH, scale),
            Phantom.build(margin + step * 1, screenH, scale),
            Mage.build(margin + step * 2, screenH, scale),
            Ranger.build(margin + step * 3, screenH, scale),
            Tank.build(margin + step * 4, screenH, scale),
        };

        for (Char characterClass : newClasses) {
            for (Skill skill : characterClass.skills) {
                if (Boolean.TRUE.equals(unlockedState.get(skill.id))) {
                    skill.unlocked = true;
                    skill.available = true;
                    for (Skill child : characterClass.skills) {
                        if (skill.id.equals(child.requiresId))
                            child.available = true;
                    }
                }
            }
        }

        return newClasses;
    }

    public void resize(int width, int height) {
        renderer.resize(width, height);
        classes = buildClasses();
    }

    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Services.AudioManager.playSound(Services.AudioManager.uiClick);
            main.showGame();
        }
        updater.update(classes);
        renderer.render(classes, updater.getHoveredSkill());
    }

    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

    public boolean isOnClassSelect() { return true; }

    public void dispose() {
        renderer.dispose();
        for (Char characterClass : classes) characterClass.dispose();
    }
}
