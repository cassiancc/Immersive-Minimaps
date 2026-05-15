package cc.cassian.immersiveminimaps.mixin;

import cc.cassian.immersiveminimaps.ModClient;
import cc.cassian.immersiveminimaps.overlay.MinimapHelpers;
import cc.cassian.immersiveoverlays.overlay.OverlayHelpers;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(OverlayHelpers.class)
public class OverlayHelpersMixin {
    @Inject(method = "isImportantItem", at = @At(value = "HEAD"))
    private static void mixin(ItemStack itemStack, CallbackInfo ci) {
        if (ModClient.CONFIG.requirements.immersive_overlays_bridge)
            MinimapHelpers.isImportantItem(itemStack);
    }
}
