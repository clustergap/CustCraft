package cn.mscraft.custcraft.Loader.Lang;

import cn.mscraft.custcraft.CustCraft;
import cn.mscraft.custcraft.Util.CreateYaml;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.Currency;
import java.util.logging.Level;

public class CreateLangYaml {
    static final FileConfiguration langYaml = (FileConfiguration) new YamlConfiguration();

    public static void reload() {
        String lang = "lang_cn";
        if (CustCraft.instance.getConfig().getString("language").equals("lang_en")) {
            lang = "lang_en";
        } else if (CustCraft.instance.getConfig().getString("language").equals("lang_ru")) {
            lang = "lang_ru";
        }
        try {
            CreateYaml.RunResponse<Void> run = CreateYaml.copyFile(CustCraft.instance, lang + ".yml", false);
            langYaml.load(run.file);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException error) {
            CustCraft.instance.getLogger().log(Level.WARNING, "Œﬁ∑®º”‘ÿ£∫" + lang + ".yml", error);
        }
        GetLangYaml.get();
    }

    static {
        reload();
    }
}
