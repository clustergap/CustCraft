package cn.mscraft.custcraft.Command.SubCommand;

import cn.mscraft.custcraft.API.IManager;
import cn.mscraft.custcraft.Loader.Lang.GetLangYaml;
import cn.mscraft.custcraft.Model.Panel;
import cn.mscraft.custcraft.Model.Recipe;
import cn.mscraft.custcraft.Util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class ViewRecipe {
    public static boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length >= 3) {
                fast(sender, args, false);
            } else {
                Msg.send(sender, GetLangYaml.VIEW_WRONG_FORMAT);
            }
        } else if (args.length == 4) {
            fast(sender, args, true);
        } else {
            Msg.send(sender, GetLangYaml.VIEW_WRONG_FORMAT);
        }
        return true;
    }

    private static void fast(CommandSender sender, String[] args, boolean four) {
        Optional<Panel> optionalPanel = IManager.getPanel(args[1]);
        if (!optionalPanel.isPresent()) {
            Msg.send(sender, GetLangYaml.PANEL_NOT_EXIST);
            return;
        }
        Panel panel = optionalPanel.get();
        Optional<Recipe> optionalRecipe = IManager.getRecipe(args[1], args[2]);
        if (four) {
            if (!optionalRecipe.isPresent()) {
                Msg.send(sender, GetLangYaml.RECIPE_NOT_EXIST);
                return;
            }
            Recipe recipe = optionalRecipe.get();
            openByOther(sender, args, panel, recipe);
        } else {
            if (!optionalRecipe.isPresent()) {
                Msg.send(sender, GetLangYaml.RECIPE_NOT_EXIST);
                return;
            }
            Recipe recipe = optionalRecipe.get();
            if (args.length == 4 &&
                    !openByOther(sender, args, panel, recipe))
                return;
            Player player = (Player)sender;
            IManager.openGUI(player, panel, recipe, true);
        }
    }

    private static boolean openByOther(CommandSender sender, String[] args, Panel panel, Recipe recipe) {
        StringBuilder sb = new StringBuilder();
        for (int i = 3; i < args.length; i++)
            sb.append(args[i]);
        Player openPlayer = Bukkit.getPlayer(sb.toString());
        if (openPlayer != null) {
            IManager.openGUI(openPlayer, panel, recipe, true);
            return true;
        }
        Msg.send(sender, GetLangYaml.PLAYER_NOT_ONLINE);
        return false;
    }
}
