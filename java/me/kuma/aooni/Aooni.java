package me.kuma.aooni;

import me.kuma.aooni.commands.*;

import me.kuma.aooni.listeners.alwaysListeners.AooniAllDamageListener;
import me.kuma.aooni.listeners.alwaysListeners.BlockBreakListener;
import me.kuma.aooni.listeners.alwaysListeners.ItemDropEvent;
import me.kuma.aooni.listeners.alwaysListeners.ItemDroppedListener;
import me.kuma.aooni.listeners.alwaysListeners.MobListener;
import me.kuma.aooni.listeners.ongameListeners.*;
import me.kuma.aooni.mains.AooniManager;
import me.kuma.aooni.others.AooniTimer;
import me.kuma.aooni.others.GrantPotionEffect;
import org.bukkit.plugin.java.JavaPlugin;

public final class Aooni extends JavaPlugin {

    private static AooniManager manager;
    private static Aooni plugin;

    @Override
    public void onEnable() {

        // Plugin startup logic;
        plugin = this;
        manager = new AooniManager();
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
        new GrantPotionEffect();
        new AooniTimer();
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
