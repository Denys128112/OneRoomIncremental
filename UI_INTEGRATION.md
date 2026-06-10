# Scene2D UI integration

## Java files

All UI code belongs to the shared `core` module:

- `core/src/main/java/Services/Main.java` - libGDX `Game` root and screen navigation.
- `core/src/main/java/Services/screens/MainMenuScreen.java` - start menu.
- `core/src/main/java/Services/screens/GameScreen.java` - integration sandbox for combat plus HUD.
- `core/src/main/java/Services/screens/UpgradeScreen.java` - five-branch shop.
- `core/src/main/java/Services/screens/BaseScreen.java` - shared `Stage` and
  `FitViewport(1600, 900)`.
- `core/src/main/java/Services/ui/GameHUD.java` - gameplay overlay.
- `core/src/main/java/Services/ui/HeartMeter.java` - four health quarters per heart.
- `core/src/main/java/Services/ui/FloatingText.java` - `Actions`-based reward text.
- `core/src/main/java/Services/ui/UpgradeCard.java` - purchasable shop card.
- `core/src/main/java/Services/ui/SettingsWindow.java` - modal sound/prestige dialog.
- `core/src/main/java/Services/ui/UiSkinFactory.java` - programmatic Scene2D `Skin`.
- `core/src/main/java/Services/stub/GameStateStub.java` - temporary UI data source.
- `core/src/main/java/Services/stub/UpgradeStub.java` - temporary upgrade DTO.
- `core/src/main/java/Services/stub/LargeNumberFormatter.java` - `BigDecimal` formatter.

`Services/CollisionChecker.java` is only a temporary compatibility class because the
existing `Player.java` imports that missing class. The combat team can replace it while
keeping the same method signatures.

## Combat integration

`GameScreen` currently draws a UI test room. Replace `buildArenaPlaceholder()` with the
team's map/world renderer. Add `GameHUD` after world actors or render calls so it remains
on top:

```java
GameHUD hud = new GameHUD(game.getSkin(), gameState);
stage.addActor(hud);
```

Call `hud.refresh()` when HP, XP, wave or credits change. The current screen also refreshes
it every 0.1 seconds as a safe prototype fallback.

Replace `GameStateStub` with an adapter exposing equivalent getters:

- health and maximum health in quarter-heart units;
- current XP and XP required for the next level;
- wave number and elapsed wave seconds;
- credits as `BigDecimal`;
- upgrade list and a purchase method.

## CraftPix assets

Assets copied from the provided archive live under `assets/ui/craftpix`:

- `TinyFontCraftpixPixel.otf`
- `buttons/button-up.png`
- `buttons/button-down.png`
- `logo/frame.png`
- `icons/general.png`
- `icons/warrior.png`
- `icons/ranger.png`
- `icons/mage.png`
- `icons/hybrid.png`
- `icons/credits.png`
- `icons/settings.png`
- `interface/panels.png`
- `bars/bars.png`
- `palette.png`
- `../backgrounds/main-menu.png` - generated 16:9 Sci-Fi city background for the start menu.

Exact loading paths are centralized in `UiSkinFactory`. Generated Pixmap drawables are
used for XP fills, heart quadrants, panel backgrounds and locked-state colors.

The CraftPix font contains Latin glyphs only. Ukrainian UI text uses:

- `assets/ui/fonts/Unbounded.ttf` for titles and section headings;
- `assets/ui/fonts/RussoOne-Regular.ttf` for buttons, HUD and body text.

Both fonts use the SIL Open Font License. Their license files are stored beside
the font files.

## Dependencies and launch

`gdx-freetype` was added to `core/build.gradle`, and desktop FreeType natives were added
to `lwjgl3/build.gradle`. The desktop main class now points to the repository's real
package, `Services.lwjgl3.Lwjgl3Launcher`.

```bash
bash gradlew lwjgl3:run
```
