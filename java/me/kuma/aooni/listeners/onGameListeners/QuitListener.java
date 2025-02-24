package me.kuma.aooni.listeners.onGameListeners;

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
            manager.AooniCounter--;
            if (manager.AooniCounter == 0) manager.GameEnd(3);
        }
        if (manager.hiroshiteam.hasEntry(player.getName())) {
            manager.hiroshiteam.removeEntry(player.getName());
            if (manager.hiroshiteam.getSize() == 0) manager.GameEnd(2);
        }
        if (manager.gameStatus.equalsIgnoreCase("onGame")) manager.SetScoreboard();
    }
}
