# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

ComparaCarro is an Android app for browsing and comparing cars. Application id `com.comparacarro`, package `com.comparacarro`. UI is in Portuguese (Brazilian).

## Build & test commands

Use the Gradle wrapper. The project uses JDK 17 toolchain.

- `./gradlew assembleDebug` — build the debug APK
- `./gradlew build` — full build (assemble + lint + unit tests on all modules)
- `./gradlew ktlintCheck` — run Ktlint over all modules (rules in `.editorconfig`)
- `./gradlew ktlintFormat` — auto-fix Ktlint violations
- `./gradlew test` — run unit tests across all modules
- `./gradlew :feature:home:testDebugUnitTest` — unit tests for a single module
- `./gradlew :feature:home:testDebugUnitTest --tests "com.home.HomeViewModelTest"` — single test class
- `./gradlew validateDebugScreenshotTest` — run Compose screenshot tests (`android.experimental.enableScreenshotTest=true`)
- `./gradlew updateDebugScreenshotTest` — regenerate screenshot test baselines
- `./gradlew connectedAndroidTest` — instrumented tests on a connected device/emulator

The local backend URL is hardcoded in `network/src/main/java/comparacarro/network/di/NetworkModule.kt` (`http://192.168.1.9:8080`); update it to point at your dev server. `usesCleartextTraffic="true"` is set in the manifest for local HTTP.

## Module graph

```
:app                                    application; wires Koin + AppNavigator + NavDisplay
├── :feature:home, :detail, :comparison screens (each: Module, Route, Screen, ViewModel, State, Event)
├── :core:ui                            theme tokens (Token*.kt), reusable composables (com.ui.*)
├── :core:data                          repositories, use cases, domain models
├── :core:common                        Navigator interface, NavOptions, Constants
├── :core:navigation                    NavKey routes, EntryProvider interface
├── :core:utils
└── :network                            Ktor client, CarsApi, NetworkResult sealed class
```

`feature:comparison` ships two screens: `com.comparison.*` (compare) and `com.selectCompare.*` (selection). Each registers its own Koin `@Module` and `EntryProvider`.

## Architecture patterns to preserve

**Navigation (Navigation 3, custom Navigator):**
- `core:common` defines `Navigator` (suspendless `navigate(route, options)` / `goBack()`) and `NavOptions(popUpTo, popUpToInclusive, replaceTop, singleTop)`. Don't switch to AndroidX NavController — Navigation 3 here uses `NavDisplay` over a `mutableStateListOf<NavKey>` backstack (see `app/.../AppNavigator.kt`).
- Routes are `@Serializable data object/class ... : NavKey` and live in `:core:navigation` so feature modules don't depend on each other. Add new routes there.
- ViewModels receive `Navigator` from Koin and use Kotlin delegation: `class FooViewModel(..., navigator: Navigator) : ViewModel(), Navigator by navigator`. Call `navigate(SomeRoute(...), NavOptions(singleTop = true))` directly inside the VM — do **not** add nav callbacks to the screen.
- Each feature exposes an `EntryProvider` (e.g. `HomeScreenProvider`) that returns an `EntryProviderScope<NavKey>.() -> Unit` with its `entry<RouteType> { ... }` builder. The `:app` module registers each provider via `bind EntryProvider::class` in Koin; `EntriesProviderAggregator(getAll())` collects them all and `NavGraph.kt` composes them in a single `entryProvider { ... }` block. To add a screen: define a `NavKey` in `:core:navigation`, write `fun EntryProviderScope<NavKey>.fooScreenRoute() { entry<FooRoute> { ... } }`, expose it via a `XxxScreenProvider : EntryProvider`, then `single { XxxScreenProvider() } bind EntryProvider::class` in `ComparaCarroApplication.navigationModule`.

