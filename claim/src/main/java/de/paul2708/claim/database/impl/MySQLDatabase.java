package de.paul2708.claim.database.impl;

import de.paul2708.claim.ClaimPlugin;
import de.paul2708.claim.database.Database;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.database.DatabaseResult;
import de.paul2708.claim.database.impl.mysql.MySQLConnection;
import de.paul2708.claim.model.chunk.ChunkData;
import de.paul2708.claim.model.chunk.ChunkWrapper;
import de.paul2708.claim.model.chunk.CityChunk;
import de.paul2708.claim.model.ClaimProfile;
import de.paul2708.claim.model.ProfileManager;
import org.bukkit.Bukkit;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.SQLException;
import java.util.UUID;

/**
 * This database is based on a local mysql server.
 *
 * @author Paul2708
 */
public class MySQLDatabase implements Database {

    private MySQLConnection connection;

    /**
     * Create a new mysql database with a connection.
     *
     * @param connection connection
     */
    public MySQLDatabase(MySQLConnection connection) {
        this.connection = connection;
    }

    /**
     * Connect to the database.
     *
     * @throws DatabaseException if an exception is thrown
     */
    @Override
    public void connect() throws DatabaseException {
        try {
            connection.connect();
        } catch (Exception e) {
            throw new DatabaseException("Couldn't connect to the database.", e);
        }
    }

    /**
     * Set up the database connection.
     *
     * @throws DatabaseException if an exception is thrown
     */
    @Override
    public void setUp() throws DatabaseException {
        // Nothing to do here
    }

