package me.glaremasters.multieconomy.database.databases.mysql;

import me.glaremasters.multieconomy.MultiEconomy;

/**
 * Created by GlareMasters on 5/31/2018.
 */
class Query {

    static final String prefix = MultiEconomy.getI().getConfig().getString("database.prefix");

    static final String CREATE_TABLE_ECONOMY = "CREATE TABLE IF NOT EXISTS `" + prefix + "economy` (\n"
            + "    `id` int  NOT NULL AUTO_INCREMENT ,\n"
            + "    `eco_name` varchar(32)  NOT NULL ,\n"
            + "    PRIMARY KEY (`id`), \n"
            + "    UNIQUE (`eco_name`)\n"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    static final String CREATE_TABLE_BALANCE = "CREATE TABLE IF NOT EXISTS `" + prefix + "balance` (\n"
            + "    `id` int  NOT NULL AUTO_INCREMENT,\n"
            + "    `balance` double NOT NULL ,\n"
            + "    `uuid` varchar(36) NOT NULL ,\n"
            + "    `eco_id` int  NOT NULL ,\n"
            + "    PRIMARY KEY (`id`)\n"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    static final String ADD_INITIAL_AMOUNTS = "INSERT IGNORE INTO `" + prefix + "balance` (`balance`, `uuid`, `eco_id`) VALUES(?, ?, ?)";

    static final String GET_ECO_ID = "SELECT id FROM " + prefix + "economy WHERE eco_name=?";

    static final String ADD_ECO_TYPES = "INSERT IGNORE INTO `" + prefix + "economy` (eco_name) VALUES(?)";

    static final String HAS_BALANCE = "SELECT COUNT(`id`) count FROM `" + prefix + "balance` WHERE `uuid` = ? AND `eco_id` = ?";
}
