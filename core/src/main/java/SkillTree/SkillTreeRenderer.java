package SkillTree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import skills.PlayerSkillsHolder;
import stub.GameStateStub;
import stub.LargeNumberFormatter;
import ui.UiSkinFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SkillTreeRenderer {
    private final Stage stage;
    private final Skin skin;
    private final GameStateStub state;
    private Char[] currentClasses;
    private Runnable currentOnBack;

    private float scale = 1f;

    private static final float BASE_CARD_W = 105f;
    private static final float BASE_CARD_H = 130f;
    private static final float BASE_ICON   = 56f;

    private float cardW;
    private float cardH;
    private float iconSize;

    private Table tooltipTable;
    private Label tooltipName;
    private Label tooltipDesc;
    private Label tooltipStatus;

    public SkillTreeRenderer(Skin skin, GameStateStub state) {
        this.skin = skin;
        this.state = state;
        stage = new Stage(new ScreenViewport());
        recalcSizes();
    }

    public void buildUI(Char[] classes, Runnable onBack) {
        this.currentClasses = classes;
        this.currentOnBack = onBack;
        recalcSizes();
        stage.clear();

        Stack screen = new Stack();
        screen.setFillParent(true);
        stage.addActor(screen);

        Image background = new Image(skin.getDrawable("main-menu-background"));
        background.setScaling(com.badlogic.gdx.utils.Scaling.fill);
        screen.add(background);

        Table dimLayer = new Table();
        dimLayer.setBackground(skin.getDrawable("dark"));
        dimLayer.setColor(1f, 1f, 1f, 0.82f);
        screen.add(dimLayer);

        Table root = new Table();
        root.top().pad(s(18f));
        screen.add(root);

        Label title = new Label("ПІКСЕЛЬНЕ ДЕРЕВО ПРОКАЧОК", skin, "title");
        title.setColor(UiSkinFactory.CYAN);
        title.setAlignment(Align.center);
        title.setFontScale(0.64f * scale);
        root.add(title).colspan(classes.length).width(s(1220f)).padBottom(s(4f)).row();

        Label subtitle = new Label("Купуй вузли за кредити. Коли дерево заповнене, герой знаходить заповітний ключ.", skin);
        subtitle.setAlignment(Align.center);
        subtitle.setWrap(true);
        subtitle.setColor(UiSkinFactory.MUTED);
        subtitle.setFontScale(0.76f * scale);
        root.add(subtitle).colspan(classes.length).width(s(1120f)).padBottom(s(12f)).row();

        root.add(buildLegend()).colspan(classes.length).padBottom(s(12f)).row();

        Table classRow = new Table();
        classRow.top().padLeft(s(8f)).padRight(s(8f));
        for (Char c : classes) {
            int roots = countRoots(c.skills);
            float colW = Math.max(cardW + s(24f), roots * (cardW + s(12f)) + s(12f));
            classRow.add(buildClassColumn(c)).width(colW).padLeft(s(5f)).padRight(s(5f)).top();
        }

        ScrollPane scroll = new ScrollPane(classRow, skin);
        scroll.setFadeScrollBars(false);
        scroll.setScrollingDisabled(false, false);
        scroll.setOverscroll(false, true);
        root.add(scroll).grow().colspan(classes.length).row();

        TextButton back = new TextButton("ПОВЕРНУТИСЯ НА АРЕНУ (ESC)", skin);
        back.getLabel().setFontScale(0.78f * scale);
        back.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) { onBack.run(); }
        });
        root.add(back).width(s(280f)).height(s(56f)).padTop(s(12f)).colspan(classes.length);

        buildTooltipActor();
    }

    public void render(Char[] classes, Skill hovered) {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int w, int h) {
        stage.getViewport().update(w, h, true);
        if (currentClasses != null && currentOnBack != null) {
            buildUI(currentClasses, currentOnBack);
        }
    }

    public Stage getStage() { return stage; }
    public void dispose()   { stage.dispose(); }


    private void recalcSizes() {
        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();
        scale    = Math.max(0.5f, Math.min(sw / 1920f, sh / 1080f));
        cardW    = BASE_CARD_W * scale;
        cardH    = BASE_CARD_H * scale;
        iconSize = BASE_ICON   * scale;
    }

    private float s(float base) { return base * scale; }


    private void buildTooltipActor() {
        tooltipTable = new Table();
        tooltipTable.setBackground(skin.getDrawable("panel-strong"));
        tooltipTable.pad(s(14f));
        tooltipTable.setVisible(false);

        float tw = s(250f);

        tooltipName = new Label("", skin, "heading");
        tooltipName.setFontScale(0.58f * scale);
        tooltipName.setColor(UiSkinFactory.CYAN);
        tooltipName.setWrap(true);

        tooltipDesc = new Label("", skin, "small");
        tooltipDesc.setFontScale(0.80f * scale);
        tooltipDesc.setWrap(true);

        tooltipStatus = new Label("", skin, "small");
        tooltipStatus.setFontScale(0.72f * scale);

        tooltipTable.add(tooltipName).width(tw).left().row();
        tooltipTable.add(tooltipDesc).width(tw).left().padTop(s(8f)).row();
        tooltipTable.add(tooltipStatus).width(tw).left().padTop(s(10f)).row();

        stage.addActor(tooltipTable);
    }

    private void showTooltip(Skill skill) {
        if (tooltipTable == null) return;

        tooltipName.setText(skill.name);
        tooltipDesc.setText(skill.description);

        if (skill.unlocked) {
            tooltipStatus.setText("Відкрито");
            tooltipStatus.setColor(UiSkinFactory.CYAN);
        } else if (skill.available) {
            tooltipStatus.setText("Ціна: " + LargeNumberFormatter.format(skill.cost));
            tooltipStatus.setColor(canAfford(skill) ? UiSkinFactory.GOLD : UiSkinFactory.RED);
        } else {
            tooltipStatus.setText("Спочатку відкрий попередній вузол");
            tooltipStatus.setColor(UiSkinFactory.LOCKED);
        }

        tooltipTable.pack();

        float stageX = Gdx.input.getX();
        float stageY = stage.getHeight() - Gdx.input.getY();
        float ttW = tooltipTable.getWidth();
        float ttH = tooltipTable.getHeight();
        float stW = stage.getWidth();
        float stH = stage.getHeight();

        float tx = stageX + s(18f);
        float ty = stageY - ttH / 2f;

        if (tx + ttW > stW - 4f) tx = stageX - ttW - s(18f);
        if (ty < 4f) ty = 4f;
        if (ty + ttH > stH - 4f) ty = stH - ttH - 4f;

        tooltipTable.setPosition(tx, ty);
        tooltipTable.setVisible(true);
        tooltipTable.toFront();
    }

    private void hideTooltip() {
        if (tooltipTable != null) tooltipTable.setVisible(false);
    }

    private int countRoots(Skill[] skills) {
        int n = 0;
        for (Skill sk : skills) if (sk.requiresId == null) n++;
        return Math.max(1, n);
    }

    private Table buildClassColumn(Char c) {
        Table col = new Table();
        col.setBackground(skin.getDrawable("skill-panel-map"));
        col.pad(s(8f));
        col.top();

        Table header = new Table();
        header.setBackground(skin.getDrawable("panel-strong"));
        header.pad(s(10f));

        Image icon = new Image(toDrawable(c.icon));
        icon.setColor(c.skills.length > 0 ? Color.WHITE : UiSkinFactory.LOCKED);
        header.add(icon).size(s(46f)).row();

        Label name = new Label(c.name, skin, "heading");
        name.setFontScale(0.54f * scale);
        name.setColor(c.skills.length > 0 ? classColor(c.name) : UiSkinFactory.LOCKED);
        name.setAlignment(Align.center);
        header.add(name).width(cardW + s(26f)).padTop(s(5f)).row();

        Label progress = new Label(unlockedCount(c.skills) + " / " + c.skills.length + " ВУЗЛІВ", skin, "small");
        progress.setAlignment(Align.center);
        progress.setColor(UiSkinFactory.MUTED);
        progress.setFontScale(0.62f * scale);
        header.add(progress).width(cardW + s(26f)).padTop(s(3f));
        col.add(header).growX().padBottom(s(10f)).row();

        List<Skill> roots = new ArrayList<>();
        for (Skill sk : c.skills) if (sk.requiresId == null) roots.add(sk);

        if (roots.size() <= 1) buildLinearBranch(col, c.skills, c);
        else buildMultiBranch(col, c, roots);

        return col;
    }

    private void buildLinearBranch(Table col, Skill[] skills, Char owner) {
        boolean first = true;
        for (Skill skill : skills) {
            if (!first) col.add(makeConnector()).height(s(16f)).growX().row();
            col.add(buildSkillCard(skill, owner)).width(cardW).height(cardH).padBottom(s(2f)).row();
            first = false;
        }
    }

    private void buildMultiBranch(Table col, Char owner, List<Skill> roots) {
        Table branchRow = new Table();
        branchRow.top();
        for (Skill root : roots) {
            Table branch = new Table();
            branch.top();
            List<Skill> chain = buildChain(owner.skills, root);
            boolean first = true;
            for (Skill skill : chain) {
                if (!first) branch.add(makeConnector()).height(s(16f)).growX().row();
                branch.add(buildSkillCard(skill, owner)).width(cardW).height(cardH).padBottom(s(2f)).row();
                first = false;
            }
            branchRow.add(branch).top().padLeft(s(4f)).padRight(s(4f));
        }
        col.add(branchRow).row();
    }

    private List<Skill> buildChain(Skill[] all, Skill root) {
        List<Skill> chain = new ArrayList<>();
        chain.add(root);
        String cur = root.id;
        boolean found = true;
        while (found) {
            found = false;
            for (Skill s : all) {if (cur.equals(s.requiresId)) { chain.add(s); cur = s.id; found = true; break; }}
        }
        return chain;
    }

    private Table makeConnector() {
        Table c = new Table();
        Table line = new Table();
        line.setBackground(skin.getDrawable("xp-fill-texture"));
        c.add(line).width(s(2f)).height(s(16f)).center();
        return c;
    }

    private Table buildSkillCard(final Skill skill, final Char owner) {
        final Table card = new Table();
        refreshCardBg(card, skill);
        card.pad(s(6f));
        card.top();

        Image skillIcon = new Image(toDrawable(skill.icon));
        tintIcon(skillIcon, skill);
        card.add(skillIcon).size(iconSize).center().row();

        Label nameLabel = new Label(skill.name, skin);
        nameLabel.setFontScale(0.60f * scale);
        nameLabel.setWrap(true);
        nameLabel.setAlignment(Align.center);
        nameLabel.setColor(skill.unlocked  ? UiSkinFactory.CYAN : skill.available ? UiSkinFactory.TEXT : UiSkinFactory.LOCKED);
        card.add(nameLabel).width(cardW - s(12f)).center().padTop(s(4f)).row();

        if (skill.unlocked) {
            Label done = new Label("✓", skin, "small");
            done.setColor(UiSkinFactory.CYAN);
            done.setFontScale(0.65f * scale);
            done.setAlignment(Align.center);
            card.add(done).center().padTop(s(2f));
        } else if (skill.available) {
            Label hint = new Label(LargeNumberFormatter.format(skill.cost), skin, "small");
            hint.setColor(canAfford(skill) ? UiSkinFactory.GOLD : UiSkinFactory.RED);
            hint.setFontScale(0.55f * scale);
            hint.setAlignment(Align.center);
            card.add(hint).center().padTop(s(2f));
        }

        card.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent ev, float x, float y, int ptr, Actor from) {
                if (skill.available && !skill.unlocked) card.setBackground(skin.getDrawable("border"));
                showTooltip(skill);
            }

            @Override
            public void exit(InputEvent ev, float x, float y, int ptr, Actor to) {
                refreshCardBg(card, skill);
                hideTooltip();
            }

            @Override
            public void clicked(InputEvent ev, float x, float y) {
                if (!skill.unlocked && skill.available) {
                    if (!state.spendCredits(skill.cost)) {
                        Services.AudioManager.playSound(Services.AudioManager.uiError);
                        showTooltip(skill);
                        return;
                    }
                    skill.unlocked = true;
                    for (Skill child : owner.skills)
                        if (skill.id.equals(child.requiresId)) child.available = true;
                    if (PlayerSkillsHolder.instance != null) PlayerSkillsHolder.instance.unlock(skill.id);
                    Services.AudioManager.playSound(Services.AudioManager.uiUpgrade);
                    buildUI(currentClasses, currentOnBack);
                } else {
                    Services.AudioManager.playSound(Services.AudioManager.uiError);
                }
            }
        });

        return card;
    }

    private Table buildLegend() {
        Table legend = new Table();
        legend.setBackground(skin.getDrawable("skill-panel-map-blue"));
        legend.pad(s(10f));
        Image coin = new Image(skin.getDrawable("pixel-coin-icon"));
        Label credits = new Label("КРЕДИТИ: " + LargeNumberFormatter.format(state.getCredits()), skin, "heading");
        credits.setFontScale(0.52f * scale);
        credits.setColor(UiSkinFactory.GOLD);
        legend.add(coin).size(s(28f)).padRight(s(8f));
        legend.add(credits).padRight(s(22f));
        addLegendItem(legend, UiSkinFactory.CYAN, "КУПЛЕНО");
        addLegendItem(legend, UiSkinFactory.GOLD, "ДОСТУПНО");
        addLegendItem(legend, UiSkinFactory.LOCKED, "ЗАБЛОКОВАНО");
        return legend;
    }

    private void addLegendItem(Table legend, Color color, String text) {
        Table marker = new Table();
        marker.setBackground(skin.getDrawable("border"));
        marker.setColor(color);
        Label label = new Label(text, skin, "small");
        label.setFontScale(0.7f * scale);
        label.setColor(UiSkinFactory.TEXT);
        legend.add(marker).width(s(18f)).height(s(18f)).padLeft(s(14f));
        legend.add(label).padLeft(s(8f)).padRight(s(12f));
    }

    private int unlockedCount(Skill[] skills) {
        int unlocked = 0;
        for (Skill skill : skills) if (skill.unlocked) unlocked++;
        return unlocked;
    }

    private Color classColor(String name) {
        if ("ВОЇН".equals(name)) return Color.valueOf("FFB34D");
        if ("ФАНТОМ".equals(name)) return Color.valueOf("B18CFF");
        if ("МАГ".equals(name)) return UiSkinFactory.CYAN;
        if ("ЛУЧНИК".equals(name)) return Color.valueOf("73F3C0");
        if ("СТРАЖ".equals(name)) return Color.valueOf("FF8A9D");
        return UiSkinFactory.CYAN;
    }

    private TextureRegionDrawable toDrawable(Texture tex) {
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return new TextureRegionDrawable(new TextureRegion(tex));
    }

    private void tintIcon(Image img, Skill skill) {
        if (skill.unlocked) img.setColor(Color.WHITE);
        else if (skill.available) img.setColor(UiSkinFactory.MUTED);
        else img.setColor(UiSkinFactory.LOCKED);
    }

    private void refreshCardBg(Table card, Skill skill) {
        card.setBackground(skin.getDrawable(skill.unlocked ? "border" : skill.available ? "skill-panel-map-blue" : "skill-panel-map"));
    }

    private boolean canAfford(Skill skill) {
        BigDecimal cost = skill.cost == null ? BigDecimal.ZERO : skill.cost;
        return state.getCredits().compareTo(cost) >= 0;
    }
}
