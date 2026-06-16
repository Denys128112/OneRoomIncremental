package SkillTree;

import com.badlogic.gdx.graphics.Texture;

public class Mage {
    public static Char build(float headerX, float screenH, float scale) {
        float cy = screenH - 80f * scale;
        float startY = cy - 75f * scale;
        float step = 75f * scale;

        float fireX = headerX - 90f * scale;
        float iceX = headerX - 30f * scale;
        float waterX = headerX + 30f * scale;
        float earthX = headerX + 90f * scale;

        Skill[] skills = {
            // FIRE
            new Skill("spark", "Іскра",
                "Підпалює ворогів спалахом вогняної магії, збільшуючи вогняну шкоду.",
                new Texture("skillTree/Spark.png"),
                fireX, startY, null),

            new Skill("fireball", "Вогняна Куля",
                "Випускає вогняну кулю що вибухає при влучанні, завдаючи шкоди всім у зоні вибуху.",
                new Texture("skillTree/Fireball.png"),
                fireX, startY - step, "spark"),

            new Skill("infernal_explosion", "Пекельний Вибух",
                "Викликає масштабний вогняний вибух по широкій площі, завдаючи важкої шкоди. Відкат: 40 сек. Активація : R",
                new Texture("skillTree/InfernalExplosion.png"),
                fireX, startY - step * 2, "fireball"),

            new Skill("infernal_explosion_2", "Пекельний Вибух II",
                "Посилює Пекельний Вибух — вибух знищує групи ворогів у яких менше 300 здоров'я у своєму радіусі.",
                new Texture("skillTree/InfernalExplosion.png"),
                fireX, startY - step * 3, "infernal_explosion"),

            // ICE
            new Skill("freezing", "Морозний Дотик",
                "Охолоджує сусідніх ворогів при влучанні, значно знижуючи їхню швидкість руху.",
                new Texture("skillTree/Freezing.png"),
                iceX, startY, null),

            new Skill("frosty_breath", "Крижаний Подих",
                "Випускає заморожуючий потік що паралізує ворогів на 5 секунд. Відкат: 15 сек. Активація: R",
                new Texture("skillTree/FrostyBreath.png"),
                iceX, startY - step, "freezing"),

            new Skill("ice_storm", "Крижана Буря",
                "Викликає хуртовину що заморожує всіх ворогів у заданому радіусі навколо героя. Відкат: 10 сек. Активація: Q",
                new Texture("skillTree/IceStorm.png"),
                iceX, startY - step * 2, "frosty_breath"),

            // WATER
            new Skill("wave", "Хвиля",
                "Випускає хвилю води що відштовхує ворогів на задану відстань. Відкат: 10 сек. Активація: R",
                new Texture("skillTree/Wave.png"),
                waterX, startY, null),

            new Skill("whirlpool", "Вир",
                "Створює вихор що притягує всіх сусідніх ворогів до центру та завдає постійної шкоди. Відкат: 15 сек. Активація: Q",
                new Texture("skillTree/Whirlpool.png"),
                waterX, startY - step, "wave"),

            new Skill("tsunami", "Цунамі",
                "Вивільняє масивну хвилю в усіх напрямках навколо героя, відкидаючи ворогів та завдаючи важкої шкоди. Відкат: 20 сек. Активація: Z",
                new Texture("skillTree/Tsunami.png"),
                waterX, startY - step * 2, "whirlpool"),

            // EARTH
            new Skill("stone", "Кам'яна Шкіра",
                "Зміцнює тіло героя земляною магією, збільшує максимальну кількість здоров'я.",
                new Texture("skillTree/Stone.png"),
                earthX, startY, null),

            new Skill("earthquake", "Землетрус",
                "Б'є по землі викликаючи ударну хвилю що приголомшує всіх ворогів у широкій зоні навколо героя. Відкат: 10 сек. Активація: R",
                new Texture("skillTree/Earthquake.png"),
                earthX, startY - step, "stone"),

            new Skill("stone_wall", "Кам'яна Стіна",
                "Піднімає кам'яну стіну що блокує рух ворогів та відштовхує їх назад. Відкат: 10 сек. Активація: E. Розвертання: R",
                new Texture("skillTree/StoneWall.png"),
                earthX, startY - step * 2, "earthquake"),
        };
        return new Char("МАГ", "skillTree/mage.png", headerX, cy, skills, false);
    }
}
