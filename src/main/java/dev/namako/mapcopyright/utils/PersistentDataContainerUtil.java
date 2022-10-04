package dev.namako.mapcopyright.utils;

import dev.namako.mapcopyright.MapCopyright;
import dev.namako.mapcopyright.dataType.UUIDDataType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.List;
import java.util.UUID;

public class PersistentDataContainerUtil {
  private static final NamespacedKey uuidKey = new NamespacedKey(MapCopyright.getInstance(), "map");

  public static UUID getUUID(ItemStack itemStack) {
    if(itemStack.getItemMeta() == null) return null;
    PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
    if(container.has(uuidKey, new UUIDDataType())) {
      return container.get(uuidKey, new UUIDDataType());
    } else {
      return null;
    }
  }

  public static void setUUID(ItemStack itemStack, UUID uuid) {
    if(itemStack.getItemMeta() == null) return;
    ItemMeta itemMeta = itemStack.getItemMeta();
    PersistentDataContainer container = itemMeta.getPersistentDataContainer();
    container.remove(uuidKey);
    container.set(uuidKey, new UUIDDataType(), uuid);
    itemStack.setItemMeta(itemMeta);
  }

  public static void remove(ItemStack itemStack) {
    if(itemStack.getItemMeta() == null) return;
    ItemMeta itemMeta = itemStack.getItemMeta();
    PersistentDataContainer container = itemMeta.getPersistentDataContainer();
    container.remove(uuidKey);
    itemStack.setItemMeta(itemMeta);
  }

  public static boolean isCopyable(UUID uuid, ItemStack itemStack) {
    if(Bukkit.getPlayer(uuid).hasPermission(new Permission("mapcopyright.ignore"))) return true;
    if(!isProtected(itemStack)) return true;
    return getUUID(itemStack).toString().equals(uuid.toString());
  }

  public static boolean isProtected(ItemStack itemStack) {
    if(!itemStack.hasItemMeta()) return false;
    if(!itemStack.getItemMeta().hasLore()) return false;
    List<String> lore = itemStack.getItemMeta().getLore();
    return lore.get(lore.size() - 1).equals(String.format("%sProtected", ChatColor.DARK_AQUA));
  }
}
