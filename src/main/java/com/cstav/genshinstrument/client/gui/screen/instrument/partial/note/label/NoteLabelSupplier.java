package com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.label;

import com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.NoteButton;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

/**
 * A functional interface for supplying a label for a note button
 */
@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface NoteLabelSupplier {
    public static final NoteLabelSupplier EMPTY = (note) -> TextComponent.EMPTY;

    /**
     * @param note The button to compute the label for
     * @return The label that should be associated with the given {@code note}
     */
    public Component get(final NoteButton note);
}
