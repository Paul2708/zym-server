# Chunk - Claim
This spigot plugin provides a claim and plot system, to claim certain chunks. They have a griefing protection to avoid abusing.

Note: As the plugin has to be fast developed, the code is not as clean as it should be. Refactoring is planned.

## Features
- simple command usage (`/chunk claim` and `/chunk buy`)
- local data storage (json)
- griefing protection
- claiming chunks in a freebuild world
- higher prices for more chunks
- custom tab list

## Usage
### Installation
1. Put WorldEdit and WorldGuard into your plugin folder.
2. Put the jar into your plugin folder and reload the server.
3. Enjoy!

### How to use?
- Join the server and search for chunk you want to claim.
- If you found one, use `/chunk buy` to buy a chunk claimer.
Hint: Hold the claimer in your main hand or use `F3 + G` to draw the chunk border.
- Right-click the claimer or use `/chunk claim` to claim your current chunk. It's now yours!
- Type `/chunk buy` to buy another claimer - but the price will be increased.

### Prices
This table shows the prices for the chunks.

| Chunk number | Price (in diamonds) |
|--------------|---------------------|
| 1            | free                |
| 2            | 8                   |
| 3            | 16                  |
| 4            | 32                  |
| 5            | 64                  |
| 6            | 64                  |
| 7            | 128                 |
| 8            | 128                 |
| >= 9         | 192                 |


### Commands
| Command           | Description                                       | Usage                      | Permission       |
|-------------------|---------------------------------------------------|----------------------------|------------------|
| /chunk help       | Show the help overview.                           | /chunk help                | none             |
| /chunk buy        | Open a GUI to buy a chunk claimer.                | /chunk buy                 | none             |
| /chunk claim      | Claim your current chunk.                         | /chunk claim               | none             |
| /chunk info       | Show information about the current chunk.         | /chunk info                | none             |
| /chunk opinfo     | Show more information about the current chunk.    | /chunk opinfo              | chunk.opinfo     |
| /chunk playerinfo | Show information about a player.                  | /chunk playerinfo [player] | chunk.playerinfo |
| /chunk unclaim    | Unclaim the current chunk.                        | /chunk unclaim             | none             |
| /chunk remove     | Unclaim the current chunk even if it's not yours. | /chunk remove              | chunk.remove     |
| /chunk giveclaimer| Give a claimer to a player.                       | /chunk giveclaimer [player]| chunk.giveclaimer|
| /chunk version    | Print the current plugin version.                 | /chunk version             | chunk.version    |
| /live             | Trigger the live status in the tab list.          | /live                      | none             |
| /tphelp           | Teleport to a near player, if you are stucked.    | /tphelp                    | none             |

## Protection
### Global protection
- canceled any explosion
- canceled fire spreading and burning blocks

### Claimed chunk protection
- cancel building and breaking blocks
- cancel water flowing into other claimed chunks
- cancel piston bugs by moving blocks into other chunks
- cancel trees growing into other chunks
- cancel damage to entities (expect players) in other claimed chunks
- cancel armor stand manipulation in other claimed chunks
- cancel interacting on other chunks
- and more...

## Changelog
The changelog can be found [here](CHANGELOG.md).

## Contact
If you find any issues, please let me know!

Paul2708 - [BitBucket](https://bitbucket.org/Paul2708/) [Twitter](https://twitter.com/theplayerpaul) Discord: Paul2708#1098 [Mail](mailto:playerpaul2708@gmx.de)