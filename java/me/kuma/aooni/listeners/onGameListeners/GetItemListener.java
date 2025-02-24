package me.kuma.aooni.listeners.onGameListeners;

import me.kuma.aooni.Aooni;

import me.kuma.aooni.mains.AooniManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class GetItemListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        AooniManager manager = Aooni.getManager();
        if (!manager.gameStatus.equalsIgnoreCase("OnGame")) return;

        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        if (manager.aooniteam.getEntries().contains(player.getName())) {
            event.setCancelled(true);
            return;
        }

        if (!manager.hiroshiteam.getEntries().contains(player.getName())) return;

        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;

        InventoryHolder holder = clickedInventory.getHolder();
        if (holder != null && event.getView().getTopInventory().getType() == InventoryType.CHEST) {
            ItemStack item = event.getCurrentItem();
            if (item == null) return;

            if (item.getType() == Material.COOKED_BEEF) {
                return;
            }
            if (hasExactItem(player, item)) {
                player.sendMessage("すでにこのアイテムを持っています。");
                event.setCancelled(true);
                return;
            }
            ItemStack newItem = item.clone();
            newItem.setAmount(1);
            player.getInventory().addItem(newItem);
            event.setCancelled(true);
        }

    }

    private boolean hasExactItem(Player player, ItemStack targetItem) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.isSimilar(targetItem)) {
                return true;
            }
        }
        return false;
    }
}
