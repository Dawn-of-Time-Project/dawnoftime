package org.dawnoftime.dawnoftime;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoTBCommon {
	public static final String MOD_ID = "dawnoftimebuilder";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);
	public static final Identifier CREATIVE_ICONS = Identifier.fromNamespaceAndPath(MOD_ID, "textures/gui/creative_icons.png");

	public static void init() {}
}