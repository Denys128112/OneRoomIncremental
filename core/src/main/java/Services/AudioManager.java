package Services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    public static Music menuMusic;
    public static Music battleMusic;
    public static Music bossMusic;
    private static Music currentMusic;

    public static Sound uiHover;
    public static Sound uiClick;
    public static Sound uiUpgrade;
    public static Sound uiError;

    public static Sound weapSwordSwing;
    public static Sound weapBowShoot;
    public static Sound weapStaffCast;
    public static Sound weapSpearThrow;

    public static Sound playerHurt;
    public static Sound playerDeath;

    public static Sound enemyHurtMonster;
    public static Sound enemyHurtBone;
    public static Sound enemyDeath;
    public static Sound enemyShoot;
    public static Sound enemyMagic;

    public static Sound sysCoin;
    public static Sound sysExp;
    public static Sound sysLevelUp;
    public static Sound sysWaveClear;

    public static Sound envChestOpen;
    public static Sound envBoxBreak;

    public static void load() {
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/music_menu.mp3"));
        battleMusic = Gdx.audio.newMusic(Gdx.files.internal("music/music_battle.mp3"));
        bossMusic = Gdx.audio.newMusic(Gdx.files.internal("music/music_boss.mp3"));

        menuMusic.setLooping(true); battleMusic.setLooping(true); bossMusic.setLooping(true);
        menuMusic.setVolume(0.4f); battleMusic.setVolume(0.3f); bossMusic.setVolume(0.4f);

        uiHover = Gdx.audio.newSound(Gdx.files.internal("sounds/ui_hover.ogg"));
        uiClick = Gdx.audio.newSound(Gdx.files.internal("sounds/ui_click.ogg"));
        uiUpgrade = Gdx.audio.newSound(Gdx.files.internal("sounds/ui_upgrade.ogg"));
        uiError = Gdx.audio.newSound(Gdx.files.internal("sounds/ui_error.ogg"));

        weapSwordSwing = Gdx.audio.newSound(Gdx.files.internal("sounds/weap_sword_swing.ogg"));
        weapBowShoot = Gdx.audio.newSound(Gdx.files.internal("sounds/weap_bow_shoot.ogg"));
        weapStaffCast = Gdx.audio.newSound(Gdx.files.internal("sounds/weap_staff_cast.ogg"));
        weapSpearThrow = Gdx.audio.newSound(Gdx.files.internal("sounds/weap_spear_throw.ogg"));

        playerHurt = Gdx.audio.newSound(Gdx.files.internal("sounds/player_hurt.ogg"));
        playerDeath = Gdx.audio.newSound(Gdx.files.internal("sounds/player_death.ogg"));

        enemyHurtMonster = Gdx.audio.newSound(Gdx.files.internal("sounds/enemy_hurt_monster.ogg"));
        enemyHurtBone = Gdx.audio.newSound(Gdx.files.internal("sounds/enemy_hurt_bone.ogg"));
        enemyDeath = Gdx.audio.newSound(Gdx.files.internal("sounds/enemy_death.ogg"));
        enemyShoot = Gdx.audio.newSound(Gdx.files.internal("sounds/weap_bow_shoot.ogg"));
        enemyMagic = Gdx.audio.newSound(Gdx.files.internal("sounds/enemy_magic.ogg"));

        sysCoin = Gdx.audio.newSound(Gdx.files.internal("sounds/sys_coin.ogg"));
        sysExp = Gdx.audio.newSound(Gdx.files.internal("sounds/sys_exp.ogg"));
        sysLevelUp = Gdx.audio.newSound(Gdx.files.internal("sounds/sys_level_up.ogg"));
        sysWaveClear = Gdx.audio.newSound(Gdx.files.internal("sounds/sys_wave_clear.ogg"));

        envChestOpen = Gdx.audio.newSound(Gdx.files.internal("sounds/env_chest_open.ogg"));
        envBoxBreak = Gdx.audio.newSound(Gdx.files.internal("sounds/env_box_break.ogg"));
    }

    public static void playMusic(Music musicToPlay) {
        if (currentMusic == musicToPlay) return;
        if (currentMusic != null) currentMusic.stop();
        currentMusic = musicToPlay;
        if (currentMusic != null) currentMusic.play();
    }

    public static void playSound(Sound sound, float volume) {
        if (sound != null) {
            sound.play(volume);
        }
    }

    public static void playSound(Sound sound) {
        playSound(sound, 1.0f);
    }

    public static void dispose() {
        if (menuMusic != null) menuMusic.dispose();
        if (battleMusic != null) battleMusic.dispose();
        if (bossMusic != null) bossMusic.dispose();

        if (uiHover != null) uiHover.dispose();
        if (uiClick != null) uiClick.dispose();
        if (uiUpgrade != null) uiUpgrade.dispose();
        if (uiError != null) uiError.dispose();

        if (weapSwordSwing != null) weapSwordSwing.dispose();
        if (weapBowShoot != null) weapBowShoot.dispose();
        if (weapStaffCast != null) weapStaffCast.dispose();
        if (weapSpearThrow != null) weapSpearThrow.dispose();

        if (playerHurt != null) playerHurt.dispose();
        if (playerDeath != null) playerDeath.dispose();

        if (enemyHurtMonster != null) enemyHurtMonster.dispose();
        if (enemyHurtBone != null) enemyHurtBone.dispose();
        if (enemyDeath != null) enemyDeath.dispose();
        if (enemyShoot != null) enemyShoot.dispose();
        if (enemyMagic != null) enemyMagic.dispose();

        if (sysCoin != null) sysCoin.dispose();
        if (sysExp != null) sysExp.dispose();
        if (sysLevelUp != null) sysLevelUp.dispose();
        if (sysWaveClear != null) sysWaveClear.dispose();

        if (envChestOpen != null) envChestOpen.dispose();
        if (envBoxBreak != null) envBoxBreak.dispose();
    }
}
