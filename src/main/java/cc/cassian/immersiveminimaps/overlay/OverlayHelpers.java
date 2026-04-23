package cc.cassian.immersiveminimaps.overlay;

import cc.cassian.immersiveminimaps.ModClient;
import cc.cassian.immersiveminimaps.config.ModConfig;
import cc.cassian.immersiveminimaps.helpers.ModLists;
import net.minecraft.client.DeltaTracker;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.client.gui.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

import static cc.cassian.immersiveminimaps.ModClient.CONFIG;

public class OverlayHelpers {

	public static void checkInventoryForOverlays(Minecraft minecraft){
		if ((CONFIG.minimap_enable)  && minecraft.level != null) {
			OverlayHelpers.checkInventoryForItems(minecraft.player);
		}
	}

	public static boolean playerHasPotions(Player player, boolean leftAlign) {
		if (!CONFIG.moved_by_effects) return false;
		if (!leftAlign) return false;
		// Technically, we should check whether these are ambient,
		// but Map Atlases doesn't and still covers our overlay.
		// return Player.areAllEffectsAmbient(player.getActiveEffects());
		return !player.getActiveEffects().isEmpty();
	}

	public static int moveBy(Player player) {
		boolean hasBeneficial =
				player.getActiveEffects().stream().anyMatch(p -> p.getEffect().value().isBeneficial());
		boolean hasNegative =
				player.getActiveEffects().stream().anyMatch(p -> !p.getEffect().value().isBeneficial());
		if (hasNegative) {
			return 42;
		} else if (hasBeneficial) {
			return 16;
		}
		else return 0;
	}

	public static boolean shouldCancelRender(Minecraft mc) {
		if (mc.options.hideGui) return true;
		if (!CONFIG.minimap_enable) return true;
		//? if >26 {
		/*if (CONFIG.hide_from_debug) {
			return mc.debugEntries.isOverlayVisible();
		}
		*///?}
		return false;
	}

	private static void findImportantContainerContents(ItemStack container) {
		List<ItemStack> list = getContainerContents(container).toList();
		for (ItemStack itemStack : list) {
			if (CONFIG.search_containers_for_containers) {
				isImportantItemOrContainer(itemStack);
			} else {
				isImportantItem(itemStack);
			}
		}
	}

	private static void isImportantItem(ItemStack itemStack) {
		if (itemStack.isEmpty())
			return;
		var item = itemStack.getItem();
//		if (ModLists.items.contains(item))
//			MinimapOverlay.show = true;
	}

	public static void checkInventoryForItems(Player player) {
		if (CONFIG.require_item) {
			setOverlays(false);
			if (player == null)
				return;
			isImportantItemOrContainer(player.getOffhandItem());
			for (EquipmentSlot value : EquipmentSlot.values()) {
				isImportantItemOrContainer(player.getItemBySlot(value));
			}
			checkInventoryForStack(player.getInventory());
		} else {
			setOverlays(true);
		}
	}

	private static void setOverlays(boolean b) {
//		MinimapOverlay.show = b;
	}

	public static void isImportantItemOrContainer(ItemStack stack) {
		isImportantItem(stack);
		if (isContainer(stack)) {
			findImportantContainerContents(stack);
		}
	}

	public static Stream<ItemStack> getContainerContents(ItemStack stack) {
		if (!isContainer(stack)) return Stream.empty();
		var components = stack.getComponents();
		if (components.has(DataComponents.BUNDLE_CONTENTS)) {
			BundleContents bundleContents = components.get(DataComponents.BUNDLE_CONTENTS);
			if (bundleContents != null)
				return bundleContents.itemCopyStream();
		}
		else if (components.has(DataComponents.CONTAINER)) {
			ItemContainerContents containerContents = components.get(DataComponents.CONTAINER);
			if (containerContents != null) {
				//? if <26 {
				return containerContents.stream();
				//?} else {
				/*return containerContents.allItemsCopyStream();
				*///?}

			}
		}
		return Stream.empty();
	}

	public static boolean isContainer(ItemStack stack) {
		if (!CONFIG.search_containers) return false;
		if (stack.isEmpty()) return false;
		var components = stack.getComponents();
		if (components.has(DataComponents.BUNDLE_CONTENTS)) {
			return true;
		}
		else if (components.has(DataComponents.CONTAINER)) {
			return true;
		}
		return true;
	}

	public static boolean checkInventoryForItem(Inventory inventory, Item item, boolean value) {
		if (value) return true;
		else return checkInventoryForStack(inventory, item) != ItemStack.EMPTY;
	}

	public static void checkInventoryForStack(Inventory inventory) {
		for (ItemStack stack :
				inventory.getNonEquipmentItems()
		) {
			isImportantItem(stack);
			if (isContainer(stack)) {
				findImportantContainerContents(stack);
			}
		}
	}

	public static @NotNull ItemStack checkInventoryForStack(Inventory inventory, Item item) {
		for (ItemStack stack : inventory.getNonEquipmentItems()
		) {
			if (stack.is(item)) return stack;
			else if (item != null && stack.is(item))
				return stack;
			else if (isContainer(stack)) {
				List<ItemStack> contents = getContainerContents(stack).toList();
				for (ItemStack content : contents) {
					if (item != null && content.is(item))
						return content;
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public static int getPlacement(int windowWidth, int fontWidth, boolean leftAlign) {
		if (leftAlign) {
			return 9;
		} else {
			return windowWidth-2-fontWidth;
		}
	}


	public static void drawString(GuiGraphics guiGraphics, Font font, Component text, int x, int y, Integer color) {
		color = ARGB.opaque(color);
		guiGraphics.drawString(font, text, x, y, color);
	}

	public static void drawString(GuiGraphics guiGraphics, Font font, String text, int x, int y, Integer color) {
		color = ARGB.opaque(color);
		guiGraphics.drawString(font, text, x, y, color);
	}

	public static void blit(GuiGraphics guiGraphics, Identifier texture, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight) {
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight);
	}

	public static void blitSprite(GuiGraphics GuiGraphics, String texture, int x, int y) {
		blitSprite(GuiGraphics, ModClient.locate("textures/gui/sprites/%s.png".formatted(texture)), x, y);
	}

	public static void blitSprite(GuiGraphics GuiGraphics, Identifier texture, int x, int y) {
		blitSprite(GuiGraphics, texture, x, y, 16);
	}

	public static void blitSprite(GuiGraphics GuiGraphics, Identifier texture, int x, int y, int size) {
		blit(GuiGraphics, texture, x, y, 0, 0, size, size, size, size);
	}

	static boolean hasBeenToggled = false;

	public static void checkKeybind() {
		if (!hasBeenToggled) {
			if (ModClient.zoomIn.isDown()) {
				MinimapOverlay.INSTANCE.setScale(1);
				hasBeenToggled = true;
			}
			else if (ModClient.zoomOut.isDown()) {
				MinimapOverlay.INSTANCE.setScale(-1);
				hasBeenToggled = true;
			} else {
				hasBeenToggled = false;
			}
		}
	}
}