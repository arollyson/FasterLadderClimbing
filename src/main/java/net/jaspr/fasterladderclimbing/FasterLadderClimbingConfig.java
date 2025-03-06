package net.jaspr.fasterladderclimbing;

import org.apache.commons.lang3.tuple.Pair;

import net.neoforged.neoforge.common.ModConfigSpec;

public class FasterLadderClimbingConfig {

	public static final FasterLadderClimbingConfig CONFIG;
	public static final ModConfigSpec CONFIG_SPEC;

	private static ModConfigSpec.BooleanValue allowQuickAscensionConfig;
	private static ModConfigSpec.BooleanValue allowQuickDescensionConfig;
	private static ModConfigSpec.IntValue speedModifierConfig;

	private FasterLadderClimbingConfig(ModConfigSpec.Builder builder) {
		builder.push("General");

		allowQuickAscensionConfig = builder
				.comment(
						"Allow going UP faster. If [false], then player can only climb up the ladder at normal speed.")
				.define("allowQuickAscension", true);
		allowQuickDescensionConfig = builder
				.comment(
						"Allow going DOWN faster. If [false], then player can only climb down the ladder at normal speed.")
				.define("allowQuickDescension", true);
		speedModifierConfig = builder
				.comment("Speed modifier. 0 is Vanilla speed, 10 is lightning speed.")
				.defineInRange("speedModifier", 4, 0, 10);

		builder.pop();
	}

	public static boolean getAllowQuickAscension() {
		return allowQuickAscensionConfig.get();
	}

	public static boolean getAllowQuickDescension() {
		return allowQuickDescensionConfig.get();
	}

	public static int getSpeedModifier() {
		return speedModifierConfig.get();
	}

	static {
		Pair<FasterLadderClimbingConfig, ModConfigSpec> pair = new ModConfigSpec.Builder()
				.configure(FasterLadderClimbingConfig::new);
		CONFIG_SPEC = pair.getRight();
		CONFIG = pair.getLeft();
	}
}