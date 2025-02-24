package me.kuma.aooni.commands;

import me.kuma.aooni.Aooni;
import me.kuma.aooni.mains.AooniManager;
import me.kuma.aooni.others.AooniTimer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChangeAooniTimeLimitCommand extends AbstractCommand {

    public ChangeAooniTimeLimitCommand(Aooni plugin) {
        super(plugin);
    }

    @Override
    AbstractCommand getInstance() {
        return this;
    }

    @Override
    public String getCommandName() {
        return "ChangeAooniTimeLimit";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        AooniManager manager = Aooni.getManager();

        if(!(sender instanceof Player))return true;
        Player player = (Player)sender;
        if(!manager.permissionSet.contains(player.getUniqueId()))return true;

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "/ChangeAooniTimeLimit 時間制限(秒)");
            return true;
        }
        try {
            int num = Integer.parseInt(args[0]);
            if (!(num > 0 && num < 3600)) {
                sender.sendMessage(ChatColor.RED + "1~3600秒の範囲しか設定できません！");
                return true;
            }
            AooniTimer.ChangeTimeLimit(num);
            sender.sendMessage(ChatColor.GREEN + "制限時間が" + args[0]+ "秒になりました");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "/ChangeAooniTimeLimit 制限時間(秒)");
        }
        return true;
    }
}


