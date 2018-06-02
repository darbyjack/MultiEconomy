package me.glaremasters.multieconomy.database.databases.mysql;

import me.glaremasters.multieconomy.MultiEconomy;

/**
 * Created by GlareMasters on 5/31/2018.
 */
class Query {

    static final String prefix = MultiEconomy.getI().getConfig().getString("database.prefix");

    static final String CREATE_TABLE_PLAYERS = "CREATE TABLE IF NOT EXISTS `" + prefix + "players` (\n"
            + "    `id` int  NOT NULL AUTO_INCREMENT ,\n"
            + "    `UUID` varchar(36)  NOT NULL ,\n"
            + "    PRIMARY KEY (`id`),\n"
            + "    UNIQUE (`UUID`)\n"
            + ");\n";

    static final String CREATE_TABLE_ECONOMY = "CREATE TABLE IF NOT EXISTS `" + prefix + "economy` (\n"
            + "    `id` int  NOT NULL AUTO_INCREMENT ,\n"
            + "    `eco_name` varchar(32)  NOT NULL ,\n"
            + "    PRIMARY KEY (`id`), \n"
            + "    UNIQUE (`eco_name`)\n"
            + ");";

    static final String CREATE_TABLE_BALANCE = "CREATE TABLE IF NOT EXISTS `" + prefix + "balance` (\n"
            + "    `id` int  NOT NULL AUTO_INCREMENT,\n"
            + "    `balance` double NOT NULL ,\n"
            + "    `player_id` int  NOT NULL ,\n"
            + "    `eco_id` int  NOT NULL ,\n"
            + "    PRIMARY KEY (`id`)\n"
            + ");";


    static final String ADD_USER = "INSERT IGNORE INTO " + prefix + "players (UUID) VALUES(?)";

    static final String GET_USER = "SELECT id FROM " + prefix + "players WHERE UUID=?";

    static final String ADD_INITIAL_AMOUNTS = "INSERT INTO `" + prefix + "balance (balance, player_id, eco_id) VALUES(?, ?, ?)";

    static final String GET_ECO_ID = "SELECT id FROM " + prefix + "economy WHERE eco_name=?";

    static final String ADD_ECO_TYPES = "INSERT IGNORE INTO `" + prefix + "economy` (eco_name) VALUES(?)";

}
