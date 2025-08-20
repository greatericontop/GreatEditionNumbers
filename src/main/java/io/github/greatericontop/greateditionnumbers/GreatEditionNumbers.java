package io.github.greatericontop.greateditionnumbers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class GreatEditionNumbers extends JavaPlugin {
    public List<String> trackedRecipes;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        trackedRecipes = this.getConfig().getStringList("tracked-recipes");

        this.getServer().getPluginManager().registerEvents(new CraftingListener(this), this);

        Bukkit.getScheduler().runTaskTimer(this, this::saveAll, 1200L, 1200L);

        this.getLogger().info("GreatEditionNumbers finished setting up!");
    }

    private void saveAll() {
        this.saveConfig();
    }

}
