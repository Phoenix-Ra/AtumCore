package me.phoenixra.atum.core.database;

import me.phoenixra.atum.core.AtumPlugin;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MySQLDatabase implements Database {

    private final AtumPlugin plugin;
    private final Logger log;
    private Connection connection;
    private final String host;
    private final String username;
    private final String password;
    private final String database;
    private final int port;

    /**
     * MySQL Database.
     * <p></p>
     * Tries to connect to the database using the given parameters
     * after the creation of an instance
     *
     * @param plugin   the plugin instance
     * @param host     The host
     * @param database The database
     * @param username The username
     * @param password The password
     */
    public MySQLDatabase(AtumPlugin plugin, String host, int port, String database, String username, String password) {
        this.plugin = plugin;
        this.database = database;
        this.port = port;
        this.host = host;
        this.username = username;
        this.password = password;
        this.log = getPlugin().getLogger();
        if(initialize()) {
            plugin.addTaskOnDisable(this::close);
        }
    }

    private boolean initialize() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false", username, password);
            return true;
        } catch (ClassNotFoundException e) {
            log.severe("ClassNotFoundException! " + e.getMessage());
        } catch (SQLException e) {
            log.severe("SQLException! " + e.getMessage());
        }
        return false;
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(0)) {
                initialize();
            }
        } catch (SQLException e) {
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
