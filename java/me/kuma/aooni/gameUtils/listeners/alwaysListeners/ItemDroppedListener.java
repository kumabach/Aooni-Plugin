package me.kuma.aooni.gameUtils.listeners.alwaysListeners;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

public class ItemDroppedListener implements Listener {
    @EventHandler
    public void onItemDrop(ItemSpawnEvent event) {
        ItemStack droppedItem = event.getEntity().getItemStack();
        Item itemEntity = event.getEntity();
        if (droppedItem.getType() != Material.COOKED_BEEF) {
            itemEntity.remove();
        }
    }
}
