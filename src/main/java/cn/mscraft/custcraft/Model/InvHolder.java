package cn.mscraft.custcraft.Model;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InvHolder implements InventoryHolder {
    private String panelId;

    public InvHolder(String panelId) {
        this.panelId = panelId;
    }

    public Inventory getInventory() {
        return null;
    }

    public String getPanelId() {
        return this.panelId;
    }
}
