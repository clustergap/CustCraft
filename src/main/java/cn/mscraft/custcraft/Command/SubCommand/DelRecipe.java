package cn.mscraft.custcraft.Command.SubCommand;

import cn.mscraft.custcraft.API.IManager;
import cn.mscraft.custcraft.Loader.Lang.GetLangYaml;
import cn.mscraft.custcraft.Loader.Yaml;
import cn.mscraft.custcraft.Model.Panel;
import cn.mscraft.custcraft.Model.Recipe;
import cn.mscraft.custcraft.Util.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class DelRecipe {
    public static boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 3) {
            Optional<Panel> optionalPanel = IManager.getPanel(args[1]);
            if (optionalPanel.isPresent()) {
                Optional<Recipe> optionalRecipe = IManager.getRecipe(args[1], args[2]);
                if (optionalRecipe.isPresent()) {
                    Recipe recipe = optionalRecipe.get();
                    IManager.unregisterRecipe(args[1], args[2]);
                    Yaml.delRecipe(recipe);
                    Msg.send(sender, GetLangYaml.RECIPE_SUCCESSFULLY_DEL);
                } else {
                    Msg.send(sender, GetLangYaml.RECIPE_NOT_EXIST);
                }
            } else {
                Msg.send(sender, GetLangYaml.PANEL_NOT_EXIST);
            }
        } else {
            Msg.send(sender, GetLangYaml.DEL_WRONG_FORMAT);
        }
        return true;
    }
}
