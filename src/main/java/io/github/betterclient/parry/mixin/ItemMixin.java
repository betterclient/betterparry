package io.github.betterclient.parry.mixin;

import io.github.betterclient.parry.BetterParryFabric;
import io.github.betterclient.parry.BetterParryMod;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
	@Inject(at = @At(value = "HEAD"), method = "use", cancellable = true)
	public void onUseItem(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		var stack = user.getStackInHand(hand);
		var isQuilt = ClientBrandRetriever.getClientModName().startsWith("quilt");
		var cfg = isQuilt ? BetterParryMod.getBetterParryMod().config : BetterParryFabric.getBetterParryMod().config;
		if(stack.getItem() instanceof SwordItem) { //Check if we have sword on hand
			boolean requireShield = cfg.shouldProiritirizeShield;

			if(user.getStackInHand(Hand.OFF_HAND).getItem() instanceof ShieldItem && requireShield) {
				user.setCurrentHand(Hand.OFF_HAND);
				cir.cancel();
			}
			user.setCurrentHand(hand);
			user.setSprinting(false);
			if(cfg.animationVersion) {
				cir.setReturnValue(TypedActionResult.pass(stack));
			}
			else {
				cir.setReturnValue(TypedActionResult.consume(stack));
			}
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "getMaxUseTime", cancellable = true)
	public void onGetMaxUseTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if(stack.getItem() instanceof SwordItem) {
			cir.setReturnValue(72000);
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "getUseAction", cancellable = true)
	public void onGetUseAction(ItemStack stack, CallbackInfoReturnable<UseAction> cir) {
		if(stack.getItem() instanceof SwordItem) {
			cir.setReturnValue(UseAction.BLOCK);
		}
	}
}
