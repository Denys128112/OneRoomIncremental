# Standardized top-down heroes

The three sheets are assembled from the original Craftpix character assets:

- `hero-1-topdown.png`
- `hero-2-topdown.png`
- `hero-3-topdown.png`

Each sheet is `384x96`, uses `32x32` frames and contains three rows:

1. Down
2. Side, facing left
3. Up

Columns use the common game layout:

1. Idle 1
2. Idle 2
3. Move 1
4. Move 2
5. Move 3
6. Move 4
7. Attack 1
8. Attack 2
9. Attack 3
10. Hurt
11. Death 1
12. Death 2

The source pack has only one side-facing animation. The game uses
`SpriteSheetLayout.threeDirectionsMirrored()` to draw the right-facing
direction as a mirrored copy of the left-facing row.
