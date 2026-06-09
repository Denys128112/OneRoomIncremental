package ui;

import stub.GameStateStub;
import stub.LargeNumberFormatter;
import stub.UpgradeStub;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import java.math.BigDecimal;

/** One purchasable upgrade card used inside UpgradeScreen's vertical ScrollPane. */
public class UpgradeCard extends Table {
    private final GameStateStub state;
    private final UpgradeStub upgrade;
    private final Label levelLabel;
    private final TextButton buyButton;

    public UpgradeCard(Skin skin, GameStateStub state, UpgradeStub upgrade, Runnable afterPurchase) {
        this.state = state;
        this.upgrade = upgrade;
        setBackground(skin.getDrawable("panel-strong"));
        pad(20f);

        Image icon = new Image(skin.getDrawable(upgrade.getBranch().icon));
        icon.setColor(UiSkinFactory.CYAN);
        add(icon).size(82f).padRight(20f);

        Table text = new Table();
        text.left();
        Label name = new Label(upgrade.getName(), skin, "heading");
        name.setFontScale(0.68f);
        name.setWrap(true);
        name.setAlignment(Align.left);
        text.add(name).width(530f).left().row();
        Label description = new Label(upgrade.getDescription(), skin, "small");
        description.setWrap(true);
        text.add(description).width(530f).left().padTop(7f).row();
        levelLabel = new Label("", skin);
        text.add(levelLabel).left().padTop(10f);
        add(text).growX().left();

        buyButton = new TextButton("", skin);
        buyButton.getLabel().setWrap(true);
        buyButton.getLabel().setAlignment(Align.center);
        buyButton.getLabel().setFontScale(0.78f);
        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!buyButton.isDisabled() && state.buy(upgrade)) {
                    refresh();
                    afterPurchase.run();
                }
            }
        });
        add(buyButton).width(210f).height(112f).padLeft(18f);
        refresh();
    }

    public void refresh() {
        BigDecimal cost = upgrade.getCost();
        boolean affordable = state.getCredits().compareTo(cost) >= 0;
        levelLabel.setText("РІВЕНЬ " + upgrade.getLevel());
        buyButton.setText("КУПИТИ\n" + LargeNumberFormatter.format(cost));
        buyButton.setDisabled(!affordable);
        buyButton.setColor(affordable ? Color.WHITE : UiSkinFactory.LOCKED);
    }
}
