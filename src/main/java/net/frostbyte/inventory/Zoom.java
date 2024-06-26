package net.frostbyte.inventory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.frostbyte.inventory.config.ImprovedInventoryConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.sound.SoundEvents;

@Environment(EnvType.CLIENT)
public class Zoom implements ClientTickEvents.EndTick {
    int standardFOV = 90;
    public static KeyBinding zoomKey;
    public void setKeyBindings() {
        KeyBindingHelper.registerKeyBinding(zoomKey = new KeyBinding("Zoom", InputUtil.Type.KEYSYM, InputUtil.GLFW_KEY_C, "Improved Inventory"));
    }
    @Override
    public void onEndTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }

        if (client.currentScreen == null) {
            if (zoomKey.isPressed()) {
                if (client.options.getFov().getValue() == standardFOV) {
                    client.player.playSound(SoundEvents.ITEM_SPYGLASS_USE, 1.0F, 1.0F);
                    client.options.getFov().setValue(ImprovedInventoryConfig.zoomFOV);
                }
            } else {
                if (client.options.getFov().getValue() == ImprovedInventoryConfig.zoomFOV) {
                    client.player.playSound(SoundEvents.ITEM_SPYGLASS_STOP_USING, 1.0F, 1.0F);
                    client.options.getFov().setValue(standardFOV);
                }
            }
        } else {
            if (client.currentScreen.shouldPause()) {
                if (client.options.getFov().getValue() == ImprovedInventoryConfig.zoomFOV) {
                    client.player.playSound(SoundEvents.ITEM_SPYGLASS_STOP_USING, 1.0F, 1.0F);
                    client.options.getFov().setValue(standardFOV);
                } else if (client.options.getFov().getValue() != standardFOV) {
                    standardFOV = client.options.getFov().getValue();
                }
            } else {
                if (client.options.getFov().getValue() == ImprovedInventoryConfig.zoomFOV) {
                    client.player.playSound(SoundEvents.ITEM_SPYGLASS_STOP_USING, 1.0F, 1.0F);
                    client.options.getFov().setValue(standardFOV);
                }
            }
        }
    }
}
