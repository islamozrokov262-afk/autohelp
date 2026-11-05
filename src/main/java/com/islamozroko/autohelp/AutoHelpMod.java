package com.islamozroko.autohelp;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class AutoHelpMod implements ClientModInitializer {
    private static KeyMapping helpKey;
    private static int cooldown = 0;
    
    // Используем Identifier вместо ResourceLocation
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("autohelp", "main")
    );

    @Override
    public void onInitializeClient() {
        helpKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.autohelp.send",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_H,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (cooldown > 0) {
                cooldown--;
            }

            while (helpKey.consumeClick()) {
                if (client.player != null && cooldown <= 0) {
                    Player player = client.player;
                    
                    int x = (int) player.getX();
                    int y = (int) player.getY();
                    int z = (int) player.getZ();
                    
                    String msg = String.format("!хелп на %d, %d, %d", x, y, z);
                    
                    if (client.player.connection != null) {
                        client.player.connection.sendChat(msg);
                    }
                    
                    cooldown = 40;
                }
            }
        });
    }
}
