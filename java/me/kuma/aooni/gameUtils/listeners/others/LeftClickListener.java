package me.kuma.aooni.gameUtils.listeners.others;

import me.kuma.aooni.Aooni;
import me.kuma.aooni.gameUtils.listeners.commands.TestCommand;
import me.kuma.aooni.gameUtils.listeners.mains.AooniManager;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.Block;
import com.google.gson.Gson;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


import java.io.*;
import java.util.*;
import java.util.List;

public class LeftClickListener implements Listener {

    public static Map<Integer, Set<Location>> chests;
    private int chestType;

    @EventHandler
    public static void onPlayerInteract(PlayerInteractEvent event) {

        if(chests==null){
            chests=new HashMap<>();
            chests.put(0, new HashSet<>());
            chests.put(1, new HashSet<>());
            chests.put(2, new HashSet<>());
        }

        AooniManager manager = Aooni.getManager();
        Player player = event.getPlayer();

        if(!event.getAction().toString().contains("LEFT_CLICK")&&!event.getAction().toString().contains("RIGHT_CLICK")) return;
        if(!manager.gameStatus.equalsIgnoreCase("onGame")) return;
        if(event.getClickedBlock()==null)return;
        Block block = event.getClickedBlock();

        if(block.getType() == Material.GOLD_BLOCK) {
            Location location = block.getLocation();
            if(location.getX()!=-167||location.getY()!=8||location.getZ()!=24) return;
            if (manager.hiroshiteam.hasEntry(player.getName())) return;
            if (manager.aooniteam.hasEntry(player.getName())) return;
            manager.revival(player);
            return;
        }
        if(block.getType() == Material.DIAMOND_BLOCK) {
            int key=manager.Keys;
            Location location = block.getLocation();
            ItemStack item = player.getItemInHand();
            String name = item.getItemMeta().getDisplayName();
            if(location.getX()==(double)37.0&&location.getY()==(double)12.0&&location.getZ()==(double)-91.0&&key==0&&name.equalsIgnoreCase("地下室の鍵")) {
                manager.Keys++;
                Block block1 = new Location(Bukkit.getWorld("world"),38.0,12.0,-91.0).getBlock();
                Block block2 = new Location(Bukkit.getWorld("world"),38.0,13.0,-91.0).getBlock();
                block1.setType(Material.AIR);
                block2.setType(Material.AIR);
                player.getInventory().setItemInHand(new ItemStack(Material.AIR));
                for(Player p: Bukkit.getOnlinePlayers()) p.sendMessage(ChatColor.AQUA+player.getName()+"が地下室を解放しました！");
                for(Player p: Bukkit.getOnlinePlayers()) p.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 1.0f);
            }
            if(location.getX()==(double)0.0&&location.getY()==(double)31.0&&location.getZ()==(double)-97.0&&key==1&&name.equalsIgnoreCase("五階の鍵")) {
                manager.Keys++;
                Block block3 = new Location(Bukkit.getWorld("world"),0.0,31.0,-98.0).getBlock();
                block3.setType(Material.LAPIS_BLOCK);
                player.getInventory().setItemInHand(new ItemStack(Material.AIR));
                for(Player p: Bukkit.getOnlinePlayers()) p.sendMessage(ChatColor.AQUA+player.getName()+"が五階を解放しました！");
                for(Player p: Bukkit.getOnlinePlayers()) p.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 1.0f);
            }
        }
    }

//    public static void makeJson() throws IOException {
//        Gson gson = new Gson();
//        File file = new File(Aooni.getPlugin().getDataFolder().getAbsolutePath() + "/chests" + TestCommand.chestType + ".json");
//
//        int type = TestCommand.chestType;
//        Map<String, List<Double>> mp = new HashMap<>();
//
//        mp.put("x", new ArrayList<>());
//        mp.put("y", new ArrayList<>());
//        mp.put("z", new ArrayList<>());
//
//        for (Location location : chests.get(type)) {
//            mp.get("x").add(location.getX());
//            mp.get("y").add(location.getY());
//            mp.get("z").add(location.getZ());
//        }
//
//        file.getParentFile().mkdirs();
//
//        try (Writer writer = new FileWriter(file, false)) {
//            gson.toJson(mp, writer);
//        }
//
//        System.out.println("Notes saved.");
//    }
}

