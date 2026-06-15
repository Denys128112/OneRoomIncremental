package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Programmatic Skin based on files copied from the CraftPix archive.
 *
 * Asset paths are relative to the project's assets directory.
 */
public final class UiSkinFactory {
    public static final Color CYAN = Color.valueOf("63E6FF");
    public static final Color GOLD = Color.valueOf("FFC857");
    public static final Color TEXT = Color.valueOf("ECF7FF");
    public static final Color MUTED = Color.valueOf("8CA0B8");
    public static final Color LOCKED = Color.valueOf("4A5262");
    public static final Color RED = Color.valueOf("FF4F70");

    private static final String ROOT = "ui/craftpix/";
    private static final String UKRAINIAN_CHARACTERS =
        FreeTypeFontGenerator.DEFAULT_CHARS
            + "АБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯ"
            + "абвгґдеєжзиіїйклмнопрстуфхцчшщьюя"
            + "’";

    private UiSkinFactory() {
    }

    public static Skin create() {
        Skin skin = new Skin();
        BitmapFont small = font("ui/fonts/RussoOne-Regular.ttf", 17);
        BitmapFont body = font("ui/fonts/RussoOne-Regular.ttf", 22);
        BitmapFont heading = font("ui/fonts/Unbounded.ttf", 28);
        BitmapFont title = font("ui/fonts/Unbounded.ttf", 52);
        skin.add("small-font", small);
        skin.add("body-font", body);
        skin.add("heading-font", heading);
        skin.add("title-font", title);

        addTextureDrawable(skin, "logo-frame", ROOT + "logo/frame.png");
        addTextureDrawable(skin, "main-menu-background", "ui/backgrounds/main-menu.png");
        addTextureDrawable(skin, "general-icon", ROOT + "icons/general.png");
        addTextureDrawable(skin, "warrior-icon", ROOT + "icons/warrior.png");
        addTextureDrawable(skin, "ranger-icon", ROOT + "icons/ranger.png");
        addTextureDrawable(skin, "mage-icon", ROOT + "icons/mage.png");
        addTextureDrawable(skin, "hybrid-icon", ROOT + "icons/hybrid.png");
        addTextureDrawable(skin, "credits-icon", ROOT + "icons/credits.png");
        addTextureDrawable(skin, "settings-icon", ROOT + "icons/settings.png");
        addTextureDrawable(skin, "skill-panel-map", ROOT + "gui/interface/TileMap1.png");
        addTextureDrawable(skin, "skill-panel-map-blue", ROOT + "gui/interface/TileMap5.png");
        addTextureDrawable(skin, "pixel-button-up", ROOT + "gui/buttons/Button3.png");
        addTextureDrawable(skin, "pixel-button-down", ROOT + "gui/buttons/Button4.png");
        addTextureDrawable(skin, "pixel-key-icon", ROOT + "gui/buttons/Icon_01.png");
        addTextureDrawable(skin, "pixel-coin-icon", ROOT + "gui/buttons/Icon_09.png");

        Drawable panel = solid(skin, "panel-texture", Color.valueOf("10192CDD"));
        Drawable panelStrong = solid(skin, "panel-strong-texture", Color.valueOf("192641F2"));
        Drawable dark = solid(skin, "dark-texture", Color.valueOf("060A14FA"));
        Drawable border = solid(skin, "border-texture", Color.valueOf("4C6D91"));
        Drawable xpBackground = solid(skin, "xp-background-texture", Color.valueOf("26344D"));
        Drawable xpFill = solid(skin, "xp-fill-texture", CYAN);
        Drawable scrollKnob = solid(skin, "scroll-knob-texture", CYAN);
        Drawable heartFull = solid(skin, "heart-full-texture", RED);
        Drawable heartEmpty = solid(skin, "heart-empty-texture", Color.valueOf("3A2634"));
        skin.add("panel", panel, Drawable.class);
        skin.add("panel-strong", panelStrong, Drawable.class);
        skin.add("dark", dark, Drawable.class);
        skin.add("border", border, Drawable.class);
        skin.add("heart-full", heartFull, Drawable.class);
        skin.add("heart-empty", heartEmpty, Drawable.class);

        Texture buttonUpTexture = nearest(ROOT + "buttons/button-up.png");
        Texture buttonDownTexture = nearest(ROOT + "buttons/button-down.png");
        skin.add("button-up-texture", buttonUpTexture);
        skin.add("button-down-texture", buttonDownTexture);
        Drawable buttonUp = new NinePatchDrawable(new NinePatch(buttonUpTexture, 2, 2, 2, 2));
        Drawable buttonDown = new NinePatchDrawable(new NinePatch(buttonDownTexture, 2, 2, 2, 2));

        skin.add("default", new Label.LabelStyle(body, TEXT));
        skin.add("small", new Label.LabelStyle(small, MUTED));
        skin.add("heading", new Label.LabelStyle(heading, CYAN));
        skin.add("title", new Label.LabelStyle(title, TEXT));
        skin.add("gold", new Label.LabelStyle(heading, GOLD));

        TextButton.TextButtonStyle button = new TextButton.TextButtonStyle();
        button.up = buttonUp;
        button.over = buttonDown;
        button.down = buttonDown;
        button.disabled = buttonDown;
        button.font = body;
        button.fontColor = Color.valueOf("151D2D");
        button.overFontColor = TEXT;
        button.downFontColor = CYAN;
        button.disabledFontColor = LOCKED;
        skin.add("default", button);

        ProgressBar.ProgressBarStyle progress = new ProgressBar.ProgressBarStyle();
        progress.background = xpBackground;
        progress.knobBefore = xpFill;
        progress.knob = xpFill;
        skin.add("default-horizontal", progress);

        Slider.SliderStyle slider = new Slider.SliderStyle();
        slider.background = xpBackground;
        slider.knobBefore = xpFill;
        slider.knob = border;
        skin.add("default-horizontal", slider);

        ScrollPane.ScrollPaneStyle scroll = new ScrollPane.ScrollPaneStyle();
        scroll.background = panel;
        scroll.vScroll = xpBackground;
        scroll.vScrollKnob = scrollKnob;
        skin.add("default", scroll);

        Window.WindowStyle window = new Window.WindowStyle(heading, CYAN, dark);
        skin.add("default", window);
        return skin;
    }

    private static BitmapFont font(String path, int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            Gdx.files.internal(path));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = Color.WHITE;
        parameter.minFilter = Texture.TextureFilter.Nearest;
        parameter.magFilter = Texture.TextureFilter.Nearest;
        parameter.characters = UKRAINIAN_CHARACTERS;
        BitmapFont result = generator.generateFont(parameter);
        generator.dispose();
        return result;
    }

    private static void addTextureDrawable(Skin skin, String name, String path) {
        Texture texture = nearest(path);
        skin.add(name + "-texture", texture);
        skin.add(name, new TextureRegionDrawable(texture), Drawable.class);
    }

    private static Texture nearest(String path) {
        Texture texture = new Texture(Gdx.files.internal(path));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        return texture;
    }

    private static Drawable solid(Skin skin, String textureName, Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        skin.add(textureName, texture);
        return new TextureRegionDrawable(texture);
    }
}
