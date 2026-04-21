# SKDS Core

This repository currently targets Forge 1.20.1 on the active branch.

## What It Is

SKDS Core is the shared foundation layer used by the 1.20.1 Water Physics Overhaul port and its add-ons.

It provides the runtime systems the rest of the workspace expects:

- block update scheduling and synchronized world work execution
- multithreading helpers and worker-thread plumbing
- shared extension points and interfaces used by the water simulation
- shared packet/debug infrastructure
- a small client config screen for performance tuning

This mod is not meant to add a large standalone gameplay feature set by itself. Its job is to supply the infrastructure that the other SKDS and WPO modules build on.

## Main Systems

### Block Update Budgeting

SKDS Core manages a configurable block-update budget and tick cutoff so the dependent mods can do heavy world work without trying to spend unlimited time in one server tick.

The main performance controls are:

- `performancePreset`
- `minBlockUpdates`
- `timeout`

Presets expose a simple way to move between safer performance targets, while `CUSTOM` unlocks manual values.

## Configuration

The common config is stored at:

```text
config/skds_core/main.toml
```

The in-game config screen exposes the same main values:

- performance preset
- minimum block updates per tick
- timeout cutoff before the end of the server tick

## Where It Fits

This repository is the shared dependency for:

- `Water-Physics-Overhaul`
- `WPO-Environmental-Expansion`
- `WPO-Hydraulic-Utilities`

If the base WPO mod or either add-on is being built from source, SKDS Core needs to be present in the expected sibling location.

## Credits

- Original SKDS work: `Sasai_Kudasai_BM`
- 1.18.2 work used in the porting path: `Felicis`
- 1.20.1 port and repository maintenance: [`dev-willbird1936`](https://github.com/dev-willbird1936)

## Related Repositories

- [`Water-Physics-Overhaul`](https://github.com/dev-willbird1936/Water-Physics-Overhaul)
- [`WPO-Environmental-Expansion`](https://github.com/dev-willbird1936/WPO-Environmental-Expansion)
- [`WPO-Hydraulic-Utilities`](https://github.com/dev-willbird1936/WPO-Hydraulic-Utilities)

## Build

This repository builds as a standalone Forge 1.20.1 mod.

Typical local build:

```powershell
.\gradlew.bat build
```

Explicit version build:

```powershell
.\gradlew.bat build -PmcVersion=1.20.1
```

Stage the release jar into the workspace release folder:

```powershell
.\gradlew.bat stageRelease -PmcVersion=1.20.1
```

Current workspace layout:

```text
..\SKDS-Core
..\Water-Physics-Overhaul
..\WPO-Environmental-Expansion
..\WPO-Hydraulic-Utilities
```

Version-specific Minecraft and release values now live in `versions/<mcVersion>.properties`. For future ports, add a new version file and use a branch like `mc/1.21.1` when source compatibility starts to diverge.

## Version Strategy

- Stable repository name, without the Minecraft version in the repo title
- `main` for the current maintained line
- `mc/<minecraft-version>` branches when code starts to diverge between game versions
- release tags in the form `v<minecraft-version>-<mod-version>`
