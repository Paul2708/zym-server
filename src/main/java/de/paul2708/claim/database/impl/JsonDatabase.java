package de.paul2708.claim.database.impl;

import de.paul2708.claim.database.Database;
import de.paul2708.claim.database.DatabaseException;
import de.paul2708.claim.model.ClaimInformation;
import de.paul2708.claim.util.Pair;
import org.json.simple.DeserializationException;
import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;
import org.json.simple.parser.ParseException;

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
            List<Pair<Integer, Integer>> chunks = new ArrayList<>();

            for (Object otherObject : (JsonArray) jsonObject.get("chunks")) {
                JsonObject jsonChunk = (JsonObject) otherObject;

                chunks.add(new Pair<>(jsonChunk.getInteger("x"), jsonChunk.getInteger("z")));
            }

            ClaimInformation.create(uuid, chunks);
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
    public void updateClaimInformation(UUID uuid, Pair<Integer, Integer> chunk, boolean add) throws DatabaseException {
        ClaimInformation information = ClaimInformation.get(uuid);
        information.updateChunk(chunk, add);

        try (FileWriter writer = new FileWriter(JsonDatabase.PATH)) {
            writer.write(Jsoner.prettyPrint(jsonArray.toJson()));
            writer.flush();
        } catch (IOException e) {
            throw new DatabaseException("Couldn't update the claim information.", e);
        }
    }

    /**
     * Check if a chunk is already claimed.
     *
     * @param chunk chunk
     */
    @Override
    public boolean isClaimed(Pair<Integer, Integer> chunk) {
        for (ClaimInformation information : ClaimInformation.getAll()) {
            if (information.contains(chunk)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Disconnect from the database.
     */
    @Override
    public void disconnect() {
        // Nothing to do here
    }
}
