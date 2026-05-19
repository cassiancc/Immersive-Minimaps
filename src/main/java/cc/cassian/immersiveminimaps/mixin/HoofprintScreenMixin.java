package cc.cassian.immersiveminimaps.mixin;

import cc.cassian.immersiveminimaps.ModClient;
import cc.cassian.immersiveminimaps.overlay.MinimapHelpers;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import garden.hestia.hoofprint.HoofprintScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.awt.*;
import java.util.UUID;

@IfModLoaded("hoofprint")
@Mixin(HoofprintScreen.class)
public class HoofprintScreenMixin {

    //? if >26 {
    @WrapOperation(method = "renderPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ARGB;color(IIII)I", ordinal = 0))
    private int setHoofprintColor(int alpha, int red, int green, int blue, Operation<Integer> original, @Local(argsOnly = true) UUID id, @Local(ordinal = 0) boolean friend) {
        if (ModClient.CONFIG.style.draw_players_with_locator_bar_colours && ModClient.CONFIG.apply_requirements_to_hoofprint) {
            return MinimapHelpers.getPlayerMapColour(id, friend);
        } else {
            return original.call(alpha, red, green, blue);
        }
    }
    //?}


}
