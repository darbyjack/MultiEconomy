package me.glaremasters.multieconomy.database;

import org.bukkit.entity.Player;

/**
 * Created by GlareMasters on 5/31/2018.
 */
public interface DatabaseProvider {

    void initialize();

    void addUser(Player player);


}
