# SelectCompare — design spec

**Source caveat:** No dedicated SelectCompare screen exists in the Stitch project. This spec extrapolates from the *Listagem em Lista* screen (`b55e65b24431460b967f825fff7eef02`) by adding row-selection state and a sticky bottom bar. Treat any value labelled *(extrapolated)* as a proposal.

The screen lives under `com.selectCompare.*` and is the entry point to `feature:comparison` — the user picks one or two cars, then taps "Comparar" to enter the comparison screen.

## Layout

```
┌──────────────────────────────────────┐
│  glass header  (back + "Comparar")   │  surfaceVariant @ 60% + blur
├──────────────────────────────────────┤
│  Selected: 1/2  •  filter chips      │  status row + horizontal chip scroll
├──────────────────────────────────────┤
│  ╭──── car row (selected) ────╮      │  surfaceContainerHigh (raised)
│  │ photo • Model • price       │      │  primary glow border (extrapolated)
│  ╰─────────────────────────────╯      │
│  ╭──── car row (unselected) ──╮      │  surfaceContainer (default)
│  │ photo • Model • price       │      │
│  ╰─────────────────────────────╯      │
│  ...                                  │
├──────────────────────────────────────┤
│  [   Comparar (2)   ]                │  sticky primary CTA, glass surround
└──────────────────────────────────────┘
```

## Tokens used

| Region | Token |
|---|---|
| Window | `colors.background` |
| Header | `colors.surfaceGlass` |
| Status row text | `typography.labelMedium`, `colors.textSecondary` |
| Filter chip | `colors.surfaceRaised` + `Pill` |
| Car row (default) | `colors.surface` (`surfaceContainer`) + `Md` |
| Car row (selected) | `colors.surfaceRaised` (`surfaceContainerHigh`) + `Md` *(extrapolated)* |
| Selected glow ring *(extrapolated)* | 1dp `colors.accentPrimary` border at 40% opacity, `Md` shape |
| Sticky CTA bar | Glass surround: `colors.surfaceGlass` over content, top safe-padding `TokenSpacing.Block` |
| Primary CTA | `colors.interactivePrimary` (gradient) |
| Disabled CTA | `colors.surfaceRaised` fill, `colors.textSecondary` text |
| Row vertical gap | `TokenSpacing.Item` (8dp) |

## Row anatomy

Re-uses `com.ui.SmallCard` as the row primitive. The selection state is handled by the **container**, not the card:
- Default: card on `colors.surface`
- Selected: card on `colors.surfaceRaised` + an outer 1dp `colors.accentPrimary @ 40%` ring

Do **not** add a checkbox icon — selection is implied by the surface tone shift (consistent with the no-divider, tonal-depth philosophy of the system).

## Sticky CTA bar

- Position: bottom of screen, above the system nav bar
- Background: `colors.surfaceGlass` (`surfaceVariant @ 60%` + 24dp blur)
- Top edge has no border — depth comes from the blur
- CTA spans full width minus `TokenSpacing.ScreenHorizontal` on each side
- Disabled state when `selectedCount < 2`:
  - Fill switches from gradient to solid `colors.surfaceRaised`
  - Text from `onPrimaryFixed` (black) to `colors.textSecondary`

## Selection rules

- Max selection: **2 cars**
- Tapping a third car deselects the oldest selection (FIFO) — a small toast/snackbar surface confirms ("Substituído por X")
- Tapping an already-selected card deselects it
- Selection state lives in `SelectCompareScreenState`; the `Comparar` CTA is disabled until `state.selected.size == 2`

## States

| State | Render |
|---|---|
| `Loading` | Tertiary-gold loader, no rows visible |
| `Success` | Rows + sticky CTA |
| `Error` | `colors.error` text + ghost retry |
| `Empty` | "Nenhum carro disponível" centered in `colors.textSecondary` |

## Interactions

- Tap row → toggles selection; updates status counter
- Tap "Comparar" CTA → `navigate(ComparisonRoute(car1Id, car2Id), NavOptions(replaceTop = true))` — replaces SelectCompare on the back stack so back from Comparison returns to Home, not the selector
- Back button → `goBack()`

## Out of scope

- Multi-select beyond 2 cars
- Drag-to-reorder selected items (always renders left = first, right = second)
- Sorting/grouping the list
