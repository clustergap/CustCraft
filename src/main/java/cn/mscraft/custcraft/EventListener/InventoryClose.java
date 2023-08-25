package cn.mscraft.custcraft.EventListener;

import cn.mscraft.custcraft.API.Events.PanelCloseEvent;
import cn.mscraft.custcraft.API.IManager;
import cn.mscraft.custcraft.Command.SubCommand.EditRecipe;
import cn.mscraft.custcraft.Model.InvHolder;
import cn.mscraft.custcraft.Model.Panel;
import cn.mscraft.custcraft.Util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class InventoryClose implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void panelClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof InvHolder) {
            String id = ((InvHolder)holder).getPanelId();
            if (EditRecipe.EDITING.containsKey(event.getPlayer().getName())) {
                EditRecipe.EDITING.remove(event.getPlayer().getName());
            } else {
                Optional<Panel> optionalPanel = IManager.getPanel(id);
                if (optionalPanel.isPresent()) {
                    Panel panel = optionalPanel.get();
                    Inventory inventory = event.getInventory();
                    boolean isCraft = true;
                    List<Integer> bs = panel.getButtonSlots();
                    for (Integer slot : bs) {
                        if (!ItemStackUtil.isSimilar(inventory.getItem(slot.intValue()), ItemStackUtil.getButton()))
                            isCraft = false;
                    }
                    if (isCraft) {
                        PanelCloseEvent panelCloseEvent = new PanelCloseEvent(panel, (Player)event.getPlayer());
                        Bukkit.getPluginManager().callEvent((Event)panelCloseEvent);
                        List<Integer> matrix = panel.getMatrixSlots();
                        List<Integer> resultSlots = panel.getResultsSlots();
                        returnItemStack(event, matrix);
                        returnItemStack(event, resultSlots);
                    }
                }
            }
        }
    }

    private void returnItemStack(InventoryCloseEvent event, List<Integer> slots) {
        Location location = event.getPlayer().getLocation();
        World world = location.getWorld();
        slots.stream()
                .map(event.getInventory()::getItem)
                .filter(Objects::nonNull)
                .forEach(itemStack -> {
                    if (event.getPlayer().getInventory().first(itemStack) == -1 && event.getPlayer().getInventory().firstEmpty() == -1) {
                        world.dropItem(location, itemStack);
                    } else {
                        event.getPlayer().getInventory().addItem(new ItemStack[] { itemStack });
                    }
                });
    }
}

