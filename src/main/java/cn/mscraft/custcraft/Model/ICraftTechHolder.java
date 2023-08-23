package cn.mscraft.custcraft.Model;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ICraftTechHolder implements InventoryHolder {
    private String panelId;

    public ICraftTechHolder(String panelId) {
        this.panelId = panelId;
    }

    public Inventory getInventory() {
        return null;
    }

    public String getPanelId() {
        return this.panelId;
    }
}
