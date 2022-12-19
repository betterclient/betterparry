package io.github.betterclient.parry.mixin;

import io.github.betterclient.parry.BetterParryMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	protected ItemStack activeItemStack;
	@Shadow public abstract boolean isUsingItem();
	@Shadow public abstract boolean blockedByShield(DamageSource source);
	private DamageSource cached;
	private boolean shouldAppearBlocking = false;

	@Inject(at = @At(value = "HEAD"), method = "isBlocking", cancellable = true) //Code to return true to isblocking if its using the sword blocking
	public void makeFakeBlockingOnShield(CallbackInfoReturnable<Boolean> cir) {
		var item = this.activeItemStack.getItem();
		if(item instanceof SwordItem) {
			cir.setReturnValue(shouldAppearBlocking);
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "damage")
	public void cacheTheDamageSource(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		this.cached = source;
	}

	@ModifyVariable(method = "damage", at = @At("HEAD"), index = 2, argsOnly = true)
	private float makeSwordBlockingBlockDamange(float old) {
		var item = this.activeItemStack.getItem();
		shouldAppearBlocking = true;
		if(item instanceof SwordItem && this.isUsingItem() && this.blockedByShield(cached)) {
			double multiplier = BetterParryMod.getBetterParryMod().config.multiplier;
			for(BetterParryMod.OverrideValue v : BetterParryMod.getBetterParryMod().config.overrides) {
				if(item == v.getItem()) multiplier = v.multiplier;
			}
			old *= multiplier;
		}
		shouldAppearBlocking = false;
		return old;
	}

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
}