**Dependency injection (Koin Annotations + KSP):**
- Every module declares an empty class annotated `@Module @ComponentScan("<package>")` (e.g. `HomeModule`, `DataModule`, `NetworkModule`). The top-level `AppModule` in `:app` `includes` all of them; KSP generates `*.module` extensions.
- Bindings use annotations on the implementation class: `@Single`, `@KoinViewModel` (e.g. `CardRepositoryImpl`, `HomeViewModel`). Prefer this over manual `module { }` DSL — the only manual module in the project is the navigation one in `ComparaCarroApplication.kt` (because it uses `bind` to multiple interfaces).
- `startKoin` is invoked once in `ComparaCarroApplication.onCreate()` with `AppModule().module` and `navigationModule`. `@KoinApplication` annotates the marker class `KoinApp`.
- The `comparacarro.koin` convention plugin auto-applies KSP and adds the koin-bom + annotations dependencies. Apply it to any new module that uses `@KoinViewModel`/`@Single` etc.

**Convention plugins (`build-logic/convention/`):**
Don't duplicate Android/Compose config in module `build.gradle.kts`. Apply the right convention plugin instead:
- `app.application` / `app.library` — sets compileSdk 36, minSdk 24, JVM 17, applies Kotlin Android plugin, adds `androidx.core.ktx` + JUnit + Espresso baseline. Also applies Ktlint (`org.jlleitschuh.gradle.ktlint`) via `configureKtlint()` so every Android module is auto-linted; rules live in `.editorconfig`.
- `comparacarro.android.compose` — turns on `buildFeatures.compose`, enables screenshot test (`com.android.compose.screenshot`), adds Compose BOM, Material 3, Navigation3, Koin Compose
- `comparacarro.koin` — applies KSP, adds `koin-bom`, `koin-core`, `koin-android`, `koin-annotations`, registers `koin-ksp-compiler`

When adding a new module: register it in `settings.gradle.kts`, then apply `app.library` + whichever of `comparacarro.android.compose` / `comparacarro.koin` it needs. Ktlint is included automatically through `app.library` / `app.application`.

**Feature module shape:** Every feature follows the same file layout — `XxxModule.kt` (Koin), `XxxScreenRoute.kt` (Navigation 3 entry + `EntryProvider`), `XxxScreen.kt` (state-hoisted composable), `XxxScreenContent.kt` (optional, the actual content layout), `XxxViewModel.kt` (`@KoinViewModel`, `Navigator by navigator`), `XxxScreenState.kt` (sealed class with `Loading`/`Success`/`Error`), `XxxScreenEvent.kt` (sealed input events). Keep this shape when adding screens.

**Networking:** Ktor `HttpClient(OkHttp)` is built in `KtorClientProvider`; the API surface is `CarsApi` returning `NetworkResult<T>` (sealed `Success(data)` / `Error(code, message)`). Repositories in `:core:data` map backend models (`comparacarro.network.model.*`) into domain models (`com.data.model.*`) and never leak Ktor or `NetworkResult` upward.

## Design system

`core/ui/src/main/java/com/theme/` holds the token files (`TokenColors`, `TokenSpacing`, `TokenShapes`, `TokenIconSize`, `TokenFontSizes`, `TokenDefaultTypography`) and `ComparaCarrosTheme`. Use these tokens in composables instead of hard-coded values; semantic tokens like `TokenSpacing.ScreenHorizontal` and `TokenShapes.Card` exist for common cases. Full reference: `DESIGN_SYSTEM.md`. `DebugColors.kt` provides `debugColors()` / `debugPlainColors()` for inspecting which Material 3 color roles are used by which surfaces.

## Versions catalog

All dependency versions live in `gradle/libs.versions.toml`. Add new libs there, not inline. Notable: Kotlin 2.2.10, AGP 8.9.3, Compose BOM 2025.09.00, Koin 4.1.1, Koin Annotations 2.2.0-RC1, Ktor 2.3.12, Navigation 3 alpha (`nav3Core = 1.0.0-alpha06`).
