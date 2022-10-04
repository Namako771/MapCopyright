package dev.namako.mapcopyright.commands;

import dev.namako.mapcopyright.utils.PersistentDataContainerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArtExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(!(sender instanceof Player player)) {
      sender.sendMessage("Player only");
      return true;
    }
    UUID playerUniqueId = player.getUniqueId();
    if(args.length == 1) {
      switch(args[0]) {
        case "info" -> {
          ItemStack itemStack = player.getInventory().getItemInMainHand();
          UUID uuid = PersistentDataContainerUtil.getUUID(itemStack);
          if(itemStack.getType() != Material.FILLED_MAP || (uuid == null)) {
            warn(player, "Copy Protection is disabled");
            return true;
          }
          Player designer = Bukkit.getPlayer(uuid);
          String format = String.format("%sProtected by %s", ChatColor.DARK_AQUA, ChatColor.GOLD + designer.getName());
          player.sendMessage(format);
          return true;
        }

        case "lock" -> {
          ItemStack itemStack = player.getInventory().getItemInMainHand();
          if(itemStack.getType() != Material.FILLED_MAP) {
            warn(player, "You can only use it on filled maps");
            return true;
          }
          if(PersistentDataContainerUtil.isProtected(itemStack)) {
            warn(player, "Copy Protection is already activated");
            return true;
          }
          ItemMeta itemMeta = itemStack.getItemMeta();
          List<String> lore = new ArrayList<>();
          lore.add(String.format("%sProtected", ChatColor.DARK_AQUA));
          itemMeta.setLore(lore);
          itemStack.setItemMeta(itemMeta);
          PersistentDataContainerUtil.setUUID(itemStack, player.getUniqueId());
          success(player, "Copy Protection activated");
          return true;
        }

        case "unlock" -> {
          ItemStack itemStack = player.getInventory().getItemInMainHand();
          if(itemStack.getType() != Material.FILLED_MAP) {
            warn(player, "You can only use it on filled maps");
            return true;
          }
          if(!PersistentDataContainerUtil.isCopyable(playerUniqueId, itemStack)) {
            warn(player, "You cannot remove Copy Protection");
            return true;
          }
          if(!PersistentDataContainerUtil.isProtected(itemStack)) {
            warn(player, "Copy Protection is already removed");
            return true;
          }
          ItemMeta itemMeta = itemStack.getItemMeta();
          List<String> lore = new ArrayList<>();
          itemMeta.setLore(lore);
          itemStack.setItemMeta(itemMeta);
          PersistentDataContainerUtil.remove(itemStack);
          success(player, "Copy Protection removed");
          return true;
        }

        default -> sendUsage(player);
      }
    } else {
      sendUsage(player);
      return true;
    }
    return false;
  }

  private void sendUsage(Player player) {
    warn(player, """
        Usage======
         /art info
         /art lock
         /art unlock
        """);
  }

  private void warn(Player player, String message) {
    player.sendMessage(ChatColor.RED + message);
  }

  private void success(Player player, String message) {
    player.sendMessage(ChatColor.DARK_AQUA + message);
  }
}
