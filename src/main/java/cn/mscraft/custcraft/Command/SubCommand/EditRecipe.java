package cn.mscraft.custcraft.Command.SubCommand;

import cn.mscraft.custcraft.API.IManager;
import cn.mscraft.custcraft.Loader.Lang.GetLangYaml;
import cn.mscraft.custcraft.Model.Panel;
import cn.mscraft.custcraft.Model.Recipe;
import cn.mscraft.custcraft.Util.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;

public class EditRecipe {
    public static HashMap<String, String> EDITING = new HashMap<>();
    private static cn.mscraft.custcraft.API.IManager IManager;

    public static boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length >= 3) {
                Optional<Panel> optionalPanel = IManager.getPanel(args[1]);
                if (optionalPanel.isPresent()) {
                    Panel panel = optionalPanel.get();
                    Optional<Recipe> optionalRecipe = IManager.getRecipe(args[1], args[2]);
                    if (optionalRecipe.isPresent()) {
                        Recipe recipe = optionalRecipe.get();
                        IManager.openEditRecipeGUI(player, panel, recipe);
                        EDITING.put(player.getName(), args[2]);
                    } else {
                        Msg.send((CommandSender)player, GetLangYaml.PANEL_NOT_EXIST);
                    }
                } else {
                    Msg.send((CommandSender)player, GetLangYaml.PANEL_NOT_EXIST);
                }
            } else {
                Msg.send((CommandSender)player, GetLangYaml.EDIT_WRONG_FORMAT);
            }
        }
        return true;
    }
}