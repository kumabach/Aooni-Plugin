package me.kuma.aooni.gameUtils.listeners.commands;

import me.kuma.aooni.Aooni;
import me.kuma.aooni.gameUtils.listeners.mains.AooniManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AooniGameStartCommand extends AbstractCommand {

    public AooniGameStartCommand(Aooni plugin) {
        super(plugin);
    }

    @Override
    AbstractCommand getInstance() {
        return this;
    }

    @Override
    public String getCommandName() {
        return "AooniGameStart";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        AooniManager manager=Aooni.getManager();
        if(!(sender instanceof Player))return true;
        Player player = (Player)sender;
        if(!manager.permissionSet.contains(player.getUniqueId()))return true;
        if(manager.gameStatus.equalsIgnoreCase("onGame")){
            sender.sendMessage(ChatColor.RED+"ゲームは既に始まっています!");
        }
        else {
            manager.startCountdown(5);
            manager.gameStatus = "onGame";
            sender.sendMessage(ChatColor.GREEN+"ゲームが始まります！");
        }
        return true;
    }
}
