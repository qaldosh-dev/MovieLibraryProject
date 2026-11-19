# Navigation checks

Date: 2026-11-18. Branch: `feature/niyazbek-navigation-adaptive`.

## Build and scope

Checks used a temporary integration checkout: main `24dcf2f` (F12 included) plus the seven D07 files from `feature/niyazbek-navigation-design` at `a031f43`. D07 PR #38 was not merged; early verification was authorized. The temporary APK ran on the emulator. No D07 changes were copied into this branch. Final theme #24 and D08 genre styling were not included.

`bash ./gradlew :app:assembleDebug :app:lintDebug` passed for this exact integration checkout. Only this report was added; no navigation correction was needed for the tested cases. Earlier preliminary checks on main alone are superseded by these D07 checks.

## Device and configurations

One Android Emulator, `emulator-5554`, `sdk_gphone64_arm64`, Android 16 / API 36, gesture navigation. ADB taps, accessibility hierarchy dumps and screenshots were used. Size/density overrides simulate compact screens; these are not separate physical devices.

| Configuration | Font scale | Observations |
| --- | --- | --- |
| 1080 x 2400 px at 420 dpi, portrait | 1.0 | Working app, search and all navigation scenarios |
| 2400 x 1080 px at 420 dpi, landscape | 1.0 | Search/favorites retention, repeated sections and Back |
| 720 x 1280 px at 320 dpi, portrait (360 x 640 dp) | 1.5 | Labels and Back visible; genre/details return chain passed |
| 1280 x 720 px at 320 dpi, landscape (640 x 360 dp), light | 1.3 and 1.5 | Navigation visible; filtered catalog content inaccessible |
| Same compact landscape, dark | 1.5 | Panel labels readable, no observed system-bar overlap; last genre reachable by scrolling |

## Executed scenarios

| Scenario | Result |
| --- | --- |
| Catalog search `inter` -> Interstellar details -> toolbar Back | PASS: query and filtered result retained |
| Rotate the searched catalog to landscape | PASS: `inter` and Interstellar retained |
| Add Interstellar -> rotate -> Favorites -> details -> toolbar Back | PASS: favorite retained and returned to Favorites |
| Compact portrait at 1.5: Comedy catalog -> Paddington details -> toolbar Back -> toolbar Back | PASS: returned to filtered catalog, then Genres |
| Three repetitions of Genres -> Comedy -> Favorites -> system Back | PASS: one Back returned to the root catalog |
| Three physical taps on the selected Favorites tab -> system Back | PASS: no extra section history; returned to catalog |
| Compact landscape at 1.3: scroll Genres to Comedy | PASS: last genre reachable |
| Top/bottom labels, selected section and Back in inspected D07 screenshots | PASS: no observed truncation/overlap; bottom menu hidden in details |
| Compact landscape at 1.3 and 1.5: open Comedy catalog | FAIL: movie list and clear-filter action inaccessible |

## Finding for the catalog owner

Owner: Bauyrzhan. File: `ui/screens/CatalogScreen.kt` (outside this task's allowed files).

Reproduce with 720 x 1280 px, density 320 dpi, font scale 1.3 or 1.5; rotate to landscape and open Genres -> Comedy. Only search and the active genre fit in the content area. The accessibility hierarchy contains no Paddington/Home Alone or clear-filter action. Navigation remains visible and Back works. The non-scrolling search/filter Column leaves no usable height for the weighted movie list.

No catalog or other owner files were changed. Share this reproduction with the owner, fix it in a separate authorized task, then repeat the filtered-catalog/details scenario in compact landscape. No message was sent on the user's behalf.

## Limits and follow-up

- This is a pre-merge integration result, not certification of main after #30. Repeat against the eventual merged commit, especially if review changes D07.
- Complete the blocked compact-landscape movie and clear-filter actions after the catalog fix.
- Repeat with the final theme and subsequent screen redesigns. No claim is made about those untested versions.
- No whole-app acceptance or task completion is claimed while the failing action-accessibility case remains unresolved.
- Original font, display, rotation and night settings were backed up for restoration after the run. Test-only favorite changes are removed during cleanup.
