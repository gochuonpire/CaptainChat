package me.captain.chat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import me.captain.chat.core.ChannelHandler;
import me.captain.chat.core.ChatChannel;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CaptainChat extends JavaPlugin {

    public PermissionManager pm;
    public HashMap<String, String> prefixes;
    public FileConfiguration config;
    public YamlConfiguration channelsConfig;
    public String globalFormat;
    public String localFormat;
    public ChannelHandler channelHandler;
    public Integer localRange;
    public String defChan;

    @Override
    public void onEnable() {
        prefixes = new HashMap();
        PluginManager plugm = getServer().getPluginManager();
        plugm.registerEvents(new CCPL(this), this);
        getCommand("chat").setExecutor(new Cmd(this));
        channelHandler = new ChannelHandler(this);
        loadConfigs();
        loadChannels();
        pm = PermissionsEx.getPermissionManager();
        restartPlayers();
    }

    @Override
    public void onDisable() {
    }

    public void loadConfigs() {
        config = getConfig();
        config.addDefault("Global.Format", "&7[&a+senderName&7]: +message");
        config.addDefault("Local.Format", "&7[L] +senderName: +message");
        config.addDefault("Local.AccessNode", "captainchat.local");
        config.addDefault("Global.AccessNode", "captainchat.global");
        config.addDefault("Local.Range", 50);
        config.options().copyDefaults(true);
        
        globalFormat = config.getString("Global.Format", "&7[&a+senderName&7]: +message");
        localFormat = config.getString("Local.Format", "&7[L] +senderName: +message");
        String lnode = config.getString("Local.AccessNode", "captainchat.local");
        String gnode = config.getString("Global.AccessNode", "captainchat.global");
        localRange = config.getInt("Local.Range", 50);
        ChatChannel global = new ChatChannel(this, "Global", globalFormat, gnode);
        ChatChannel local = new ChatChannel(this, "Local", localFormat, lnode);
        channelHandler.addChannel(local);
        channelHandler.addChannel(global);
        defChan = config.getString("DefaultChannel", "Global");
        saveConfig();
    }

    public void loadChannels() {
        File channelFile = new File(getDataFolder(), "channels.yml");
        if(!channelFile.exists()) {
            try {
                channelFile.createNewFile();
            } catch (IOException ex) {
            }
        }   
        channelsConfig = new YamlConfiguration();
        try {
            channelsConfig.load(channelFile);
        } catch (Exception ex) {
            System.out.println("[CaptainChat] Error loading channels.yml");
        }
        Set<String> keys = channelsConfig.getKeys(false);
        if(keys.isEmpty()) return;
        for (String key : keys) {
            String name = channelsConfig.getString(key + ".Name", "NoName");
            String format = channelsConfig.getString(key + ".Format");
            String node = channelsConfig.getString(key + ".AccessNode", "captainchat.node");
            ChatChannel newChan = new ChatChannel(this, name, format, node);
            channelHandler.addChannel(newChan);
        }
    }

    public String formatChat(Player player, ChatChannel channel, String message) {
        String format = channel.getFormat().replace("+senderName", player.getDisplayName());
        format = format.replace("+worldName", player.getWorld().getName());
        format = format.replace("+channelName", channel.getName());
        format = format.replace("+prefix", pm.getUser(player.getName()).getPrefix());
        format = format.replace("+message", message);
        format = format.replaceAll("(&([a-f0-9]))", "§$2");
        return format;
    }

    public Boolean hasPerm(Player player, String node) {
        if (player.hasPermission("captainchat.*")) {
            return true;
        }
        return player.hasPermission(node);
    }
    public void restartPlayers() {
        ChatChannel d = channelHandler.getChannel(defChan);
        for(Player p : this.getServer().getOnlinePlayers()) {
            d.announceAndAdd(p);
        }
    }
}