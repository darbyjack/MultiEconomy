package me.glaremasters.multieconomy.database.databases.yml;

import java.io.IOException;
import me.glaremasters.multieconomy.MultiEconomy;
import me.glaremasters.multieconomy.database.DatabaseProvider;
import org.bukkit.entity.Player;

/**
 * Created by GlareMasters on 5/31/2018.
 */
public class YML implements DatabaseProvider {

    @Override
    public void initialize() {
        System.out.println("Loading MultiEconomy storage file...");
    }

    @Override
    public void addUser(Player player) {
        MultiEconomy.getI().dataFileConfig.set(player.getUniqueId().toString(), "Test");
        try {
            MultiEconomy.getI().dataFileConfig.save(MultiEconomy.getI().dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
