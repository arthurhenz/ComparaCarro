# Design System

ComparaCarro's design system. Sourced from the **Brazilian Car Comparator** Stitch project (`stitch.withgoogle.com/projects/1599650830603582439`).

The system is dark-mode-only, high-contrast, with a single ignition-orange accent. The signature is **type contrast** — Inter for editorial copy, Space Grotesk for technical/numeric data — and **tonal layering** (no borders, no drop shadows, depth via surface tone shifts).

## 1. Design strategy: Kinetic Precision

| Principle | Rule |
|---|---|
| Surface depth | Layer tonal surfaces, never use 1px borders for sections |
| Accent discipline | Exactly one primary (orange) ignition CTA per screen |
| Type contrast | Inter for headlines/body, Space Grotesk for prices, RPM, tech labels |
| Asymmetry | Cards bleed images past the right edge at 45°; layouts reject grid symmetry |
| Elevation | Tonal surface shift, never `Modifier.shadow`. Modals use `0 24 48 rgba(0,0,0,0.4)` |
| Motion | Loaders are circular tertiary-gold "odometer" spinners, never linear bars |

## 2. Color palette

Dark / vibrant Material 3 scheme. All values are obsidian neutrals with an `#FF5C00` accent ramp.

### Surface ramp (low → high tone)

| Token | Hex | Usage |
|---|---|---|
| `surfaceContainerLowest` | `#000000` | Modal backdrops, deepest cockpit insets |
| `surface` (L0) | `#0E0E0E` | Window background |
| `surfaceContainerLow` | `#131313` | Section blocks |
| `surfaceContainer` (L1) | `#1A1A1A` | Cards (default) |
| `surfaceContainerHigh` (L2) | `#20201F` | Cards (raised), chips |
| `surfaceContainerHighest` | `#262626` | Filled inputs |
| `surfaceBright` | `#2C2C2C` | Glass overlays at 60% |
| `surfaceVariant` | `#262626` | Glass nav bars (used at @60% + 24px blur) |

### Content tones

| Token | Hex |
|---|---|
| `onSurface` | `#FFFFFF` |
| `onSurfaceVariant` | `#ADAAAA` |
| `outline` | `#767575` (use at 15% opacity for "ghost border") |
| `outlineVariant` | `#484847` |

### Accents

| Token | Hex | Usage |
|---|---|---|
| `primary` | `#FF9064` | Default text-on-dark accent |
| `primaryDim` | `#FF7439` | Gradient start, hover glow |
| `primaryContainer` | `#FF7941` | Gradient end, primary button fill |
| `primaryFixedDim` | `#FF5E07` | Ignition spark, focus rings |
| `onPrimaryFixed` | `#000000` | Text on primary CTAs |
| `secondary` | `#FF7439` | (Effectively redundant with primary — prefer primary) |
| `tertiary` | `#FFB951` | Loaders, badges, progress |
| `tertiaryContainer` | `#F8A91F` | Tertiary fill |
| `error` | `#FF716C` | Destructive text/icons |
| `errorContainer` | `#9F0519` | Destructive fills |

### Gradient

The primary CTA gradient is **135°, `primaryDim → primaryContainer`**:

```kotlin
Brush.linearGradient(
    colors = listOf(TokenColors.primaryDim, TokenColors.primaryContainer),
    start = Offset(0f, 0f),
    end = Offset(x, x), // 135°
)
```

Expose as `ComparaCarroTheme.colors.gradientPrimary` once the semantic layer lands.

## 3. Typography

Two families. Roles use existing `TokenFontSizes` (Small=12, Medium=16, Large=20, ExtraLarge=24); the semantic layer adds named accessors but does **not** introduce a new scale.

### Inter (`body`, `headline`)

Editorial. Used for screen titles, body copy, descriptive labels.

### Space Grotesk (`label`)

Technical / numeric. Used for **prices, horsepower, RPM, dimensions, label-* tags**. Whenever a number is rendered next to a non-numeric label, the number goes in Space Grotesk.

### Semantic role mapping (target state)

| Role | Family | Size | Weight | Use |
|---|---|---|---|---|
| `displayLarge` | Inter | 32sp* | Bold | Auth screens hero |
| `headlineLarge` | Inter | ExtraLarge (24) | Bold | Screen titles |
| `titleLarge` | Inter | Large (20) | Bold | Card titles |
| `bodyLarge` | Inter | Medium (16) | Normal | Body copy |
| `bodyMedium` | Inter | Small (12) | Normal | Caption / metadata |
| `priceLarge` | Space Grotesk | ExtraLarge (24) | Bold | Hero price |
| `priceMedium` | Space Grotesk | Medium (16) | SemiBold | List/card price |
| `labelMedium` | Space Grotesk | Small (12) | Medium | Chip text, spec labels |

