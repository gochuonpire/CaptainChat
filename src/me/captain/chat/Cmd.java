package me.captain.chat;

import java.util.ArrayList;
import me.captain.chat.core.ChatChannel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Cmd implements CommandExecutor {

    private final CaptainChat plugin;

    public Cmd(CaptainChat instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((sender instanceof Player)) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (plugin.hasPerm(player, "captainchat.use")) {
                    player.sendMessage(ChatColor.GREEN + "CpatainChat Commands:");
                    player.sendMessage(ChatColor.GRAY + "- /chat list - Lists all available channels");
                    player.sendMessage(ChatColor.GRAY + "- /chat join <channel> - Joins a channel");
                    player.sendMessage(ChatColor.GRAY + "- /chat leave - Leaves the channel you are in");
                    player.sendMessage(ChatColor.GRAY + "- /chat info <channel> - Shows info about the channel");
                } else {
                    player.sendMessage(ChatColor.GRAY + "You don't have permission to use /cc");
                }
            }
            if (args.length == 1) {
                String arg = args[0].toLowerCase();
                if (arg.equals("list")) {
                    ArrayList disp = new ArrayList();
                    ArrayList<ChatChannel> chans = plugin.channelHandler.getChannels();
                    for (ChatChannel chan : chans) {
                        if (plugin.hasPerm(player, chan.getNode())) {
                            disp.add(ChatColor.GREEN + chan.getName() + ChatColor.GRAY);
                        } else {
                            disp.add(ChatColor.RED + chan.getName() + ChatColor.GRAY);
                        }
                    }
                    player.sendMessage(ChatColor.GRAY + "Channels(" + ChatColor.GREEN + chans.size() + ChatColor.GRAY + "): " + disp.toString());
                    return true;
                }
                if (arg.equals("leave")) {
                    ArrayList<ChatChannel> chans = plugin.channelHandler.getChannels();
                    for (ChatChannel chan : chans) {
                        if (chan.containsPlayer(player)) {
                            chan.announceAndRemove(player);
                            player.sendMessage(ChatColor.YELLOW + "You left " + chan.getName());
                        }
                    }
                }
            }
            if (args.length == 2) {
                String arg = args[0].toLowerCase();
                if (arg.equals("join")) {
                    String name = args[1];
                    ChatChannel channel = plugin.channelHandler.getChannel(name);
                    ArrayList<ChatChannel> chans = plugin.channelHandler.getChannels();
                    if (channel != null) {
                        if (channel.containsPlayer(player)) {
                            player.sendMessage(ChatColor.GRAY + "You are already in " + name);
                            return true;
                        }
                        if (plugin.hasPerm(player, channel.getNode())) {
                            for (ChatChannel chan : chans) {
                                if (chan.containsPlayer(player)) {
                                    chan.announceAndRemove(player);
                                }
                            }
                            channel.announceAndAdd(player);
                        } else {
                            player.sendMessage(ChatColor.GRAY + "You don't have permission to enter " + name);
                        }
                        return true;
                    }
                    player.sendMessage(ChatColor.GRAY + name + " not found!");
                    return true;
                }

                if (arg.equals("info")) {
                    String name = args[1];
                    ChatChannel channel = plugin.channelHandler.getChannel(name);
                    if (channel != null) {
                        String line1 = ChatColor.GRAY + "Name: " + channel.getName();
                        String line2 = ChatColor.GRAY + "Access Node: " + channel.getNode();
                        ArrayList disp = new ArrayList();
                        ArrayList<Player> players = channel.getConnectedPlayers();
                        for (Player chatter : players) {
                            disp.add(ChatColor.GREEN + chatter.getDisplayName() + ChatColor.GRAY);
                        }
                        String line3 = ChatColor.GRAY + "Players(" + players.size() + "): " + disp.toString();
                        player.sendMessage(line1);
                        player.sendMessage(line2);
                        player.sendMessage(line3);
                        return true;
                    }
                    player.sendMessage(ChatColor.GRAY + name + " not found!");
                    return true;
                }
            }
        }
        return true;
    }
}