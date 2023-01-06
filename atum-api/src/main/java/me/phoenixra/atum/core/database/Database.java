package me.phoenixra.atum.core.database;

import me.phoenixra.atum.core.AtumPlugin;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public interface Database {

    /**
     * Get database connection
     *
     * @return The connection
     */
    Connection getConnection();

    /**
     * check connection
     *
     * @return false if connection is null
     */
    default boolean checkConnection() {
        return getConnection() != null;
    }

    /**
     * close the connection
     *
     */
    void close();

    /**
     * execute the query and returns its result set
     *
     * @param query the query
     * @return result set
     */
    default @Nullable ResultSet select(String query) {
        try {
            return getConnection().createStatement().executeQuery(query);
        } catch (SQLException ex) {
            getPlugin().getLogger().log(Level.SEVERE, String.format("Error executing query: %s", query), ex);
        }
        return null;
    }

    /**
     * executes the query
     *
     * @param query the query
     * @return false if failed
     */
    default boolean execute(String query) {
        try {
            getConnection().createStatement().execute(query);
            return true;
        } catch (SQLException ex) {
            getPlugin().getLogger().log(Level.SEVERE, String.format("Error executing query: %s", query), ex);
            return false;
        }
    }

    /**
     * Does the table exists?
     *
     * @param table the table
     * @return false if not exists
     */
    default boolean existsTable(String table) {
        try {
            ResultSet tables = getConnection().getMetaData().getTables(null, null, table, null);
            return tables.next();
        } catch (SQLException ex) {
            getPlugin().getLogger().log(Level.SEVERE, String.format("Error checking if table %s exists", table), ex);
            return false;
        }
    }

    /**
     * Does the column exists?
     *
     * @param table the table
     * @param column the column
     * @return false if not exists
     */
    default boolean existsColumn(String table, String column) {
        try {
            ResultSet col = getConnection().getMetaData().getColumns(null, null, table, column);
            return col.next();
        } catch (Exception ex) {
            getPlugin().getLogger().log(Level.SEVERE, String.format("Error checking if column %s exists in table %s", column, table), ex);
            return false;
        }
    }
    /**
     * execute an update
     *
     * @param query the query
     * @param async run in asynchronous thread?
     */
    default void executeUpdate(String query, boolean async) {
        final Exception exception = new Exception(); // Stores a reference to the caller's stack trace for async tasks
        Runnable executeUpdate = () -> {
            if (getConnection() != null) {
                try {
                    getConnection().createStatement().executeUpdate(query);
                } catch (SQLException ex) {
                    getPlugin().getLogger().log(Level.SEVERE, String.format("Error executing query: %s", query), ex);
                    if (!Bukkit.isPrimaryThread()) {
                        getPlugin().getLogger().log(Level.SEVERE, "Caller's stack trace:", exception);
                    }
                }
            }
        };
        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), executeUpdate);
        } else {
            executeUpdate.run();
        }
    }


    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    AtumPlugin getPlugin();
}
