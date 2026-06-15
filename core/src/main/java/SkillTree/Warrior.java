package SkillTree;

import com.badlogic.gdx.graphics.Texture;

public class Warrior {
    public static Char build(float headerX, float screenH, float scale) {
        float cy = screenH - 80f * scale;
        float startY = cy - 75f * scale;
        float step = 75f * scale;

        float leftX = headerX - 70f * scale;
        float centerX = headerX;
        float rightX = headerX + 70f * scale;

        Skill[] skills = {
            new Skill("attack_power", "Сила Атаки",
                "Пасивно збільшує базову шкоду меча на 15%.",
                new Texture("skillTree/attackDamage.png"),
                leftX, startY, null),

            new Skill("sword_iron", "Залізний Меч",
                "Замінює дерев'яний меч залізним. Шкода +20% від базової.",
                new Texture("skillTree/attackDamage.png"),
                leftX, startY - step, "attack_power"),

            new Skill("sword_tech", "Технологічний Меч",
                "Плазмовий клинок. Шкода +50% від базової, пробиває броню ворогів.",
                new Texture("skillTree/attackDamagelUltra.png"),
                leftX, startY - step * 2, "sword_iron"),

            new Skill("energy_wave", "Енергетична Хвиля",
                "Кожна 5-та атака мечем випускає ціановий снаряд що пронизує ворогів та завдає 50 шкоди.",
                new Texture("skillTree/attackDamagelUltra.png"),
                leftX, startY - step * 3, "sword_tech"),

            new Skill("sword_size", "Ультра Меча",
                "Збільшує швидкість атаки меча на 15% та дальність удару.",
                new Texture("skillTree/swordSize.png"),
                centerX, startY, null),

            new Skill("stun_strike", "Оглушливий Удар",
                "Кожна 3-тя атака оглушує ворога на 3 секунди.",
                new Texture("skillTree/stunAttack.png"),
                centerX, startY - step, "sword_size"),

            new Skill("parry", "Парирування",
                "Кожна 5-та атака активує парирування. Наступний удар ворога блокується і відкидає ворога.",
                new Texture("skillTree/parry.png"),
                centerX, startY - step * 2, "stun_strike"),

            new Skill("vortex", "Вихор",
                "Герой крутиться завдаючи 25 шкоди всім ворогам у радіусі 20 пікселів. Відкат: 10 сек. Використати: Q.",
                new Texture("skillTree/vortex.png"),
                centerX, startY - step * 3, "parry"),

            new Skill("atk_speed", "Швидкість Атаки",
                "Пасивно зменшує кулдаун між атаками на 10%.",
                new Texture("skillTree/attackSpeed.png"),
                rightX, startY, null),

            new Skill("crit_chance", "Критичний Удар",
                "Кожна 5-та атака завдає подвійну шкоду (крит).",
                new Texture("skillTree/attackSpeed.png"),
                rightX, startY - step, "atk_speed"),

            new Skill("knockback", "Відкидання",
                "15% шанс відкинути ворога на 15 пікселів при кожному ударі.",
                new Texture("skillTree/berserk.png"),
                rightX, startY - step * 2, "crit_chance"),

            new Skill("berserk", "Режим Берсерка",
                "Після отримання шкоди: +50% шкоди та +50% швидкості атаки на 5 секунд. Відкат: 10 сек.",
                new Texture("skillTree/berserk.png"),
                rightX, startY - step * 3, "knockback"),
        };
        return new Char("ВОЇН", "skillTree/warrior.png", headerX, cy, skills, false);
    }
}
