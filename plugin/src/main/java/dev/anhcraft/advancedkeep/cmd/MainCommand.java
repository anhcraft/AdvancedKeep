package dev.anhcraft.advancedkeep.cmd;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import dev.anhcraft.advancedkeep.AdvancedKeep;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("ak|keep")
public class MainCommand extends BaseCommand {
    private final AdvancedKeep instance;

    public MainCommand(AdvancedKeep instance) {
        this.instance = instance;
    }

    @HelpCommand
    @CatchUnknown
    public void root(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("give soulgem")
    @CommandPermission("keep.give.soulgem")
    @Description("Give someone a soul gem")
    public void giveSoulGem(CommandSender sender, OnlinePlayer player, int amount) {
        Player p = player.getPlayer();
        p.getInventory().addItem(instance.getSoulGem(amount));
        sender.sendMessage(ChatColor.GOLD+"Soul gem was given to "+ChatColor.WHITE+p.getName());
    }

    @Subcommand("reload")
    @CommandPermission("keep.reload")
    @Description("Reload the configuration")
    public void reload(CommandSender sender){
        instance.reload();
        sender.sendMessage(ChatColor.GOLD+"The configuration was reloaded");
    }

    @Subcommand("debug")
    @CommandPermission("keep.debug")
    @Description("Debug")
    public void debug(CommandSender sender){
        instance.debug = !instance.debug;
        sender.sendMessage(ChatColor.GOLD+"Debug mode is now "+(instance.debug ? "on" : "off"));
    }
}
