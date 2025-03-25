package me.kuma.aooni.gameUtils.listeners.alwaysListeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashSet;
import java.util.Set;

public class BlockBreakListener implements Listener {

    public static Set<Player> aoonibuilders;

    public BlockBreakListener() {
        aoonibuilders = new HashSet<>();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!(event.getPlayer() instanceof Player)) event.setCancelled(true);
        else if (!aoonibuilders.contains(event.getPlayer())) event.setCancelled(true);
    }
}
