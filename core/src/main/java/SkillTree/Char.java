package SkillTree;

import com.badlogic.gdx.graphics.Texture;

public class Char {
    public String name;
    public Texture icon;
    public float cx, cy;
    public Skill[] skills;
    public boolean stub;

    public Char(String name, String iconPath, float cx, float cy, Skill[] skills, boolean stub) {
        this.name = name;
        this.icon = new Texture(iconPath);
        this.cx = cx;
        this.cy = cy;
        this.skills = skills;
        this.stub = stub;
    }

    public void dispose() {
        if (icon != null) icon.dispose();
        for (Skill skill : skills) skill.dispose();
    }
}
