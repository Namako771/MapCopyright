package dev.namako.mapcopyright.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.namako.mapcopyright.MapCopyright;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import me.lucko.commodore.file.CommodoreFileReader;
import org.bukkit.command.Command;

import java.io.InputStream;

public class CommandRegister {
  public static void register(MapCopyright plugin, Command command) {
    try {
      Commodore commodore = CommodoreProvider.getCommodore(plugin);
      InputStream inputStream = plugin.getResource(String.format("commodore/%s.commodore", command.getName()));
      LiteralCommandNode<?> commandNode = CommodoreFileReader.INSTANCE.parse(inputStream);
      commodore.register(commandNode);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
