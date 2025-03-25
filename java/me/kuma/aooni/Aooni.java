package me.kuma.aooni;

import me.kuma.aooni.gameUtils.listeners.commands.*;

import me.kuma.aooni.gameUtils.listeners.alwaysListeners.*;
import me.kuma.aooni.gameUtils.listeners.ongameListeners.*;
import me.kuma.aooni.gameUtils.listeners.mains.AooniManager;
import me.kuma.aooni.gameUtils.listeners.others.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Aooni extends JavaPlugin {

    private static AooniManager manager;
    private static Aooni plugin;

    @Override
    public void onEnable() {

        // Plugin startup logic;
        plugin = this;
        manager = new AooniManager();
        new AndurilItem();
        new ChangeAooniSizeCommand(this);
        new AooniGameStartCommand(this);
        new TestCommand(this);
        new AooniGameEndCommand(this);
        new ChangeAooniTimeLimitCommand(this);
        new BuilderCommand(this);
        new RevivalCommand(this);
        getServer().getPluginManager().registerEvents(new MobListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getServer().getPluginManager().registerEvents(new AooniDamageListener(), this);
        getServer().getPluginManager().registerEvents(new AooniAllDamageListener(), this);
        getServer().getPluginManager().registerEvents(new GetItemListener(), this);
        getServer().getPluginManager().registerEvents(new ItemDropEvent(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new ItemDroppedListener(), this);
        getServer().getPluginManager().registerEvents(new PlaceBlockListener(), this);
        getServer().getPluginManager().registerEvents(new ItemHeldListener(), this);
        //getServer().getPluginManager().registerEvents(new LeftClickListener(), this);
        new GrantPotionEffect();
        new AooniTimer();
        try {
            new FillChests();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static AooniManager getManager() {
        return manager;
    }

    public static Aooni getPlugin() {
        return plugin;
    }
}
