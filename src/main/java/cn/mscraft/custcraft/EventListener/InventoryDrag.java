package cn.mscraft.custcraft.EventListener;

import cn.mscraft.custcraft.API.IManager;
import cn.mscraft.custcraft.CustCraft;
import cn.mscraft.custcraft.Model.InvHolder;
import cn.mscraft.custcraft.Model.Panel;
import cn.mscraft.custcraft.Util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InventoryDrag implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onDrag(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory != null) {
            InventoryHolder holder = inventory.getHolder();
            if (holder instanceof InvHolder) {
                Optional<Panel> optionalPanel = IManager.getPanel(((InvHolder)holder).getPanelId());
                if (optionalPanel.isPresent()) {
                    Panel panel = optionalPanel.get();
                    boolean isCraft = true;
                    List<Integer> bs = panel.getButtonSlots();
                    for (Integer slot : bs) {
                        if (!ItemStackUtil.isSimilar(inventory.getItem(slot.intValue()), ItemStackUtil.getButton()))
                            isCraft = false;
                    }
                    if (!isCraft) {
                        event.setCancelled(true);
                        return;
                    }
                    List<Integer> rs = panel.getResultsSlots();
                    HashSet<Integer> removeSet = new HashSet<>();
                    int a = 0;
                    Map<Integer, ItemStack> ni = event.getNewItems();
                    for (Integer slot : ni.keySet()) {
                        if (rs.contains(slot)) {
                            removeSet.add(slot);
                            ItemStack itemStack = ni.get(slot);
                            if (itemStack == null)
                                continue;
                            a += itemStack.getAmount();
                        }
                    }
                    ItemStack item = event.getOldCursor().clone();
                    if (event.getCursor() != null)
                        a += event.getCursor().getAmount();
                    item.setAmount(a);
                    event.setCursor(item);
                    Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin) CustCraft.instance, () -> removeSet.forEach(inventory::clear), 0L);
                }
            }
        }
    }
}

