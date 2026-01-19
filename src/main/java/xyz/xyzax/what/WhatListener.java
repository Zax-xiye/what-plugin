package xyz.xyzax.what;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Transformation;

public class WhatListener implements Listener {

    private final Plugin plugin;

    public WhatListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Component message = event.message();
        String plainMessage = PlainTextComponentSerializer.plainText().serialize(message);

        // 检查消息是否完全匹配 "?" 或 "？"
        if (plainMessage.trim().equals("?") || plainMessage.trim().equals("？")) {
            Player player = event.getPlayer();
            
            // 在主线程生成实体
            Bukkit.getScheduler().runTask(plugin, () -> {
                spawnQuestionMark(player);
            });
        }
    }

    private void spawnQuestionMark(Player player) {
        Location location = player.getEyeLocation();
        // 向玩家右侧偏移一点，基于玩家的朝向
        location.add(0, 0.5, 0); 

        // 创建 TextDisplay 实体
        TextDisplay display = player.getWorld().spawn(location, TextDisplay.class, entity -> {
            entity.text(Component.text("?").color(NamedTextColor.YELLOW)); // 大号黄色问号
            entity.setBillboard(Display.Billboard.CENTER); // 始终面向玩家
            entity.setBackgroundColor(Color.fromARGB(0, 0, 0, 0)); // 透明背景
            entity.setShadowed(true);
            
            // 调整缩放
            Transformation transformation = entity.getTransformation();
            transformation.getScale().set(1.5f, 1.5f, 1.5f);
            entity.setTransformation(transformation);
        });

        // 让它骑在玩家头上
        player.addPassenger(display);
        
        // 稍微调整偏移量，因为骑乘时位置是固定的
        // TextDisplay 的 translation 可以调整相对于骑乘实体的偏移
        Transformation transformation = display.getTransformation();
        // 调整位置到头侧面
        transformation.getTranslation().set(0.45f, -0.35f, 0);
        display.setTransformation(transformation);

        // 3秒后移除
        Bukkit.getScheduler().runTaskLater(plugin, display::remove, 60L);
    }
}
