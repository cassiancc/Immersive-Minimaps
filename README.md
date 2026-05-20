# Immersive Minimaps

<a href='https://modrinth.com/mod/immersive-overlays/versions?l=fabric'><img alt="fabric" height="56" src="https://raw.githubusercontent.com/intergrav/devins-badges/refs/heads/v3/assets/cozy/supported/fabric_vector.svg"></a>
<a href='https://modrinth.com/mod/sinytra-connector'><img alt="Compatible with Sinytra Connector" height="56" src="https://raw.githubusercontent.com/Sinytra/.github/refs/heads/main/badges/connector/cozy.svg"></a>

A minimalist, client-side minimap that integrates Hoofprint into the player's UI, allowing it to only be visible as long as the player has a map.

This mod pairs well with my [Immersive Overlays](https://modrinth.com/project/immersive-overlays) mod to display more information in the HUD.

## Features

A minimap overlay displaying the player's surroundings is visible whenever the player has a map in their inventory, including in a bundle. This minimap shows nearby Surveyor landmarks and any nearby players that are sharing their map with you.

Items in the minimap list can also be used on a block to place a waypoint. Clicking the block again removes the landmark.

Items in the minimap list can also be used to open the Hoofprint world map.

This mod will also adjust the requirements to open Hoofprint's world map to match Immersive Minimaps.

## Installation

Immersive Minimaps is a clientside mod. Its dependencies are listed below.

### Dependencies
- [Hoofprint](https://modrinth.com/mod/hoofprint) is required.
- On Fabric, [Fabric API](https://modrinth.com/mod/fabric-api) is required.
- On NeoForge, [Sinytra Connector](https://modrinth.com/mod/connector) is required.
- [McQoy](https://modrinth.com/mod/mcqoy) is recommended to configure the mod.
- [Immersive Overlays](https://modrinth.com/mod/immersive-overlays) is recommended for additional mod compatibility.

## FAQ

- Will this mod be ported to other versions/loaders?
  - Immersive Minimaps is actively maintained on the last three major versions - 1.20.1, 1.21.1, and 26.1. Neither this mod nor Hoofprint are natively available on NeoForge, but both can be used through [Sinytra Connector](https://modrinth.com/mod/sinytra-connector).
- Mod compatibility?
  - For sheer ease of maintenance, the base mod will only support [Trinkets](https://modrinth.com/mod/trinkets) [(Updated)](https://modrinth.com/mod/trinkets-updated). For modded backpacks or other accessory mods, add on [Immersive Overlays](https://modrinth.com/mod/immersive-overlays) and this mod will use Immersive Overlays's large amount of mod compatibility code.

### Troubleshooting / Suggestions

Immersive Minimaps is an addon for Hoofprint, which makes use of the [Surveyor Map Framework](https://modrinth.com/mod/surveyor). Immersive Minimaps renders data collected by Surveyor and Hoofprint.

- Issues and suggestions regarding the minimap are [Immersive Minimaps Issues](https://github.com/cassiancc/immersive-minimaps/issues)
- Issues and suggestions regarding map sharing, explored map area, and automatic markers are [Surveyor Issues](https://github.com/sisby-folk/surveyor)

## Credits

This mod reuses some code from [Hoofprint](https://modrinth.com/mod/hoofprint) under its [LGPL License](https://github.com/HestiMae/hoofprint/blob/26.1/LICENSE).

Mod icon is by [nöelle](https://modrinth.com/user/noelledotjpg).

The [Surveyor ecosystem](https://modrinth.com/collection/fUFr3Mvj) is trying to make mapping more accessible! (and open-source)
Feel free to contribute improvements to [Immersive Minimaps](https://github.com/cassiancc/Immersive-Overlays/issues), [Hoofprint](https://github.com/HestiMae/hoofprint/issues) or [Surveyor](https://github.com/sisby-folk/surveyor/issues), or utilize Surveyor for your own mods!