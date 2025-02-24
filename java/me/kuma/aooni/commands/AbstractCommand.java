package me.kuma.aooni.commands;

import me.kuma.aooni.Aooni;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;

public abstract class AbstractCommand implements CommandExecutor {

    public AbstractCommand(Aooni plugin) {
        PluginCommand c = plugin.getCommand(this.getCommandName());
        c.setExecutor(this.getInstance());
    }

    abstract AbstractCommand getInstance();

    public abstract String getCommandName();
}
