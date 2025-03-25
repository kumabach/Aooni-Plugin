package me.kuma.aooni.gameUtils.listeners.alwaysListeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropEvent implements Listener {

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() == Material.COOKED_BEEF) return;
        if (event.getPlayer() instanceof Player) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "アイテムはドロップできません");
        }
    }
}
