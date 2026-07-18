package net.skds.core.mixins.multithreading;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;

// Section-level lock for concurrent fluid-task worker access (see MTHooks/ThreadProvider).
// setBlockState/getBlockState are the only paths WPO's fluid engine mutates/reads through
// (via Level.setBlock -> here), so a monitor on the section instance is sufficient for
// WPO-vs-WPO worker safety. Deliberately NOT calling PalettedContainer.acquire()/release():
// that goes through vanilla's ThreadingDetector, whose cross-thread reentrancy assumptions
// aren't a match for this access pattern - it deadlocked the server thread inside
// ServerChunkCache.getChunk during world creation (initial spawn chunk load). The
// synchronized monitor here is sufficient on its own for the safety property this needs.
@Mixin(value = { LevelChunkSection.class })
public abstract class ChunkSectionMixin {

	@Inject(method = "setBlockState", at = @At(value = "HEAD"), cancellable = true)
	public synchronized void setBlockState(int x, int y, int z, BlockState blockStateIn,
			CallbackInfoReturnable<BlockState> ci) {
		ci.setReturnValue(this.setBlockState(x, y, z, blockStateIn, true));
		ci.cancel();
	}

	@Redirect(method = "getBlockState", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/level/chunk/PalettedContainer;get(III)Ljava/lang/Object;"))
	private synchronized Object wpoSync$getBlockState(PalettedContainer<?> container, int x, int y, int z) {
		return container.get(x, y, z);
	}

	@Shadow
	private BlockState setBlockState(int x, int y, int z, BlockState blockStateIn, boolean b) {
		return null;
	}
}