package stub;

import Services.DifficultyLevel;
import Services.LevelManager;
import com.badlogic.gdx.utils.Array;
import java.math.BigDecimal;

/**
 * Temporary data source for every UI screen.
 *
 * HP is stored in quarters: 4 points equal one full heart. This stub deliberately
 * contains no combat classes, so UI work can be merged while gameplay is in progress.
 */
public class GameStateStub {
    public static final int FINAL_WAVE = 15;
    private final Array<UpgradeStub> upgrades = new Array<>();
    private final LevelManager levelManager = new LevelManager();
    private BigDecimal credits = new BigDecimal("1250");
    private static int healthQuarters = 18;
    private int maxHealthQuarters = 24;
    private static int maxHealthQuartersStatic = 24;
    private int experience = 35;
    private int experienceToNextLevel = 100;
    public static int wave = 6;
    private float waveSeconds;
    private boolean soundEnabled = true;
    private float musicVolume = 0.45f;
    private float soundVolume = 1f;
    private int prestige;

    public GameStateStub() {
        addGeneralUpgrades();
        addWarriorUpgrades();
        addRangerUpgrades();
        addMageUpgrades();
        addHybridUpgrades();
    }

    public void update(float delta) {
        waveSeconds += Math.max(0f, delta);
    }

    public void addExperience(int amount) {
        experience += Math.max(0, amount);
        while (experience >= experienceToNextLevel) {
            experience -= experienceToNextLevel;
            experienceToNextLevel = Math.round(experienceToNextLevel * 1.2f);
        }
    }

    public void addCredits(BigDecimal amount) {
        credits = credits.add(amount.max(BigDecimal.ZERO));
    }

    public boolean buy(UpgradeStub upgrade) {
        BigDecimal cost = upgrade.getCost();
        if (credits.compareTo(cost) < 0) {
            return false;
        }
        credits = credits.subtract(cost);
        upgrade.purchase();
        return true;
    }

    public boolean spendCredits(BigDecimal cost) {
        if (cost == null || cost.compareTo(BigDecimal.ZERO) <= 0) return true;
        if (credits.compareTo(cost) < 0) return false;
        credits = credits.subtract(cost);
        return true;
    }

    public static void damageOneQuarter() {
        healthQuarters = Math.max(0, healthQuarters - 1);
    }

    public void healOneQuarter() {
        healthQuarters = Math.min(maxHealthQuarters, healthQuarters + 1);
    }

    public void nextWave() {
        wave++;
        waveSeconds = 0f;
    }

    public boolean isPlayerDead() {
        return healthQuarters <= 0;
    }

    public void startNewRun(DifficultyLevel difficulty) {
        levelManager.selectDifficulty(difficulty);
        healthQuarters = maxHealthQuarters;
        experience = 0;
        wave = 1;
        waveSeconds = 0f;
    }

    public void prestigeReset() {
        prestige++;
        credits = BigDecimal.ZERO;
        healthQuarters = maxHealthQuarters;
        experience = 0;
        wave = 6;
        waveSeconds = 0f;
        for (UpgradeStub upgrade : upgrades) {
            upgrade.reset();
        }
    }

    private void addGeneralUpgrades() {
        add("sprint", UpgradeStub.Branch.GENERAL, "МАТРИЦЯ СПРИНТУ", "Збільшує швидкість руху та ефективність спринту.", "50");
        add("dash", UpgradeStub.Branch.GENERAL, "ФАЗОВИЙ РИВОК", "Ривок, кадри невразливості та ударна шкода.", "120");
        add("regen", UpgradeStub.Branch.GENERAL, "НАНО-РЕГЕНЕРАЦІЯ", "Поступово відновлює чверті сердець.", "180");
        add("magnet", UpgradeStub.Branch.GENERAL, "МАГНІТ ЗДОБИЧІ", "Збільшує радіус збору предметів і кредитів.", "90");
        add("shield", UpgradeStub.Branch.GENERAL, "РЕАКТИВНИЙ ЩИТ", "Дає коротку невразливість після отримання шкоди.", "260");
        add("learning", UpgradeStub.Branch.GENERAL, "ЧІП НАВЧАННЯ", "Збільшує весь отриманий досвід.", "320");
        add("drones", UpgradeStub.Branch.GENERAL, "ДРОНИ-ПОМІЧНИКИ", "Розгортає автономних бойових помічників.", "650");
    }

    private void addWarriorUpgrades() {
        add("sword-damage", UpgradeStub.Branch.WARRIOR, "МОНОЛЕЗО", "Збільшує шкоду меча.", "80");
        add("sword-speed", UpgradeStub.Branch.WARRIOR, "СЕРВО-УДАР", "Збільшує швидкість ближніх атак.", "140");
        add("sword-size", UpgradeStub.Branch.WARRIOR, "ПОДОВЖЕНЕ ЛЕЗО", "Збільшує розмір і дальність меча.", "220");
        add("whirlwind", UpgradeStub.Branch.WARRIOR, "ЦИКЛОННИЙ УДАР", "Відкриває кругову атаку мечем.", "520");
        add("energy-wave", UpgradeStub.Branch.WARRIOR, "ЕНЕРГЕТИЧНА ХВИЛЯ", "Удари мечем випускають хвилю на відстань.", "900");
        add("berserk", UpgradeStub.Branch.WARRIOR, "ПРОТОКОЛ БЕРСЕРКА", "Низьке здоров’я значно збільшує шкоду.", "1600");
    }

