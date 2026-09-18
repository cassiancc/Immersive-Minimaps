package cc.cassian.immersiveminimaps;

import cc.cassian.immersiveminimaps.config.ModConfig;
import cc.cassian.immersiveminimaps.helpers.ModLists;
import cc.cassian.immersiveminimaps.overlay.MinimapOverlay;
import cc.cassian.immersiveminimaps.overlay.MinimapHelpers;
import cc.cassian.mru.client.util.ClientVersionedUtil;
import cc.cassian.mru.util.CommonUtils;
import com.mojang.blaze3d.platform.InputConstants;
import folk.sisby.surveyor.client.SurveyorClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModClient implements ClientModInitializer {
	public static final String MOD_ID = "immersiveminimaps";
	public static final ModConfig CONFIG = ModConfig.createToml(FabricLoader.getInstance().getConfigDir(), "", ModClient.MOD_ID, ModConfig.class);

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	//? if >26 {
	public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(locate("keybinds")); // The category translation key used to categorize in the Controls screen
	 //?} else {
	/*public static final String CATEGORY = "key.category.immersiveminimaps.keybinds";
	*///?}

	//~ if >26.2 'KEYSYM'->'KEYBOARD' {
	// A key mapping with keyboard as the default
	public static final KeyMapping zoomIn = new KeyMapping(
			"key.immersiveminimaps.zoom_in", // The translation key of the name shown in the Controls screen
			InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
			InputConstants.KEY_EQUALS, // The default keycode
			CATEGORY
	);
	public static final KeyMapping zoomOut = new KeyMapping(
			"key.immersiveminimaps.zoom_out", // The translation key of the name shown in the Controls screen
			InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
			InputConstants.KEY_MINUS, // The default keycode
			CATEGORY
	);
	public static final KeyMapping caveMode = new KeyMapping(
			"key.immersiveminimaps.cave_mode", // The translation key of the name shown in the Controls screen
			InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
			InputConstants.KEY_BACKSPACE, // The default keycode
			CATEGORY
	);
	//~}

	public static Identifier locate(String path) {
		return CommonUtils.id(MOD_ID, path);
	}

	public static Identifier withVanillaNamespace(String s) {
		//~ if >1.21 'new Identifier' -> 'Identifier.withDefaultNamespace' {
		return Identifier.withDefaultNamespace(s);
		//~}
	}

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(ModClient::tick);
		ClientLifecycleEvents.CLIENT_STARTED.register((client -> ModLists.loadLists()));
		CONFIG.registerCallback(config -> ModLists.loadLists());
		ClientVersionedUtil.registerOverlay(ModClient.locate("minimap"), MinimapOverlay.INSTANCE::extractRenderState);
        ClientVersionedUtil.registerKeyMapping(ModClient.zoomIn);
		ClientVersionedUtil.registerKeyMapping(ModClient.zoomOut);
		ClientVersionedUtil.registerKeyMapping(ModClient.caveMode);
		UseBlockCallback.EVENT.register((player, level, hand, blockHitResult) -> {
			return ModEvents.placeWaypoint(player.getItemInHand(hand).getItem(), level, player, SurveyorClient.getClientUuid(), blockHitResult.getBlockPos());
		});
	}

	private static void tick(Minecraft minecraft) {
		MinimapHelpers.checkKeybind();
		if (minecraft.level != null && minecraft.player != null) {
			MinimapOverlay.INSTANCE.init();
			MinimapOverlay.INSTANCE.changeDim(minecraft.level.dimension());
			MinimapHelpers.checkInventoryForOverlays(minecraft);
			MinimapOverlay.INSTANCE.tick(minecraft.player);
		}
	}
}