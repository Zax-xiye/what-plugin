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
        String plainMessage = PlainTextComponentSerializer.plainText().serialize(message).trim();

        // 问号
        if ((plainMessage.equals("?") || plainMessage.equals("？"))
                && plugin.getConfig().getBoolean("features.question-mark", true)) {
            spawnSymbol(event.getPlayer(), "?", NamedTextColor.YELLOW);
        }
        // 感叹号
        else if ((plainMessage.equals("!") || plainMessage.equals("！"))
                && plugin.getConfig().getBoolean("features.exclamation-mark", true)) {
            spawnSymbol(event.getPlayer(), "!", NamedTextColor.RED);
        }
        // 省略号
        else if ((plainMessage.equals("...") || plainMessage.equals("。。。"))
                && plugin.getConfig().getBoolean("features.ellipsis", true)) {
            spawnSymbol(event.getPlayer(), "...", NamedTextColor.WHITE);
        }
    }

    private void spawnSymbol(Player player, String text, NamedTextColor color) {
        // 在主线程生成实体
        Bukkit.getScheduler().runTask(plugin, () -> {
            Location location = player.getEyeLocation();
            location.add(0, 0.5, 0);

            // 创建 TextDisplay 实体
            TextDisplay display = player.getWorld().spawn(location, TextDisplay.class, entity -> {
                entity.text(Component.text(text).color(color)); 
                entity.setBillboard(Display.Billboard.CENTER);
                entity.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                entity.setShadowed(true);

                // 调整缩放
                Transformation transformation = entity.getTransformation();
                transformation.getScale().set(1.5f, 1.5f, 1.5f);
                entity.setTransformation(transformation);

                entity.setPersistent(false);
                entity.addScoreboardTag("what_plugin_mark");
            });

            // 骑在玩家头上
            player.addPassenger(display);

            // 调整位置
            Transformation transformation = display.getTransformation();
            transformation.getTranslation().set(0.45f, -0.35f, 0);
            display.setTransformation(transformation);

            Bukkit.getScheduler().runTaskLater(plugin, display::remove, 60L);
        });
    }
}
