package ca.fxco.hideinfo.Commands;

import ca.fxco.hideinfo.HideInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CommandHideInfo implements CommandExecutor {

    private final HideInfo hideInfo;

    private final String PREFIX = "§8[§dHide§5Info§8] §f";

    public CommandHideInfo(HideInfo hideInfo) {
        super();
        this.hideInfo = hideInfo;
    }

    public void sendHelpMenu(@NotNull CommandSender sender) {
        sender.sendMessage(" §6=============== §dHide§5Info §fHelp §6===============");
        for (Map.Entry<String, String> name : this.hideInfo.toggles.entrySet()) {
            sender.sendMessage(PREFIX+(this.hideInfo.config.getBoolean(name.getKey()) ? "§2" : "§4")+name.getKey()+"§f: "+name.getValue());
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp()) {
            if(args.length == 0) {
                sendHelpMenu(sender);
            } else if (args.length <= 2) {
                if (args[0].equalsIgnoreCase("help")) {
                    sendHelpMenu(sender);
                } else {
                    String value = args[0].toLowerCase();
                    if (this.hideInfo.toggles.containsKey(value)) {
                        boolean newValue;
                        if (args.length == 2) {
                            newValue = args[1].equalsIgnoreCase("true");
                        } else {
                            newValue = !this.hideInfo.config.getBoolean(value);
                        }
                        this.hideInfo.config.set(value, newValue);
                        this.hideInfo.onModifyConfig();
                        sender.sendMessage(PREFIX + "§9" + value + "§f is now: " + (this.hideInfo.config.getBoolean(value) ? "§2Showing" : "§4Hiding"));
                        if (this.hideInfo.toggleActions.containsKey(value)) {
                            if (newValue) {
                                this.hideInfo.toggleActions.get(value).enable(this.hideInfo,sender.getServer());
                            } else {
                                this.hideInfo.toggleActions.get(value).enable(this.hideInfo,sender.getServer());
                            }
                        }
                    } else {
                        sender.sendMessage(PREFIX + "§9" + args[0] + "§f is not a valid option!");
                    }
                }
            } else {
                sender.sendMessage(PREFIX+"Invalid Syntax: /hideinfo <option> [<toggle>]");
            }
        }
        return true;
    }
}
