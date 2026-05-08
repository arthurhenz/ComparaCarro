# Detail — design spec

**Source caveat:** The Stitch project does **not** include a dedicated detail screen. This spec is extrapolated from the global design strategy ("Kinetic Precision") and the cross-screen patterns observed in the listing and comparison screens. Treat any value labelled *(extrapolated)* as a proposal — adjust once the canonical Stitch screen lands.

## Layout

```
┌──────────────────────────────────────┐
│  glass back button  (top-left)       │  surfaceVariant @ 60% + blur
├──────────────────────────────────────┤
│                                      │
│         hero photo (45° crop,        │  full-bleed, height 320dp
│         bleeds right edge)           │
│                                      │
├──────────────────────────────────────┤
│  Model name (Inter, headlineLarge)   │
│  R$ 99.999 (Space Grotesk, priceLg)  │
│  spec chip row                       │  pill chips
├──────────────────────────────────────┤
│  Performance section                 │  surfaceContainerLow card
│   ┌ inset surfaceContainerLowest ┐   │  cockpit-depth pattern
│   │  HP   • Torque  • RPM        │   │  Space Grotesk numerics
│   └────────────────────────────────┘ │
├──────────────────────────────────────┤
│  Optionals (CarDetailOptional)       │
├──────────────────────────────────────┤
│  [   Adicionar à Comparação   ]      │  primary gradient CTA
└──────────────────────────────────────┘
```

## Tokens used

| Region | Token |
|---|---|
| Window | `colors.background` (`surface #0E0E0E`) |
| Back-button surface | `colors.surfaceGlass` |
| Hero photo container | `colors.surface` (transparent under image) |
| Section card | `colors.surfaceLow` (`surfaceContainerLow`) |
| **Inset spec block** | `colors.surfaceInset` (`surfaceContainerLowest`) — *cockpit depth* |
| Section title | `typography.titleLarge` (Inter Bold) |
| Spec value | `typography.priceMedium` (Space Grotesk SemiBold) |
| Spec label | `typography.labelMedium` (Space Grotesk Medium, `colors.textSecondary`) |
| Section spacing | `TokenSpacing.Section` (24dp) — replaces dividers |

## Cockpit-depth pattern *(extrapolated)*

Detail's signature visual is a `surfaceContainerLow` section card with **nested `surfaceContainerLowest` inset** for the numeric spec block — visually deeper than the surrounding card, evoking a dashboard binnacle. Always pair the inset with Space Grotesk numerics.

```kotlin
Card(colors = ComparaCarroTheme.colors.surfaceLow) {
    Text("Performance", style = ComparaCarroTheme.typography.titleLarge)
    Spacer(TokenSpacing.Item)
    Box(
        Modifier
            .background(ComparaCarroTheme.colors.surfaceInset, TokenShapes.Sm)
            .padding(TokenSpacing.Block)
    ) {
        // HP / Torque / RPM in priceMedium
    }
}
```

## Hero photo

- Aspect ~16:11, height 320dp at standard 390dp widths
- Cropped at 45° on the right edge, mirroring the list cards
- No overlay — the photo sits on `surface`, the back button is the only floating chrome

## Optionals (`com.ui.CarDetailOptional`)

Today: a row of optional-feature icons. After the refactor:
- Each optional is a chip with `colors.surfaceRaised` fill + `Pill` shape
- Icon tinted `colors.textPrimary`
- Label in `typography.labelMedium`
- Wraps to next line if needed; gap `TokenSpacing.Item`

## Primary CTA

`com.ui.PrimaryButton` rendered with the gradient pattern (`colors.interactivePrimary`). Sticky bottom of screen with `TokenSpacing.Block` padding, content `onPrimaryFixed` (`#000000`).

## Interactions

- Back button → `goBack()` via `Navigator`
- Heart (re-uses `com.ui.FavoriteButton`) → `DetailScreenEvent.ToggleFavorite`
- Primary CTA → `DetailScreenEvent.AddToComparison(carId)` then `navigate(SelectCompareRoute, NavOptions(singleTop = true))`

## States

| State | Render |
|---|---|
| `Loading` | Tertiary-gold circular loader, centered |
| `Success` | Layout above |
| `Error` | `colors.error` message + ghost retry button |

## Out of scope

- Sharing/copy-link controls
- Image gallery (single hero only for v1)
- Reviews/ratings

## Open questions for design review

1. Should the "Adicionar à Comparação" button float (sticky) or scroll with content?
2. Does the cockpit-inset depth pattern apply to the Optionals section, or only to numeric blocks?
3. Confirm hero crop angle (45° matches list cards; or steeper for hero impact?)
