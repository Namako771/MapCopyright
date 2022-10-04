package dev.namako.mapcopyright;

import dev.namako.mapcopyright.commands.CommandRegister;
import dev.namako.mapcopyright.commands.ArtExecutor;
import dev.namako.mapcopyright.utils.PersistentDataContainerUtil;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MapCopyright extends JavaPlugin implements Listener {
  private static MapCopyright plugin;

  @Override
  public void onEnable() {
    long start = System.currentTimeMillis();
    plugin = this;
    PluginManager pluginManager = getServer().getPluginManager();
    pluginManager.registerEvents(this, this);
    PluginCommand artCommand = getCommand("art");
    artCommand.setExecutor(new ArtExecutor());
    CommandRegister.register(this, artCommand);
    getLogger().info(String.format("Successfully enabled. (took %dms)", System.currentTimeMillis() - start));
  }

  @Override
  public void onDisable() {
  }

  public static MapCopyright getInstance() {
    return plugin;
  }

  @EventHandler
  public void onClickInventory(InventoryClickEvent event) {
    if(!(event.getWhoClicked() instanceof Player player)) return;
    if(event.getSlotType() == InventoryType.SlotType.RESULT) {
      ItemStack currentItem = event.getCurrentItem();
      if(PersistentDataContainerUtil.isCopyable(player.getUniqueId(), currentItem)) return;
      event.setCancelled(true);
    }
  }
}
