package net.jaspr.fasterladderclimbing;

import org.apache.commons.lang3.tuple.Pair;

import net.neoforged.neoforge.common.ModConfigSpec;

public class FasterLadderClimbingConfig {

	public static final FasterLadderClimbingConfig CONFIG;
	public static final ModConfigSpec CONFIG_SPEC;

	static {
		Pair<FasterLadderClimbingConfig, ModConfigSpec> pair = new ModConfigSpec.Builder()
				.configure(FasterLadderClimbingConfig::new);
		CONFIG_SPEC = pair.getRight();
		CONFIG = pair.getLeft();
	}

	public static ModConfigSpec.ConfigValue<Boolean> allowQuickAscension;
	public static ModConfigSpec.ConfigValue<Boolean> allowQuickDescension;
	public static ModConfigSpec.ConfigValue<Integer> speedModifier;

	private FasterLadderClimbingConfig(ModConfigSpec.Builder builder) {
		allowQuickAscension = builder
				.comment("Allow going UP faster. If [false], then player can only climb up the ladder at normal speed.")
				.translation("fasterladderclimbing.configuration.allowQuickAscension")
				.define("allowQuickAscension", true);
		allowQuickDescension = builder
				.comment(
						"Allow going DOWN faster. If [false], then player can only climb down the ladder at normal speed.")
				.translation("fasterladderclimbing.configuration.allowQuickDescension")
				.define("allowQuickDescension", true);
		speedModifier = builder
				.comment("Speed modifier. 0 is Vanilla speed, 10 is lightning speed.")
				.translation("fasterladderclimbing.configuration.speedModifier")
				.defineInRange("speedModifier", 4, 0, 10);
	}
}