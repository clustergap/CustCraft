package cn.mscraft.custcraft.Model;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Recipe {
    private String id;

    private String panelId;

    private List<ItemStack> itemMatrix;

    private List<ItemStack> results;

    public Recipe(String id, String panelID, List<ItemStack> itemMatrix, List<ItemStack> results) {
        this.id = id;
        this.panelId = panelID;
        this.itemMatrix = itemMatrix;
        this.results = results;
    }

    public String getPanelId() {
        return this.panelId;
    }

    public String getId() {
        return this.id;
    }

    public void setItemMatrix(List<ItemStack> matrix) {
        this.itemMatrix = matrix;
    }

    public List<ItemStack> getItemMatrix() {
        return this.itemMatrix;
    }

    public void setResults(List<ItemStack> results) {
        this.results = results;
    }

    public List<ItemStack> getResults() {
        return this.results;
    }
}