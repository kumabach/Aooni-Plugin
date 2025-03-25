package me.kuma.aooni.gameUtils.listeners.ongameListeners;

import me.kuma.aooni.Aooni;
import me.kuma.aooni.gameUtils.listeners.mains.AooniManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBlockListener implements Listener {

    private AooniManager manager;

    @EventHandler
    public void onPlayerQuit(BlockPlaceEvent event) {
        manager = Aooni.getManager();
        if(!manager.gameStatus.equalsIgnoreCase("onGame")) return;
        if(event.getBlock().getType() == Material.STONE_PLATE) {
            Location location = new Location(event.getBlock().getWorld(), 8, 11, -112);
            if(!(event.getBlock().getLocation().equals(location))) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED+"ここに感圧版を置くことはできません！");
            }
        }
    }
}
