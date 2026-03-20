# SKDS Core 1.20.1

Forge 1.20.1 port of SKDS Core.

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

- `Water-Physics-Overhaul-1.20.1`
- `WPO-Environmental-Expansion-1.20.1`
- `WPO-Hydraulic-Utilities-1.20.1`

If the base WPO mod or either add-on is being built from source, SKDS Core needs to be present in the expected sibling location.

## Credits

- Original SKDS work: `Sasai_Kudasai_BM`
- 1.18.2 work used in the porting path: `Felicis`
- 1.20.1 port and repository maintenance: [`dev-willbird1936`](https://github.com/dev-willbird1936)

## Related Repositories

- [`Water-Physics-Overhaul-1.20.1`](https://github.com/dev-willbird1936/Water-Physics-Overhaul-1.20.1)
- [`WPO-Environmental-Expansion-1.20.1`](https://github.com/dev-willbird1936/WPO-Environmental-Expansion-1.20.1)
- [`WPO-Hydraulic-Utilities-1.20.1`](https://github.com/dev-willbird1936/WPO-Hydraulic-Utilities-1.20.1)

## Build

This repository builds as a standalone Forge 1.20.1 mod.

Typical local build:

```powershell
.\gradlew.bat build
```
