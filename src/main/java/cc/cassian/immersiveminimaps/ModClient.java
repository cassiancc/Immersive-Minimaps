package cc.cassian.immersiveminimaps;

import cc.cassian.immersiveminimaps.config.ModConfig;
import cc.cassian.immersiveminimaps.helpers.ModLists;
import cc.cassian.immersiveminimaps.overlay.MinimapOverlay;
import cc.cassian.immersiveminimaps.overlay.MinimapHelpers;
import com.mojang.blaze3d.platform.InputConstants;
import folk.sisby.surveyor.client.SurveyorClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//? if >26 {
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
//?} else {
/*import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
*///?}
//? if >1.21.2 {
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
//?} else {
/*import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
*///?}
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
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

	// A key mapping with keyboard as the default
	public static final KeyMapping zoomIn = new KeyMapping(
			"key.immersiveminimaps.zoom_in", // The translation key of the name shown in the Controls screen
			InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
			GLFW.GLFW_KEY_EQUAL, // The default keycode
			CATEGORY
	);
	public static final KeyMapping zoomOut = new KeyMapping(
			"key.immersiveminimaps.zoom_out", // The translation key of the name shown in the Controls screen
			InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
			GLFW.GLFW_KEY_MINUS, // The default keycode
			CATEGORY
	);
	public static final KeyMapping caveMode = new KeyMapping(
			"key.immersiveminimaps.cave_mode", // The translation key of the name shown in the Controls screen
			InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
			GLFW.GLFW_KEY_BACKSPACE, // The default keycode
			CATEGORY
	);

	public static Identifier locate(String namespace, String path) {
		//? if >1.21 {
		return Identifier.fromNamespaceAndPath(namespace, path);
		//?} else {
		/*return new Identifier(namespace, path);
		 *///?}
	}

	public static Identifier locate(String path) {
		return locate(MOD_ID, path);
	}

	public static Identifier withVanillaNamespace(String s) {
		//? if >1.21 {
		return Identifier.withDefaultNamespace(s);
		 //?} else {
		/*return new Identifier(s);
		*///?}
	}

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(ModClient::tick);
		ClientLifecycleEvents.CLIENT_STARTED.register((client -> ModLists.loadLists()));
		CONFIG.registerCallback(config -> ModLists.loadLists());
		//? if >1.21.2 {
		HudElementRegistry.addFirst(ModClient.locate("minimap"), MinimapOverlay.INSTANCE::extractRenderState);
		//?} else {
		/*HudRenderCallback.EVENT.register(MinimapOverlay.INSTANCE::extractRenderState);
		*///?}
        registerKeyMapping(ModClient.zoomIn);
        registerKeyMapping(ModClient.zoomOut);
		registerKeyMapping(ModClient.caveMode);
		UseBlockCallback.EVENT.register((player, level, hand, blockHitResult) -> {
			return ModEvents.placeWaypoint(player.getItemInHand(hand).getItem(), level, player, SurveyorClient.getClientUuid(), blockHitResult.getBlockPos());
		});
	}

	private static void registerKeyMapping(KeyMapping keyMapping) {
		//? if <26 {
		/*KeyBindingHelper.registerKeyBinding(keyMapping);
		*///?} else {
		KeyMappingHelper.registerKeyMapping(keyMapping);
		//?}
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