package me.phoenixra.atum.core.database;

import me.phoenixra.atum.core.AtumPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SQLiteDatabase implements Database {

    private final AtumPlugin plugin;
    private final Logger log;
    private Connection connection;
    private final String dbLocation;
    private final String dbName;
    private File file;

    /**
     * SQLite Database.
     * <p></p>
     * Tries to connect to the database using the given parameters
     * after the creation of an instance
     *
     * @param plugin the plugin
     * @param dbName the database name
     * @param dbLocation the database file location
     */
    public SQLiteDatabase(AtumPlugin plugin, String dbName, String dbLocation) {
        this.plugin = plugin;
        this.dbName = dbName;
        this.dbLocation = dbLocation;
        this.log = getPlugin().getLogger();
        if(initialize()) {
            plugin.addTaskOnDisable(this::close);
        }
    }

    private boolean initialize() {
        if (file == null) {
            File dbFolder = new File(dbLocation);

            if (!dbFolder.exists() && !dbFolder.mkdir()) {
                log.severe("Failed to create database folder!");
                return false;
            }

            file = new File(dbFolder.getAbsolutePath() + File.separator + dbName + ".db");
        }

        try {
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
            return true;
        } catch (SQLException ex) {
            log.severe("SQLite exception on initialize " + ex);
        } catch (ClassNotFoundException ex) {
            log.severe("You need the SQLite library " + ex);
        }
        return false;
    }

    @Override
    public Connection getConnection() {
        if (connection == null) {
            initialize();
        }

        return connection;
    }

    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            log.severe("Failed to close database connection! " + e.getMessage());
        }
    }

    @Override
    public @NotNull AtumPlugin getPlugin() {
        return plugin;
    }
}