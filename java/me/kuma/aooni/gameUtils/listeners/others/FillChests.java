package me.kuma.aooni.gameUtils.listeners.others;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.kuma.aooni.Aooni;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Random;

public class FillChests {
    public static Map<Integer, Set<Location>> chests;
    private static final Random random = new Random();

    public FillChests() throws IOException {
        chests = new HashMap<>();

        for(int i=0; i<2; i++){
            readJson(i);
        }
    }

    public static void fillChests() throws IOException {
        for(int type=0; type<2; type++){
            if(chests.get(type)==null)return;
            for(Location location: chests.get(type)){
                Block block = location.getBlock();
                if (block.getState() instanceof Chest) {
                    Chest chest = (Chest) block.getState();
                    fillChestsRandom(chest);
                    location.getWorld().playSound(location, Sound.LEVEL_UP, 3.0f, 3.0f);
                    //showSparksInArea(location, 1, 2);
                }
            }
        }
        for(Player player:Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.GREEN+"チェストが補充されました!");
            player.playSound(player.getLocation(), Sound.CHEST_CLOSE, 3.0f, 3.0f);
        }
    }

    public static void fillChestsOthers() {
        for (int i = 0; i < 2; i++) {
            if (chests.get(i) == null || chests.get(i).isEmpty()) continue;

            List<Location> locations = new ArrayList<>(chests.get(i));
            int idx = random.nextInt(locations.size());
            Location location = locations.get(idx);

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(location.getX() + " " + location.getY() + " " + location.getZ());
            }

            Block block = location.getBlock();
            if (!(block.getState() instanceof Chest)) continue;
            Chest chest = (Chest) block.getState();
            Inventory inventory = chest.getInventory();

            List<Integer> emptySlots = new ArrayList<>();
            for (int j = 0; j < inventory.getSize(); j++) {
                if (inventory.getItem(j) == null) {
                    emptySlots.add(j);
                }
            }
            if (emptySlots.isEmpty()) continue;

            int slot = emptySlots.remove(random.nextInt(emptySlots.size()));
            if (i == 0) {
                inventory.setItem(slot, CustomItems.CoalKey());
                if (!emptySlots.isEmpty()) {
                    int slot2 = emptySlots.get(random.nextInt(emptySlots.size()));
                    inventory.setItem(slot2, CustomItems.Anduril());
                }
            } else {
                inventory.setItem(slot, CustomItems.LapisKey());
                if (!emptySlots.isEmpty()) {
                    int slot2 = emptySlots.get(random.nextInt(emptySlots.size()));
                    inventory.setItem(slot2, CustomItems.Anduril());
                }
            }
        }
    }



    public static void fillChestsRandom(Chest chest){

        Inventory inventory = chest.getInventory();
        List<Integer> emptySlots = new ArrayList<>();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                emptySlots.add(i);
            }
        }
        int steakCount = Math.min(random.nextInt(3) + 2, emptySlots.size());
        for (int i = 0; i < steakCount; i++) {
            int slotIndex = random.nextInt(emptySlots.size());
            int slot = emptySlots.remove(slotIndex);
            inventory.setItem(slot, new ItemStack(Material.COOKED_BEEF));
        }
    }

    public static void resetChest(){
        for(int type=0; type<2; type++){
            if(chests.get(type)==null){
                Bukkit.getPlayer("bach11").sendMessage(type+"!");
                continue;
            }
            for(Location location: chests.get(type)){
                Block block = location.getBlock();
                if(block == null)continue;
                if(!(block.getState() instanceof Chest))continue;
                Chest chest = (Chest) block.getState();
                Inventory inv = chest.getInventory();
                inv.clear();
            }
        }
    }

    public static void readJson(int type) throws IOException {

        Gson gson = new Gson();
        File file = new File(Aooni.getPlugin().getDataFolder().getAbsolutePath() + "/chests" + type + ".json");

        file.getParentFile().mkdirs();

        if (!file.exists()) {
            file.createNewFile();
        }

        Type mapType = new TypeToken<Map<String, List<Double>>>() {}.getType();
        Map<String, List<Double>> mp;

        try (FileReader reader = new FileReader(file)) {
            mp = gson.fromJson(reader, mapType);
            if (mp == null) {
                mp = new HashMap<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            mp = new HashMap<>();
        }

        List<Double> xList = mp.getOrDefault("x", new ArrayList<>());
        List<Double> yList = mp.getOrDefault("y", new ArrayList<>());
        List<Double> zList = mp.getOrDefault("z", new ArrayList<>());

        for (int i = 0; i < xList.size(); i++) {
            double x = xList.get(i);
            double y = yList.get(i);
            double z = zList.get(i);
            Location location = new Location(Bukkit.getServer().getWorld("world"), x, y, z);
            Block block = location.getBlock();
            if(block == null)continue;
            if (block.getType() != Material.CHEST)continue;
            if(chests.get(type)==null)chests.put(type,new HashSet<>());
            chests.get(type).add(location);
        }
    }

    private static void showSparksInArea(Location center, int duration, int radius) {
        Random random = new Random();

        // duration秒間エフェクトを表示
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) { // 1回のループで10個の火花を表示
                    double offsetX = (random.nextDouble() - 0.5) * 2 * radius;
                    double offsetY = (random.nextDouble() - 0.5) * 2 * radius;
                    double offsetZ = (random.nextDouble() - 0.5) * 2 * radius;

                    Location sparkLocation = center.clone().add(offsetX, offsetY, offsetZ);
                    sparkLocation.getWorld().playEffect(sparkLocation, Effect.FIREWORKS_SPARK, 0);
                }
            }
        }.runTaskTimer(Aooni.getPlugin(), 0L, 20L); // 1秒ごとにエフェクトを表示

        // duration秒後にエフェクトの表示を停止
        new BukkitRunnable() {
            @Override
            public void run() {
                cancel(); // これでエフェクト表示のタスクを終了
            }
        }.runTaskLater(Aooni.getPlugin(), duration * 20L); // duration秒後に停止
    }
}
