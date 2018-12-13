package me.captain.chat.core;

import java.util.ArrayList;
import org.bukkit.plugin.Plugin;

public class ChannelHandler {

    private final Plugin plugin;
    private final ArrayList<ChatChannel> onlineChannels = new ArrayList();

    public ChannelHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    public void addChannel(ChatChannel newChannel) {
        onlineChannels.add(newChannel);
    }

    public void removeChannel(ChatChannel channel) {
        onlineChannels.remove(channel);
    }

    public Boolean isOnline(ChatChannel channel) {
        return onlineChannels.contains(channel);
    }

    public void removeAllChannels() {
        onlineChannels.clear();
    }

    public ArrayList<ChatChannel> getChannels() {
        return onlineChannels;
    }

    public ChatChannel getChannel(String name) {
        for (ChatChannel channel : onlineChannels) {
            if (channel.getName().equals(name)) {
                return channel;
            }
        }
        return null;
    }
}