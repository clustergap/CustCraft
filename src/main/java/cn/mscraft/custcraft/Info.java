package cn.mscraft.custcraft;

import java.util.Arrays;
import java.util.List;

public class Info {
    private String serverVersion;

    private String serverName;

    private List<String> oldVersion = Arrays.asList(new String[] { "v1_9_R1", "v1_9_R2", "v1_10_R1", "v1_11_R1", "v1_12_R1", "v1_13_R1", "v1_13_R2" });

    private List<String> oldMaterialVersion = Arrays.asList(new String[] { "v1_9_R1", "v1_9_R2", "v1_10_R1", "v1_11_R1", "v1_12_R1" });

    public Info(String ServerVersion, String serverName) {
        this.serverVersion = ServerVersion;
        this.serverName = serverName;
    }

    public String getServerVersion() {
        return this.serverVersion;
    }

    public String getServerName() {
        return this.serverName;
    }

    public boolean isOldVersion() {
        for (String s : this.oldVersion) {
            if (this.serverVersion.equals(s))
                return true;
        }
        return false;
    }

    public boolean isOldMaterialVersion() {
        for (String s : this.oldMaterialVersion) {
            if (this.serverVersion.equals(s))
                return true;
        }
        return false;
    }
}