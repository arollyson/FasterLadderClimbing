/**
 * This class was implemented by <JaSpr>. It is distributed as part
 * of the FasterLadderClimbing Mod.
 * https://github.com/JaSpr/FasterLadderClimbing
 *
 * FasterLadderClimbing is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * This class was derived from works created by <Vazkii> which were distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 */
package net.jaspr.fasterladderclimbing;

import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.ModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(FasterLadderClimbing.MOD_ID)
public class FasterLadderClimbing {

	public static final String MOD_ID = "fasterladderclimbing";
	public static final Logger LOGGER = LogManager.getLogger(FasterLadderClimbing.MOD_ID);

	public FasterLadderClimbing() {
		//Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
		ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> DisplayTest.IGNORESERVERONLY, (a, b) -> true));

		// Load Config
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, FasterLadderClimbingConfig.CONFIG_SPEC);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
		if(event.phase == TickEvent.Phase.START) {
			final Player player = event.player;

			if (player.onClimbable() && !player.isCrouching()) {
				EntityClimber climber = new EntityClimber(player);
	
				if (FasterLadderClimbingConfig.allowQuickDescension && climber.isFacingDownward() && !climber.isMovingForward() && !climber.isMovingBackward()) {
					climber.moveDownFarther();
				} else if (FasterLadderClimbingConfig.allowQuickAscension && climber.isFacingUpward() && climber.isMovingForward()) {
					climber.moveUpFarther();
				}
			}
		}
	}

	private class EntityClimber {
		private Player player;

		public EntityClimber(Player player) {
			this.player = player;
		}

		private boolean isFacingDownward() {
			return player.getXRot() > 0;
		}

		private boolean isFacingUpward() {
			return player.getXRot() < 0;
		}

		private boolean isMovingForward() {
			return player.zza > 0;
		}

		private boolean isMovingBackward() {
			return player.zza < 0;
		}

		private float getElevationChangeUpdate() {
			return (float)Math.abs(player.getXRot() / 90.0) * (((float)FasterLadderClimbingConfig.speedModifier) / 10);
		}

		public void moveUpFarther() {
			int px = 0;
			float dx = getElevationChangeUpdate();
			Vec3 move = new Vec3(px, dx, px);
			player.move(MoverType.SELF, move);
		}

		public void moveDownFarther() {
			int px = 0;
			float dx = getElevationChangeUpdate();
			Vec3 move = new Vec3(px, (dx * -1), px);
			player.move(MoverType.SELF, move);
		}
	}
}