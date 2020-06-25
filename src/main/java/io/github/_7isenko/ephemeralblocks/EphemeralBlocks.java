package io.github._7isenko.ephemeralblocks;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class EphemeralBlocks extends JavaPlugin {
    // How to build: Maven/EphemeralBlocks/Lifecycle/package
    public static Plugin plugin;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        plugin = this;
        config = this.getConfig();
        this.getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Visit my github page https://github.com/7isenko");
    }
}