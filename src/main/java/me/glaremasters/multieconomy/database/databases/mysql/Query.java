package me.glaremasters.multieconomy.database.databases.mysql;

import me.glaremasters.multieconomy.MultiEconomy;

/**
 * Created by GlareMasters on 5/31/2018.
 */
class Query {

    static final String prefix = MultiEconomy.getI().getConfig().getString("database.prefix");

    static final String CREATE_TABLE_PLAYERS = "CREATE TABLE IF NOT EXIST `" + prefix + "Players` (\n"
            + "    `id` int  NOT NULL AUTO_INCREMENT ,\n"
            + "    `UUID` varchar(36)  NOT NULL ,\n"
            + "    PRIMARY KEY (`id`)\n"
            + ");\n";

    static final String CREATE_TABLE_ECONOMY = "CREATE TABLE IF NOT EXIST `" + prefix + "Economy` (\n"
            + "    `id` int  NOT NULL AUTO_INCREMENT ,\n"
            + "    `eco_name` varchar(32)  NOT NULL ,\n"
            + "    PRIMARY KEY (`id`)\n"
            + ");";

    static final String CREATE_TABLE_BALANCE = "CREATE TABLE IF NOT EXIST `" + prefix + "Balance` (\n"
            + "    `id` int  NOT NULL AUTO_INCREMENT,\n"
            + "    `balance` double NOT NULL ,\n"
            + "    `player_id` int  NOT NULL ,\n"
            + "    `eco_id` int  NOT NULL ,\n"
            + "    PRIMARY KEY (`id`)\n"
            + ");";

    static final String ADD_USER = "INSERT INTO economy (uuid) VALUES(?)";

    static final String CHECK_COLUMN = "ALTER TABLE economy ADD ? INT";

}
