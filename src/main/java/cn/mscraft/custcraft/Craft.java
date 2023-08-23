package cn.mscraft.custcraft;

import cn.mscraft.custcraft.API.Events.PanelCraftEvent;
import cn.mscraft.custcraft.API.IManager;
import cn.mscraft.custcraft.Command.SubCommand.EditRecipe;
import cn.mscraft.custcraft.Loader.Lang.GetLangYaml;
import cn.mscraft.custcraft.Loader.Yaml;
import cn.mscraft.custcraft.Model.Panel;
import cn.mscraft.custcraft.Model.Recipe;
import cn.mscraft.custcraft.Util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Craft {
    public static void craft(Player player, Panel panel, Inventory inventory) {
        if (!EditRecipe.EDITING.containsKey(player.getName())) {
            LinkedList<ItemStack> items = new LinkedList<>();
            for (Iterator<Integer> iterator1 = panel.getMatrixSlots().iterator(); iterator1.hasNext(); ) {
                int i = ((Integer)iterator1.next()).intValue();
                items.addLast(inventory.getItem(i));
            }
            Recipe pair = null;
            Collection<Recipe> recipes = IManager.getRecipes(panel.getId());
            for (Recipe recipe : recipes) {
                if (canCraft(recipe.getItemMatrix(), items))
                    pair = recipe;
            }
            LinkedList<ItemStack> RSI = new LinkedList<>();
            for (Iterator<Integer> iterator2 = panel.getResultsSlots().iterator(); iterator2.hasNext(); ) {
                int slot = ((Integer)iterator2.next()).intValue();
                if (inventory.getItem(slot) != null)
                    RSI.addLast(inventory.getItem(slot));
            }
            if (pair != null &&
                    canOverlap(pair.getResults(), RSI)) {
                PanelCraftEvent panelCraftEvent = new PanelCraftEvent(panel, player, pair, items);
                Bukkit.getPluginManager().callEvent((Event)panelCraftEvent);
                if (!panelCraftEvent.isCancelled()) {
                    int j = 0;
                    for (Iterator<Integer> iterator = panel.getMatrixSlots().iterator(); iterator.hasNext(); ) {
                        int k = ((Integer)iterator.next()).intValue();
                        if (inventory.getItem(k) != null) {
                            ItemStack item = inventory.getItem(k);
                            if (item != null)
                                item.setAmount(item.getAmount() - ((ItemStack)pair.getItemMatrix().get(j)).getAmount());
                        }
                        j++;
                    }
                    List<Integer> resultSlots = panel.getResultsSlots();
                    for (int i = 0; i < resultSlots.size(); i++) {
                        if (RSI.isEmpty()) {
                            inventory.setItem(((Integer)resultSlots.get(i)).intValue(), pair.getResults().get(i));
                        } else {
                            ItemStack item = inventory.getItem(((Integer)resultSlots.get(i)).intValue());
                            if (item != null)
                                item.setAmount(item.getAmount() + ((ItemStack)pair.getResults().get(i)).getAmount());
                        }
                    }
                }
            }
        } else {
            add(player, panel, inventory);
        }
    }

    private static void add(Player player, Panel panel, Inventory inventory) {
        List<Integer> ms = panel.getMatrixSlots();
        List<Integer> rs = panel.getResultsSlots();
        LinkedList<ItemStack> matrix = new LinkedList<>();
        LinkedList<ItemStack> results = new LinkedList<>();
        ItemStack none = new ItemStack(Material.AIR);
        for (Integer s : ms) {
            ItemStack item = inventory.getItem(s.intValue());
            matrix.addLast((item != null) ? item : none);
        }
        for (Integer s : rs) {
            ItemStack item = inventory.getItem(s.intValue());
            results.addLast((item != null) ? item : none);
        }
        Recipe recipe = new Recipe((String) EditRecipe.EDITING.get(player.getName()), panel.getId(), matrix, results);
        IManager.registerRecipe(recipe);
        Yaml.saveRecipe(recipe);
        Msg.send((CommandSender)player, GetLangYaml.RECIPE_SUCCESSFULLY_EDIT);
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)CustCraft.instance, player::closeInventory, 1L);
    }

    private static boolean canCraft(List<ItemStack> recipeList, List<ItemStack> craftList) {
        if (recipeList.size() == craftList.size()) {
            for (int i = 0; i < recipeList.size(); i++) {
                ItemStack recipeStack = recipeList.get(i);
                if (recipeStack == null || recipeStack.getType() == Material.AIR)
                    recipeStack = null;
                ItemStack craftStack = craftList.get(i);
                if (craftStack == null || craftStack.getType() == Material.AIR)
                    craftStack = null;
                if (recipeStack != null || craftStack != null) {
                    if (recipeStack == null || !recipeStack.equals(craftStack))
                        return false;
                    if (recipeStack.getAmount() > craftStack.getAmount())
                        return false;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean canOverlap(List<ItemStack> recipeResultList, List<ItemStack> panelResultList) {
        if (panelResultList.isEmpty())
            return true;
        for (int i = 0; i < recipeResultList.size(); i++) {
            ItemStack recipeResultStack = recipeResultList.get(i);
            if (recipeResultStack != null) {
                ItemStack panelResultStack = panelResultList.get(i);
                if (panelResultStack != null) {
                    if (!recipeResultStack.isSimilar(panelResultStack))
                        return false;
                    if (recipeResultStack.getAmount() + panelResultStack.getAmount() > panelResultStack.getMaxStackSize())
                        return false;
                }
            }
        }
        return true;
    }
}
