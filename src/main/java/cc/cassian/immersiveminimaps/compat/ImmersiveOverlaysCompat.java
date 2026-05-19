package cc.cassian.immersiveminimaps.compat;

import cc.cassian.immersiveminimaps.ModClient;
import cc.cassian.immersiveminimaps.overlay.MinimapHelpers;
import cc.cassian.immersiveoverlays.config.ModConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static cc.cassian.immersiveoverlays.overlay.CompassOverlay.*;

public class ImmersiveOverlaysCompat {
    public static <E> boolean replaceCoordinatesWithImmersiveOverlaysCoordinates(List<Component> instance, E e, Operation<Boolean> original, int x, @Nullable Integer y, int z) {
        if (ModClient.CONFIG.apply_requirements_to_hoofprint && MinimapHelpers.shouldUseImmersiveOverlaysSettings()) {
            MutableComponent line = Component.empty();
            Style textStyle = Style.EMPTY.withColor(ModConfig.get().compass_text_colour);
            if (showX) {
                line.append(xText(x + ", ", textStyle));
            }

            if (showY && y != null) {
                line.append(yText(y + ", ", textStyle));
            }

            if (showZ) {
                line.append(zText(String.valueOf(z), textStyle));
            }
            if (showX || (showY && y != null) || showZ)
                return instance.add(line);
            return false;
        }
        else return original.call(instance, e);
    }

    private static Component xText(String x, Style textStyle) {
        MutableComponent xLiteral = Component.literal(x).withStyle(textStyle);
        return Component.translatable("gui.immersiveoverlays.coordinates.x", xLiteral).withStyle(Style.EMPTY.withColor(ModConfig.get().compass_x_colour));
    }

    private static Component yText(String y, Style textStyle) {
        MutableComponent yLiteral = Component.literal(y).withStyle(textStyle);
        return Component.translatable("gui.immersiveoverlays.coordinates.y", new Object[]{yLiteral}).withStyle(Style.EMPTY.withColor(ModConfig.get().compass_y_colour));
    }

    private static Component zText(String z, Style textStyle) {
        MutableComponent zLiteral = Component.literal(z).withStyle(textStyle);
        return Component.translatable("gui.immersiveoverlays.coordinates.z", new Object[]{zLiteral}).withStyle(Style.EMPTY.withColor(ModConfig.get().compass_z_colour));
    }
}
