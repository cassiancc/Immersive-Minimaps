package cc.cassian.immersiveminimaps.helpers;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cc.cassian.immersiveminimaps.ModClient.CONFIG;

public class ModLists {
	public static ArrayList<Item> items = new ArrayList<>();


	public static void loadLists() {
		var registry = BuiltInRegistries.ITEM;
		addAll(registry, CONFIG.items, items);
	}
	/**
	 * @param registry The registry to check - usually {@link BuiltInRegistries#ITEM}
	 * @param idList - A list of strings to convert to items.
	 * @param itemList - A list of items to fill with strings.
	 */
	private static void addAll(DefaultedRegistry<Item> registry, List<String> idList, ArrayList<Item> itemList) {
		itemList.clear();
		for (String itemId : idList) {
			Optional<Item> item = registry.getOptional(Identifier.tryParse(itemId));
			item.ifPresent(itemList::add);
		}
	}

}