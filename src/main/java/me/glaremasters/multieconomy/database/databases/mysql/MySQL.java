package me.glaremasters.multieconomy.database.databases.mysql;

import com.sun.rowset.CachedRowSetImpl;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.HashMap;
import javax.sql.rowset.CachedRowSet;
import javax.xml.transform.Result;

import me.glaremasters.multieconomy.MultiEconomy;
import me.glaremasters.multieconomy.database.DatabaseProvider;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by GlareMasters on 5/31/2018.
 */
public class MySQL implements DatabaseProvider {

    private Connection connection;
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    @Override
    public void initialize() {

        ConfigurationSection databaseSection = MultiEconomy.getI().getConfig()
                .getConfigurationSection("database");
        if (databaseSection == null) {
            throw new IllegalStateException("MySQL not configured correctly. Cannot continue.");
        }

        this.host = databaseSection.getString("host");
        this.port = databaseSection.getInt("port");
        this.database = databaseSection.getString("database");

        this.username = databaseSection.getString("username");
        this.password = databaseSection.getString("password");

        try {
            synchronized (this) {
                if (connection != null && !connection.isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(
                        "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username,
                        this.password);
                BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        executeUpdate(Query.CREATE_TABLE_PLAYERS);
                        executeUpdate(Query.CREATE_TABLE_BALANCE);
                        executeUpdate(Query.CREATE_TABLE_ECONOMY);

                        for (String type : MultiEconomy.getI().getConfig().getStringList("economy-types")) {
                            executeUpdate(Query.ADD_ECO_TYPES, type);
                        }
                    }
                };
                runnable.runTaskAsynchronously(MultiEconomy.getI());

            }
        } catch (Exception e) {
            // connection error
        }

    }

    @Override
    public void addUser(Player player) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                executeUpdate(Query.ADD_USER, player.getUniqueId().toString());
                player.sendMessage(""+getUserId(player.getUniqueId().toString()));
            }
        };
        runnable.runTaskAsynchronously(MultiEconomy.getI());
    }

    //not sure if you want public or private
    public int getUserId(String UUID) {
        try {
            ResultSet rs = getResultSet(Query.GET_USER, UUID);
            while (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            //handle
        }
        return 0;
    }

    private void executeUpdate(String query, Object... parameters) {
        try (PreparedStatement statement = connection
                .prepareStatement(query)) {

            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    statement.setObject(i + 1, parameters[i]);
                }
            }

            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private ResultSet getResultSet(String query, Object... parameters) {
        try (PreparedStatement statement = connection
                .prepareStatement(query)) {
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
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("Duplicates")
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
