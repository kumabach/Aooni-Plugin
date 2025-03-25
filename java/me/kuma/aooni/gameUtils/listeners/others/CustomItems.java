package me.kuma.aooni.gameUtils.listeners.others;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItems {
    public static ItemStack CoalKey() {
        // 石炭アイテムを作成
        ItemStack coal = new ItemStack(Material.COAL);

        // アイテムメタデータを取得
        ItemMeta meta = coal.getItemMeta();
        if (meta != null) {
            // アイテムの名前を設定
            meta.setDisplayName("地下室の鍵");

            // エンチャントを追加（例: 耐久力）
            meta.addEnchant(Enchantment.DURABILITY, 1, true);

            // アイテムメタをアイテムに設定
            coal.setItemMeta(meta);
        }

        return coal;
    }
    public static ItemStack Anduril() {

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
    public static ItemStack LapisKey() {
        // ラピスラズリアイテムを作成
        ItemStack lapis = new ItemStack(Material.INK_SACK, 1, (short) 4);

        // アイテムメタデータを取得
        ItemMeta meta = lapis.getItemMeta();
        if (meta != null) {
            // アイテムの名前を設定
            meta.setDisplayName("五階の鍵");

            // エンチャントを追加（例: 耐久力）
            meta.addEnchant(Enchantment.DURABILITY, 1, true);

            // アイテムメタをアイテムに設定
            lapis.setItemMeta(meta);
        }

        return lapis;
    }
}
