package cn.mscraft.custcraft.Loader;

import cn.mscraft.custcraft.API.IManager;
import cn.mscraft.custcraft.Model.Panel;
import cn.mscraft.custcraft.Model.Recipe;
import cn.mscraft.custcraft.Resource;
import cn.mscraft.custcraft.Util.ItemSerializerUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class Yaml {
    public static void loadRecipes() {
        YamlConfiguration yaml = Resource.getRecipe();
        loadRecipes(yaml);
    }

    public static void loadRecipes(YamlConfiguration yaml) {
        for (String key : yaml.getKeys(false)) {
            String panelId = yaml.getString(key + ".panelId");
            String itemMatrix = yaml.getString(key + ".itemMatrix");
            String results = yaml.getString(key + ".results");
            if (panelId != null && itemMatrix != null && results != null) {
                Recipe recipe = new Recipe(key, panelId, ItemSerializerUtils.fromNbtString(itemMatrix), ItemSerializerUtils.fromNbtString(results));
                IManager.registerRecipe(recipe);
            }
        }
    }

    public static void saveRecipe(Recipe recipe) {
        YamlConfiguration yaml = Resource.getRecipe();
        yaml.set(recipe.getId() + ".panelId", recipe.getPanelId());
        yaml.set(recipe.getId() + ".itemMatrix", ItemSerializerUtils.toNbtString(recipe.getItemMatrix()));
        yaml.set(recipe.getId() + ".results", ItemSerializerUtils.toNbtString(recipe.getResults()));
        Resource.saveRecipes();
    }

    public static void delRecipe(Recipe recipe) {
        YamlConfiguration yaml = Resource.getRecipe();
        yaml.set(recipe.getId(), null);
        Resource.saveRecipes();
    }

    public static void loadPanels() {
        YamlConfiguration yaml = Resource.getPanel();
        loadPanels(yaml);
    }

    public static void loadPanels(YamlConfiguration yaml) {
        for (String key : yaml.getKeys(false)) {
            Boolean debug = yaml.getBoolean(key + ".debug");
            String row = yaml.getString(key + ".row");
            String title = yaml.getString(key + ".title");
            List<Integer> matrix = yaml.getIntegerList(key + ".matrix");
            Boolean isButton = yaml.getBoolean(key + ".isButton");
            List<Integer> buttonSlots = yaml.getIntegerList(key + ".buttonSlots");
            List<Integer> resultSlots = yaml.getIntegerList(key + ".resultSlots");
            Panel panel = new Panel(key, debug, Integer.parseInt(row), title, isButton, buttonSlots, matrix, resultSlots);
            IManager.registerPanel(panel);
        }
    }
}
