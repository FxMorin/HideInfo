package ca.fxco.hideinfo.Interfaces;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

public interface ToggleAction {

    public void enable(Plugin plugin, Server server);

    public void disable(Plugin plugin,Server server);
}
