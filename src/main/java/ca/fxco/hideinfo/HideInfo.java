package ca.fxco.hideinfo;

import ca.fxco.hideinfo.Commands.CommandHideInfo;
import ca.fxco.hideinfo.ToggleActions.SpectatorToggleAction;
import ca.fxco.hideinfo.Interfaces.ToggleAction;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;

public final class HideInfo extends JavaPlugin implements Listener {

    //Plugin made for PhoenixSC & Shoezilla by FX - PR0CESS

    public LinkedHashMap<String, String> toggles = new LinkedHashMap<>();
    public LinkedHashMap<String, ToggleAction> toggleActions = new LinkedHashMap<>();
    public FileConfiguration config = getConfig();

    public HideInfo() {
        super();
        initToggles();
        initToggleActions();
    }

    protected HideInfo(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        initToggles();
        initToggleActions();
    }

    public void initToggles() {
        toggles.put("leave", "Toggle player leave messages");
        toggles.put("join", "Toggle player join messages");
        toggles.put("chat", "Toggle chat");
        toggles.put("me", "Toggle /me");
        toggles.put("msg", "Toggle /msg, /tell, & /w");
        toggles.put("teammsg", "Toggle /teammsg, & /tm");
        toggles.put("plugins", "Toggle /plugins, /pl, & /?");
        toggles.put("vanilla", "Toggle /minecraft:<cmd>");
        toggles.put("secure", "Toggle /<namespace>:<cmd>");
        toggles.put("pinglist", "Toggle seeing player list in ping");
        toggles.put("spectator", "Toggle spectators seeing other spectators");
        toggles.put("help", "Toggle /help");
    }

    public void initToggleActions() {
        toggleActions.put("spectator", new SpectatorToggleAction());
    }

    @Override
    public void onEnable() {
        for (String name : toggles.keySet()) {
            config.addDefault(name, true);
        }
        config.options().copyDefaults(true);
        saveConfig();

        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("hideinfo").setExecutor(new CommandHideInfo(this));
        getLogger().info("[HideInfo] FX was here!");
    }

    public void onModifyConfig() {
        saveConfig();
    }

    @Override
    public void onDisable() {}

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!config.getBoolean("join")) {
            event.setJoinMessage("");
        }
        if (!config.getBoolean("spectator")) {
            SpectatorToggleAction.setVisibility(this, event.getPlayer(), event.getPlayer().getGameMode());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(PlayerChatEvent event) {
        if (!config.getBoolean("chat") && !event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChatAsync(AsyncPlayerChatEvent event) {
        if (!config.getBoolean("chat") && !event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!config.getBoolean("leave")) {
            event.setQuitMessage("");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        if (!event.getPlayer().isOp()) {
            String msg = event.getMessage().toLowerCase();
            String[] args = msg.split(" ");
            if (!config.getBoolean("secure") && args.length > 0 && args[0].contains(":")) {
                event.setCancelled(true);
            } else if (!config.getBoolean("vanilla") && msg.startsWith("/minecraft:")) {
                event.setCancelled(true);
            } else if (!config.getBoolean("msg") && (msg.startsWith("/tell") || msg.startsWith("/msg") || msg.startsWith("/w"))) {
                event.setCancelled(true);
            } else if (!config.getBoolean("me") && msg.startsWith("/me")) {
                event.setCancelled(true);
            } else if (!config.getBoolean("teammsg") && (msg.startsWith("/teammsg") || msg.startsWith("/tm"))) {
                event.setCancelled(true);
            } else if (!config.getBoolean("plugins") && (msg.startsWith("/plugins") || msg.startsWith("/pl") || msg.startsWith("/?"))) {
                event.setCancelled(true);
            } else if (!config.getBoolean("help") && msg.startsWith("/help")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPing(@NonNull ServerListPingEvent event) {
        if (!config.getBoolean("pinglist")) {
            try {
                final Iterator<Player> players = event.iterator();
                while (players.hasNext()) {players.remove();}
            } catch (final UnsupportedOperationException ignored) {}
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        if (!config.getBoolean("spectator")) {
            SpectatorToggleAction.setVisibility(this, event.getPlayer(), event.getNewGameMode());
        }
    }
}
