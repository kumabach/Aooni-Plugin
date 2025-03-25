package me.kuma.aooni.gameUtils.listeners.others;

import me.kuma.aooni.Aooni;
import me.kuma.aooni.gameUtils.listeners.commands.TestCommand;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.Block;
import com.google.gson.Gson;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


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

        if (event.getAction().toString().contains("LEFT_CLICK")) {
            Block block = event.getClickedBlock();
            if (block != null) {
                if(block.getType() == Material.CHEST) {
                    double x=block.getLocation().getX();
                    double y=block.getLocation().getY();
                    double z=block.getLocation().getZ();
                    Player player = event.getPlayer();
                    player.sendMessage(ChatColor.GREEN+"チェストを"+ TestCommand.chestType+"に追加しました！");
                    Location location = new Location (player.getWorld(),x,y,z);
                    chests.get(TestCommand.chestType).add(location);
                }
            }
        }
    }

    public static void makeJson() throws IOException {
        Gson gson = new Gson();
        File file = new File(Aooni.getPlugin().getDataFolder().getAbsolutePath() + "/chests" + TestCommand.chestType + ".json");

        int type = TestCommand.chestType;
        Map<String, List<Double>> mp = new HashMap<>();

        mp.put("x", new ArrayList<>());
        mp.put("y", new ArrayList<>());
        mp.put("z", new ArrayList<>());

        for (Location location : chests.get(type)) {
            mp.get("x").add(location.getX());
            mp.get("y").add(location.getY());
            mp.get("z").add(location.getZ());
        }

        file.getParentFile().mkdirs();

        try (Writer writer = new FileWriter(file, false)) {
            gson.toJson(mp, writer);
        }

        System.out.println("Notes saved.");
    }
}

