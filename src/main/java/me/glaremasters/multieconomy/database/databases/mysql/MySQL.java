package me.glaremasters.multieconomy.database.databases.mysql;

import com.sun.rowset.CachedRowSetImpl;
import com.zaxxer.hikari.HikariDataSource;
import me.glaremasters.multieconomy.MultiEconomy;
import me.glaremasters.multieconomy.database.DatabaseProvider;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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

        new Thread(() -> {
            executeUpdate(Query.CREATE_TABLE_BALANCE);
            executeUpdate(Query.CREATE_TABLE_ECONOMY);

            // adds the different eco's to the database
            for (String type : MultiEconomy.getI().getConfig().getStringList("economy-types")) {
                executeUpdate(Query.ADD_ECO_TYPES, type);
            }
        }).start();
    }

    @Override
    public void addUser(Player player) {
        new Thread(() -> {
            for (String type : MultiEconomy.getI().getConfig().getStringList("economy-types")) {
                int ecoId = getEcoId(type);
                if (!hasBalance(player.getUniqueId().toString(), ecoId))
                    executeUpdate(Query.ADD_INITIAL_AMOUNTS, Integer.parseInt(MultiEconomy.getI().getConfig().getString(type + ".start_amount")), player.getUniqueId().toString(), getEcoId(type));
            }
        }).start();
    }

    /**
     * Gets the balance from the database based on the economy
     * @param uuid the uuid of the player
     * @param eco the economy name
     * @return returns double value with the balance
     */
    public double getBalance(String uuid, String eco) {
        try {
            ResultSet rs = getResultSet(Query.GET_BALANCE, uuid, getEcoId(eco));
            while (rs.next()) {
                return rs.getInt("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     *  Gets all the balances the player has
     * @param uuid the uuid of the player
     * @return A hashmap with the balance and the economy name for that balance
     */
    public HashMap<String, Double> getBalances(String uuid) {
        HashMap<String, Double> balances = new HashMap<String, Double>();
        try {
            ResultSet rs = getResultSet(Query.GET_BALANCE, uuid);
            while (rs.next()) {
                balances.put(getEcoName(rs.getInt("eco_id")), rs.getDouble("balance"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balances;
    }

    /**
     * Sets the balance for the player
     * @param uuid the uuid of the player
     * @param eco the economy name
     * @param balance the new balance to be set
     */
    public void setBalance(String uuid, String eco, double balance) {
        new Thread(() -> {
            executeUpdate(Query.SET_BALANCE, balance, uuid, getEcoId(eco));
        }).start();
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

    private String getEcoName(int ecoId) {
        try {
            ResultSet rs = getResultSet(Query.GET_ECO_NAME, ecoId);
            while (rs.next()) {
                return rs.getString("eco_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean hasBalance(String uuid, int ecoId) {
        try {
            ResultSet rs = getResultSet(Query.HAS_BALANCE, uuid, ecoId);
            while (rs.next()) {
                if (rs.getInt("count") == 1) return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
            close(connection, statement);
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
            close(connection, statement);
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
