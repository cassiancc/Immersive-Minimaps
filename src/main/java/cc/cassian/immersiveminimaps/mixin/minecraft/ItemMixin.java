package cc.cassian.immersiveminimaps.mixin.minecraft;

import cc.cassian.immersiveminimaps.ModClient;
import cc.cassian.immersiveminimaps.helpers.ModLists;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import garden.hestia.hoofprint.HoofprintScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@IfModLoaded("hoofprint")
@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    private void minimapAtlasUse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<
            //? if >26 {
            InteractionResult
            //?} else {
            /*net.minecraft.world.InteractionResultHolder<ItemStack>
            *///?}
            > cir) {
        Item item = (Item) (Object) this;
        if (ModClient.CONFIG.items_open_world_map && level.isClientSide() && ModLists.items.contains(item)) {
            Minecraft.getInstance().setScreen(new HoofprintScreen());
            cir.setReturnValue(
                    //? if >26 {
                    InteractionResult.SUCCESS
                    //?} else {
                    /*net.minecraft.world.InteractionResultHolder.success(player.getItemInHand(hand))
                    *///?}
            );
        }
    }


}
