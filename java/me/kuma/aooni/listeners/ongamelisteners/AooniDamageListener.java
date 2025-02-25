package me.kuma.aooni.listeners.ongamelisteners;

import me.kuma.aooni.Aooni;
import me.kuma.aooni.mains.AooniManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scoreboard.Team;

public class AooniDamageListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        AooniManager manager = Aooni.getManager();

        if (!manager.gameStatus.equalsIgnoreCase("onGame")) {
            event.setCancelled(true);
            return;
        }

        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        event.setDamage(0);
        String attacker = event.getDamager().getName();
        String victim = event.getEntity().getName();
        Team aooni = manager.aooniteam;
        Team hiroshi = manager.hiroshiteam;
        if (aooni.hasEntry(attacker) && hiroshi.hasEntry(victim)) {
            ((Player) event.getEntity()).getInventory().clear();
            manager.hiroshiteam.removeEntry(victim);
            Player player = Bukkit.getPlayer(victim);
            Location targetLocation = new Location(player.getWorld(), -79, 5, 22);
            player.teleport(targetLocation);
            for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage(ChatColor.BLUE + victim + "は青鬼に食べられた");
            if (manager.hiroshiteam.getSize() == 0) manager.GameEnd(2);
        } else {
            event.setCancelled(true);
        }
    }
}
