package SkillTree;

import com.badlogic.gdx.graphics.Texture;

public class Ranger {
    public static Char build(float headerX, float screenH, float scale) {
        float cy = screenH - 80f * scale;
        float startY = cy - 75f * scale;
        float step = 75f * scale;

        float bowX = headerX - 55f * scale;
        float pistolX = headerX + 55f * scale;
        float sharedX = headerX;

        Skill[] skills = {
            new Skill("bow_basic", "Дерев'яний Лук",
                "Базовий лук. Чим довше тримаєш кнопку — тим більше шкоди (до +15%).",
                new Texture("skillTree/hitRange.png"),
                bowX, startY, null),

            new Skill("bow_iron", "Залізний Лук",
                "Базова шкода +5%, дальність стрільби +20%.",
                new Texture("skillTree/Sniper.png"),
                bowX, startY - step, "bow_basic"),

            new Skill("bow_enchanted", "Зачарований Лук",
                "Стріли автоматично наводяться на найближчого ворога.",
                new Texture("skillTree/Sniper.png"),
                bowX, startY - step * 2, "bow_iron"),

            new Skill("bow_explosive", "Вибуховий Лук",
                "Стріли вибухають при влучанні: шкода по площі +урон. Шкода луку +10%.",
                new Texture("skillTree/BurstingArrow.png"),
                bowX, startY - step * 3, "bow_enchanted"),

            new Skill("piercing", "Пробивний Постріл",
                "Стріла пронизує першого ворога і вражає другого з тією ж силою, після чого зникає.",
                new Texture("skillTree/PiercingShot.png"),
                bowX, startY - step * 4, "bow_explosive"),

            new Skill("cryo_arrows", "Кріо-Стріли",
                "15% шанс заморозити ворога на 2 секунди при кожному влучанні стрілою.",
                new Texture("skillTree/Freezing.png"),
                bowX, startY - step * 5, "piercing"),

            new Skill("ricochet", "Рикошет",
                "Кулі відбиваються від стін до 2 разів. Якщо після відбиття влучає у ворога — рикошет зупиняється.",
                new Texture("skillTree/Ricochet.png"),
                sharedX, startY - step, null),

            new Skill("bullet_destroy", "Знищувач Куль",
                "Твої кулі знищують ворожі снаряди при зіткненні — обидва снаряди зникають.",
                new Texture("skillTree/BulletBreaker.png"),
                sharedX, startY - step * 2, "ricochet"),

            new Skill("crossfire", "Перехресний Вогонь",
                "При кожному пострілі додатково випускає снаряд позаду гравця.",
                new Texture("skillTree/Crossfire.png"),
                sharedX, startY - step * 3, "bullet_destroy"),

            new Skill("crossfire2", "Перехресний Вогонь II",
                "Додатково випускає снаряди ліворуч і праворуч. Разом — 4 напрямки.",
                new Texture("skillTree/Crossfire.png"),
                sharedX, startY - step * 4, "crossfire"),

            new Skill("pistol1", "Пістолет МК-1",
                "Дуже мала шкода але надзвичайно висока швидкострільність.",
                new Texture("skillTree/poison.png"),
                pistolX, startY, null),

            new Skill("pistol2", "Пістолет МК-2",
                "Більше шкоди, відкидає ворогів назад. Дещо нижча швидкострільність.",
                new Texture("skillTree/poison.png"),
                pistolX, startY - step, "pistol1"),

            new Skill("pistol3", "Пістолет МК-3",
                "Випускає 3 снаряди конусом одночасно.",
                new Texture("skillTree/DoubleShot.png"),
                pistolX, startY - step * 2, "pistol2"),

            new Skill("pistol4", "Пістолет МК-4",
                "3 снаряди конусом з автонаведенням на найближчого ворога.",
                new Texture("skillTree/DoubleShot.png"),
                pistolX, startY - step * 3, "pistol3"),

            new Skill("turret", "Авто-Турель",
                "Розгортає турель біля курсору. Автонаведення на ворогів. Відкат: 30 сек. Використати: G.",
                new Texture("skillTree/showerofarrows.png"),
                pistolX, startY - step * 4, "pistol4"),
        };
        return new Char("ЛУЧНИК", "skillTree/ranger.png", headerX, cy, skills, false);
    }
}
