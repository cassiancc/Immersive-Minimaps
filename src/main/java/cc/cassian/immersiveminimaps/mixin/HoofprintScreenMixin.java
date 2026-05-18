package cc.cassian.immersiveminimaps.mixin;

import cc.cassian.immersiveminimaps.compat.ImmersiveOverlaysCompat;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import garden.hestia.hoofprint.HoofprintScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@IfModLoaded("immersiveoverlays")
@Mixin(HoofprintScreen.class)
public class HoofprintScreenMixin {

    @Shadow
    private int hoveredWorldX;

    @Shadow
    private int hoveredWorldZ;

    //? if >26 {
    @WrapOperation(method = "lambda$render$3", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    private <E> boolean minimapImportantItem(List<Component> instance, E e, Operation<Boolean> original, @Local(argsOnly = true, name = "y") int y) {
        return ImmersiveOverlaysCompat.replaceCoordinatesWithImmersiveOverlaysCoordinates(instance, e, original, hoveredWorldX, y, hoveredWorldZ);
    }

    @WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    private <E> boolean minimapImportantItem(List<Component> instance, E e, Operation<Boolean> original) {
        return ImmersiveOverlaysCompat.replaceCoordinatesWithImmersiveOverlaysCoordinates(instance, e, original, hoveredWorldX, null, hoveredWorldZ);
    }
    //?} else {
    /*@WrapOperation(method = "lambda$render$3", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    private <E> boolean minimapImportantItem(List<Component> instance, E e, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) int y) {
        return ImmersiveOverlaysCompat.replaceCoordinatesWithImmersiveOverlaysCoordinates(instance, e, original, hoveredWorldX, y, hoveredWorldZ);
    }
    
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    private <E> boolean minimapImportantItem(List<Component> instance, E e, Operation<Boolean> original) {
        return ImmersiveOverlaysCompat.replaceCoordinatesWithImmersiveOverlaysCoordinates(instance, e, original, hoveredWorldX, null, hoveredWorldZ);
    }
    *///?}


}
