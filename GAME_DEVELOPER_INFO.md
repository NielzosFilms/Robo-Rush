# Game Developer Info

On this page is some more in depth info about the game.

------------------------------------------------------------------------

### General Game Info
- The game runs at `60 ticks per second`
- Textures are 16 x 16 `pixels`

------------------------------------------------------------------------

### Current Version
- `ALPHA`
- [Exact Version](./src/game/system/main/Game.java#L40)

------------------------------------------------------------------------

### World Generation Info
- Infinite terrain generation
- `World` consists of multiple `Chunk's`
- `Biome` generation is based off of this chart:
<img src="./biome_guide.png" alt="Biome Guide Chart">

------------------------------------------------------------------------

### Chunk Info
- A `Chunk` keeps track off all the `Entities` and `Tiles` inside
- A `Chunk` is 16 x 16 `Tiles`

------------------------------------------------------------------------

### Tile Info
- A `Tile` is 16 x 16 `pixels`
- Edge type is selected with an 8 bit byte
- First four bytes are the connections<br/>North, East, South and West.
- Last four bytes are the connections in the corners<br/>NorthEast, SouthEast, SouthWest and NorthWest

<img src="./tile_edge_guide.png" alt="Tile Edge Guide">