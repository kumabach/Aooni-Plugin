package me.kuma.aooni.commands;


import me.kuma.aooni.Aooni;
import me.kuma.aooni.mains.AooniManager;
import me.kuma.aooni.others.AndurilItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand extends AbstractCommand {

    public TestCommand(Aooni plugin) {
        super(plugin);
    }

    @Override
    AbstractCommand getInstance() {
        return this;
    }

    @Override
    public String getCommandName() {
        return "Test";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        AooniManager manager = Aooni.getManager();

        if(!(sender instanceof Player))return true;
        Player player = (Player)sender;
        if(!manager.permissionSet.contains(player.getUniqueId()))return true;
        ItemStack item = AndurilItem.createItem();

        net.minecraft.server.v1_8_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item); // BukkitのItemStackをNMS形式に変換
        NBTTagCompound tag = nmsItemStack.hasTag() ? nmsItemStack.getTag() : new NBTTagCompound();

// UHCidタグを取得して表示
        int uhcId = tag.getInt("UHCid");
        player.sendMessage(String.valueOf(uhcId));
        player.getInventory().addItem(item);
        return true;
    }
}
