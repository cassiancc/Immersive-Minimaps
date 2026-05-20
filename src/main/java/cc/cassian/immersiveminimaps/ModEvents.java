package cc.cassian.immersiveminimaps;

import cc.cassian.immersiveminimaps.helpers.ModLists;
import folk.sisby.surveyor.Surveyor;
import folk.sisby.surveyor.landmark.Landmark;
import folk.sisby.surveyor.landmark.WorldLandmarks;
import folk.sisby.surveyor.landmark.component.LandmarkComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class ModEvents {
    public static InteractionResult placeWaypoint(Item item, Level level, Player player, UUID owner, BlockPos pos) {
        if (ModClient.CONFIG.items_place_landmarks && ModLists.items.contains(item)) {
            if (level.isClientSide()) {
                WorldLandmarks landmarks = WorldLandmarks.of(level);
                if (landmarks == null) {
                    sendOverlayMessage(player, Component.translatable("landmark.immersiveminimaps.disabled").withStyle(ChatFormatting.YELLOW));
                } else {
                    Identifier id = Surveyor.id("block/%s/%s/%s".formatted(pos.getX(), pos.getY(), pos.getZ()));

                    if (landmarks.contains(owner, id)) {
                        landmarks.removeAll(landmark -> {
                            BlockPos landmarkPos = landmark.get(LandmarkComponentTypes.POS);
                            if (landmarkPos == null) return false;
                            return landmark.owner() == owner && landmarkPos.equals(pos);
                        });
                        sendOverlayMessage(player, Component.translatable("landmark.immersiveminimaps.removed", pos.toShortString()).withStyle(ChatFormatting.GREEN));
                    } else {
                        landmarks.put(Landmark.create(owner, id, (builder) -> LandmarkComponentTypes.forBlock(builder, level, pos)));
                        sendOverlayMessage(player, Component.translatable("landmark.immersiveminimaps.added", pos.toShortString()).withStyle(ChatFormatting.GREEN));
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private static void sendOverlayMessage(Player player, MutableComponent mutableComponent) {
        //? if >26 {
        player.sendOverlayMessage(mutableComponent);
        //?} else {
        /*player.displayClientMessage(mutableComponent, true);
        *///?}
    }
}
