package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import entities.Enemy;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Right-side wave tracker. Enemies are grouped by type to keep large waves readable.
 */
public class EnemyTrackerPanel extends Table implements Disposable {
    private static final float PANEL_WIDTH = 350f;

    private final Skin skin;
    private final List<Enemy> enemies;
    private final Label totalLabel;
    private final Table enemyList;
    private final ObjectMap<String, Texture> iconTextures = new ObjectMap<>();

    private boolean detailsOpen;

    public EnemyTrackerPanel(Skin skin, List<Enemy> enemies) {
        this.skin = skin;
        this.enemies = enemies;
        setFillParent(true);
        top().right();
        padTop(205f);
        padRight(22f);
        padBottom(105f);

        Table panel = new Table();
        panel.setBackground(skin.getDrawable("panel-strong"));
        panel.top();
        panel.pad(14f);

        Label title = new Label("ВОРОГИ ХВИЛІ", skin, "heading");
        title.setFontScale(0.67f);
        title.setAlignment(Align.center);
        panel.add(title).width(PANEL_WIDTH - 28f).padBottom(3f).row();

        totalLabel = new Label("", skin, "small");
        totalLabel.setAlignment(Align.center);
        panel.add(totalLabel).width(PANEL_WIDTH - 28f).padBottom(10f).row();

        enemyList = new Table();
        enemyList.top();
        ScrollPane scroll = new ScrollPane(enemyList, skin);
        scroll.setFadeScrollBars(false);
        scroll.setScrollingDisabled(true, false);
        scroll.setOverscroll(false, false);
        panel.add(scroll).width(PANEL_WIDTH - 28f).growY();

        add(panel).width(PANEL_WIDTH).growY();
        refresh();
    }

    public void refresh() {
        LinkedHashMap<String, EnemyGroup> groups = collectGroups();
        int aliveCount = 0;
        for (EnemyGroup group : groups.values()) aliveCount += group.count;
        totalLabel.setText("НА КАРТІ: " + aliveCount);

        enemyList.clearChildren();
        if (groups.isEmpty()) {
            Label cleared = new Label("ХВИЛЮ ЗАЧИЩЕНО", skin, "small");
            cleared.setAlignment(Align.center);
            enemyList.add(cleared).width(PANEL_WIDTH - 52f).padTop(28f);
            return;
        }

        for (EnemyGroup group : groups.values()) {
            enemyList.add(createEnemyCard(group))
                .width(PANEL_WIDTH - 48f)
                .padBottom(9f)
                .row();
        }
    }

    public boolean isDetailsOpen() {
        return detailsOpen;
    }

    private LinkedHashMap<String, EnemyGroup> collectGroups() {
        LinkedHashMap<String, EnemyGroup> groups = new LinkedHashMap<>();
        for (Enemy enemy : enemies) {
            if (enemy.isDead()) continue;
            EnemyUiInfo info = EnemyUiInfo.from(enemy);
            EnemyGroup group = groups.get(info.key);
            if (group == null) {
                group = new EnemyGroup(info, enemy);
                groups.put(info.key, group);
            }
            group.add(enemy);
        }
        return groups;
    }

