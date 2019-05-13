package de.paul2708.transfer;

import de.paul2708.transfer.mysql.MySQLConnection;
import org.json.simple.DeserializationException;
import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This main class holds the program entry.
 *
 * @author Paul2708
 */
@Deprecated
public final class Main {

    private static final String WORLD = "NewWorld";

    /**
     * Nothing to call here.
     */
    private Main() {
        throw new IllegalAccessError();
    }

    /**
     * Transfer the json entries to the mysql database.
     *
     * @param args 6 arguments: json database path, mysql host, mysql port, user, password and database
     */
    public static void main(String[] args) {
        if (args.length != 6) {
            System.err.println("Invalid parameters.");
            System.exit(-1);
        } else {
            // Check file
            Path path = Paths.get(args[0]);

            if (Files.notExists(path)) {
                System.err.println("File doesn't exist.");
                System.exit(-1);
            }

            // Parse the file
            JsonArray jsonArray = new JsonArray();

            try {
                jsonArray = (JsonArray) Jsoner.deserialize(new FileReader(args[0]));
            } catch (IOException | DeserializationException e) {
                System.err.println("Couldn't parse the file.");
                System.exit(-1);
            }

            // Connect to mysql database
            MySQLConnection connection = new MySQLConnection(args[1], Integer.parseInt(args[2]), args[3], args[4],
                    args[5]);

            connection.connect();

            // Insert data
            for (Object object : jsonArray) {
                JsonObject jsonObject = (JsonObject) object;

                UUID uuid = UUID.fromString(jsonObject.getString("uuid"));
                int claimer = jsonObject.getInteger("level");

                // Insert player data
                byte[] uuidBytes = new byte[16];
                ByteBuffer.wrap(uuidBytes)
                        .order(ByteOrder.BIG_ENDIAN)
                        .putLong(uuid.getMostSignificantBits())
                        .putLong(uuid.getLeastSignificantBits());

                try {
                    connection.execute("INSERT INTO `players` (`uuid`, `claimer`) VALUES (?, ?)",
                            uuidBytes, claimer);

                    System.out.println("Inserted player data for " + uuid.toString());
                } catch (SQLException e) {
                    System.err.println("Error while inserting player with uuid " + uuid.toString() + ".");
                    e.printStackTrace();
                }

                AtomicInteger integer = new AtomicInteger();

                try {
                    connection.query(resultSet -> {

                        try {
                            if (resultSet.first()) {
                                integer.set(resultSet.getInt("id"));
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }, "SELECT `id` FROM `players` WHERE `uuid` = ?", uuidBytes);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // Insert chunk data
                for (Object otherObject : (JsonArray) jsonObject.get("chunks")) {
                    JsonObject jsonChunk = (JsonObject) otherObject;

                    int x = jsonChunk.getInteger("x");
                    int z = jsonChunk.getInteger("z");

                    try {
                        connection.execute(
                                "INSERT INTO `chunks` (`world`, `x`, `z`, `owner`, `group_chunk`) VALUES (?, ?, ?, ?, ?)",
                                Main.WORLD, x, z, integer.get(), 0);
                        System.out.println("Inserted chunk data");
                    } catch (SQLException e) {
                        System.err.println("Error while inserting chunk data.");
                        e.printStackTrace();
                    }
                }
            }

            connection.close();
        }
    }
}
