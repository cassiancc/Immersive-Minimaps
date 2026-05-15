package cc.cassian.immersiveminimaps.overlay;

import cc.cassian.immersiveminimaps.ModClient;
import cc.cassian.immersiveminimaps.compat.ModCompat;
import cc.cassian.immersiveminimaps.compat.TrinketsCompat;
import cc.cassian.immersiveminimaps.helpers.ModLists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
//? if >1.21.2 {
import net.minecraft.client.renderer.RenderPipelines;
//?}
//? if >1.21 {
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.core.component.DataComponents;
//?} else {
/*import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
*///?}
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

import static cc.cassian.immersiveminimaps.ModClient.CONFIG;

public class MinimapHelpers {

	public static void checkInventoryForOverlays(Minecraft minecraft){
		if ((CONFIG.minimap_enable)  && minecraft.level != null) {
			MinimapHelpers.checkInventoryForItems(minecraft.player);
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

	//? if >1.21 {
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
	//?}

	public static boolean shouldCancelRender(Minecraft mc) {
		if (mc.options.hideGui) return true;
		if (!CONFIG.minimap_enable) return true;
		//? if >26 {
		if (CONFIG.hide_from_debug) {
			return mc.debugEntries.isOverlayVisible();
		}
		//?}
		return false;
	}

	private static void findImportantContainerContents(ItemStack container) {
		List<ItemStack> list = getContainerContents(container).toList();
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
		if (ModCompat.IMMERSIVE_OVERLAYS && CONFIG.requirements.immersive_overlays_bridge) return;
		if (CONFIG.requirements.require_item) {
			MinimapOverlay.showMinimap = false;
			if (player == null)
				return;
			isImportantItemOrContainer(player.getOffhandItem());
			if (CONFIG.requirements.require_item_in_hand) {
				isImportantItemOrContainer(player.getMainHandItem());
			} else {
				for (EquipmentSlot value : EquipmentSlot.values()) {
					isImportantItemOrContainer(player.getItemBySlot(value));
				}
				checkInventoryForStack(player.getInventory());
				//? if <26 {
				/*if (ModCompat.TRINKETS)
					TrinketsCompat.checkForImportantAccessories(player);
				*///?} else {
                if (ModCompat.TRINKETS_UPDATED)
                    TrinketsCompat.checkForImportantAccessories(player);
                //?}
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


	public static Stream<ItemStack> getContainerContents(ItemStack stack) {
		if (!isContainer(stack)) return Stream.empty();
		//? if >1.20.5 {
		var components = stack.getComponents();
		if (components.has(DataComponents.BUNDLE_CONTENTS)) {
			BundleContents bundleContents = components.get(DataComponents.BUNDLE_CONTENTS);
			if (bundleContents != null)
				return bundleContents.itemCopyStream();
		}
		else if (components.has(DataComponents.CONTAINER)) {
			ItemContainerContents containerContents = components.get(DataComponents.CONTAINER);
			if (containerContents != null) {
				//? >26 {
				return containerContents.allItemsCopyStream();
				 //?} else {
				/*return containerContents.stream();
				*///?}
			}
		}
		//?} else {
        /*CompoundTag compoundtag = stack.getTag();
        if (compoundtag == null) {
            return Stream.empty();
        } else {
            if (compoundtag.contains("Items")) {
                ListTag listtag = compoundtag.getList("Items", 10);
                return listtag.stream().map(CompoundTag.class::cast).map(ItemStack::of);
            }
            else if (compoundtag.contains("BlockEntityTag")) {
                var compound = compoundtag.getCompound("BlockEntityTag");
                ListTag listtag = compound.getList("Items", 10);
                return listtag.stream().map(CompoundTag.class::cast).map(ItemStack::of);
            }
        }
        *///?}
		return Stream.empty();
	}


	public static boolean isContainer(ItemStack stack) {
		if (!CONFIG.requirements.search_containers) return false;
		if (stack.isEmpty()) return false;
		//? if >1.20.5 {
		var components = stack.getComponents();
		if (components.has(DataComponents.BUNDLE_CONTENTS)) {
			return true;
		}
		else if (components.has(DataComponents.CONTAINER)) {
			return true;
		}
		//?} else {
        /*CompoundTag compoundtag = stack.getTag();
        if (compoundtag == null) {
            return false;
        } else {
            if (compoundtag.contains("Items")) {
                return true;
            }
            else if (compoundtag.contains("BlockEntityTag")) {
                if (compoundtag.getCompound("BlockEntityTag").contains("Items")) {
                    return true;
                }
            }
        }
        *///?}
		return true;
	}

	public static void checkInventoryForStack(Inventory inventory) {
		for (ItemStack stack :
				//? if >1.21.2 {
				inventory.getNonEquipmentItems()
				//?} else {
				/*inventory.items
				*///?}
		) {
			isImportantItem(stack);
			if (isContainer(stack)) {
				findImportantContainerContents(stack);
			}
		}
	}

	public static void blit(GuiGraphicsExtractor guiGraphics, Identifier texture, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight) {
		guiGraphics.blit(
				//? if >1.21.2
				RenderPipelines.GUI_TEXTURED,
				texture, x, y, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight);
	}

	public static void blit(GuiGraphicsExtractor guiGraphics, Identifier id, int x, int y, float u, float v, int width, int height, int uWidth, int vHeight, int textureWidth, int textureHeight, int color) {
		guiGraphics.blit(
				//? if >1.21.2 {
				RenderPipelines.GUI_TEXTURED,id, x, y, u, v, width, height, uWidth, vHeight, textureWidth, textureHeight, color
				//?} else {
				/*id, x, y, 0, u, v, uWidth, vHeight, textureWidth, textureHeight
				*///?}
		);
	}

	public static void blitSprite(GuiGraphicsExtractor guiGraphics, Identifier texture, int x, int y, int size) {
		blit(guiGraphics, texture, x, y, 0, 0, size, size, size, size);
	}

	public static void blitSprite(GuiGraphicsExtractor guiGraphics, Identifier background, int x, int y, int width, int height) {
		//? if >1.20.2 {
		guiGraphics.blitSprite(
				//? if >1.21.2
				RenderPipelines.GUI_TEXTURED,
				background, x, y, width, height);
		//?} else
		//blitSprite(guiGraphics, ModClient.locate(background.getNamespace(), "textures/gui/sprites/%s.png".formatted(background.getPath())), x, y, width);
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
}