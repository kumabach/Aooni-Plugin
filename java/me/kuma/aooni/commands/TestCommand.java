package me.kuma.aooni.commands;

import me.kuma.aooni.Aooni;
import me.kuma.aooni.mains.AooniManager;
import me.kuma.aooni.others.AooniTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand extends AbstractCommand {

    public TestCommand(Aooni plugin) {
        super(plugin);
    }

    @Override
    AbstractCommand getInstance() {
        return this;
    }

    @Override
    public String getCommandName() {
        return "Test";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        AooniManager manager = Aooni.getManager();

        if(!(sender instanceof Player))return true;
        Player player = (Player)sender;
        if(!manager.permissionSet.contains(player.getUniqueId()))return true;

        AooniTimer.launchFirework(player.getLocation());
        AooniTimer.launchFirework(player.getLocation());
        AooniTimer.launchFirework(player.getLocation());
        return true;
    }
}
