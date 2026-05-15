package cc.cassian.immersiveminimaps.compat;

import cc.cassian.immersiveminimaps.overlay.MinimapHelpers;
//? if fabric && <26 {
/*import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.util.Tuple;
*///?}
//? if =26.1 {
import eu.pb4.trinkets.api.TrinketsApi;
import net.minecraft.util.Tuple;
//?}
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TrinketsCompat {
    public static void checkForImportantAccessories(Player player) {
        //? if fabric && <26 {
        /*var capability = TrinketsApi.getTrinketComponent(player);
        if (capability.isPresent()) {
            for (Tuple<SlotReference, ItemStack> slotReferenceItemStackTuple : capability.get().getAllEquipped()) {
                MinimapHelpers.isImportantItemOrContainer(slotReferenceItemStackTuple.getB());
            }
        }
        *///?}
        //? if =26.1 {
        TrinketsApi.getAttachment(player).getAllEquipped().stream().map(Tuple::getB).forEach(MinimapHelpers::isImportantItemOrContainer);
        //?}
    }
}
