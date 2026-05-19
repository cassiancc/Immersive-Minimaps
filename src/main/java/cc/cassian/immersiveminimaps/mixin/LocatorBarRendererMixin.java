package cc.cassian.immersiveminimaps.mixin;

import cc.cassian.immersiveminimaps.overlay.MinimapHelpers;
import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
//? if >26 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.ARGB;
import net.minecraft.world.waypoints.PartialTickSupplier;
import net.minecraft.world.waypoints.TrackedWaypoint;
//?}
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@IfMinecraftVersion(minVersion = "26.1")
//? if >26 {
@Mixin(net.minecraft.client.gui.contextualbar.LocatorBarRenderer.class)
//?} else {
/*@Mixin(Entity.class)
*///?}
public class LocatorBarRendererMixin {

    //? if >26 {
    @Inject(method = "lambda$extractRenderState$1", at = @At(value = "RETURN"))
    private static void minimapImportantItem(Entity cameraEntity, Level level, PartialTickSupplier partialTickSupplier, GuiGraphicsExtractor graphics, int top, TrackedWaypoint waypoint, CallbackInfo ci) {
        waypoint.id().ifLeft(id->{
            int color = ARGB.setBrightness(ARGB.color(255, id.hashCode()), 0.9F);
            MinimapHelpers.PLAYER_LOCATOR_BAR_COLOURS.put(id, color | 0xFF000000);
        });
    }
    //?}
}
