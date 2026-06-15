package SkillTree;

import com.badlogic.gdx.graphics.Texture;

public class Tank {
    public static Char build(float headerX, float screenH, float scale) {
        float cy = screenH - 80f * scale;
        float startY = cy - 75f * scale;
        float step = 75f * scale;
        float leftX = headerX - 70f * scale;
        float centerX = headerX;
        float rightX = headerX + 70f * scale;

        Skill[] skills = {
            new Skill("regeneration", "Регенерація",
            "Пасивно відновлює 1/4 серця щосекунди протягом усієї гри.",
            new Texture("skillTree/Regeneration.png"),
            leftX, startY, null),

            new Skill("vampirism", "Вампіризм",
                "Вбивство ворога відновлює 5% відсутнього HP. Чим менше HP — тим більше відновлення.",
                new Texture("skillTree/Vampirism.png"),
                leftX, startY - step, "regeneration"),

            new Skill("rylai", "Повторне Народження",
                "При смерті автоматично воскрешає з 30% HP. Відкат: 3 хв.",
                new Texture("skillTree/Rylai.png"),
                leftX, startY - step * 2, "vampirism"),

            new Skill("second_wind", "Другий Подих",
                "Коли HP падає нижче 50% — одноразово відновлює 50% від максимального запасу здоров'я.",
                new Texture("skillTree/AdditionalHP.png"),
                rightX, startY, null),

            new Skill("tough_skin", "Міцна Шкіра",
                "Збільшує максимальне HP на 15%. Поточне здоров'я також зростає на цю кількість.",
                new Texture("skillTree/maxHP.png"),
                rightX, startY - step, "second_wind"),

            new Skill("invulnerability", "Невразливість",
                "Активує щит на 30 сек, що блокує весь вхідний урон. Відкат: 60 сек. Використати: C.",
                new Texture("skillTree/shield.png"),
                rightX, startY - step * 2, "tough_skin"),

            new Skill("enemy1", "Вивчення Ворога I",
                "Пасивно: вороги завдають на 10% менше шкоди герою.",
                new Texture("skillTree/EnemyStudy.png"),
                centerX, startY, null),

            new Skill("enemy2", "Вивчення Ворога II",
                "Пасивно: всі вороги рухаються на 10% повільніше.",
                new Texture("skillTree/EnemyStudy.png"),
                centerX, startY - step, "enemy1"),

            new Skill("enemy3", "Вивчення Ворога III",
                "Пасивно: всі вороги мають на 15% менше максимального HP.",
                new Texture("skillTree/EnemyStudy.png"),
                centerX, startY - step * 2, "enemy2"),

            new Skill("enemy4", "Вивчення Ворога IV",
                "Пасивно: вороги залишають на 20% більше кредитів та досвіду.",
                new Texture("skillTree/EnemyStudy.png"),
                centerX, startY - step * 3, "enemy3"),

            new Skill("helper1", "Бойовий Дрон I",
                "Розгортає одного бойового дрона. Літає праворуч від героя, автоматично атакує ворогів.",
                new Texture("skillTree/Companion.png"),
                centerX, startY - step * 4, "enemy4"),

            new Skill("helper2", "Бойовий Дрон II",
                "Додає другого дрона ліворуч. Обидва дрони рухаються швидше.",
                new Texture("skillTree/Companion.png"),
                centerX, startY - step * 5, "helper1"),

            new Skill("helper3", "Бойовий Дрон III",
                "Додає третього дрона позаду. Всі дрони завдають на 30% більше шкоди.",
                new Texture("skillTree/CompanionUltra.png"),
                centerX, startY - step * 6, "helper2"),
        };
        return new Char("СТРАЖ", "skillTree/guardian.png", headerX, cy, skills, false);
    }
}
