package cn.mscraft.custcraft;

import cn.mscraft.custcraft.API.IManager;
import cn.mscraft.custcraft.Command.MainCmd;
import cn.mscraft.custcraft.Loader.Lang.CreateLangYaml;
import cn.mscraft.custcraft.Loader.Yaml;
import cn.mscraft.custcraft.Model.ICraftTechHolder;
import cn.mscraft.custcraft.Model.Panel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public final class CustCraft extends JavaPlugin {

    public static JavaPlugin instance;
    public static Info info;

    @Override
    public void onEnable() {
        instance = this;
        info = new Info(Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3], getServer().getName());

        this.saveDefaultConfig();
        File modelFile = new File(this.getDataFolder() + File.separator + "model");
        if (!modelFile.exists()) {
            modelFile.mkdir();
        }
        (new RegisterEvent()).run();
        CreateLangYaml.reload();
        Resource.saveResource("Recipe.yml", File.separator + "Model", false);
        Resource.saveResource("Panel.yml", File.separator + "Model", false);
        Yaml.loadPanels();
        Yaml.loadRecipes();
        Bukkit.getPluginCommand("icraft").setExecutor((CommandExecutor)new MainCmd());
        IManager.titleSelGui.put("selTitle", CustCraft.instance.getConfig().getString("selectGui.title").replaceAll("&", "§"));
    }

    @Override
    public void onDisable() {
        if (!Bukkit.getOnlinePlayers().isEmpty())
            for (Player player : Bukkit.getOnlinePlayers()) {
                Inventory inventory = player.getOpenInventory().getTopInventory();
                InventoryHolder holder = inventory.getHolder();
                if (!(holder instanceof ICraftTechHolder))
                    continue;
                String id = ((ICraftTechHolder)holder).getPanelId();
                if (id != null) {
                    player.closeInventory();
                    Optional<Panel> optionalPanel = IManager.getPanel(id);
                    if (optionalPanel.isPresent()) {
                        Panel panel = optionalPanel.get();
                        List<Integer> matrix = panel.getMatrixSlots();
                        List<Integer> resultSlots = panel.getResultsSlots();
                        Location location = player.getLocation();
                        World world = location.getWorld();
                        if (world == null)
                            return;
                        Iterator<Integer> iterator;
                        for (iterator = matrix.iterator(); iterator.hasNext(); ) {
                            int i = ((Integer)iterator.next()).intValue();
                            ItemStack item = inventory.getItem(i);
                            if (item != null)
                                world.dropItem(location, item);
                        }
                        for (iterator = resultSlots.iterator(); iterator.hasNext(); ) {
                            int i = ((Integer)iterator.next()).intValue();
                            ItemStack item = inventory.getItem(i);
                            if (item != null) {
                                if (player.getInventory().first(item) == -1 && player.getInventory().firstEmpty() == -1) {
                                    world.dropItem(location, item);
                                    continue;
                                }
                                player.getInventory().addItem(new ItemStack[] { item });
                            }
                        }
                    }
                }
            }
    }
}
