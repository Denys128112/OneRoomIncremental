package ui;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import skills.PlayerSkills;
import skills.PlayerSkillsHolder;

import java.util.ArrayList;
import java.util.List;

public class SkillHotbarPanel {
    private static final float ICON_SIZE = 44f;
    private static final float KEY_H = 16f;
    private static final float SLOT_H = ICON_SIZE + KEY_H + 2f;
    private static final float SLOT_W  = ICON_SIZE;
    private static final float SLOT_PAD = 3f;
    private static final float PANEL_PAD = 2f;

    private static final float LEFT_X = 10f;
    private static final float LEFT_BOTTOM= 100f;

    private final Skin skin;
    private final Stage stage;

    private Table leftPanel;
    private Table weaponPanel;
    private Table tooltip;
    private Label tooltipName;
    private Label tooltipDesc;

    private final Texture texSprint  = new Texture("skillTree/sprint.png");
    private final Texture texDash = new Texture("skillTree/Dash.png");
    private final Texture texTeleport = new Texture("skillTree/Teleport.png");
    private final Texture texEcho = new Texture("skillTree/shield.png");
    private final Texture texInvuln = new Texture("skillTree/shield.png");
    private final Texture texTurret = new Texture("skillTree/showerofarrows.png");
    private final Texture texVortex = new Texture("skillTree/vortex.png");
    private final Texture texInfernal = new Texture("skillTree/InfernalExplosion.png");
    private final Texture texFrosty = new Texture("skillTree/FrostyBreath.png");
    private final Texture texIceStorm = new Texture("skillTree/IceStorm.png");
    private final Texture texWave = new Texture("skillTree/Wave.png");
    private final Texture texWhirlpool = new Texture("skillTree/Whirlpool.png");
    private final Texture texTsunami = new Texture("skillTree/Tsunami.png");
    private final Texture texEarthquake = new Texture("skillTree/Earthquake.png");
    private final Texture texStoneWall = new Texture("skillTree/StoneWall.png");
    private final Texture texFireball = new Texture("skillTree/Fireball.png");

    public SkillHotbarPanel(Skin skin, Stage stage) {
        this.skin = skin;
        this.stage = stage;
        buildTooltip();
    }

    public void refresh(int selectedSlot, skills.MageSkills.Element mageElement) {
        PlayerSkills ps = PlayerSkillsHolder.instance;

        if (leftPanel != null) leftPanel.remove();
        leftPanel = new Table();
        leftPanel.setBackground(skin.getDrawable("panel-strong"));
        leftPanel.pad(PANEL_PAD);
        leftPanel.top().left();

        List<SlotDef> always = buildAlwaysSlots(ps);
        for (SlotDef sd : always) addSlotVertical(leftPanel, sd);

        leftPanel.pack();
        leftPanel.setPosition(LEFT_X, LEFT_BOTTOM);
        stage.addActor(leftPanel);

        if (weaponPanel != null) weaponPanel.remove();
        List<SlotDef> weapon = buildWeaponSlots(ps, selectedSlot, mageElement);
        if (!weapon.isEmpty()) {
            weaponPanel = new Table();
            weaponPanel.setBackground(skin.getDrawable("panel-strong"));
            weaponPanel.pad(PANEL_PAD);
            weaponPanel.center();

            for (SlotDef sd : weapon) addSlotHorizontal(weaponPanel, sd);

            weaponPanel.pack();

            float invSlotSize = 45f;
            float invSpacing = 8f;
            float invTotal = invSlotSize * 4 + invSpacing * 3;
            float screenW = stage.getViewport().getWorldWidth();
            float invStartX = (screenW - invTotal) / 2f;
            float panelW = weaponPanel.getWidth();
            weaponPanel.setPosition(invStartX - panelW - 8f, 14f);
            stage.addActor(weaponPanel);
        }

        tooltip.toFront();
    }

    private void addSlotVertical(Table panel, SlotDef sd) {
        Table slot = buildSlotTable(sd);
        panel.add(slot).width(SLOT_W).height(SLOT_H).padBottom(SLOT_PAD).row();
    }

    private void addSlotHorizontal(Table panel, SlotDef sd) {
        Table slot = buildSlotTable(sd);
        panel.add(slot).width(SLOT_W).height(SLOT_H).padRight(SLOT_PAD);
    }

