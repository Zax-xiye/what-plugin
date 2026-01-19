package xyz.xyzax.what;

import org.bukkit.plugin.java.JavaPlugin;

public final class What extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("What? 已加载！");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("What? 已卸载！");
    }
}
