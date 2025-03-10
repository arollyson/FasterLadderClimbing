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

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@Mod(value = FasterLadderClimbing.MOD_ID, dist = Dist.CLIENT)
public class FasterLadderClimbing {

	public static final String MOD_ID = "fasterladderclimbing";
	public static final Logger LOGGER = LogUtils.getLogger();

	public FasterLadderClimbing(IEventBus modEventBus, ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.CLIENT, FasterLadderClimbingConfig.CONFIG_SPEC);
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		NeoForge.EVENT_BUS.addListener(FasterLadderClimbing::adjustClimbingSpeed);
	}

	private static void adjustClimbingSpeed(final PlayerTickEvent.Pre event) {
		if (event.getEntity().level().isClientSide) {
			final Player player = event.getEntity();

			if (player.onClimbable() && !player.isCrouching()) {
				EntityClimber climber = new EntityClimber(player);

				if (FasterLadderClimbingConfig.allowQuickDescension.get() && climber.isFacingDownward()
						&& !climber.isMovingForward() && !climber.isMovingBackward()) {
					climber.moveDownFarther();
				} else if (FasterLadderClimbingConfig.allowQuickAscension.get() && climber.isFacingUpward()
						&& climber.isMovingForward()) {
					climber.moveUpFarther();
				}
			}
		}
	}

	private static class EntityClimber {
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
			return (float) Math.abs(player.getXRot() / 90.0)
					* (((float) FasterLadderClimbingConfig.speedModifier.get()) / 10);
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