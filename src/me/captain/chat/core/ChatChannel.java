package me.captain.chat.core;

import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ChatChannel {

    private final Plugin plugin;
    private ArrayList<Player> connectedPlayers = new ArrayList();
    private final String format;
    private final String name;
    private final String accessNode;

    public ChatChannel(Plugin plugin, String name, String format, String node) {
        this.plugin = plugin;
        this.name = name;
        accessNode = node;
        this.format = format;
    }

    public ArrayList<Player> getConnectedPlayers() {
        return connectedPlayers;
    }

    public void setConnectedPlayers(ArrayList<Player> players) {
        connectedPlayers = players;
    }

    public void sendMessage(String message) {
        for (Player player : connectedPlayers) {
            player.sendMessage(message);
        }
    }

    public void addPlayer(Player player) {
        connectedPlayers.add(player);
    }

    public void removePlayer(Player player) {
        connectedPlayers.remove(player);
    }

    public Boolean containsPlayer(Player player) {
        return connectedPlayers.contains(player);
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public String getNode() {
        return accessNode;
    }

    public void announceAndAdd(Player player) {
        connectedPlayers.add(player);
        sendMessage("§e" + player.getDisplayName() + " §ehas joined " + getName());
    }

    public void announceAndRemove(Player player) {
        connectedPlayers.remove(player);
        sendMessage("§e" + player.getDisplayName() + " §ehas left " + getName());
    }
}