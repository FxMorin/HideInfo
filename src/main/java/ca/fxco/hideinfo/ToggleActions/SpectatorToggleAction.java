package ca.fxco.hideinfo.ToggleActions;

import ca.fxco.hideinfo.Interfaces.ToggleAction;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SpectatorToggleAction implements ToggleAction {

    public static void setVisibility(Plugin plugin, Player player, GameMode gamemode) {
        if (gamemode == GameMode.SPECTATOR) { //is now in spectator
            player.getServer().getOnlinePlayers().forEach((others) -> others.hidePlayer(plugin,player));
        } else { //is no longer in spectator
            player.getServer().getOnlinePlayers().forEach((others) -> others.showPlayer(plugin,player));
        }
    }

    @Override
    public void enable(Plugin plugin, Server server) {
        server.getOnlinePlayers().forEach((player) -> {
           if (player.getGameMode() == GameMode.SPECTATOR) {
               server.getOnlinePlayers().forEach((others) -> others.hidePlayer(plugin,player));
           }
        });
    }

    @Override
    public void disable(Plugin plugin,Server server) {
        server.getOnlinePlayers().forEach((player) -> {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                server.getOnlinePlayers().forEach((others) -> others.showPlayer(plugin,player));
            }
        });
    }
}
