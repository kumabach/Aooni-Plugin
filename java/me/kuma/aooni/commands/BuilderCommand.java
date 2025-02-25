package me.kuma.aooni.commands;

import me.kuma.aooni.Aooni;
import me.kuma.aooni.listeners.alwayslisteners.BlockBreakListener;
import me.kuma.aooni.mains.AooniManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuilderCommand extends AbstractCommand {

    public BuilderCommand(Aooni plugin) {
        super(plugin);
    }

    @Override
    AbstractCommand getInstance() {
        return this;
    }

    @Override
    public String getCommandName() {
        return "Builder";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        AooniManager manager = Aooni.getManager();

        if(!(sender instanceof Player))return true;
        Player player = (Player)sender;
        if(!manager.permissionSet.contains(player.getUniqueId()))return true;

        if (BlockBreakListener.aoonibuilders.contains((Player) sender)) {
            BlockBreakListener.aoonibuilders.remove((Player) sender);
            sender.sendMessage(ChatColor.GREEN+"ビルダーモードがオフになりました！");
        }
        else {
            BlockBreakListener.aoonibuilders.add((Player) sender);
            sender.sendMessage(ChatColor.GREEN+"ビルダーモードがオンになりました！");
        }
        return true;
    }
}
