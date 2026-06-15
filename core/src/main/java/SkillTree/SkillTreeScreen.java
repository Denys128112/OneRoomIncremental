package SkillTree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import Services.Main;
import com.badlogic.gdx.Screen;
import java.util.HashMap;
import java.util.Map;

public class SkillTreeScreen implements Screen {
    private final Main main;
    private Char[] classes;
    private final SkillTreeRenderer renderer;

    public SkillTreeScreen(Main main) {
        this.main = main;
        renderer = new SkillTreeRenderer(main.getSkin(), main.getGameState());
        classes = buildClasses(null);
        renderer.buildUI(classes, main::showGame);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(renderer.getStage());
        renderer.buildUI(classes, main::showGame);
    }

    private Char[] buildClasses(Char[] previous) {
        Map<String, Boolean> unlockedState = new HashMap<>();
        if (previous != null) {
            for (Char c : previous) {
                for (Skill s : c.skills) {
                    unlockedState.put(s.id, s.unlocked);
                }
                c.dispose();
            }
        }

        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        float scale = Math.min(screenW / 900f, screenH / 900f);
        float margin = screenW * 0.06f;
        float usable = screenW - margin * 2f;
        float step = usable / 4f;

        Char[] newClasses = new Char[]{
            Warrior.build(margin,            screenH, scale),
            Phantom.build(margin + step,     screenH, scale),
            Mage.build(margin + step * 2,    screenH, scale),
            Ranger.build(margin + step * 3,  screenH, scale),
            Tank.build(margin + step * 4,    screenH, scale),
        };
        assignCosts(newClasses);

        for (Char c : newClasses) {
            for (Skill skill : c.skills) {
                if (Boolean.TRUE.equals(unlockedState.get(skill.id))) {
                    skill.unlocked = true;
                    skill.available = true;
                    for (Skill child : c.skills) {
                        if (skill.id.equals(child.requiresId))
                            child.available = true;
                    }
                }
            }
        }
        return newClasses;
    }

    public boolean areAllSkillsUnlocked() {
        for (Char c : classes) {
            for (Skill skill : c.skills) {
                if (!skill.unlocked) return false;
            }
        }
        return true;
    }

    private void assignCosts(Char[] classes) {
        for (int classIndex = 0; classIndex < classes.length; classIndex++) {
            for (Skill skill : classes[classIndex].skills) {
                int depth = depthOf(classes[classIndex].skills, skill);
                skill.setCost(60 + classIndex * 20 + depth * 90);
            }
        }
    }

    private int depthOf(Skill[] skills, Skill skill) {
        int depth = 0;
        String required = skill.requiresId;
        while (required != null) {
            depth++;
            String next = null;
            for (Skill candidate : skills) {
                if (required.equals(candidate.id)) {
                    next = candidate.requiresId;
                    break;
                }
            }
            required = next;
        }
        return depth;
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        classes = buildClasses(classes);
        renderer.buildUI(classes, main::showGame);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.04f, 0.06f, 0.12f, 1f);
        Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Services.AudioManager.playSound(Services.AudioManager.uiClick);
            main.showGame();
            return;
        }
        renderer.render(classes, null);
    }

    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        renderer.dispose();
        for (Char c : classes) c.dispose();
    }
}
