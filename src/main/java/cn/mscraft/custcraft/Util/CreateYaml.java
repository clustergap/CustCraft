package cn.mscraft.custcraft.Util;

import cn.mscraft.custcraft.CustCraft;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CreateYaml {
    public static class RunResponse<T> {
        public static final String PLUGIN_RESOURCE_NOT_FOUND = "plugin.reource.not.found";

        public static final String PLUGIN_FILE_EXISTS = "plugin.file.exists";

        public String error;

        public File file;

        public boolean success;

        public T param;

        public RunResponse(boolean success, File file, String errorMessage) {
            this(success, file, errorMessage, null);
        }

        public RunResponse(boolean success, File file, String errorMessage, T param) {
            this.success = success;
            this.file = file;
            this.error = errorMessage;
            this.param = param;
        }
    }

    private CreateYaml() {
        throw new IllegalArgumentException();
    }

    public static <T> RunResponse<T> copyFile(Plugin pl, String path, boolean cover) throws IOException {
        pl.getClass();
        path.getClass();
        File df = pl.getDataFolder();
        File to = new File(df, path);
        if (to.isFile() && !cover)
            return new RunResponse<>(true, to, "plugin.file.exists");
        InputStream is = pl.getResource(path);
        if (is == null)
            return new RunResponse<>(false, to, "plugin.reource.not.found");
        try (InputStream ct = is) {
            byte[] buffer = new byte[1024];
            (new File(to, "..")).mkdirs();
            to.createNewFile();
            try (FileOutputStream fos = new FileOutputStream(to)) {
                while (true) {
                    int leng = ct.read(buffer);
                    if (leng == -1)
                        break;
                    fos.write(buffer, 0, leng);
                }
            }
        }
        return new RunResponse<>(true, to, null);
    }

    public static RunResponse<FileConfiguration> copyAndLoad(Plugin main, String path) throws IOException, InvalidConfigurationException {
        RunResponse<FileConfiguration> r = copyFile(main, path, false);
        if (!r.success)
            return r;
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.load(r.file);
        r.param = (FileConfiguration) yamlConfiguration;
        return r;
    }

    public static FileConfiguration load(String path) throws IOException, InvalidConfigurationException {
        return (FileConfiguration)(copyAndLoad((Plugin) CustCraft.instance, path)).param;
    }
}
