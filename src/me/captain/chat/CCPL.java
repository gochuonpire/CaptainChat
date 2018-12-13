package me.captain.chat;

import java.util.ArrayList;
import me.captain.chat.core.ChatChannel;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CCPL implements Listener {

    private final CaptainChat plugin;

    public CCPL(CaptainChat instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        ArrayList<ChatChannel> channels = plugin.channelHandler.getChannels();
        Boolean isIn = false;
        for (ChatChannel chan : channels) {
            if (chan.containsPlayer(player)) {
                isIn = true;
            }
        }
        if (!isIn) {
            ChatChannel def = plugin.channelHandler.getChannel(plugin.defChan);
            def.announceAndAdd(player);
        }
        ChatChannel global = plugin.channelHandler.getChannel("Global");
        if (global.containsPlayer(player)) {
            String message = event.getMessage();
            String dMsg = plugin.formatChat(player, global, message);
            event.setFormat(dMsg);
            return;
        }
        ChatChannel local = plugin.channelHandler.getChannel("Local");
        if (local.containsPlayer(player)) {
            String message = event.getMessage();
            String dMsg = plugin.formatChat(player, local, message);
            event.setCancelled(true);
            ArrayList<Player> players = local.getConnectedPlayers();
            Boolean sent = false;
            for (Player chatter : players) {
                if (chatter == player) {
                    player.sendMessage(dMsg);
                } else {
                    Location chatterLoc = chatter.getLocation();
                    Location playerLoc = player.getLocation();
                    Double maxDist = plugin.localRange.doubleValue();
                    Double realDist = chatterLoc.distance(playerLoc);
                    if (realDist < maxDist) {
                        chatter.sendMessage(dMsg);
                        sent = true;
                    }
                }
            }

            if (!sent) {
                player.sendMessage(ChatColor.GRAY + "Nobody was around to hear you!");
                return;
            }
            return;
        }
        for (ChatChannel channel : channels) {
            if (channel.containsPlayer(player)) {
                String message = event.getMessage();
                String dmsg = plugin.formatChat(player, channel, message);
                channel.sendMessage(dmsg);
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ChatChannel defChannel = plugin.channelHandler.getChannel(plugin.defChan);
        if (defChannel != null) {
            defChannel.announceAndAdd(player);
            return;
        }
        ChatChannel global = plugin.channelHandler.getChannel("Global");
        global.announceAndAdd(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ArrayList<ChatChannel> chans = plugin.channelHandler.getChannels();
        for (ChatChannel chan : chans) {
            if (chan.containsPlayer(player)) {
                chan.announceAndRemove(player);
            }
        }
    }
}