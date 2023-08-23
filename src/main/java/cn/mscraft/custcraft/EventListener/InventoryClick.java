package cn.mscraft.custcraft.EventListener;

import cn.mscraft.custcraft.API.IManager;
import cn.mscraft.custcraft.Command.SubCommand.EditRecipe;
import cn.mscraft.custcraft.Craft;
import cn.mscraft.custcraft.CustCraft;
import cn.mscraft.custcraft.Model.ICraftTechHolder;
import cn.mscraft.custcraft.Model.Panel;
import cn.mscraft.custcraft.Util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;

public class InventoryClick implements Listener {
    @EventHandler
    void onSelGuiClick(InventoryClickEvent ev) {
        InventoryView gui = ev.getView();
        if (gui != null &&
                gui.getTitle().equals(IManager.titleSelGui.get("selTitle"))) {
            if (ev.getCurrentItem() != null && ev.getCurrentItem().getItemMeta() != null) {
                ItemStack item = ev.getCurrentItem();
                Player player = (Player)ev.getWhoClicked();
                if (item.getType().equals(Material.CHEST)) {
                    player.openWorkbench(null, true);
                } else if (item.getType().equals(Material.ENDER_CHEST)) {
                    Optional<Panel> optionalPanel = IManager.getPanel(CustCraft.instance.getConfig().getString("setting.gui"));
                    if (optionalPanel.isPresent()) {
                        Panel panel = optionalPanel.get();
                        IManager.openCraftingGUI(player, panel);
                    }
                }
            }
            ev.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void clickBaffle(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && event.getCurrentItem() != null) {
            InventoryHolder holder = inventory.getHolder();
            if (holder instanceof ICraftTechHolder && ItemStackUtil.isSimilar(event.getCurrentItem(), ItemStackUtil.getBaffle()))
                event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void clickButton(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && event.getCurrentItem() != null) {
            InventoryHolder holder = inventory.getHolder();
            if (holder instanceof ICraftTechHolder &&
                    event.getCurrentItem().isSimilar(ItemStackUtil.getButton())) {
                Optional<Panel> optionalPanel = IManager.getPanel(((ICraftTechHolder)holder).getPanelId());
                if (optionalPanel.isPresent()) {
                    Panel panel = optionalPanel.get();
                    event.setCancelled(true);
                    Craft.craft((Player)event.getWhoClicked(), panel, event.getClickedInventory());
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void clickClose(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && event.getCurrentItem() != null) {
            InventoryHolder holder = inventory.getHolder();
            if (holder instanceof ICraftTechHolder &&
                    event.getCurrentItem().isSimilar(ItemStackUtil.getClose())) {
                event.setCancelled(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) CustCraft.instance, event.getWhoClicked()::closeInventory, 0L);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void clickResult(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && event.getCursor().getType() != Material.AIR) {
            InventoryHolder holder = inventory.getHolder();
            if (holder instanceof ICraftTechHolder && !EditRecipe.EDITING.containsKey(event.getWhoClicked().getName())) {
                Optional<Panel> optionalPanel = IManager.getPanel(((ICraftTechHolder)holder).getPanelId());
                if (optionalPanel.isPresent()) {
                    Panel panel = optionalPanel.get();
                    if (panel.getResultsSlots().contains(Integer.valueOf(event.getSlot())))
                        event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void shiftClick(InventoryClickEvent event) {
        if (event.isShiftClick()) {
            Inventory inventory = event.getWhoClicked().getOpenInventory().getTopInventory();
            if (event.getClickedInventory() != null && event.getCurrentItem() != null && !event.getClickedInventory().equals(inventory)) {
                InventoryHolder holder = inventory.getHolder();
                if (holder instanceof ICraftTechHolder) {
                    Optional<Panel> optionalPanel = IManager.getPanel(((ICraftTechHolder)holder).getPanelId());
                    if (optionalPanel.isPresent()) {
                        Panel panel = optionalPanel.get();
                        if (panel.getResultsSlots().contains(Integer.valueOf((inventory.first(event.getCurrentItem()) != -1) ? inventory.first(event.getCurrentItem()) : inventory.firstEmpty())))
                            event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void clickView(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null) {
            InventoryHolder holder = inventory.getHolder();
            if (holder instanceof ICraftTechHolder) {
                Optional<Panel> optionalPanel = IManager.getPanel(((ICraftTechHolder)holder).getPanelId());
                if (optionalPanel.isPresent()) {
                    Panel panel = optionalPanel.get();
                    if (!EditRecipe.EDITING.containsKey(event.getWhoClicked().getName())) {
                        boolean isCraft = true;
                        List<Integer> bs = panel.getButtonSlots();
                        for (Integer slot : bs) {
                            if (!ItemStackUtil.isSimilar(inventory.getItem(slot.intValue()), ItemStackUtil.getButton()))
                                isCraft = false;
                        }
                        if (!isCraft)
                            event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void doubleClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (holder instanceof ICraftTechHolder &&
                event.getClick() == ClickType.DOUBLE_CLICK)
            event.setCancelled(true);
    }
}


