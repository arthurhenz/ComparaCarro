# Comparison — design spec

Source: Stitch screen `524c00848b0d4311914aa079b0b13c4e` (*Comparação de Carros - Alinhada*). Two cars side-by-side, with aligned spec rows below.

## Layout

```
┌──────────────────────────────────────┐
│  glass header  (back + title)        │
├──────────────────────────────────────┤
│  ╭──── car A ────╮  ╭──── car B ────╮│  hero block per car
│  │  photo (45°)   │  │  photo (45°)  ││  surfaceContainer
│  │  Model         │  │  Model        ││  Inter titleLarge
│  │  R$ price      │  │  R$ price     ││  Space Grotesk priceMedium
│  ╰────────────────╯  ╰───────────────╯│
├──────────────────────────────────────┤
│  Performance                         │  section title (Inter)
│  ┌─ A ──────┐    ┌─ B ──────┐        │  aligned spec rows
│  │ 240 HP   │    │ 180 HP * │        │  Space Grotesk priceMedium
│  └──────────┘    └──────────┘        │  diff highlight on A or B
├──────────────────────────────────────┤
│  Dimensions                          │
│  ...                                 │
├──────────────────────────────────────┤
│  Features                            │
│  ✓ ABS                ✓ ABS          │
│  ✓ Cruise            — Cruise        │
└──────────────────────────────────────┘
```

## Tokens used

| Region | Token |
|---|---|
| Window | `colors.background` |
| Header | `colors.surfaceGlass` |
| Car hero block | `colors.surface` (`surfaceContainer`) + `Md` |
| Section block | `colors.surfaceLow` (`surfaceContainerLow`) |
| Spec row cell | `colors.surface` (nested) |
| Section title | `typography.titleLarge` (Inter Bold) |
| Numeric value | `typography.priceMedium` (Space Grotesk) |
| Spec label | `typography.labelMedium` (Space Grotesk Medium, `colors.textSecondary`) |
| **Diff highlight (winner)** | `colors.accentPrimary` text (`primary #FF9064`) |
| **Diff highlight (loser)** | `colors.accentTertiary` text (`tertiary #FFB951`) |
| Inter-section gap | `TokenSpacing.Section` (24dp) — no dividers |

## Hero blocks

- Two equal-width 50/50 blocks, separated by `TokenSpacing.Item` (8dp) horizontally
- Each is the **same primitive as a Home card** (`com.ui.LargeCard`) but at half-width with a smaller hero image
- Photo still uses the 45° right-edge crop — visually the two photos lean toward each other, framing the comparison

## Spec row anatomy

Each "row" is actually two parallel cells, **one per car**. The Stitch reference aligns them with consistent label slots above each numeric:

```kotlin
Row(horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Item)) {
    SpecCell(label = "HP", value = "240", isWinner = true, modifier = Modifier.weight(1f))
    SpecCell(label = "HP", value = "180", isWinner = false, modifier = Modifier.weight(1f))
}
```

`SpecCell` is a new candidate component for the per-component PR stack — propose it during the `feature:comparison` refactor.

## Diff highlighting

For numeric specs (HP, torque, MPG, etc.), the **higher value is colored `colors.accentPrimary`**, the lower is colored `colors.accentTertiary`. Both stay in Space Grotesk. Don't apply diff highlighting to non-orderable specs (e.g. "Color"); render those in `colors.textPrimary` for both cars.

For boolean features (ABS, Cruise Control, Heated Seats):
- Present → `Icon(Check, tint = colors.accentPrimary)`
- Absent → `Icon(Dash, tint = colors.textSecondary)` *(em-dash glyph or `Icons.Outlined.Remove`)*

## States

| State | Render |
|---|---|
| `Loading` | Tertiary-gold loader |
| `Success` | Layout above |
| `Error` | `colors.error` + retry primary CTA |
| **EmptyOnOneSide** | If only one car is selected (rare race condition), show its block + a "Selecionar segundo carro" ghost CTA in the right hero slot. Tap routes to SelectCompare. |

## Interactions

- Back → `goBack()`
- Tap a hero block → could route to that car's Detail (proposal — confirm with design)
- "Trocar carros" action *(extrapolated)*: swaps A↔B in-place via state mutation; does not re-route

## Out of scope

- More than 2 cars compared
- Saving/sharing the comparison
- Tabbed sections (current spec uses scroll; revisit if spec grows long)
