package me.kuma.aooni.gameUtils.listeners.commands;

import me.kuma.aooni.Aooni;
import me.kuma.aooni.gameUtils.listeners.mains.AooniManager;
import org.bukkit.Bukkit;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RevivalCommand extends AbstractCommand {

    public RevivalCommand(Aooni plugin) {
        super(plugin);
    }

    @Override
    AbstractCommand getInstance() {
        return this;
    }

    @Override
    public String getCommandName() {
        return "aoonirevival";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        AooniManager manager = Aooni.getManager();
        if (!(sender instanceof CommandBlock)) return true;
        if (args.length != 1) return true;
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) return true;
        if (!manager.gameStatus.equalsIgnoreCase("onGame")) return true;
        if (manager.aooniteam.hasEntry(player.getName())) return true;
        if (manager.hiroshiteam.hasEntry(player.getName())) return true;
        manager.revival(player);
        return true;
    }
}
