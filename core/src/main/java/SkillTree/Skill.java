package SkillTree;

import com.badlogic.gdx.graphics.Texture;

public class Skill {
    public String id, name, description;
    public Texture icon;
    public float x, y;
    public boolean unlocked = false;
    public boolean available;
    public String requiresId;

    public Skill(String id, String name, String description, Texture icon, float x, float y, String requiresId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.x = x;
        this.y = y;
        this.requiresId = requiresId;
        this.available = (requiresId == null);
    }

    public void dispose() {
        if (icon != null)
            icon.dispose();
    }
}
