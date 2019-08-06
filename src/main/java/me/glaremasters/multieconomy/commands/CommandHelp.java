package me.glaremasters.multieconomy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import me.glaremasters.multieconomy.util.Constants;

@CommandAlias(Constants.ROOT_ALIAS)
public class CommandHelp extends BaseCommand {

    @HelpCommand
    @CommandPermission("me.help")
    @Description("")
    public void execute(co.aikar.commands.CommandHelp help) {
        help.showHelp();
    }

}
