package cc.cassian.immersiveminimaps.config;

import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.DisplayName;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.IntegerRange;
import folk.sisby.kaleido.lib.quiltconfig.api.values.ValueList;

public class ModConfig extends WrappedConfig {

	@DisplayName("Enable Minimap")
	public boolean minimap_enable = true;
	@Comment("NOT YET IMPLEMENTED")
	public boolean moved_by_effects = true;
	@Comment("Hide the minimap when the F3 menu is open.")
	public boolean hide_from_debug = true;
	public int defaultScale = -1;
	public int size = 80;
	@IntegerRange(max = 500, min = 0)
	public int xOffset = 5;
	@IntegerRange(max = 500, min = 0)
	public int yOffset = 5;

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
	}
}