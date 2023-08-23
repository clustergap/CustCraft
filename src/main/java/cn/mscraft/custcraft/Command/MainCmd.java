package cn.mscraft.custcraft.Command;

import cn.mscraft.custcraft.Command.SubCommand.*;
import cn.mscraft.custcraft.Loader.Lang.GetLangYaml;
import cn.mscraft.custcraft.Util.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCmd implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.toLowerCase().equalsIgnoreCase("icraft")) {
            String subCmd = SafeThis.get(args, 0, "");
            switch (subCmd.toLowerCase()) {
                case "help":
                    if (sender.hasPermission("icraft.help"))
                        Msg.send(sender, GetLangYaml.COMMAND_HELP);
                    break;
                case "openpanel":
                    if (sender.hasPermission("icraft.open") &&
                            OpenPanel.execute(sender, command, label, args))
                        return true;
                    break;
                case "addrecipe":
                    if (sender.hasPermission("icraft.admin") &&
                            AddRecipe.execute(sender, command, label, args))
                        return true;
                    break;
                case "delrecipe":
                    if (sender.hasPermission("icraft.admin") &&
                            DelRecipe.execute(sender, command, label, args))
                        return true;
                    break;
                case "editrecipe":
                    if (sender.hasPermission("icraft.admin") &&
                            EditRecipe.execute(sender, command, label, args))
                        return true;
                    break;
                case "viewrecipe":
                    if (sender.hasPermission("icraft.view") &&
                            ViewRecipe.execute(sender, command, label, args))
                        return true;
                    break;
            }
        }
        return true;
    }
}

