package me.kuma.aooni.gameUtils.listeners.commands;


import me.kuma.aooni.Aooni;
import me.kuma.aooni.gameUtils.listeners.mains.AooniManager;
import me.kuma.aooni.gameUtils.listeners.others.CustomItems;
import me.kuma.aooni.gameUtils.listeners.others.FillChests;
import me.kuma.aooni.gameUtils.listeners.others.TitleSender;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class TestCommand extends AbstractCommand {

    TitleSender ts;

    public static int chestType;
    public TestCommand(Aooni plugin) {
        super(plugin);
        chestType =0;
        ts=new TitleSender();
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
        //player.getInventory().addItem(CustomItems.CoalKey());
        //player.getInventory().addItem(CustomItems.LapisKey());

        for(Player p: Bukkit.getOnlinePlayers()){
            ts.setTime(p, 1, 3, 1);
            ts.sendTitle(p, "ゲーム開始！", "準備しよう！", null);
        }
        return true;

//        if(args.length==0){
//            player.sendMessage("数字を入力してください");
//            return true;
//        }
//        else{
//            int type;
//            try {
//                type = Integer.parseInt(args[0]);
//            } catch (NumberFormatException e) {
//                player.sendMessage("数字を入力してください");
//                return true;
//            }
//        }

//        if(args.length ==0){
//            ItemStack item = AndurilItem.createItem();
//            net.minecraft.server.v1_8_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item); // BukkitのItemStackをNMS形式に変換
//            NBTTagCompound tag = nmsItemStack.hasTag() ? nmsItemStack.getTag() : new NBTTagCompound();
//            int uhcId = tag.getInt("UHCid");
//            player.sendMessage(String.valueOf(uhcId));
//            player.getInventory().addItem(item);
//            player.sendTitle("yo","YO");
//            return true;
//        }
//        else if(args[0].equalsIgnoreCase("change")){
//            chestType++;
//            chestType %=3;
//            sender.sendMessage(ChatColor.DARK_AQUA+"the type was changed to "+chestType);
//        }
//        else if(args[0].equalsIgnoreCase("save")){
//            try {
//                LeftClickListener.makeJson();
//                sender.sendMessage("いけました");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        else if(args[0].equalsIgnoreCase("ensure")){
//            player.sendMessage(LeftClickListener.chests.get(chestType).size()+"です");
//        }
//        else if(args[0].equalsIgnoreCase("fill")){
//            try {
//                FillChests.readJson(0);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            try {
//                FillChests.fillchests(0);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        else if(args[0].equalsIgnoreCase("world")){
//            for (World world : Bukkit.getServer().getWorlds()) {
//                player.sendMessage(world.getName());
//            }
//        }
//        else player.sendMessage("このコマンドは無効です");
          //return true;
    }

}
