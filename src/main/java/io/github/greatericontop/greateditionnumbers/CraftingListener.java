package io.github.greatericontop.greateditionnumbers;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CraftingListener implements Listener {

    private final GreatEditionNumbers plugin;
    public CraftingListener(GreatEditionNumbers plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST) // tries to run last to insert lore at the end in case any custom item plugins edit it
    public void onCraft(CraftItemEvent event) {
        String stringKey;
        if (event.getRecipe() instanceof ShapedRecipe sr) {
            stringKey = sr.getKey().toString();
        } else if (event.getRecipe() instanceof ShapelessRecipe slr) {
            stringKey = slr.getKey().toString();
        } else {
            return; // not a recipe we care about
        }
        if (!plugin.trackedRecipes.contains(stringKey))  return;
        if (plugin.getConfig().getBoolean("skip-creative-players") && event.getWhoClicked().getGameMode() == GameMode.CREATIVE)  return;
        String configLocation = String.format("_data_.%s", stringKey);
        int count = plugin.getConfig().getInt(configLocation, 0) + 1;
        ItemStack stack = event.getInventory().getResult();
        if (stack == null) {
            plugin.getLogger().warning(String.format("Result for craft (%s) is null, it probably shouldn't be!", stringKey));
            return;
        }
        ItemMeta im = stack.getItemMeta();
        if (im == null) {
            plugin.getLogger().warning(String.format("Result for craft (%s) doesn't have an ItemMeta!", stringKey));
            return;
        }
        List<String> lore = im.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.add(String.format("§7Edition #%s%,d", count == 1 ? "§6" : "§f", count));
        stack.setItemMeta(im);
        plugin.getConfig().set(configLocation, count);
    }

}
