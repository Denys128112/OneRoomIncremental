from pathlib import Path

from collections import deque
import colorsys

from PIL import Image


ROOT = Path(__file__).resolve().parents[1]
FRAME_SOURCE = (128, 256)
FRAME_TARGET = (48, 64)
GRID = (12, 4)


SHEETS = {
    "assets/enemies/minotaur/minotaur-1-source.png":
        "assets/enemies/minotaur/minotaur-1-topdown.png",
    "assets/enemies/werewolf/black-werewolf-source.png":
        "assets/enemies/werewolf/black-werewolf-topdown.png",
    "assets/enemies/gorgon/gorgon-1-source.png":
        "assets/enemies/gorgon/gorgon-1-topdown.png",
    "assets/enemies/karasu-tengu/karasu-tengu-source.png":
        "assets/enemies/karasu-tengu/karasu-tengu-topdown.png",
    "assets/enemies/yamabushi-tengu/yamabushi-tengu-source.png":
        "assets/enemies/yamabushi-tengu/yamabushi-tengu-topdown.png",
    "assets/enemies/kitsune/kitsune-source.png":
        "assets/enemies/kitsune/kitsune-topdown.png",
}

VARIANTS = {
    "assets/enemies/minotaur/minotaur-1-topdown.png": [
        ("assets/enemies/minotaur/minotaur-2-topdown.png", 0.58, 0.85, 0.92),
        ("assets/enemies/minotaur/minotaur-3-topdown.png", 0.00, 1.10, 0.95),
    ],
    "assets/enemies/werewolf/black-werewolf-topdown.png": [
        ("assets/enemies/werewolf/white-werewolf-topdown.png", None, 0.08, 1.45),
        ("assets/enemies/werewolf/red-werewolf-topdown.png", 0.02, 0.72, 1.05),
    ],
    "assets/enemies/gorgon/gorgon-1-topdown.png": [
        ("assets/enemies/gorgon/gorgon-2-topdown.png", 0.52, 0.95, 1.08),
        ("assets/enemies/gorgon/gorgon-3-topdown.png", 0.00, 1.05, 0.92),
    ],
}


def remove_chroma(image):
    pixels = image.load()
    for y in range(image.height):
        for x in range(image.width):
            red, green, blue, _ = pixels[x, y]
            is_magenta = (
                red > 170
                and blue > 150
                and green < 135
                and abs(red - blue) < 110
            )
            if is_magenta:
                pixels[x, y] = (red, green, blue, 0)
    return image


def find_components(image):
    alpha = image.getchannel("A")
    pixels = alpha.load()
    visited = set()
    components = []

    for y in range(image.height):
        for x in range(image.width):
            if pixels[x, y] == 0 or (x, y) in visited:
                continue
            queue = deque([(x, y)])
            visited.add((x, y))
            component = []
            while queue:
                current_x, current_y = queue.popleft()
                component.append((current_x, current_y))
                for offset_y in (-1, 0, 1):
                    for offset_x in (-1, 0, 1):
                        next_x = current_x + offset_x
                        next_y = current_y + offset_y
                        point = (next_x, next_y)
                        if (
                            0 <= next_x < image.width
                            and 0 <= next_y < image.height
                            and point not in visited
                            and pixels[next_x, next_y] > 0
                        ):
                            visited.add(point)
                            queue.append(point)
            if len(component) >= 1000:
                components.append(component)
    return components


def extract_component(image, component):
    left = min(point[0] for point in component)
    top = min(point[1] for point in component)
    right = max(point[0] for point in component) + 1
    bottom = max(point[1] for point in component) + 1
    mask = Image.new("L", (right - left, bottom - top), 0)
    mask_pixels = mask.load()
    for x, y in component:
        mask_pixels[x - left, y - top] = image.getpixel((x, y))[3]

    cleaned = Image.new("RGBA", mask.size, (0, 0, 0, 0))
    cleaned.paste(image.crop((left, top, right, bottom)), mask=mask)
    return cleaned


def rebuild(source_path, target_path):
    source = remove_chroma(Image.open(source_path).convert("RGBA"))
    components_by_row = [[] for _ in range(GRID[1])]
    for component in find_components(source):
        center_x = sum(point[0] for point in component) / len(component)
        center_y = sum(point[1] for point in component) / len(component)
        row = min(GRID[1] - 1, int(center_y / FRAME_SOURCE[1]))
        components_by_row[row].append((center_x, component))

    frames = []
    for row, components in enumerate(components_by_row):
        if len(components) != 11:
            raise ValueError(
                f"{source_path.name}: expected 11 silhouettes in row {row}, "
                f"found {len(components)}"
            )
        ordered = [
            extract_component(source, component)
            for _, component in sorted(components, key=lambda item: item[0])
        ]
        # The generated source has one idle frame. Duplicate it so the compact
        # 12-column runtime layout remains compatible with every enemy class.
        frames.extend([ordered[0].copy(), *ordered])

    content_width = max(frame.width for frame in frames)
    content_height = max(frame.height for frame in frames)
    scale = min(
        (FRAME_TARGET[0] - 4) / content_width,
        (FRAME_TARGET[1] - 4) / content_height,
    )

    output = Image.new(
        "RGBA",
        (FRAME_TARGET[0] * GRID[0], FRAME_TARGET[1] * GRID[1]),
        (0, 0, 0, 0),
    )
    for index, frame in enumerate(frames):
        fixed_view = frame
        scaled_size = (
            max(1, round(fixed_view.width * scale)),
            max(1, round(fixed_view.height * scale)),
        )
        fixed_view = fixed_view.resize(scaled_size, Image.Resampling.NEAREST)
        column = index % GRID[0]
        row = index // GRID[0]
        paste_x = column * FRAME_TARGET[0] + (FRAME_TARGET[0] - scaled_size[0]) // 2
        paste_y = row * FRAME_TARGET[1] + FRAME_TARGET[1] - scaled_size[1] - 2
        output.alpha_composite(fixed_view, (paste_x, paste_y))

    output.save(target_path, optimize=False)
    print(f"rebuilt {target_path.relative_to(ROOT)} at scale {scale:.3f}")


def recolor(source_path, target_path, target_hue, saturation, brightness):
    source = Image.open(source_path).convert("RGBA")
    output = Image.new("RGBA", source.size, (0, 0, 0, 0))
    source_pixels = source.load()
    output_pixels = output.load()

    for y in range(source.height):
        for x in range(source.width):
            red, green, blue, alpha = source_pixels[x, y]
            if alpha == 0:
                continue
            hue, source_saturation, value = colorsys.rgb_to_hsv(
                red / 255,
                green / 255,
                blue / 255,
            )
            if source_saturation > 0.12 and target_hue is not None:
                hue = target_hue
            source_saturation = min(1, source_saturation * saturation)
            value = min(1, value * brightness)
            new_red, new_green, new_blue = colorsys.hsv_to_rgb(
                hue,
                source_saturation,
                value,
            )
            output_pixels[x, y] = (
                round(new_red * 255),
                round(new_green * 255),
                round(new_blue * 255),
                alpha,
            )

    output.save(target_path, optimize=False)
    print(f"recolored {target_path.relative_to(ROOT)}")


if __name__ == "__main__":
    for source_name, target_name in SHEETS.items():
        rebuild(ROOT / source_name, ROOT / target_name)
    for source_name, variants in VARIANTS.items():
        for target_name, hue, saturation, brightness in variants:
            recolor(
                ROOT / source_name,
                ROOT / target_name,
                hue,
                saturation,
                brightness,
            )
