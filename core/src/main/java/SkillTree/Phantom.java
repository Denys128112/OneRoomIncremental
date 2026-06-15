package SkillTree;

import com.badlogic.gdx.graphics.Texture;

public class Phantom {
    public static Char build(float headerX, float screenH, float scale) {
        float cy = screenH - 80f * scale;
        float startY = cy - 75f * scale;
        float step = 75f * scale;
        float leftX = headerX - 35f * scale;
        float rightX = headerX + 35f * scale;

        Skill[] skills = {
            new Skill("sprint", "Прискорення",
                "Активує сплеск швидкості +25% на 5 секунд. Відкат: 20 сек. Використати: SHIFT.",
                new Texture("skillTree/sprint.png"),
                leftX, startY, null),

            new Skill("speed_up", "Розгін I",
                "Постійно збільшує швидкість руху на 15% від початкової.",
                new Texture("skillTree/speed-up.png"),
                leftX, startY - step, "sprint"),

            new Skill("swift_step", "Розгін II",
                "Додатково збільшує постійну швидкість руху на 30% від початкової.",
                new Texture("skillTree/speed-up.png"),
                leftX, startY - step * 2, "speed_up"),

            new Skill("dash", "Ривок",
                "Миттєвий ривок на 25 пікселів у напрямку руху. Відкат: 15 сек. Використати: X.",
                new Texture("skillTree/Dash.png"),
                leftX, startY - step * 3, "swift_step"),

            new Skill("invul_dash", "Невразливий Ривок",
                "Під час ривку та 5 секунд після — повна невразливість до будь-якого урону.",
                new Texture("skillTree/Dash.png"),
                leftX, startY - step * 4, "dash"),

            new Skill("dash_damage", "Удар Ривком",
                "Ривок крізь ворога завдає 25 шкоди. Відкат зменшується до 10 сек.",
                new Texture("skillTree/DashUltra.png"),
                leftX, startY - step * 5, "invul_dash"),

            new Skill("teleport", "Телепорт",
                "Миттєве переміщення до курсору. Відкат: 60 сек. Використати: T.",
                new Texture("skillTree/Teleport.png"),
                leftX, startY - step * 6, "dash_damage"),

            new Skill("attack_speed", "Швидкість Атаки",
                "Постійно зменшує кулдаун між атаками на 25%.",
                new Texture("skillTree/attackSpeed.png"),
                rightX, startY, null),

            new Skill("counterattack", "Контратака",
                "Наступна атака після ухилення завдає 50% більше шкоди.",
                new Texture("skillTree/counterattack.png"),
                rightX, startY - step, "attack_speed"),

            new Skill("echo", "Відлуння",
                "Миттєво знищує всі ворожі снаряди на екрані. Відкат: 30 сек. Використати: V.",
                new Texture("skillTree/shield.png"),
                rightX, startY - step * 2, "counterattack"),
        };
        return new Char("ФАНТОМ", "skillTree/phantom.png", headerX, cy, skills, false);
    }
}
