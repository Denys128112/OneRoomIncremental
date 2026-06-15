package ui;

import Services.DifficultyLevel;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class InstructionsWindow extends Window {
    private final Runnable onClose;

    public InstructionsWindow(
        Skin skin,
        DifficultyLevel difficulty,
        String closeButtonText,
        Runnable onClose
    ) {
        super("ЯК ГРАТИ", skin);
        this.onClose = onClose;
        setModal(true);
        setMovable(false);
        setResizable(false);
        padTop(64f);
        padLeft(26f);
        padRight(26f);
        padBottom(22f);

        Table content = new Table();
        content.top().left();
        content.pad(12f);

        addSection(
            content,
            "МЕТА",
            "Переживай хвилі, збирай кредити й купуй усі вузли дерева навичок. "
                + "Коли дерево повністю заповнене, герой знаходить заповітний ключ і забіг переходить у фінальну сцену."
        );
        addControls(content);
        addWeapons(content);
        addSection(
            content,
            "ХВИЛІ",
            "З кожною хвилею ворогів стає більше та відкриваються нові типи. "
                + "Кожна п'ята хвиля містить гарантованого мінотавра-боса. "
                + "Хвилі тривають, доки ти не викупиш усі прокачки."
        );
        addSection(
            content,
            "ПОТОЧНА СКЛАДНІСТЬ",
            difficulty.title + ". " + difficulty.description
        );
        addSection(
            content,
            "ПІДКАЗКА",
            "Рухайся постійно, змінюй зброю відповідно до ситуації та не дозволяй "
                + "ворогам оточити героя."
        );

        ScrollPane scroll = new ScrollPane(content, skin);
        scroll.setFadeScrollBars(false);
        scroll.setScrollingDisabled(true, false);
        scroll.setOverscroll(false, true);
        add(scroll).width(850f).height(590f).colspan(2).grow().row();

        TextButton close = new TextButton(closeButtonText, skin);
        close.getLabel().setAlignment(Align.center);
        close.getLabel().setWrap(true);
        close.getLabel().setFontScale(0.78f);
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                close();
            }
        });
        add(close).width(390f).height(66f).colspan(2).padTop(18f);
    }

    public void showCentered(Stage stage) {
        stage.addActor(this);
        setSize(930f, 760f);
        setPosition(
            (stage.getWidth() - getWidth()) * 0.5f,
            (stage.getHeight() - getHeight()) * 0.5f
        );
        stage.setKeyboardFocus(this);
    }

    public void close() {
        remove();
        if (onClose != null) onClose.run();
    }

    private void addControls(Table content) {
        addSectionTitle(content, "КЕРУВАННЯ");

        Table controls = new Table();
        controls.setBackground(getSkin().getDrawable("panel-strong"));
        controls.pad(14f);
        addControlRow(controls, "W  A  S  D", "Рух героя");
        addControlRow(controls, "SHIFT", "Прискорений біг після відкриття навички фантома");
        addControlRow(controls, "МИША", "Напрямок атаки");
        addControlRow(controls, "Л.К.М", "Атака або заряджання лука");
        addControlRow(controls, "1  2  3  4", "Вибір активної зброї");
        addControlRow(controls, "F", "Відкрити дерево навичок");
        addControlRow(controls, "H", "Відкрити або закрити інструкцію");
        addControlRow(controls, "ESC", "Повернутися з дерева навичок на арену");
        content.add(controls).width(790f).left().padBottom(22f).row();
    }

    private void addWeapons(Table content) {
        addSectionTitle(content, "ЗБРОЯ");

        Table weapons = new Table();
        weapons.setBackground(getSkin().getDrawable("panel"));
        weapons.pad(14f);
        addWeaponRow(weapons, "1", "МЕЧ", "Швидка атака навколо героя");
        addWeaponRow(
            weapons,
            "2",
            "ЛУК",
            "Затисни ЛКМ для заряджання, відпусти для пострілу"
        );
        addWeaponRow(weapons, "3", "ПОСОХ", "Швидкі магічні снаряди");
        addWeaponRow(weapons, "4", "СПИС", "Пробиває одразу кількох ворогів");
        content.add(weapons).width(790f).left().padBottom(22f).row();
    }

    private void addControlRow(Table table, String keyText, String actionText) {
        Table key = keyCap(keyText, 190f);
        Label action = new Label(actionText, getSkin());
        action.setWrap(true);
        action.setFontScale(0.9f);

        table.add(key).width(190f).height(48f).pad(5f).left();
        table.add(action).width(535f).left().padLeft(14f).row();
    }

    private void addWeaponRow(Table table, String slot, String name, String description) {
        Table key = keyCap(slot, 58f);

        Label nameLabel = new Label(name, getSkin(), "heading");
        nameLabel.setColor(UiSkinFactory.GOLD);
        nameLabel.setFontScale(0.66f);

        Label descriptionLabel = new Label(description, getSkin());
        descriptionLabel.setWrap(true);
        descriptionLabel.setFontScale(0.82f);

        table.add(key).width(58f).height(48f).pad(6f).left();
        table.add(nameLabel).width(145f).left().padLeft(10f);
        table.add(descriptionLabel).width(510f).left().padLeft(10f).row();
    }

    private Table keyCap(String text, float width) {
        Table key = new Table();
        key.setBackground(getSkin().getDrawable("border"));
        key.pad(2f);

        Table keyFace = new Table();
        keyFace.setBackground(getSkin().getDrawable("dark"));
        Label label = new Label(text, getSkin(), "heading");
        label.setAlignment(Align.center);
        label.setColor(UiSkinFactory.CYAN);
        label.setFontScale(0.62f);
        keyFace.add(label).width(width - 8f).height(38f);
        key.add(keyFace).grow();
        return key;
    }

    private void addSectionTitle(Table content, String titleText) {
        Label title = new Label(titleText, getSkin(), "heading");
        title.setColor(UiSkinFactory.CYAN);
        title.setFontScale(0.74f);
        content.add(title).width(790f).left().padTop(8f).padBottom(8f).row();
    }

    private void addSection(Table content, String titleText, String bodyText) {
        addSectionTitle(content, titleText);

        Label body = new Label(bodyText, getSkin());
        body.setWrap(true);
        body.setAlignment(Align.topLeft);
        body.setFontScale(0.88f);
        content.add(body).width(790f).left().padBottom(18f).row();
    }
}
