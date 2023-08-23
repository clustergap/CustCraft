package cn.mscraft.custcraft.Command.SubCommand;

import cn.mscraft.custcraft.API.IManager;
import cn.mscraft.custcraft.Loader.Lang.GetLangYaml;
import cn.mscraft.custcraft.Model.Panel;
import cn.mscraft.custcraft.Util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class OpenPanel {
    public static boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length >= 2) {
                Optional<Panel> optionalPanel = IManager.getPanel(args[1]);
                if (!optionalPanel.isPresent()) {
                    Msg.send(sender, GetLangYaml.PANEL_NOT_EXIST);
                    return true;
                }
                Panel panel = optionalPanel.get();
                if (args.length == 3 &&
                        !openByOther(sender, args, panel))
                    return true;
                IManager.openCraftingGUI(player, optionalPanel.get());
            } else {
                Msg.send(sender, GetLangYaml.OPEN_WRONG_FORMAT);
            }
        } else if (args.length == 3) {
            Optional<Panel> optionalPanel = IManager.getPanel(args[1]);
            if (!optionalPanel.isPresent()) {
                Msg.send(sender, GetLangYaml.PANEL_NOT_EXIST);
                return true;
            }
            Panel panel = optionalPanel.get();
            openByOther(sender, args, panel);
        } else {
            Msg.send(sender, GetLangYaml.OPEN_WRONG_FORMAT);
        }
        return true;
    }

    private static boolean openByOther(CommandSender sender, String[] args, Panel panel) {
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; i++)
            sb.append(args[i]);
        Player openPlayer = Bukkit.getPlayer(sb.toString());
        if (openPlayer != null) {
            IManager.openCraftingGUI(openPlayer, panel);
            return true;
        }
        Msg.send(sender, GetLangYaml.PLAYER_NOT_ONLINE);
        return false;
    }
}
