package Services.screens;

import Services.Main;
import Services.stub.LargeNumberFormatter;
import Services.stub.UpgradeStub;
import Services.ui.UpgradeCard;
import Services.ui.UiSkinFactory;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

/** Between-wave shop with five branch filters and a vertical list of upgrade cards. */
public class UpgradeScreen extends BaseScreen {
    private final Table cardList = new Table();
    private final Array<UpgradeCard> visibleCards = new Array<>();
    private final Label creditsLabel;
    private UpgradeStub.Branch selectedBranch = UpgradeStub.Branch.GENERAL;

    public UpgradeScreen(Main game) {
        super(game);

        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(game.getSkin().getDrawable("dark"));
        root.pad(24f);
        stage.addActor(root);

        Table header = new Table();
        header.setBackground(game.getSkin().getDrawable("panel-strong"));
        header.pad(18f);
        header.add(new Label("АРСЕНАЛ МІЖ ХВИЛЯМИ", game.getSkin(), "heading")).left().expandX();
        header.add(new Image(game.getSkin().getDrawable("credits-icon"))).size(42f).padRight(12f);
        creditsLabel = new Label("", game.getSkin(), "gold");
        header.add(creditsLabel).right().padRight(24f);
        header.add(button("ПОВЕРНУТИСЯ НА АРЕНУ", game::showGame)).width(300f).height(66f);
        root.add(header).growX().height(110f).padBottom(18f).row();

        Table content = new Table();
        content.add(buildBranchMenu()).width(300f).growY().padRight(18f);

        cardList.top();
        cardList.pad(18f);
        ScrollPane scrollPane = new ScrollPane(cardList, game.getSkin());
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setOverscroll(false, true);
        content.add(scrollPane).grow();
        root.add(content).grow();

        rebuildCards();
    }

    private Table buildBranchMenu() {
        Table branches = new Table();
        branches.setBackground(game.getSkin().getDrawable("panel"));
        branches.top().pad(18f);
        branches.add(new Label("ГІЛКА ПОКРАЩЕНЬ", game.getSkin(), "small")).padBottom(16f).row();
        for (UpgradeStub.Branch branch : UpgradeStub.Branch.values()) {
            TextButton branchButton = button(branch.title, () -> {
                selectedBranch = branch;
                rebuildCards();
            });
            branches.add(branchButton).width(260f).height(76f).padBottom(12f).row();
        }
        branches.add().growY();
        Label prestige = new Label("---" + game.getGameState().getPrestige(), game.getSkin(), "small");
        prestige.setColor(UiSkinFactory.GOLD);
        branches.add(prestige).padTop(18f);
        return branches;
    }

    private void rebuildCards() {
        cardList.clearChildren();
        visibleCards.clear();

        Table title = new Table();
        Image icon = new Image(game.getSkin().getDrawable(selectedBranch.icon));
        icon.setColor(UiSkinFactory.CYAN);
        title.add(icon).size(54f).padRight(14f);
        title.add(new Label(selectedBranch.title, game.getSkin(), "heading")).left();
        cardList.add(title).left().padBottom(16f).row();

        for (UpgradeStub upgrade : game.getGameState().getUpgrades()) {
            if (upgrade.getBranch() != selectedBranch) {
                continue;
            }
            UpgradeCard card = new UpgradeCard(
                game.getSkin(), game.getGameState(), upgrade, this::refreshPrices);
            visibleCards.add(card);
            cardList.add(card).growX().height(172f).padBottom(14f).row();
        }
        cardList.add().growY();
        refreshPrices();
    }

    private void refreshPrices() {
        creditsLabel.setText(LargeNumberFormatter.format(game.getGameState().getCredits()));
        for (UpgradeCard card : visibleCards) {
            card.refresh();
        }
    }

    private TextButton button(String text, Runnable action) {
        TextButton button = new TextButton(text, game.getSkin());
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                action.run();
            }
        });
        return button;
    }
}
