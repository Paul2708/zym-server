# Chunk - Claim
This spigot plugin provides a claim and plot system, to claim certain chunks. They have a griefing protection to avoid abusing.

Note: As the plugin has to be fast developed, the code is not as clean as it should be. Refactoring is planned.

## Features
- simple command usage (`/chunk help` for more information)
- local data storage based on mysql
- griefing protection
- claiming chunks in a freebuild world
- group chunks and city chunks
- higher prices for more chunks
- custom tab list
- ...

## Usage
### Installation
0. Setup your mysql database. An example database will be uploaded soon.
1. Put WorldEdit and WorldGuard into your plugin folder.
2. Put the jar into your plugin folder and reload the server.
3. Enjoy!

### Private chunks
- Join the server and search for chunk you want to claim.
- If you found one, use `/chunk buy normal` to buy a chunk claimer.
Hint: Hold the claimer in your main hand or use `F3 + G` to draw the chunk border.
- Right-click the claimer or use `/chunk claim` to claim your current chunk. It's now yours!
- Type `/chunk buy` to buy another claimer - but the price will be increased.

### Group chunks
- If you want to claim a group chunk, use `/chunk buy group` and `/chunk claim`.
Note: The chunks next to it will be not protected - everybody (excluding you) can claim them.
- Use `/chunk permit [Player]` to permit a certain player to build on your chunk.
- `/chunk permitall [Player]` will permit a player to build on all your chunks.
- Remove the permission by `/chunk forbid [Player]`.

### City chunks
- City chunks can be claimed by admins only.
- They can decide if players are allowed to build on them or not.

### Prices
This table shows the prices for private chunks.

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

The group chunk prices are constantly 5 diamonds. But the amount of possible claimers depend on your private chunks.


### Commands
| Command            | Description                                                | Usage                            | Permission                      |
|--------------------|------------------------------------------------------------|----------------------------------|---------------------------------|
| /chunk help        | Show the help overview.                                    | /chunk help                      | none                            |
| /chunk info        | Show (detailed) information about a player or chunk.       | /chunk info <Player>             | none (for more information: OP) |
| /chunk bypass      | Bypass the protection.                                     | /chunk bypass                    | chunk.bypass                    |
| /chunk buy         | Buy a claimer for a group or private chunk.                | /chunk buy [group|normal]        | none                            |
| /chunk claim       | Claim the current chunk while you holding a claimer.       | /chunk claim                     | none                            |
| /chunk unclaim     | Unclaim your current chunk.                                | /chunk unclaim                   | none                            |
| /chunk giveclaimer | Give a claimer to a player.                                | /chunk giveclaimer [player]      | chunk.giveclaimer               |
| /chunk remove      | Remove the current chunk.                                  | /chunk remove                    | chunk.remove                    |
| /chunk permit      | Permit a player to build on your group chunk.              | /chunk permit [player]           | none                            |
| /chunk permitall   | Permit a player to build on all your group chunks.         | /chunk permitall [player]        | none                            |
| /chunk forbid      | Remove the permission for a player.                        | /chunk forbid [player]           | none                            |
| /chunk permitlist  | Show a list of all player, that have access on your chunk. | /chunk permitlist                | none                            |
| /chunk version     | Show the current plugin version.                           | /chunk version                   | chunk.version                   |
| /live              | Trigger your live status.                                  | /live                            | none                            |
| /tphelp            | Teleport to a near player, if you are stucked.             | /tphelp [player] (with cooldown) | none                            |

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