package cc.cassian.immersiveminimaps.mixin;

import cc.cassian.immersiveminimaps.ModClient;
import cc.cassian.immersiveminimaps.overlay.MinimapOverlay;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import garden.hestia.hoofprint.Hoofprint;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@IfModLoaded("hoofprint")
@Mixin(Hoofprint.class)
public class HoofprintMixin {

    @Inject(method = "lambda$onInitializeClient$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"), cancellable = true)
    private static void minimapImportantItem(Minecraft c, CallbackInfo ci) {
        if (ModClient.CONFIG.apply_requirements_to_hoofprint && !MinimapOverlay.showMinimap) {
            ci.cancel();
        }
    }
}
