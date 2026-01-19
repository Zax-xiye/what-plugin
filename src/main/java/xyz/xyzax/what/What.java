package xyz.xyzax.what;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public final class What extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        getLogger().info("What? 已加载！");

        // 注册监听器
        getServer().getPluginManager().registerEvents(new WhatListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("What? 已卸载！");

        // 清理所有残留的问号
        getServer().getWorlds().forEach(world -> {
            world.getEntities().stream()
                    .filter(entity -> entity.getScoreboardTags().contains("what_plugin_mark"))
                    .forEach(Entity::remove);
        });
    }
}
