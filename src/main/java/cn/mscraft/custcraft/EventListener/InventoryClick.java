package cn.mscraft.custcraft.EventListener;

import cn.mscraft.custcraft.API.IManager;
import cn.mscraft.custcraft.Command.SubCommand.EditRecipe;
import cn.mscraft.custcraft.Craft;
import cn.mscraft.custcraft.CustCraft;
import cn.mscraft.custcraft.Model.InvHolder;
import cn.mscraft.custcraft.Model.Panel;
import cn.mscraft.custcraft.Util.ItemStackUtil;
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
            if (holder instanceof InvHolder && ItemStackUtil.isSimilar(event.getCurrentItem(), ItemStackUtil.getBaffle()))
                event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void clickButton(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && event.getCurrentItem() != null) {
//            System.out.println(event.getCurrentItem().toString());
            InventoryHolder holder = inventory.getHolder();
            if (holder instanceof InvHolder) {
                Optional<Panel> optionalPanel = IManager.getPanel(((InvHolder) holder).getPanelId());
                // 判断点击的物品是否与设定的“合成按钮”
                if (event.getCurrentItem().isSimilar(ItemStackUtil.getButton())) {
                    if (optionalPanel.isPresent()) {
                        Panel panel = optionalPanel.get();
                        event.setCancelled(true);
                        // 执行配方的全部操作
                        Craft.craft((Player) event.getWhoClicked(), panel, event.getClickedInventory());
                    }
//                } else {
//                    if (optionalPanel.isPresent()) {
//                        Panel panel = optionalPanel.get();
//                        Craft.craft((Player) event.getWhoClicked(), panel, event.getClickedInventory());
//                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void clickResult(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && event.getCursor().getType() != Material.AIR) {
            InventoryHolder holder = inventory.getHolder();
            if (holder instanceof InvHolder && !EditRecipe.EDITING.containsKey(event.getWhoClicked().getName())) {
                Optional<Panel> optionalPanel = IManager.getPanel(((InvHolder)holder).getPanelId());
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
                if (holder instanceof InvHolder) {
                    Optional<Panel> optionalPanel = IManager.getPanel(((InvHolder)holder).getPanelId());
                    if (optionalPanel.isPresent()) {
                        Panel panel = optionalPanel.get();
                        if (panel.getResultsSlots().contains(Integer.valueOf((inventory.first(event.getCurrentItem()) != -1) ? inventory.first(event.getCurrentItem()) : inventory.firstEmpty())))
                            event.setCancelled(true);
                    }
                }
            }
        }
    }

//    @EventHandler(ignoreCancelled = true)
//    public void clickView(InventoryClickEvent event) {
//        Inventory inventory = event.getClickedInventory();
//        if (inventory != null) {
//            InventoryHolder holder = inventory.getHolder();
//            if (holder instanceof ICraftTechHolder) {
//                Optional<Panel> optionalPanel = IManager.getPanel(((ICraftTechHolder)holder).getPanelId());
//                if (optionalPanel.isPresent()) {
//                    Panel panel = optionalPanel.get();
//                    if (!EditRecipe.EDITING.containsKey(event.getWhoClicked().getName())) {
//                        boolean isCraft = true;
//                        List<Integer> bs = panel.getButtonSlots();
//                        for (Integer slot : bs) {
//                            if (!ItemStackUtil.isSimilar(inventory.getItem(slot.intValue()), ItemStackUtil.getButton()))
//                                isCraft = false;
//                        }
//                        if (!isCraft)
//                            event.setCancelled(true);
//                    }
//                }
//            }
//        }
//    }

    @EventHandler(ignoreCancelled = true)
    public void doubleClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (holder instanceof InvHolder && event.getClick() == ClickType.DOUBLE_CLICK)
            event.setCancelled(true);
    }
}


