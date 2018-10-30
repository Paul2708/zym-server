# Chunk - Claim
---
This spigot plugin provides a simple claim and plot system, to claim certain chunks. They have a small griefing protection to avoid abusing.

## Features
- simple command usage (`/claim` and `/claim [setitem|remove]`)
- set a custom item as price per chunk
- local data storage (json)
- small griefing protection

## Usage
1. Put the jar into your plugin folder and reload the server.
2. Set the item by `/claim setitem [amount]` and reload the server again.
3. Enjoy!

## Protection
### Global protection
- canceled any explosion

### Claimed chunk protection
- cancel building and breaking blocks
- cancel water flowing into other claimed chunks
- cancel piston bugs by moving blocks into other chunks
- cancel trees growing into other chunks
- cancel damage to entities (expect players) in other claimed chunks
- cancel armor stand manipulation in other claimed chunks
- cancel interacting on other chunks

## Contact
If you find any issues, please let me know! Also if you are using the plugin.

Paul2708 - [BitBucket](https://bitbucket.org/Paul2708/) [Twitter](https://twitter.com/theplayerpaul) Discord: Paul2708#1098 [Mail](mailto:playerpaul2708@gmx.de)