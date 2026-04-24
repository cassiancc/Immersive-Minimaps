package cc.cassian.immersiveminimaps.config;

import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.DisplayName;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.IntegerRange;
import folk.sisby.kaleido.lib.quiltconfig.api.values.ValueList;
import folk.sisby.kaleido.lib.quiltconfig.api.values.ValueMap;
import folk.sisby.kaleido.lib.quiltconfig.impl.Comments;
import folk.sisby.surveyor.client.SurveyorClient;
import garden.hestia.hoofprint.HoofprintConfig;
import garden.hestia.hoofprint.HoofprintMapStorage;
import garden.hestia.hoofprint.util.ConstantLightMap;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModConfig extends WrappedConfig {

	@DisplayName("Enable Minimap")
	public boolean minimap_enable = true;
	@Comment("NOT YET IMPLEMENTED")
	public boolean moved_by_effects = true;
	@Comment("Hide the minimap when the F3 menu is open.")
	public boolean hide_from_debug = true;
	public int defaultScale = -1;
	public int size = 80;
	@Comment("NOT YET IMPLEMENTED")
	public boolean left_align = true;

	@Comment({"Options to change the visuals of the map to be more or less vanilla-style."})
	public Requirements requirements = new Requirements();
	public static class Requirements implements WrappedConfig.Section {
		@DisplayName("Require item")
		@Comment("Require an item to activate overlays. This is the intended way to use the mod.")
		public boolean require_item = true;
		@DisplayName("Allow items in bundles/containers")
		@Comment("As an example, allows for a compass inside a bundle or Shulker Box to count.")
		public boolean search_containers = true;
		@DisplayName("Allow containers in bundles/containers")
		@Comment("As an example, allows for a compass inside a bundle inside a Shulker Box to count.")
		public boolean search_containers_for_containers = true;
		@DisplayName("Items that show minimap overlay")
		public ValueList<String> items = ValueList.create("", "minecraft:map", "minecraft:filled_map");
	}

	@Comment({"Options to change the visuals of the map to be more or less vanilla-style."})
	public Style style = new Style();
	public static class Style implements WrappedConfig.Section {
		@Comment("Whether to render a background behind the map.")
		public boolean draw_background = true;
		@Comment("Whether to show disconnected players and offline group members on the minimap.")
		public boolean offlinePlayers = true;
	}
}