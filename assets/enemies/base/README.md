# Початкові вороги

Ця папка містить чотири готові аркуші ворогів із головного
top-down-набору:

- `enemy-1-topdown.png`
- `enemy-2-topdown.png`
- `enemy-3-topdown.png`
- `enemy-4-topdown.png`

Кожен аркуш має розмір `384x96`, кадри `32x32` та три рядки:

1. вниз;
2. убік, обличчям ліворуч;
3. вгору.

Правий напрямок створюється дзеркальним відображенням другого рядка
через `SpriteSheetLayout.threeDirectionsMirrored()`.

Оригінальні окремі PNG без змін збережені в
`assets/original/topdown-kit/enemies`.
