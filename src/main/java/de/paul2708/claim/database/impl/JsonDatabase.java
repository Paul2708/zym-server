package de.paul2708.claim.database.impl;

import de.paul2708.claim.database.Database;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.model.ChunkData;
import de.paul2708.claim.model.ClaimInformation;
import org.json.simple.DeserializationException;
import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This database is located in a json file in the plugin folder.
 *
 * @author Paul2708
 */
public class JsonDatabase implements Database {

    private static final String PATH = "plugins/Chunk Claim/database.json";

    private JsonArray jsonArray;

    /**
     * Connect to the database.
     */
    @Override
    public void connect() {
        // Nothing to do here
    }

    /**
     * Set up the database connection.
     *
     * @throws DatabaseException if an exception is thrown
     */
    @Override
    public void setUp() throws DatabaseException {
        // Create the file
        Path path = Paths.get(JsonDatabase.PATH);

        if (Files.notExists(path)) {
            if (path.getParent() != null && Files.notExists(path.getParent())) {
                try {
                    Files.createDirectories(path.getParent());
                } catch (IOException e) {
                    throw new DatabaseException("Couldn't create the directory.", e);
                }
            }

            try {
                Files.createFile(path);
                Files.write(path, Jsoner.prettyPrint(new JsonArray().toJson()).getBytes());
            } catch (IOException e) {
                throw new DatabaseException("Couldn't create the file.", e);
            }
        }

        // Parse the file
        try {

            this.jsonArray = (JsonArray) Jsoner.deserialize(new FileReader(JsonDatabase.PATH));
        } catch (IOException | DeserializationException e) {
            throw new DatabaseException("Couldn't parse the file.", e);
        }
    }

    /**
     * Get a list of all claim information.
     */
    @Override
    public void resolveClaimInformation() {
        for (Object object : jsonArray) {
            JsonObject jsonObject = (JsonObject) object;

            UUID uuid = UUID.fromString(jsonObject.getString("uuid"));
            List<ChunkData> chunks = new ArrayList<>();

            for (Object otherObject : (JsonArray) jsonObject.get("chunks")) {
                JsonObject jsonChunk = (JsonObject) otherObject;

                chunks.add(new ChunkData(jsonChunk.getInteger("x"), jsonChunk.getInteger("z")));
            }

            int level = jsonObject.getInteger("level");

            ClaimInformation.create(uuid, chunks, level);
        }
    }

    /**
     * Update the claim information for a player.
     *
     * @param uuid  player uuid
     * @param chunk updated chunk
     * @param add   true if the chunk will be added, otherwise false to remove it
     * @throws DatabaseException if an exception is thrown
     */
    @Override
    public void updateClaimInformation(UUID uuid, ChunkData chunk, boolean add) throws DatabaseException {
        ClaimInformation information = ClaimInformation.get(uuid);
        information.updateChunk(chunk, add);

        this.jsonArray.clear();

        for (ClaimInformation claimInformation : ClaimInformation.getAll()) {
            jsonArray.add(claimInformation.toJson());
        }

        this.save();
    }

    /**
     * Check if a chunk is already claimed.
     *
     * @param chunk chunk
     */
    @Override
    public boolean isClaimed(ChunkData chunk) {
        // TODO: Remove me
        for (ClaimInformation information : ClaimInformation.getAll()) {
            if (information.contains(chunk)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Create a new entry for the uuid.
     *
     * @param uuid player uuid
     * @throws DatabaseException if an exception is thrown
     */
    @Override
    public void create(UUID uuid) throws DatabaseException {
        for (Object object : jsonArray) {
            JsonObject jsonObject = (JsonObject) object;

            if (jsonObject.getString("uuid").equalsIgnoreCase(uuid.toString())) {
                return;
            }
        }

        ClaimInformation.create(uuid, new ArrayList<>(), 0);
        this.jsonArray.add(ClaimInformation.get(uuid).toJson());

        this.save();
    }

    /**
     * Disconnect from the database.
     */
    @Override
    public void disconnect() {
        ClaimInformation.clear();
    }

    /**
     * Save the file.
     *
     * @throws DatabaseException if writing to the file throws any exceptions
     */
    private void save() throws DatabaseException {
        try (FileWriter writer = new FileWriter(JsonDatabase.PATH)) {
            writer.write(Jsoner.prettyPrint(jsonArray.toJson()));
            writer.flush();
        } catch (IOException e) {
            throw new DatabaseException("Couldn't update the claim information.", e);
        }
    }
}
