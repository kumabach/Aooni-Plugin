package me.kuma.aooni.commands;

import me.kuma.aooni.Aooni;
import me.kuma.aooni.mains.AooniManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AooniGameEndCommand extends AbstractCommand {

    public AooniGameEndCommand(Aooni plugin) {
        super(plugin);
    }

    @Override
    AbstractCommand getInstance() {
        return this;
    }

    @Override
    public String getCommandName() {
        return "AooniGameEnd";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        AooniManager manager = Aooni.getManager();
        if(!(sender instanceof Player))return true;
        Player player = (Player)sender;
        if(!manager.permissionSet.contains(player.getUniqueId()))return true;

        if (!manager.gameStatus.equalsIgnoreCase("onGame")) {
            sender.sendMessage(ChatColor.RED + "ゲームがまだ始まっていません!");
        } else {
            manager.gameEnd(1);
            sender.sendMessage(ChatColor.GREEN + "ゲームを終了しました！");
        }
        return true;
    }
}
