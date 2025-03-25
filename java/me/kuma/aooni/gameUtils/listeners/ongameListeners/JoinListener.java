package me.kuma.aooni.gameUtils.listeners.ongameListeners;

import me.kuma.aooni.Aooni;
import me.kuma.aooni.gameUtils.listeners.mains.AooniManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        AooniManager manager = Aooni.getManager();
        Player player = event.getPlayer();
        if (manager.aooniteam.hasEntry(player.getName())) manager.aooniCounter++;

        if (!manager.gameStatus.equalsIgnoreCase("onGame")) {
            player.getInventory().clear();
        }
        else {
            if(manager.aooniteam.hasEntry(player.getName())) {
                manager.setScoreboard();
                player.getInventory().clear();
                ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
                ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
                ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
                ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
                ItemStack stonePressurePlates = new ItemStack(Material.STONE_PLATE, 64);
                player.getInventory().addItem(stonePressurePlates);
                player.getInventory().setArmorContents(new ItemStack[]{boots, leggings, chestplate, helmet});
            } else{
                player.getInventory().setArmorContents(new ItemStack[4]);
            }
        }
        Location targetLocation = new Location(player.getWorld(), -79, 5, 22);
        player.teleport(targetLocation);
    }
}
