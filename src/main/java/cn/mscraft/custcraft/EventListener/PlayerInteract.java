package cn.mscraft.custcraft.EventListener;

import cn.mscraft.custcraft.API.IManager;
import cn.mscraft.custcraft.CustCraft;
import cn.mscraft.custcraft.Model.Panel;
import cn.mscraft.custcraft.Util.ReturnMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

public class PlayerInteract implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void clickBlock(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() != null && event.getClickedBlock().getType().equals(ReturnMaterial.craftingTableMaterial())) {
            if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                return;
            event.setCancelled(true);
            String type = CustCraft.instance.getConfig().getString("setting.default");
            if (type.equals("sel")) {
                IManager.openSelGui(player);
            } else if (type.equals("new")) {
                Optional<Panel> optionalPanel = IManager.getPanel(CustCraft.instance.getConfig().getString("setting.gui"));
                if (optionalPanel.isPresent()) {
                    Panel panel = optionalPanel.get();
                    event.setCancelled(true);
                    IManager.openCraftingGUI(player, panel);
                }
            }
        }
    }
}
