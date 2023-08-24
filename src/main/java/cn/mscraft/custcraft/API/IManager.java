package cn.mscraft.custcraft.API;

import cn.mscraft.custcraft.API.Events.PanelOpenEvent;
import cn.mscraft.custcraft.Craft;
import cn.mscraft.custcraft.CustCraft;
import cn.mscraft.custcraft.Loader.Yaml;
import cn.mscraft.custcraft.Model.ICraftTechHolder;
import cn.mscraft.custcraft.Model.Panel;
import cn.mscraft.custcraft.Model.Recipe;
import cn.mscraft.custcraft.Util.ItemStackUtil;
import cn.mscraft.custcraft.Util.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class IManager {
    private static final HashMap<String, HashMap<String, Recipe>> RECIPES = new HashMap<>();

    private static final HashMap<String, Panel> PANELS = new HashMap<>();

    public static void hook(JavaPlugin plugin, File recipe, File panel) {
        if (Bukkit.getPluginManager().isPluginEnabled((Plugin)plugin)) {
            if (recipe.exists())
                Yaml.loadRecipes(YamlConfiguration.loadConfiguration(recipe));
            if (panel.exists())
                Yaml.loadPanels(YamlConfiguration.loadConfiguration(panel));
        } else {
            LogUtil.send("§f[iCraft] §4附属插件" + plugin.getName() + "未启用, Hook失败");
        }
    }

    public static void hook(JavaPlugin plugin) {
        String path = plugin.getDataFolder().getPath();
        File recipe = new File(path + File.separator + "Recipe.yml");
        File panel = new File(path + File.separator + "Panel.yml");
        hook(plugin, recipe, panel);
    }

    public static void registerRecipe(Recipe recipe) {
        RECIPES.computeIfAbsent(recipe.getPanelId(), k -> new HashMap<>());
        ((HashMap<String, Recipe>)RECIPES.get(recipe.getPanelId())).put(recipe.getId(), recipe);
    }

    public static void unregisterRecipe(String panelId, String RecipeId) {
        ((HashMap)RECIPES.get(panelId)).remove(RecipeId);
    }

    public static Optional<Recipe> getRecipe(String panelId, String recipeId) {
        return Optional.ofNullable(RECIPES.get(panelId)).map(recipes -> (Recipe)recipes.get(recipeId));
    }

    public static Collection<Recipe> getRecipes() {
        return (Collection<Recipe>)RECIPES.values().stream().map(HashMap::values).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static Collection<Recipe> getRecipes(String panelID) {
        return Optional.ofNullable(RECIPES.get(panelID)).map(HashMap::values).orElse(Collections.emptyList());
    }

    public static void registerPanel(Panel panel) {
        PANELS.put(panel.getId(), panel);
    }

    public static void unregisterPanel(Panel panel) {
        PANELS.remove(panel.getId());
    }

    public static Optional<Panel> getPanel(String panelId) {
        return Optional.ofNullable(PANELS.get(panelId));
    }

    public static Collection<Panel> getPanels() {
        return PANELS.values();
    }

    public static void openCraftingGUI(Player player, Panel panel) {
        openGUI(player, panel, null, false);
    }

    public static void openEditRecipeGUI(Player player, Panel panel, Recipe recipe) {
        openGUI(player, panel, recipe, false);
    }

    public static void openGUI(Player player, Panel panel, Recipe recipe, boolean isView) {
        boolean isCraft = (recipe == null);
        List<ItemStack> results = null;
        List<ItemStack> matrix = null;
        if (!isCraft) {
            results = recipe.getResults();
            matrix = recipe.getItemMatrix();
        }
        PanelOpenEvent panelOpenEvent = new PanelOpenEvent(panel, player);
        Bukkit.getPluginManager().callEvent((Event)panelOpenEvent);
        if (!panelOpenEvent.isCancelled()) {
            String titleGui = panel.getTitle();
            int rowGui = panel.getRow();
            Inventory GUI = Bukkit.createInventory((InventoryHolder)new ICraftTechHolder(panel.getId()), rowGui, titleGui);
            int j = 0;
            int k = 0;
            for (int i = 0; i < GUI.getSize(); i++) {
                if (panel.getMatrixSlots().contains(Integer.valueOf(i))) {
                    if (!isCraft && matrix.size() >= j + 1) {
                        GUI.setItem(i, matrix.get(j));
                        j++;
                    }
                } else if (panel.getButtonSlots().contains(Integer.valueOf(i)) && !isView) {
                    if (panel.getIsButton()) {
                        GUI.setItem(i, ItemStackUtil.getButton());
                    }
                } else if (panel.getResultsSlots().contains(Integer.valueOf(i))) {
                    if (!isCraft && results.size() >= k + 1) {
                        GUI.setItem(i, results.get(k));
                        k++;
                    }
                } else {
                    if (panel.getDebug())
                        GUI.setItem(i, ItemStackUtil.getBaffle());
                }
            }
            player.openInventory(GUI);
        }
    }

    public static HashMap<String, String> titleSelGui = new LinkedHashMap<>();

    public static void openSelGui(Player player) {
        String vanillaGui = CustCraft.instance.getConfig().getString("selectGui.vanillaGuiButton").replaceAll("&", "§");
                String newGui = CustCraft.instance.getConfig().getString("selectGui.newGuiButton").replaceAll("&", "§");
                        Inventory gui = Bukkit.createInventory((InventoryHolder)player, 9, titleSelGui.get("selTitle"));
        gui.setItem(3, itemBuild(Material.CHEST, vanillaGui));
        gui.setItem(5, itemBuild(Material.ENDER_CHEST, newGui));
        player.openInventory(gui);
    }

    private static ItemStack itemBuild(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}

