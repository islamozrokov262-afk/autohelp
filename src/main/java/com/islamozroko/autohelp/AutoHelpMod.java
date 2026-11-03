package com.islamozroko.autohelp;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

public class AutoHelpMod implements ClientModInitializer {
    private static KeyMapping helpKey;
    private static int cooldown = 0;

    @Override
    public void onInitializeClient() {
        // Биндим на клавишу H
        helpKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.autohelp.send",
                GLFW.GLFW_KEY_H,
                "category.autohelp"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (cooldown > 0) {
                cooldown--;
            }

            if (helpKey.consumeClick() && client.player != null && cooldown <= 0) {
                Player player = client.player;
                
                // Берем координаты
                int x = (int) player.getX();
                int y = (int) player.getY();
                int z = (int) player.getZ();
                
                // Формируем сообщение
                String msg = String.format("!хелп на [%d, %d, %d]", x, y, z);
                
                // Отправляем в чат
                if (client.player.connection != null) {
                    client.player.connection.sendChat(msg);
                }
                
                // Кулдаун 2 секунды (40 тиков), чтобы не спамить
                cooldown = 40; 
            }
        });
    }
}
