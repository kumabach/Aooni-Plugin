package me.kuma.aooni.listeners.ongameListeners;

import me.kuma.aooni.Aooni;

import me.kuma.aooni.mains.AooniManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GetItemListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        AooniManager manager = Aooni.getManager();

        if (!manager.gameStatus.equalsIgnoreCase("OnGame")) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (!manager.hiroshiteam.getEntries().contains(player.getName())) {
            event.setCancelled(true);
            return;
        }
        if (event.getCurrentItem().getType() == Material.COOKED_BEEF) return;

        ItemStack pointerItem = event.getCursor();

        //自分にアイテムを移動　チェストのインベントリ左クリック
        //自分にアイテムを移動　チェストのインベントリ左クリック意外
        //チェストにアイテムを移動　ポインターにアイテムなくてチェストのインベントリを左右クリック以外
        //チェストにアイテムを移動　ポインターにアイテムあってチェストのインベントリ左右クリック

        if (event.getClick() == ClickType.DOUBLE_CLICK) return;

        if (event.getClickedInventory().getHolder() instanceof Chest) {
            event.setCancelled(true);
            ItemStack getItem = event.getCurrentItem();
            if (pointerItem == getItem) return;

            if (pointerItem.getType() != Material.AIR) {
                player.getInventory().addItem(pointerItem);
            }
            if (hasExactItem(player, getItem)) {
                player.sendMessage(ChatColor.RED + "そのアイテムを既に持っています！");
                return;
            } else {

                ItemStack newItem = getItem.clone();
                newItem.setAmount(1);

                if ((event.getClick().isLeftClick() && event.getClick() != ClickType.SHIFT_LEFT) || event.getClick().isRightClick()) {
                    player.setItemOnCursor(newItem);
                } else {
                    player.getInventory().addItem(newItem);
                }
                return;
            }
        }
        if (event.getView().getTopInventory().getHolder() instanceof Chest && event.getClickedInventory().getHolder() instanceof Player) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "そのインベントリ操作は許可されていません！");
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