    private Table createEnemyCard(final EnemyGroup group) {
        Table card = new Table();
        card.setBackground(skin.getDrawable("panel"));
        card.pad(9f);

        Image icon = new Image(iconFor(group.info));
        icon.setScaling(com.badlogic.gdx.utils.Scaling.fit);
        card.add(icon).size(52f).padRight(10f);

        Table text = new Table();
        text.left();
        Label name = new Label(group.info.name + "  ×" + group.count, skin);
        name.setFontScale(0.76f);
        name.setEllipsis(true);
        text.add(name).width(218f).left().row();

        ProgressBar health = new ProgressBar(
            0f, Math.max(1f, group.maxHp), 1f, false, skin
        );
        health.setValue(group.currentHp);
        Label healthText = new Label(
            "HP " + group.currentHp + " / " + group.maxHp, skin, "small"
        );
        healthText.setFontScale(0.72f);
        healthText.setAlignment(Align.center);
        Stack healthStack = new Stack();
        healthStack.add(health);
        healthStack.add(healthText);
        text.add(healthStack).width(218f).height(27f).padTop(6f).left();
        card.add(text).left();

        card.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showDetails(group);
            }
        });
        return card;
    }

    private void showDetails(EnemyGroup group) {
        detailsOpen = true;
        final Dialog dialog = new Dialog(group.info.name, skin) {
            @Override
            protected void result(Object object) {
                detailsOpen = false;
            }

            @Override
            public boolean remove() {
                detailsOpen = false;
                return super.remove();
            }
        };
        dialog.setModal(true);
        dialog.setMovable(false);
        dialog.padTop(70f);
        dialog.padLeft(28f);
        dialog.padRight(28f);
        dialog.padBottom(22f);

        Table content = dialog.getContentTable();
        Image icon = new Image(iconFor(group.info));
        icon.setScaling(com.badlogic.gdx.utils.Scaling.fit);
        content.add(icon).size(116f).padRight(24f).top();

        Table stats = new Table();
        stats.left();
        addDetail(stats, "ЗАЛИШИЛОСЯ", String.valueOf(group.count));
        addDetail(stats, "ЗДОРОВ’Я", group.currentHp + " / " + group.maxHp);
        addDetail(stats, "АТАКА", String.valueOf(group.sample.getAttackDamage()));
        addDetail(stats, "ШВИДКІСТЬ", String.valueOf(Math.round(group.sample.getSpeed())));
        addDetail(stats, "НАГОРОДА", group.sample.getExperienceReward()
            + " XP  •  " + group.sample.getCreditReward() + " кредитів");
        content.add(stats).width(450f).left().row();

        Label description = new Label(group.info.description, skin);
        description.setWrap(true);
        description.setAlignment(Align.topLeft);
        description.setFontScale(0.88f);
        content.add(description).width(590f).colspan(2).padTop(22f).left();

        TextButton close = new TextButton("ЗАКРИТИ", skin);
        close.getLabel().setFontScale(0.78f);
        dialog.getButtonTable().add(close).width(230f).height(62f).padTop(22f);
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        Stage stage = getStage();
        dialog.show(stage);
        dialog.setSize(730f, 510f);
        dialog.setPosition(
            (stage.getWidth() - dialog.getWidth()) * 0.5f,
            (stage.getHeight() - dialog.getHeight()) * 0.5f
        );
    }

    private void addDetail(Table table, String title, String value) {
        Label titleLabel = new Label(title, skin, "small");
        titleLabel.setFontScale(0.78f);
        Label valueLabel = new Label(value, skin);
        valueLabel.setFontScale(0.8f);
        valueLabel.setWrap(true);
        table.add(titleLabel).width(150f).left().padBottom(8f);
        table.add(valueLabel).width(290f).left().padBottom(8f).row();
    }

    private TextureRegionDrawable iconFor(EnemyUiInfo info) {
        Texture texture = iconTextures.get(info.texturePath);
        if (texture == null) {
            texture = new Texture(Gdx.files.internal(info.texturePath));
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            iconTextures.put(info.texturePath, texture);
        }
        TextureRegion[][] frames = TextureRegion.split(
            texture, info.frameWidth, info.frameHeight
        );
        return new TextureRegionDrawable(frames[0][0]);
    }

    @Override
    public void dispose() {
        for (ObjectMap.Entry<String, Texture> entry : iconTextures.entries()) {
            entry.value.dispose();
        }
        iconTextures.clear();
    }

    private static final class EnemyGroup {
        final EnemyUiInfo info;
        final Enemy sample;
        int count;
        int currentHp;
        int maxHp;

        EnemyGroup(EnemyUiInfo info, Enemy sample) {
            this.info = info;
            this.sample = sample;
        }

        void add(Enemy enemy) {
            count++;
            currentHp += enemy.getHp();
            maxHp += enemy.getMaxHp();
        }
    }
}
