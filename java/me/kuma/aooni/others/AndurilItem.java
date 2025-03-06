package me.kuma.aooni.others;

import me.kuma.aooni.Aooni;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
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

    public static ItemStack createItem() {

        ItemStack anduril = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = anduril.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("§aAndüril");
            meta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
            anduril.setItemMeta(meta);
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(anduril);
        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
        NBTTagCompound extraAttributes = tag.hasKey("ExtraAttributes") ? tag.getCompound("ExtraAttributes") : new NBTTagCompound();

        extraAttributes.setInt("UHCid", 50008);
        tag.set("ExtraAttributes", extraAttributes);
        nmsStack.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsStack);

    }
}
