package me.kuma.aooni.listeners.ongamelisteners;

import me.kuma.aooni.Aooni;
import me.kuma.aooni.mains.AooniManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private AooniManager manager;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        manager = Aooni.getManager();
        Player player = event.getPlayer();

        if (manager.aooniteam.hasEntry(player.getName())) {
            manager.aooniCounter--;
            if (manager.aooniCounter == 0) manager.gameEnd(3);
        }
        if (manager.hiroshiteam.hasEntry(player.getName())) {
            manager.hiroshiteam.removeEntry(player.getName());
            if (manager.hiroshiteam.getSize() == 0) manager.gameEnd(2);
        }
        if (manager.gameStatus.equalsIgnoreCase("onGame")) manager.setScoreboard();
    }
}
