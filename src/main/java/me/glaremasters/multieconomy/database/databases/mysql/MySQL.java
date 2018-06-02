package me.glaremasters.multieconomy.database.databases.mysql;

import com.sun.rowset.CachedRowSetImpl;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.sql.rowset.CachedRowSet;
import me.glaremasters.multieconomy.MultiEconomy;
import me.glaremasters.multieconomy.database.DatabaseProvider;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * Created by GlareMasters on 5/31/2018.
 */
public class MySQL implements DatabaseProvider {

    private HikariDataSource hikari;

    private HashMap<String, String> player_id = new HashMap<>();
    private HashMap<String, String> eco_id = new HashMap<>();

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

        MultiEconomy.newChain()
                .async(() -> execute(Query.CREATE_TABLE_PLAYERS))
                .async(() -> execute(Query.CREATE_TABLE_BALANCE))
                .async(() -> execute(Query.CREATE_TABLE_ECONOMY))
                .sync(() -> System.out.println("Tables created"))
                .execute((exception, task) -> {
            if (exception != null) exception.printStackTrace();
        });

        Bukkit.getServer().getScheduler().runTaskLater(MultiEconomy.getI(), () -> {
            for (String type : MultiEconomy.getI().getConfig().getStringList("economy-types")) {
                MultiEconomy.newChain()
                        .async(() -> execute(Query.ADD_ECO_TYPES, type))
                        .execute((exception, task) -> {
                            if (exception != null) exception.printStackTrace();
                        });
            }
        }, 20);
    }

    @Override
    public void addUser(Player player) {
        MultiEconomy.newChain()
                .async(() -> execute(Query.ADD_USER, player.getUniqueId().toString()))
                .execute((exception, task) -> {
                    if (exception != null) {
                        exception.printStackTrace();
                    }
                });
        Bukkit.getServer().getScheduler().runTaskLater(MultiEconomy.getI(),
                () -> MultiEconomy.newChain().async(() -> {
                    try (ResultSet res = executeQuery(Query.GET_USER, player.getUniqueId().toString())) {
                        assert res != null;
                        if (res.next()) {
                            player_id.put(player.getUniqueId().toString(), res.getString(1));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }).execute((ex, task) -> {
                    if (ex != null) {
                        ex.printStackTrace();
                    }
                }), 20);
        Bukkit.getServer().getScheduler().runTaskLater(MultiEconomy.getI(), new Runnable() {
            @Override
            public void run() {
            }
        }, 20);

    }

    private void execute(String query, Object... parameters) {

        try (Connection connection = hikari
                .getConnection(); PreparedStatement statement = connection
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

    private ResultSet executeQuery(String query, Object... parameters) {
        try (Connection connection = hikari
                .getConnection(); PreparedStatement statement = connection
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
