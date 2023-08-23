package cn.mscraft.custcraft;

import cn.mscraft.custcraft.EventListener.InventoryClick;
import cn.mscraft.custcraft.EventListener.InventoryClose;
import cn.mscraft.custcraft.EventListener.InventoryDrag;
import cn.mscraft.custcraft.EventListener.PlayerInteract;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import java.util.Arrays;

public class RegisterEvent {
    void run() {
        for (Listener listener : Arrays.<Listener>asList(new Listener[]{
                (Listener) new InventoryClick(),
                (Listener) new InventoryClose(),
                (Listener) new InventoryDrag(),
                (Listener) new PlayerInteract()
        }))
            CustCraft.instance.getServer().getPluginManager().registerEvents(listener, (Plugin) CustCraft.instance);
    }
}
