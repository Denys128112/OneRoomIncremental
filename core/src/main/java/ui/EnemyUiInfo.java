package ui;

import entities.Enemy;
import entities.Goblin;
import entities.Gorgon;
import entities.Kitsune;
import entities.Minotaur;
import entities.Necromant;
import entities.Rat;
import entities.Shaman;
import entities.SkeletArcher;
import entities.SkeletMage;
import entities.SkeletSwordman;
import entities.Tengu;
import entities.Werewolf;

/** Display metadata kept outside combat classes so UI text cannot affect gameplay. */
final class EnemyUiInfo {
    final String key;
    final String name;
    final String description;
    final String texturePath;
    final int frameWidth;
    final int frameHeight;

    private EnemyUiInfo(
        String key,
        String name,
        String description,
        String texturePath,
        int frameWidth,
        int frameHeight
    ) {
        this.key = key;
        this.name = name;
        this.description = description;
        this.texturePath = texturePath;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }

    static EnemyUiInfo from(Enemy enemy) {
        if (enemy instanceof Minotaur) {
            return large(
                "minotaur", "Мінотавр",
                "Великий броньований ворог. Повільний, але має багато здоров’я.",
                "enemies/minotaur/minotaur-1-topdown.png"
            );
        }
        if (enemy instanceof Gorgon) {
            return large(
                "gorgon", "Горгона",
                "Отруює героя та може ненадовго зупинити його поглядом.",
                "enemies/gorgon/gorgon-1-topdown.png"
            );
        }
        if (enemy instanceof Kitsune) {
            return large(
                "kitsune", "Кіцуне",
                "Телепортується поблизу героя та випускає залп примарного вогню.",
                "enemies/kitsune/kitsune-topdown.png"
            );
        }
        if (enemy instanceof Tengu) {
            Tengu tengu = (Tengu) enemy;
            if (tengu.isYamabushi()) {
                return large(
                    "yamabushi-tengu", "Ямабусі-тенгу",
                    "Міцний повітряний боєць, який переслідує героя через перешкоди.",
                    "enemies/yamabushi-tengu/yamabushi-tengu-topdown.png"
                );
            }
            return large(
                "karasu-tengu", "Карасу-тенгу",
                "Дуже швидкий повітряний ворог, що ігнорує стіни під час погоні.",
                "enemies/karasu-tengu/karasu-tengu-topdown.png"
            );
        }
        if (enemy instanceof Werewolf) {
            return large(
                "werewolf", "Вовкулака",
                "Різко кидається на героя. Після удару об стіну ненадовго оглушується.",
                "enemies/werewolf/black-werewolf-topdown.png"
            );
        }
        if (enemy instanceof SkeletMage) {
            return small(
                "skeleton-mage", "Скелет-маг",
                "Телепортується ближче до героя й атакує здалеку, але дає мало досвіду.",
                "enemies/base/enemy-4-topdown.png"
            );
        }
        if (enemy instanceof SkeletArcher) {
            return small(
                "skeleton-archer", "Скелет-лучник",
                "Тримає дистанцію та випускає снаряди в напрямку героя.",
                "enemies/base/enemy-4-topdown.png"
            );
        }
        if (enemy instanceof Necromant) {
            return small(
                "necromancer", "Некромант",
                "Уникає героя та повертає полеглих ворогів до бою.",
                "enemies/base/enemy-4-topdown.png"
            );
        }
        if (enemy instanceof Shaman) {
            return small(
                "shaman", "Шаман",
                "Повільний витривалий ворог із дистанційною атакою.",
                "enemies/base/enemy-4-topdown.png"
            );
        }
        if (enemy instanceof SkeletSwordman) {
            return small(
                "skeleton-swordsman", "Скелет-воїн",
                "Повільно наближається та атакує героя в ближньому бою.",
                "enemies/base/enemy-3-topdown.png"
            );
        }
        if (enemy instanceof Goblin) {
            return small(
                "goblin", "Гоблін",
                "Звичайний ворог ближнього бою із середнім запасом здоров’я.",
                "enemies/base/enemy-2-topdown.png"
            );
        }
        if (enemy instanceof Rat) {
            return small(
                "rat", "Щур",
                "Слабкий, але швидкий ворог перших хвиль.",
                "enemies/base/enemy-1-topdown.png"
            );
        }
        return small(
            enemy.getClass().getName(), "Невідомий ворог",
            "Інформація про цього ворога ще не додана.",
            "enemies/base/enemy-1-topdown.png"
        );
    }

    private static EnemyUiInfo small(
        String key, String name, String description, String path
    ) {
        return new EnemyUiInfo(key, name, description, path, 32, 32);
    }

    private static EnemyUiInfo large(
        String key, String name, String description, String path
    ) {
        return new EnemyUiInfo(key, name, description, path, 48, 64);
    }
}
