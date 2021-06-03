package io.github.narikakun.mc.hypixelrank.hypixelrank;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;

public final class Hypixelrank extends JavaPlugin implements Listener {

    public String prefix = ChatColor.GOLD + "[HypixelRank]" + ChatColor.WHITE;
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        System.out.println(prefix + "プラグインが有効になりました。");
    }

    @Override
    public void onDisable() {
        System.out.println(prefix + "プラグインが無効になりました。");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        String player_prefix = getConfig().getString("prefixes.default");
        ConfigurationSection prefixes = getConfig().getConfigurationSection("prefixes");
        for (String rank: prefixes.getKeys(false)) {
            if (p.isPermissionSet("hypixel.rank." + rank)) player_prefix = prefixes.getString(rank);
        }
        event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        for (Player players : Bukkit.getOnlinePlayers())
        {
            players.sendMessage(player_prefix.replaceAll("&", "§").replaceAll("%playername", p.getDisplayName()) + ": " + event.getMessage());
        }
        event.setCancelled(true);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (sender.hasPermission("hypixel.rank.youtube")) {
                if (cmd.getName().equalsIgnoreCase("unnick")) {
                    sender.sendMessage(prefix + ChatColor.GREEN + "あなたの名前がリセットされました。");
                    p.setDisplayName(p.getName());
                    p.setPlayerListName(p.getName());
                }

                if (cmd.getName().equalsIgnoreCase("nick")) {
                    if (args.length == 1) {
                        sender.sendMessage(prefix + ChatColor.GREEN + "あなたのニックネームが" + ChatColor.RED + args[0] + ChatColor.GREEN + "に変更されました。");
                        p.setDisplayName(args[0]);
                        p.setPlayerListName(args[0]);
                    } else {
                        sender.sendMessage(prefix + ChatColor.RED + "変更するニックネームが入力されていません。");
                    }
                }
            } else {
                sender.sendMessage(prefix + ChatColor.RED + "このコマンドは、YOUTUBEランクが必要です。");
            }
        } else {
            sender.sendMessage(prefix + ChatColor.RED + "プレイヤー以外実行できません。");
        }
        return false;
    }
}
