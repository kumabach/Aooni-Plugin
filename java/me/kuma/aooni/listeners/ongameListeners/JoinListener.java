package me.kuma.aooni.listeners.ongameListeners;

import me.kuma.aooni.Aooni;
import me.kuma.aooni.mains.AooniManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        AooniManager manager = Aooni.getManager();
        Player player = event.getPlayer();
        if (manager.aooniteam.hasEntry(player.getName())) manager.aooniCounter++;
        if (manager.gameStatus.equalsIgnoreCase("onGame")) manager.setScoreboard();
        player.setScoreboard(manager.scoreboard);
        Location targetLocation = new Location(player.getWorld(), -79, 5, 22);
        player.teleport(targetLocation);
    }
}
