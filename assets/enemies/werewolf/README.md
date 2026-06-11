# Top-down werewolf sprites

## Sprite sheets

- `black-werewolf-topdown.png`: basic werewolf
- `white-werewolf-topdown.png`: stronger armored variant
- `red-werewolf-topdown.png`: elite variant

All three sheets use the same layout:

- Sheet size: `576x256`
- Grid: `12 columns x 4 rows`
- Frame size: `48x64`
- Transparent background

Rows:

1. Down
2. Left
3. Right
4. Up

Columns:

1. Idle 1
2. Idle 2
3. Walk 1
4. Walk 2
5. Walk 3
6. Walk 4
7. Attack 1
8. Attack 2
9. Attack 3
10. Hurt
11. Death 1
12. Death 2

The larger `black-werewolf-source.png` file is retained as the editable
generation source. Use nearest-neighbor scaling when resizing any sheet.
