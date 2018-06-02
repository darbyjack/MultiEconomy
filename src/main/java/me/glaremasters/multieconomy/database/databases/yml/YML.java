package me.glaremasters.multieconomy.database.databases.yml;

import static me.glaremasters.multieconomy.api.API.setAmount;
import java.io.IOException;
import me.glaremasters.multieconomy.MultiEconomy;
import me.glaremasters.multieconomy.database.DatabaseProvider;
import org.bukkit.configuration.file.FileConfiguration;
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
        FileConfiguration dC = MultiEconomy.getI().dataFileConfig;
        FileConfiguration c = MultiEconomy.getI().getConfig();
        String UUID = player.getUniqueId().toString();
        for (String type : c.getStringList("economy-types")) {
            if (dC.get(UUID + "." + type) == null) {
                setAmount(UUID, type, c.getInt(type + ".start_amount"));
            }
        }
        try {
            MultiEconomy.getI().dataFileConfig.save(MultiEconomy.getI().dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
