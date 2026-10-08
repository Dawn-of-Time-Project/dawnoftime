package org.dawnoftime.dawnoftime.client.tooltip;

import net.minecraft.ChatFormatting;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * Builds the body text of a tooltip. Two colors only:
 * <ul>
 *     <li>Gray: all the text.</li>
 *     <li>Aqua: click instructions written between brackets in the lang file, e.g. "[shift + right-click]", and block names passed as arguments.</li>
 * </ul>
 * Lang files never contain color codes. "%s" is replaced by the next argument.
 */
public final class TooltipText {

    private TooltipText() {}

    public static MutableComponent of(String key, Object... args) {
        String text = Language.getInstance().getOrDefault(key);
        MutableComponent result = Component.empty();
        StringBuilder current = new StringBuilder();
        boolean highlighted = false;
        int argIndex = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '[' || c == ']') {
                append(result, current, highlighted);
                highlighted = c == '[';
            } else if (c == '%' && i + 1 < text.length() && text.charAt(i + 1) == 's') {
                append(result, current, highlighted);
                Object arg = argIndex < args.length ? args[argIndex++] : "";
                result.append(arg instanceof Component component ? component : Component.literal(String.valueOf(arg)));
                i++;
            } else {
                current.append(c);
            }
        }
        append(result, current, highlighted);
        return result.withStyle(ChatFormatting.GRAY);
    }

    private static void append(MutableComponent result, StringBuilder text, boolean highlighted) {
        if (text.length() == 0) return;
        MutableComponent part = Component.literal(text.toString());
        result.append(highlighted ? part.withStyle(ChatFormatting.AQUA) : part);
        text.setLength(0);
    }
}
