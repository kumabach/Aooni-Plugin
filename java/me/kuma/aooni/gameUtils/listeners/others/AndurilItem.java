package me.kuma.aooni.gameUtils.listeners.others;

import me.kuma.aooni.Aooni;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class AndurilItem {

    public AndurilItem() {
        andurilTimer();
    }

    public static void andurilTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player: Bukkit.getOnlinePlayers()) {
                    int newSlot = player.getInventory().getHeldItemSlot();
                    ItemStack newItem = player.getInventory().getItem(newSlot);
                    if(newItem == null) continue;
                    if(newItem.getType() == Material.IRON_SWORD) {
                        player.removePotionEffect(PotionEffectType.SPEED);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 0, false, false));
                    }
                }
            }
        }.runTaskTimer(Aooni.getPlugin(), 0, 20);
    }
}
