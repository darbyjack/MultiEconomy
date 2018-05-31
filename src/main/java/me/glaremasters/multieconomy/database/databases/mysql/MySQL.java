package me.glaremasters.multieconomy.database.databases.mysql;

import com.sun.rowset.CachedRowSetImpl;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;
import me.glaremasters.multieconomy.MultiEconomy;
import me.glaremasters.multieconomy.database.DatabaseProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

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

        MultiEconomy.newChain().async(() -> execute(Query.CREATE_TABLE)).sync(() -> System.out
                .println("Tables created")).execute((exception, task) -> {
            if (exception != null)
                exception.printStackTrace();
        });


    }

    @Override
    public void addUser(Player player) {
        MultiEconomy.newChain().async(() -> execute(Query.ADD_USER, player.getUniqueId().toString())).execute((exception, task) -> {
            if (exception != null) exception.printStackTrace();
        });
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