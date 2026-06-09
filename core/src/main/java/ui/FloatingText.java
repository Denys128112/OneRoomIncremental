package ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

/** Creates short-lived Scene2D text using Actions.moveBy and Actions.fadeOut. */
public final class FloatingText {
    private FloatingText() {
    }

    public static void show(Stage stage, Skin skin, String text, float stageX, float stageY, Color color) {
        Label label = new Label(text, skin, "heading");
        label.setColor(color);
        label.setAlignment(Align.center);
        label.pack();
        label.setPosition(stageX - label.getWidth() / 2f, stageY);
        label.addAction(Actions.sequence(
            Actions.parallel(
                Actions.moveBy(0f, 110f, 0.85f, Interpolation.fade),
                Actions.fadeOut(0.85f)
            ),
            Actions.removeActor()
        ));
        stage.addActor(label);
    }
}