    private void addRangerUpgrades() {
        add("bow-charge", UpgradeStub.Branch.RANGER, "ПЛАЗМОВИЙ ЛУК", "Натягуй стрілу довше для більшої шкоди.", "90");
        add("explosive-arrow", UpgradeStub.Branch.RANGER, "ВИБУХОВИЙ БОЛТ", "Стріли завдають шкоди по площі.", "360");
        add("homing-arrow", UpgradeStub.Branch.RANGER, "РОЗУМНА СТРІЛА", "Снаряди самі знаходять найближчі цілі.", "720");
        add("fire-rate", UpgradeStub.Branch.RANGER, "РОЗГІН ПІСТОЛЕТА", "Збільшує швидкострільність пістолета.", "120");
        add("shotgun", UpgradeStub.Branch.RANGER, "ДРОБОВИЙ МАСИВ", "Випускає кілька снарядів конусом.", "480");
        add("ricochet", UpgradeStub.Branch.RANGER, "РИКОШЕТ ВІД СТІН", "Снаряди відбиваються від стін кімнати.", "840");
        add("mine-drone", UpgradeStub.Branch.RANGER, "ДРОН-МІНЕР", "Розставляє міни наближення на арені.", "1400");
    }

    private void addMageUpgrades() {
        add("fire", UpgradeStub.Branch.MAGE, "ПІРО-ПОЛЕ", "Вогонь залишає тривалу шкоду по площі.", "110");
        add("ice", UpgradeStub.Branch.MAGE, "КРІО-БЛОКУВАННЯ", "Уповільнює та згодом заморожує ворогів.", "180");
        add("water", UpgradeStub.Branch.MAGE, "ГРАВІТАЦІЙНИЙ ПРИПЛИВ", "Відштовхує або стягує групи ворогів.", "280");
        add("earth", UpgradeStub.Branch.MAGE, "ТЕРАФОРМ-БРОНЯ", "Дає броню та створює тимчасові стіни.", "420");
        add("orbitals", UpgradeStub.Branch.MAGE, "ОРБІТАЛЬНІ СФЕРИ", "Сфери навколо героя пошкоджують ворогів.", "760");
        add("chain-emp", UpgradeStub.Branch.MAGE, "ЛАНЦЮГОВИЙ ЕМІ", "Блискавка перестрибує між близькими цілями.", "1250");
    }

    private void addHybridUpgrades() {
        add("storm-blade", UpgradeStub.Branch.HYBRID, "ГРОЗОВЕ ЛЕЗО", "Воїн + Маг: удари мечем запускають ланцюгову блискавку.", "2400");
        add("bullet-parry", UpgradeStub.Branch.HYBRID, "РОЗУМНЕ ПАРИРУВАННЯ", "Стрілець + Воїн: відбиті кулі повертаються з автонаведенням.", "2600");
        add("steam-burst", UpgradeStub.Branch.HYBRID, "ПАРОВИЙ ВИБУХ", "Стрілець + Маг: вогонь і вода створюють відштовхувальну хмару.", "3000");
        add("drone-aegis", UpgradeStub.Branch.HYBRID, "ДРОН-ЕГІДА", "Загальна + Маг: орбітальні дрони проєктують щит.", "3600");
        add("chrono-slash", UpgradeStub.Branch.HYBRID, "ХРОНО-РОЗРІЗ", "Усі класи: ривок зупиняє час і виконує широкий удар.", "5000");
    }

    private void add(String id, UpgradeStub.Branch branch, String name, String description, String cost) {
        upgrades.add(new UpgradeStub(id, branch, name, description, cost, "1.22"));
    }

    public Array<UpgradeStub> getUpgrades() {
        return upgrades;
    }

    public BigDecimal getCredits() {
        return credits;
    }

    public int getHealthQuarters() {
        return healthQuarters;
    }

    public int getMaxHealthQuarters() {
        return maxHealthQuarters;
    }

    public int getExperience() {
        return experience;
    }

    public int getExperienceToNextLevel() {
        return experienceToNextLevel;
    }

    public int getWave() {
        return wave;
    }

    public float getWaveSeconds() {
        return waveSeconds;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public float getMusicVolume() {
        return soundEnabled ? musicVolume : 0f;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = clamp01(musicVolume);
    }

    public float getRawMusicVolume() {
        return musicVolume;
    }

    public float getSoundVolume() {
        return soundEnabled ? soundVolume : 0f;
    }

    public void setSoundVolume(float soundVolume) {
        this.soundVolume = clamp01(soundVolume);
    }

    public float getRawSoundVolume() {
        return soundVolume;
    }

    private float clamp01(float value) {
        if (value < 0f) return 0f;
        if (value > 1f) return 1f;
        return value;
    }

    public int getPrestige() {
        return prestige;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public static int getHealthQuartersStatic() {return healthQuarters;}

    public void addMaxHealthQuarters(int amount) {maxHealthQuarters += amount;}
    public static int getMaxHealthQuartersStatic() {return maxHealthQuartersStatic;}
}