    private Table buildSlotTable(final SlotDef sd) {
        Table slot = new Table();
        slot.setBackground(skin.getDrawable(sd.ready ? "border" : "panel"));
        slot.pad(1f);

        Texture tex = sd.icon;
        tex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        Image img = new Image(new TextureRegionDrawable(new TextureRegion(tex)));
        img.setColor(sd.ready ? Color.WHITE : new Color(0.4f, 0.4f, 0.4f, 1f));

        Label keyLabel = new Label(sd.key, skin, "small");
        keyLabel.setFontScale(0.65f);
        keyLabel.setColor(sd.ready ? UiSkinFactory.GOLD : UiSkinFactory.MUTED);

        slot.add(img).size(ICON_SIZE).center().row();
        slot.add(keyLabel).center().padTop(1f);

        slot.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent e, float x, float y, int ptr, Actor from) {
                tooltipName.setText(sd.name);
                tooltipDesc.setText("Клавіша: " + sd.key);
                tooltip.pack();

                float wx = slot.localToStageCoordinates(new com.badlogic.gdx.math.Vector2(0, 0)).x;
                float wy = slot.localToStageCoordinates(new com.badlogic.gdx.math.Vector2(0, 0)).y;
                tooltip.setPosition(wx + SLOT_W + 6f, wy);
                tooltip.setVisible(true);
                tooltip.toFront();
            }
            @Override
            public void exit(InputEvent e, float x, float y, int ptr, Actor to) {
                tooltip.setVisible(false);
            }
        });

        return slot;
    }

    private List<SlotDef> buildAlwaysSlots(PlayerSkills ps) {
        List<SlotDef> list = new ArrayList<>();
        if (ps == null) return list;

        if (ps.phantom.isSprintUnlocked())
            list.add(new SlotDef(texSprint,"Прискорення","SHIFT", ps.phantom.isSprintReady()));
        if (ps.phantom.isDashUnlocked())
            list.add(new SlotDef(texDash,"Ривок","X", ps.phantom.isDashReady()));
        if (ps.phantom.isEchoUnlocked())
            list.add(new SlotDef(texEcho, "Відлуння", "V", ps.phantom.isEchoReady()));
        if (ps.tank.isInvulnReady() || ps.tank.getInvulnCooldown() > 0)
            list.add(new SlotDef(texInvuln,   "Невразливість","C", ps.tank.isInvulnReady()));
        if (ps.phantom.isTeleportReady() || ps.phantom.getTeleportCooldown() > 0)
            list.add(new SlotDef(texTeleport, "Телепорт", "T",  ps.phantom.isTeleportReady()));
        if (ps.ranger.isTurretReady() || ps.ranger.getTurretCooldown() > 0)
            list.add(new SlotDef(texTurret, "Авто-Турель", "G", ps.ranger.isTurretReady()));

        return list;
    }

    private List<SlotDef> buildWeaponSlots(PlayerSkills ps, int slot, skills.MageSkills.Element el) {
        List<SlotDef> list = new ArrayList<>();
        if (ps == null) return list;

        switch (slot) {
            case 0: // Sword
                if (ps.warrior.isVortexReady() || ps.warrior.getVortexCooldown() > 0)
                    list.add(new SlotDef(texVortex, "Вихор", "Q", ps.warrior.isVortexReady()));
                break;

            case 2: // Staff
                if (el == skills.MageSkills.Element.FIRE) {
                    if (ps.mage.isFireballUnlocked())
                        list.add(new SlotDef(texFireball,"Вогняна Куля", "авто", true));
                    if (ps.mage.isInfernalReady() || ps.mage.getInfernalCooldown() > 0)
                        list.add(new SlotDef(texInfernal, "Пекельний Вибух", "R",
                            ps.mage.isInfernalReady()));
                } else if (el == skills.MageSkills.Element.ICE) {
                    if (ps.mage.isFrostyBreathUnlocked())
                        list.add(new SlotDef(texFrosty, "Крижаний Подих", "R", ps.mage.isFrostyBreathReady()));
                    if (ps.mage.isIceStormUnlocked())
                        list.add(new SlotDef(texIceStorm, "Крижана Буря", "Q", ps.mage.isIceStormReady()));
                } else if (el == skills.MageSkills.Element.WATER) {
                    if (ps.mage.isWaveUnlocked())
                        list.add(new SlotDef(texWave, "Хвиля","R", ps.mage.isWaveReady()));
                    if (ps.mage.isWhirlpoolUnlocked())
                        list.add(new SlotDef(texWhirlpool,"Вир", "Q", ps.mage.isWhirlpoolReady()));
                    if (ps.mage.isTsunamiUnlocked())
                        list.add(new SlotDef(texTsunami,"Цунамі", "Z", ps.mage.isTsunamiReady()));
                } else if (el == skills.MageSkills.Element.EARTH) {
                    if (ps.mage.isEarthquakeUnlocked())
                        list.add(new SlotDef(texEarthquake,"Землетрус","R", ps.mage.isEarthquakeReady()));
                    if (ps.mage.isStoneWallUnlocked())
                        list.add(new SlotDef(texStoneWall,"Кам'яна Стіна","E", ps.mage.isStoneWallReady()));
                }
                break;

            default: break;
        }
        return list;
    }

    private void buildTooltip() {
        tooltip = new Table();
        tooltip.setBackground(skin.getDrawable("panel-strong"));
        tooltip.pad(8f);
        tooltip.setVisible(false);

        tooltipName = new Label("", skin, "small");
        tooltipName.setColor(UiSkinFactory.CYAN);
        tooltipName.setFontScale(0.75f);

        tooltipDesc = new Label("", skin, "small");
        tooltipDesc.setFontScale(0.65f);

        tooltip.add(tooltipName).left().row();
        tooltip.add(tooltipDesc).left().padTop(3f);
        stage.addActor(tooltip);
    }

    public void dispose() {
        texSprint.dispose();  texDash.dispose(); texTeleport.dispose();
        texEcho.dispose(); texInvuln.dispose(); texTurret.dispose();
        texVortex.dispose(); texInfernal.dispose(); texFrosty.dispose();
        texIceStorm.dispose(); texWave.dispose(); texWhirlpool.dispose();
        texTsunami.dispose(); texEarthquake.dispose(); texStoneWall.dispose();
        texFireball.dispose();
    }

    private static class SlotDef {
        final Texture icon;
        final String name, key;
        final boolean ready;
        SlotDef(Texture icon, String name, String key, boolean ready) {
            this.icon = icon; this.name = name; this.key = key; this.ready = ready;
        }
    }
}
