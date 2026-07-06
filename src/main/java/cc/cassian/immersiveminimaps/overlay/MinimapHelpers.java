package cc.cassian.immersiveminimaps.overlay;

import cc.cassian.immersiveminimaps.ModClient;
import cc.cassian.immersiveminimaps.helpers.ModLists;
import cc.cassian.mru.Platform;
import cc.cassian.mru.client.util.HudUtils;
import cc.cassian.mru.util.ItemContainerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static cc.cassian.immersiveminimaps.ModClient.CONFIG;
import static cc.cassian.immersiveminimaps.helpers.ColorUtil.color;

public class MinimapHelpers {

	private static final int GREEN = color(255, 0, 255, 76);
	private static final int WHITE = color(255, 255, 255, 255);
	public static HashMap<UUID, Integer> PLAYER_LOCATOR_BAR_COLOURS = new HashMap<>();


	public static Integer getPlayerMapColour(UUID uuid, boolean friend) {
		// singleplayer is always white
		if (!friend) return WHITE;
		// check if the locator bar chose a color for this player already, otherwise default to green
		if (CONFIG.style.draw_players_with_locator_bar_colours)
			return PLAYER_LOCATOR_BAR_COLOURS.getOrDefault(uuid, GREEN);
		else return GREEN;
	}

	public static void checkInventoryForOverlays(Minecraft minecraft){
		if ((CONFIG.minimap_enable)  && minecraft.level != null) {
			MinimapHelpers.checkInventoryForItems(minecraft.player);
		}
	}

	public static boolean playerHasPotions(Player player) {
		if (!CONFIG.moved_by_effects) return false;
		// Technically, we should check whether these are ambient,
		// but Map Atlases doesn't and still covers our overlay.
		// return Player.areAllEffectsAmbient(player.getActiveEffects());
		return !player.getActiveEffects().isEmpty();
	}

	public static int moveBy(Player player) {
		boolean hasBeneficial =
				player.getActiveEffects().stream().anyMatch(p -> p.getEffect()
						//? if >1.21
						.value()
						.isBeneficial());
		boolean hasNegative =
				player.getActiveEffects().stream().anyMatch(p -> !p.getEffect()
						//? if >1.21
						.value()
						.isBeneficial());
		if (hasNegative) return 50;
		else if (hasBeneficial) return 24;
		else return 0;
	}

	public static boolean shouldCancelRender(Minecraft mc) {
		return HudUtils.shouldCancelRender(mc, CONFIG.minimap_enable, CONFIG.hide_from_debug);
	}

	private static void findImportantContainerContents(ItemStack container) {
		List<ItemStack> list = ItemContainerUtils.getContainerContents(container).toList();
		for (ItemStack itemStack : list) {
			if (CONFIG.requirements.search_containers_for_containers) {
				isImportantItemOrContainer(itemStack);
			} else {
				isImportantItem(itemStack);
			}
		}
	}

	public static void isImportantItem(ItemStack itemStack) {
		if (itemStack.isEmpty())
			return;
		var item = itemStack.getItem();
		if (ModLists.items.contains(item))
			MinimapOverlay.showMinimap = true;
	}

	public static void checkInventoryForItems(@Nullable Player player) {
		// use immersive overlays config instead
		if (shouldSearchInsideImmersiveOverlaysContainers()) return;
		if (CONFIG.requirements.require_item) {
			MinimapOverlay.showMinimap = false;
			if (player == null)
				return;
			isImportantItemOrContainer(player.getOffhandItem());
			if (CONFIG.requirements.require_item_in_hand) {
				isImportantItemOrContainer(player.getMainHandItem());
			} else {
				ItemContainerUtils.checkPlayerForImportantItems(player, MinimapHelpers::isImportantItem, CONFIG.requirements.search_containers, CONFIG.requirements.search_containers_for_containers);
			}
		} else {
			MinimapOverlay.showMinimap = true;
		}
	}

	public static void isImportantItemOrContainer(ItemStack stack) {
		isImportantItem(stack);
		if (isContainer(stack)) {
			findImportantContainerContents(stack);
		}
	}


	public static boolean isContainer(ItemStack stack) {
		if (!CONFIG.requirements.search_containers) return false;
		return ItemContainerUtils.isContainer(stack);
	}

	static boolean hasBeenToggled = false;

	public static void checkKeybind() {
		if (ModClient.zoomIn.isDown() && !hasBeenToggled) {
			MinimapOverlay.INSTANCE.zoomIn();
			hasBeenToggled = true;
		} else if (ModClient.zoomOut.isDown() && !hasBeenToggled) {
			MinimapOverlay.INSTANCE.zoomOut();
			hasBeenToggled = true;
		} else if (ModClient.caveMode.isDown() && !hasBeenToggled) {
			MinimapOverlay.INSTANCE.caveMode();
			hasBeenToggled = true;
		} else if (!ModClient.zoomIn.isDown() && !ModClient.zoomOut.isDown() && !ModClient.caveMode.isDown()) {
			hasBeenToggled = false;
		}
	}

    public static boolean shouldUseImmersiveOverlaysSettings() {
        return Platform.INSTANCE.isLoaded("immersiveoverlays") && CONFIG.immersive_overlays_bridge;
    }

	public static boolean shouldSearchInsideImmersiveOverlaysContainers() {
		return shouldUseImmersiveOverlaysSettings() && CONFIG.requirements.require_item && CONFIG.requirements.search_containers && !CONFIG.requirements.require_item_in_hand;
	}
}