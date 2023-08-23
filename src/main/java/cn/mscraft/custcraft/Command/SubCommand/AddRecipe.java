package cn.mscraft.custcraft.Command.SubCommand;

import cn.mscraft.custcraft.API.IManager;
import cn.mscraft.custcraft.Loader.Lang.GetLangYaml;
import cn.mscraft.custcraft.Model.Panel;
import cn.mscraft.custcraft.Util.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class AddRecipe {
    public static boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length >= 3) {
                Optional<Panel> optionalPanel = IManager.getPanel(args[1]);
                if (optionalPanel.isPresent()) {
                    Panel panel = optionalPanel.get();
                    if (!IManager.getRecipe(args[1], args[2]).isPresent()) {
                        IManager.openCraftingGUI(player, panel);
                        EditRecipe.EDITING.put(player.getName(), args[2]);
                    } else {
                        Msg.send((CommandSender)player, GetLangYaml.RECIPE_EXISTED);
                    }
                } else {
                    Msg.send((CommandSender)player, GetLangYaml.PANEL_NOT_EXIST);
                }
            } else {
                Msg.send((CommandSender)player, GetLangYaml.ADD_WRONG_FORMAT);
            }
        }
        return true;
    }
}
