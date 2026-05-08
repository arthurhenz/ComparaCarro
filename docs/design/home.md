# Home — design spec

Source: Stitch screen `b55e65b24431460b967f825fff7eef02` (*Listagem em Lista*) is canonical for Home; `4754161f446f4f80b0b533a14732e882` (*Listagem em Grade 2 Colunas*) is documented as an alternate. ComparaCarro currently ships the list variant; this spec describes that path and notes deltas for the grid variant.

## Layout (list variant)

```
┌──────────────────────────────────────┐
│  glass header  (logo + filter icon)  │  surfaceVariant @ 60% + 24dp blur
├──────────────────────────────────────┤
│  filter chip row  (horizontal scroll)│  pill chips, surfaceContainerHigh
├──────────────────────────────────────┤
│  ╭─────────────────╮  ┐              │
│  │ Model name (Inter)│  │  card row   │  surfaceContainer, Md radius
│  │ spec chips        │  │  8dp gap    │  photo cropped @45°, bleeds right
│  │ R$ 99.999 (S.Gro) │  │             │
│  ╰─────────────────╯  ┘              │
│  ╭ ... ╮                              │
│  ╰─────╯                              │
└──────────────────────────────────────┘
```

## Tokens used

| Region | Token |
|---|---|
| Window | `colors.background` (`surface #0E0E0E`) |
| Header | `colors.surfaceGlass` (`surfaceVariant @ 60%` + blur) |
| Filter chip | `colors.surfaceRaised` (`surfaceContainerHigh`) + `Pill` shape |
| Card | `colors.surface` (`surfaceContainer`) + `Md` shape |
| Card title | `typography.titleLarge` (Inter) |
| Card price | `typography.priceMedium` (Space Grotesk) |
| Card spec chips | `typography.labelMedium` (Space Grotesk) on `surfaceContainerHigh` |
| Vertical gap between cards | `TokenSpacing.Item` (8dp) |
| Section padding | `TokenSpacing.ScreenHorizontal` (16dp) |

## Card anatomy

Reuses `com.ui.LargeCard` (single car) wrapped by `com.ui.LargeCardCarousel` for any horizontal hero row, and `com.ui.SmallCard` rendered through `com.ui.SmallCardList` for the main vertical list. After the refactor:

- Card photo: 45° crop, bleeds 16dp past the right card edge. Use `Modifier.graphicsLayer { rotationZ = 0f }` + a clipping `RoundedCornerShape` that exposes the right side.
- No card border. No card shadow. Depth comes from `surfaceContainer` against the darker `surface` window.
- Favorite heart (`com.ui.FavoriteButton`) is overlaid top-right inside the card; uses `colors.accentPrimary` (selected) / `colors.textSecondary` (unselected). The current `Color.Gray.copy(alpha = 0.6f)` heart-button border becomes `colors.outlineGhost` (the 15% outline rule).

## Header

`com.ui.Header` houses logo + filter icon. Glass surface is `surfaceVariant @ 60%` with a `Modifier.blur(24.dp)` underlay (or `RenderEffect.createBlurEffect` for SDK 31+). Falls back to opaque `surfaceContainer` when blur is unavailable.

## Empty state

If the list is empty, render the loader (`CircularProgressIndicator` tinted `colors.accentTertiary` — gold) until `HomeScreenState.Success` arrives. On `Error`, render `colors.error` text + a primary CTA "Tentar novamente" using the gradient button pattern.

## Grid variant (alternate)

Stitch screen `4754161f44...` shows a 2-column grid. If activated, swap `LazyColumn(SmallCardList)` for `LazyVerticalGrid(columns = Fixed(2), gap = TokenSpacing.Item)` and keep all other tokens identical. Grid is **not** the default — kept as a feature-flag option.

## Interactions

- Tap a card → `navigate(DetailRoute(carId))` via `Navigator` (no UI callback).
- Tap heart → toggles favorite locally; ViewModel emits `HomeScreenEvent.ToggleFavorite(carId)`.
- Tap filter icon → opens filter sheet (out of scope for the refactor; stub today).

## Out of scope for the refactor

- The actual filter implementation
- Bottom nav bar (no Stitch reference for it on Home)
- Pull-to-refresh
