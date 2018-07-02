package me.glaremasters.multieconomy.database.databases.mysql;

import com.sun.rowset.CachedRowSetImpl;
import com.zaxxer.hikari.HikariDataSource;
import me.glaremasters.multieconomy.MultiEconomy;
import me.glaremasters.multieconomy.database.DatabaseProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by GlareMasters on 5/31/2018.
 */
public class MySQL implements DatabaseProvider {

    private HikariDataSource hikari;

    @Override
    public void initialize() {
        ConfigurationSection databaseSection = MultiEconomy.getI().getConfig()
                .getConfigurationSection("database");
        if (databaseSection == null) {
            throw new IllegalStateException("MySQL not configured correctly. Cannot continue.");
        }

        hikari = new HikariDataSource();
        hikari.setMaximumPoolSize(databaseSection.getInt("pool-size"));

        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

        hikari.addDataSourceProperty("serverName", databaseSection.getString("host"));
        hikari.addDataSourceProperty("port", databaseSection.getInt("port"));
        hikari.addDataSourceProperty("databaseName", databaseSection.getString("database"));

        hikari.addDataSourceProperty("user", databaseSection.getString("username"));
        hikari.addDataSourceProperty("password", databaseSection.getString("password"));

        hikari.addDataSourceProperty("characterEncoding", "utf8");
        hikari.addDataSourceProperty("useUnicode", "true");

        hikari.validate();

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                executeUpdate(Query.CREATE_TABLE_PLAYERS);
                executeUpdate(Query.CREATE_TABLE_BALANCE);
                executeUpdate(Query.CREATE_TABLE_ECONOMY);

                // adds the different eco's to the database
                for (String type : MultiEconomy.getI().getConfig().getStringList("economy-types")) {
                    executeUpdate(Query.ADD_ECO_TYPES, type);
                }
            }
        };
        runnable.runTaskAsynchronously(MultiEconomy.getI());
    }

    /**
     * Runs from the user join event to add the user if doesn't exist
     * @param player gets the player
     */
    @Override
    public void addUser(Player player) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                executeUpdate(Query.ADD_USER, player.getUniqueId().toString());
                int userId = getUserId(player.getUniqueId().toString());
                if (userId == 0) return;

                // does not work yet, trouble with duplicated amounts, can't make any of the values unique, so it needs more checks
                for (String type : MultiEconomy.getI().getConfig().getStringList("economy-types")) {
                    executeUpdate(Query.ADD_INITIAL_AMOUNTS, Integer.parseInt(MultiEconomy.getI().getConfig().getString(type + ".start_amount")), userId, getEcoId(type));
                }
            }
        };
        runnable.runTaskAsynchronously(MultiEconomy.getI());
    }

    //not sure if you want public or private
    private int getUserId(String UUID) {
        try {
            ResultSet rs = getResultSet(Query.GET_USER, UUID);
            while (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getEcoId(String ecoName) {
        try {
            ResultSet rs = getResultSet(Query.GET_ECO_ID, ecoName);
            while (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void executeUpdate(String query, Object... parameters) {
        Connection connection = null;

        PreparedStatement statement = null;

        try {
            connection = hikari.getConnection();


            statement = connection
                    .prepareStatement(query);

            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    statement.setObject(i + 1, parameters[i]);
                }
            }

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ResultSet getResultSet(String query, Object... parameters) {
        Connection connection = null;

        PreparedStatement statement = null;

        try {
            connection = hikari.getConnection();

            statement = connection
                    .prepareStatement(query);

            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    statement.setObject(i + 1, parameters[i]);
                }
            }

            CachedRowSet resultCached = new CachedRowSetImpl();
            ResultSet resultSet = statement.executeQuery();

            resultCached.populate(resultSet);
            resultSet.close();

            return resultCached;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @SuppressWarnings ("Duplicates")
    private void close(Connection connection, PreparedStatement statement) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }
}