    /**
     * Resolve all relevant information from the database.
     *
     * @throws DatabaseException if an exception is thrown
     */
    @Override
    public void resolve() throws DatabaseException {
        try {
            // Get player information
            connection.query(resultSet -> {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    UUID uuid = fromBytes(resultSet.getBytes("uuid"));
                    int claimer = resultSet.getInt("claimer");

                    if (uuid.equals(CityChunk.OWNER)) {
                        continue;
                    }

                    ClaimProfile claimProfile = new ClaimProfile(uuid, claimer);
                    claimProfile.setId(id);

                    ProfileManager.getInstance().addProfile(claimProfile);
                }
            }, "SELECT * FROM players");

            // Get player chunks
            connection.query(resultSet -> {
                while (resultSet.next()) {
                    UUID uuid = fromBytes(resultSet.getBytes("uuid"));

                    if (uuid.equals(CityChunk.OWNER)) {
                        continue;
                    }

                    ClaimProfile profile = ProfileManager.getInstance().getProfile(uuid);

                    ChunkData chunkData = new ChunkData(resultSet.getString("world"), resultSet.getInt("x"),
                            resultSet.getInt("z"), resultSet.getBoolean("group_chunk"));
                    chunkData.setId(resultSet.getInt("id"));

                    profile.addClaimedChunk(chunkData);
                }
            }, "SELECT * FROM players, chunks WHERE chunks.owner = players.id");

            // TODO: Add access

            // Get city chunks
            connection.query(resultSet -> {
                try {
                    while (resultSet.next()) {
                        ChunkData chunkData = new ChunkData(resultSet.getString("world"),
                                resultSet.getInt("x"), resultSet.getInt("z"), false);
                        chunkData.setId(resultSet.getInt("chunks.id"));
                        CityChunk cityChunk = new CityChunk(chunkData, resultSet.getBoolean("public"));
                        cityChunk.setId(resultSet.getInt("city_chunks.id"));

                        ProfileManager.getInstance().addCityChunk(cityChunk);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }, "SELECT * FROM city_chunks, chunks WHERE city_chunks.chunk = chunks.id");
        } catch (SQLException e) {
            throw new DatabaseException("Couldn't resolve chunk information.", e);
        }
    }

    /**
     * Create a new entry for the uuid.
     *
     * @param uuid   player uuid
     * @param result database result
     */
    @Override
    public void create(UUID uuid, DatabaseResult<Integer> result) {
        runAsync(() -> {
            try {
                connection.execute(result::success,
                        "INSERT INTO players (`uuid`, `claimer`) VALUES (?, ?)", toBytes(uuid), 0);
            } catch (SQLException e) {
                result.exception(new DatabaseException("Couldn't create a database entry for " + uuid + ".", e));
            }
        });
    }

    /**
     * Add a claimed chunk.
     *
     * @param playerId   player id
     * @param chunk      claimed chunk
     * @param groupChunk true if the chunk is a group chunk, otherwise false
     * @param result     database result (inserted chunk id)
     */
    @Override
    public void addClaimedChunk(int playerId, ChunkWrapper chunk, boolean groupChunk, DatabaseResult<Integer> result) {
        runAsync(() -> {
            try {
                connection.execute(result::success,
                        "INSERT INTO chunks (`world`, `x`, `z`, `owner`, `group_chunk`) VALUES (?, ?, ?, ?, ?)",
                        chunk.getWorld(), chunk.getX(), chunk.getZ(), playerId, groupChunk ? 1 : 0);
            } catch (SQLException e) {
                result.exception(new DatabaseException("Couldn't claim chunk for " + playerId + ".", e));
            }
        });
    }

    /**
     * Remove a claimed chunk by its id.
     *
     * @param id     chunk id
     * @param result database result
     */
    @Override
    public void removeChunk(int id, DatabaseResult<Void> result) {
        runAsync(() -> {
            try {
                connection.execute("DELETE FROM access WHERE chunk = ?", id);
                connection.execute("DELETE FROM city_chunks WHERE chunk = ?", id);
                connection.execute("DELETE FROM chunks WHERE id = ?", id);
                result.success(null);
            } catch (SQLException e) {
                result.exception(new DatabaseException("Couldn't remove chunk with ID=" + id + ".", e));
            }
        });
    }

    /**
     * Set the amount of bought claimers.
     *
     * @param playerId player id
     * @param amount   new amount of bought claimers
     * @param result   database result
     */
    @Override
    public void setClaimer(int playerId, int amount, DatabaseResult<Void> result) {
        runAsync(() -> {
            try {
                connection.execute("UPDATE players SET claimer = ? WHERE id = ?",
                        amount, playerId);
                result.success(null);
            } catch (SQLException e) {
                result.exception(new DatabaseException("Couldn't update claimer for player " + playerId + ".", e));
            }
        });
    }

    /**
     * Add a city chunk.
     *
     * @param playerId     player id
     * @param chunk        updated chunk
     * @param interactable true if all players will be able to interact in it, otherwise false
     * @param result       database result (inserted city chunk id)
     */
    @Override
    public void addCityChunk(int playerId, ChunkWrapper chunk, boolean interactable, DatabaseResult<Integer> result) {
        addClaimedChunk(playerId, chunk, false, new DatabaseResult<Integer>() {

            @Override
            public void success(Integer id) {
                runAsync(() -> {
                    try {
                        connection.execute(result::success, "INSERT INTO city_chunks (`chunk`, `public`) VALUES (?, ?)",
                                id, interactable ? 1 : 0);
                    } catch (SQLException e) {
                        result.exception(new DatabaseException("Couldn't add city chunk.", e));
                    }
                });
            }

            @Override
            public void exception(DatabaseException exception) {
                result.exception(exception);
            }
        });
    }

    /**
     * Update a city chunk.
     *
     * @param id           city chunk id
     * @param interactable true if all players will be able to interact in it, otherwise false
     * @param result       database result
     */
    @Override
    public void updateCityChunk(int id, boolean interactable, DatabaseResult<Void> result) {
        runAsync(() -> {
            try {
                connection.execute("UPDATE city_chunks SET public = ? WHERE id = ?",
                        interactable ? 1 : 0, id);
                result.success(null);
            } catch (SQLException e) {
                result.exception(new DatabaseException("Couldn't update city chunk.", e));
            }
        });
    }

    /**
     * Permit a target to interact on your group chunk.
     *
     * @param playerId target id
     * @param chunkId  chunk id
     * @param result   database result
     */
    @Override
    public void addAccess(int playerId, int chunkId, DatabaseResult<Integer> result) {
        runAsync(() -> {
            try {
                connection.execute(result::success,
                        "INSERT INTO access (`player`, `chunk`) VALUES (?, ?)", playerId, chunkId);
            } catch (SQLException e) {
                result.exception(new DatabaseException("Couldn't add access for " + playerId + ".", e));
            }
        });
    }

    /**
     * Remove an access.
     *
     * @param id     access id
     * @param result database result
     */
    @Override
    public void removeAccess(int id, DatabaseResult<Void> result) {
        runAsync(() -> {
            try {
                connection.execute("DELETE FROM access WHERE id = ?", id);
                result.success(null);
            } catch (SQLException e) {
                result.exception(new DatabaseException("Couldn't remove access with ID=" + id + ".", e));
            }
        });
    }

    /**
     * Disconnect from the database.
     *
     * @throws DatabaseException if an exception is thrown
     */
    @Override
    public void disconnect() throws DatabaseException {
        connection.close();
    }

    /**
     * Run a task async.
     *
     * @param runnable runnable
     */
    private void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(ClaimPlugin.getInstance(), runnable);
    }

    /**
     * Create a byte array by uuid.
     *
     * @param uuid uuid
     * @return byte array
     */
    private byte[] toBytes(UUID uuid) {
        byte[] bytes = new byte[16];

        ByteBuffer.wrap(bytes)
                .order(ByteOrder.BIG_ENDIAN)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits());

        return bytes;
    }

    /**
     * Get an uuid from bytes.
     *
     * @param bytes array of bytes
     * @return uuid
     */
    private UUID fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        return new UUID(buffer.getLong(), buffer.getLong());
    }
}
