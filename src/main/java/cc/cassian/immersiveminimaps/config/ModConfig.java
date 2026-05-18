package cc.cassian.immersiveminimaps.config;

import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.DisplayName;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.IntegerRange;
import folk.sisby.kaleido.lib.quiltconfig.api.values.ValueList;

public class ModConfig extends WrappedConfig {

	@DisplayName("Enable Minimap")
	@Comment("Whether to show a minimap.")
	public boolean minimap_enable = true;
	@Comment("When right aligned, status effects move the minimap down")
	public boolean moved_by_effects = true;
	@Comment("Hide the minimap when the F3 menu is open.")
	public boolean hide_from_debug = true;
	@Comment("Default scale to open the minimap to.")
	public int default_scale = -1;
	@DisplayName("Apply requirements to Hoofprint")
	@Comment("Whether to apply Immersive Minimaps and Immersive Overlays requirements to Hoofprint's screen.")
	public boolean apply_requirements_to_hoofprint = true;
	@DisplayName("Immersive Overlays Bridge")
	@Comment("When present, Immersive Overlays can be used to gain a large amount of mod compatibility with third-party backpacks and accessory mods, and its requirements will be used rather than the configs set in Immersive Minimaps.")
	public boolean immersive_overlays_bridge = true;

	@Comment({"Options to change the visuals of the map to be more or less vanilla-style."})
	public Requirements requirements = new Requirements();
    public static class Requirements implements WrappedConfig.Section {

		@DisplayName("Require item")
		@Comment("Require an item to activate the minimap. This is the intended way to use the mod.")
		public boolean require_item = true;
		@Comment("Require the item in hand.")
		public boolean require_item_in_hand = false;
		@DisplayName("Allow items in bundles/containers")
		@Comment("As an example, allows for a compass inside a bundle or Shulker Box to count.")
		public boolean search_containers = true;
		@DisplayName("Allow containers in bundles/containers")
		@Comment("As an example, allows for a compass inside a bundle inside a Shulker Box to count.")
		public boolean search_containers_for_containers = true;
		@DisplayName("Items that show minimap overlay")
		@Comment("When the following items are in the inventory, show the minimap.")
		public ValueList<String> items = ValueList.create("", "minecraft:map", "minecraft:filled_map");
	}

	@DisplayName("Style and Position")
	@Comment({"Options to change the visuals of the map to be more or less vanilla-style."})
	public Style style = new Style();
	public static class Style implements WrappedConfig.Section {
		@Comment("How large to render the minimap.")
		@IntegerRange(max = 500, min = 0)
		public int size = 80;
		@Comment("Anchor the minimap to the left edge of the screen. Disable to anchor it to the right edge of the screen.")
		public boolean left_align = true;
		@Comment("Horizontal offset from the edge of the screen.")
		@IntegerRange(max = 500, min = 0)
		public int xOffset = 5;
		@Comment("Vertical offset from the edge of the screen.")
		@IntegerRange(max = 500, min = 0)
		public int yOffset = 5;
		@Comment("Whether to render a background behind the map.")
		public boolean draw_background = true;
		@Comment("Whether to render landmarks on the map.")
		public boolean draw_landmarks = true;
		@Comment("Whether to render nearby players on the map.")
		public boolean draw_players = true;
		@Comment("Whether to render far-off players on the map.")
		public boolean draw_offscreen_players = false;
    }
}