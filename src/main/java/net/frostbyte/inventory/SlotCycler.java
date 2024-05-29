package net.frostbyte.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.frostbyte.inventory.config.ImprovedInventoryConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SlotCycler implements ClientTickEvents.EndTick, HudRenderCallback {
    public static KeyBinding cycleUpKey;
    public static KeyBinding cycleDownKey;
    MinecraftClient mc;
    Identifier extraSlots = new Identifier(ImprovedInventory.MOD_ID, "textures/extra_slots.png");

    public void setKeyBindings() {
        KeyBindingHelper.registerKeyBinding(cycleUpKey = new KeyBinding("Cycle Slot Up", InputUtil.Type.KEYSYM, InputUtil.GLFW_KEY_J, "Improved Inventory"));
        KeyBindingHelper.registerKeyBinding(cycleDownKey = new KeyBinding("Cycle Slot Down", InputUtil.Type.KEYSYM, InputUtil.GLFW_KEY_H, "Improved Inventory"));
    }

    @Override
    public void onEndTick(MinecraftClient client) {
        ClientPlayerEntity player;
        mc = client;
        player = client.player;

        if (player==null)
            return;

        if (cycleUpKey.wasPressed()){
            cycleUp(player);
        }
        if (cycleDownKey.wasPressed()){
            cycleDown(player);
        }

        player.getInventory().markDirty();
    }

    void cycleDown(ClientPlayerEntity player) {
        int current = player.getInventory().selectedSlot;
        int target = current;
        int top = 9 + current;
        int middle = 18 + current;
        int bottom = 27 + current;
        for (int i = 1; i < 4; i++) {
            if (!player.getInventory().getStack(i * 9 + current).isEmpty()) {
                target = i * 9 + current;
                break;
            }
        }
        if (target == top) {
            assert mc.interactionManager != null;
            assert mc.player != null;
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, top, current, SlotActionType.SWAP, mc.player);
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, middle, current, SlotActionType.SWAP, mc.player);
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, bottom, current, SlotActionType.SWAP, mc.player);
            mc.player.getInventory().markDirty();
        } else if (target == middle) {
            assert mc.interactionManager != null;
            assert mc.player != null;
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, middle, current, SlotActionType.SWAP, mc.player);
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, bottom, current, SlotActionType.SWAP, mc.player);
            mc.player.getInventory().markDirty();
        } else if (target == bottom) {
            assert mc.interactionManager != null;
            assert mc.player != null;
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, bottom, current, SlotActionType.SWAP, mc.player);
            mc.player.getInventory().markDirty();
        }
    }

    void cycleUp(ClientPlayerEntity player) {
        int current = player.getInventory().selectedSlot;
        int target = current;
        int top = 9 + current;
        int middle = 18 + current;
        int bottom = 27 + current;
        for (int i = 1; i < 4; i++) {
            if (!player.getInventory().getStack(i * 9 + current).isEmpty()) {
                target = i * 9 + current;
                break;
            }
        }
        if (target == top) {
            assert mc.interactionManager != null;
            assert mc.player != null;
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, bottom, current, SlotActionType.SWAP, mc.player);
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, middle, current, SlotActionType.SWAP, mc.player);
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, top, current, SlotActionType.SWAP, mc.player);
            mc.player.getInventory().markDirty();
        } else if (target == middle) {
            assert mc.interactionManager != null;
            assert mc.player != null;
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, bottom, current, SlotActionType.SWAP, mc.player);
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, middle, current, SlotActionType.SWAP, mc.player);
            mc.player.getInventory().markDirty();
        } else if (target == bottom) {
            assert mc.interactionManager != null;
            assert mc.player != null;
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, bottom, current, SlotActionType.SWAP, mc.player);
            mc.player.getInventory().markDirty();
        }
    }

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        assert mc.player != null;
        if (!mc.player.isSpectator() && ImprovedInventoryConfig.slotCycle && !mc.options.hudHidden) {
            int x = 0;
            int y = 0;
            mc = MinecraftClient.getInstance();
            if (mc != null) {
                int width = mc.getWindow().getScaledWidth();
                int height = mc.getWindow().getScaledHeight();
                x = width / 2;
                y = height;
            }
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, extraSlots);
            assert mc.player != null;
            if (mc.player.getMainArm().equals(Arm.LEFT)) {
                drawContext.drawTexture(new Identifier(ImprovedInventory.MOD_ID, "textures/extra_slots.png"), x - 160, y - 23, 0, 0, 62, 24, 62, 24);
            } else {
                drawContext.drawTexture(new Identifier(ImprovedInventory.MOD_ID, "textures/extra_slots.png"), x + 98, y - 23, 0, 0, 62, 24, 62, 24);
            }
            for (int i = 3; i > 0; i--) {
                if (!mc.player.getInventory().getStack(i * 9 + mc.player.getInventory().selectedSlot).isEmpty()) {
                    if (mc.player.getMainArm().equals(Arm.LEFT)) {
                        drawContext.drawItem(mc.player.getInventory().getStack(i * 9 + mc.player.getInventory().selectedSlot), x - 157, y - 19);
                    } else {
                        drawContext.drawItem(mc.player.getInventory().getStack(i * 9 + mc.player.getInventory().selectedSlot), x + 101, y - 19);
                    }
                    break;
                }
            }
            if (mc.player.getMainArm().equals(Arm.LEFT)) {
                drawContext.drawItem(mc.player.getInventory().getMainHandStack(), x - 137, y - 19);
            } else {
                drawContext.drawItem(mc.player.getInventory().getMainHandStack(), x + 121, y - 19);
            }
            for (int i = 1; i < 4; i++) {
                if (!mc.player.getInventory().getStack(i * 9 + mc.player.getInventory().selectedSlot).isEmpty()) {
                    if (mc.player.getMainArm().equals(Arm.LEFT)) {
                        drawContext.drawItem(mc.player.getInventory().getStack(i * 9 + mc.player.getInventory().selectedSlot), x - 117, y - 19);
                    } else {
                        drawContext.drawItem(mc.player.getInventory().getStack(i * 9 + mc.player.getInventory().selectedSlot), x + 141, y - 19);
                    }
                    break;
                }
            }
        }
    }
}
