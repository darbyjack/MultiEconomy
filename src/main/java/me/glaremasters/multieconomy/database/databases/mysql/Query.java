package me.glaremasters.multieconomy.database.databases.mysql;

/**
 * Created by GlareMasters on 5/31/2018.
 */
class Query {

    static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS economy(uuid VARCHAR(36) NOT NULL)";

    static final String ADD_USER = "INSERT INTO economy (uuid) VALUES(?)";

    static final String CREATE_COLUMNS = "IF NOT EXISTS (SELECT * FROM economy = ";

}