*`displayLarge` is auth-screen only and not in current scope; add to TokenFontSizes only if/when those screens land.

## 4. Spacing

The Stitch spec uses spacing scale **2** (compact). The **no-divider** rule means a `Section` step larger than `Item` does the work of dividers.

| Token | dp | Usage |
|---|---|---|
| `Item` | 8 | Between items in a list |
| `Inline` | 12 | Between inline siblings (chip → chip, icon → text) |
| `Block` | 16 | Around block-level content |
| `Section` | 24 | Between sections (replaces dividers) |
| `ScreenHorizontal` | 16 | Standard screen edge padding |
| `ScreenTop` | 16 | Top edge padding under glass header |

## 5. Shapes

Restricted scale: **only `sm` and `md`**. No `lg` or `xl`.

| Token | Radius | Usage |
|---|---|---|
| `Sm` | 2dp | Inputs, ghost containers |
| `Md` | 6dp | Cards |
| `Button` | 4dp | Primary / secondary buttons |
| `Pill` | 50% | Chips, filter tags |

## 6. Component patterns

### Cards

- Fill `surfaceContainer` (default) or `surfaceContainerHigh` (raised)
- Shape `Md` (6dp)
- **No border**
- Vehicle photo is cropped at 45° and bleeds past the right edge — do not contain it inside card padding
- Inter for the model name; Space Grotesk for the price
- 8dp `Item` gap to siblings in lists

### Primary button

- 135° gradient `primaryDim → primaryContainer`
- Text `onPrimaryFixed` (`#000000`)
- Shape `Button` (4dp)
- On hover: 20% primary glow halo (Compose: animate a `surfaceTint` overlay)

### Secondary / ghost button

- No fill
- 1dp border using `outline #767575` at **15% opacity**
- Inter, `onSurface` text

### Chips

- Fill `surfaceContainerHigh`
- Shape `Pill`
- Space Grotesk, `labelMedium`

### Inputs

- Fill `surfaceContainerHighest`
- Shape `Sm` (2dp)
- Focus indicator is a **2dp `primaryFixedDim` bottom bar**, not a full border

### Glass header / nav bar

- Background `surfaceVariant @ 60%`
- 24dp backdrop blur
- Sits above content in a `Box`

### Loaders

- `CircularProgressIndicator` tinted `tertiary #FFB951`
- Never `LinearProgressIndicator`

## 7. Semantic accessor API (target state)

Mirroring Jetsnack: `ComparaCarroTheme.colors.<role>` instead of raw `TokenColors.*`. Implementation lands in `chore/theme-jetsnack-semantic-tokens`.

```kotlin
@Stable
class ComparaCarroColors(
    background: Color,
    surface: Color,
    surfaceRaised: Color,
    surfaceInput: Color,
    interactivePrimary: Brush,    // the gradient
    interactivePrimarySolid: Color,
    interactiveSecondary: Color,  // ghost border @15%
    textPrimary: Color,
    textSecondary: Color,
    textInteractive: Color,       // onPrimaryFixed
    accent: Color,                // tertiary gold
    error: Color,
    isDark: Boolean,
) { /* mutableStateOf-backed fields, updateFrom() */ }

object ComparaCarroTheme {
    val colors: ComparaCarroColors
        @Composable @ReadOnlyComposable get() = LocalComparaCarroColors.current
    val typography: ComparaCarroTypography
        @Composable @ReadOnlyComposable get() = LocalComparaCarroTypography.current
}
```

`ComparaCarroTheme` becomes the canonical entry point; raw `TokenColors.*` is internal to the theme module after the migration.

## 8. Migration rules

When refactoring a `com.ui.*` composable or feature screen:

1. Replace `TokenColors.X` with `ComparaCarroTheme.colors.<role>`.
2. Replace hard-coded `Color(0xFF...)` with semantic role.
3. Replace hard-coded `dp` spacing with `TokenSpacing.<role>`.
4. Replace hard-coded `RoundedCornerShape(N.dp)` with `TokenShapes.<role>`.
5. Numerics get Space Grotesk via `style = ComparaCarroTheme.typography.priceMedium` (or similar role).
6. Never use `Modifier.shadow` — switch to a `surfaceContainerHigh` fill.
7. Never use `Divider` — increase spacing to `TokenSpacing.Section`.

## 9. Per-screen specs

- [Home](docs/design/home.md)
- [Detail](docs/design/detail.md)
- [Comparison](docs/design/comparison.md)
- [SelectCompare](docs/design/select-compare.md)
