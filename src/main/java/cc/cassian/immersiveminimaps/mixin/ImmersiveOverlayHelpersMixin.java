package cc.cassian.immersiveminimaps.mixin;

import cc.cassian.immersiveminimaps.overlay.MinimapHelpers;
import cc.cassian.immersiveminimaps.overlay.MinimapOverlay;
import cc.cassian.immersiveoverlays.overlay.OverlayHelpers;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@IfModLoaded("immersiveoverlays")
@Mixin(OverlayHelpers.class)
public class ImmersiveOverlayHelpersMixin {
    @Inject(method = "isImportantItem", at = @At(value = "HEAD"))
    private static void minimapImportantItem(ItemStack itemStack, CallbackInfo ci) {
        if (MinimapHelpers.shouldUseBridge())
            MinimapHelpers.isImportantItem(itemStack);
    }

    @Inject(method = "setOverlays", at = @At(value = "HEAD"))
    private static void minimapRequireItem(boolean b, CallbackInfo ci) {
        if (MinimapHelpers.shouldUseBridge())
            MinimapOverlay.showMinimap = b;
    }
}
