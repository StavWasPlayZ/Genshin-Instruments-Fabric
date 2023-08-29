package com.cstav.genshinstrument.client.config.enumType.label;

import com.cstav.genshinstrument.client.gui.screen.instrument.drum.DrumNoteButton;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.NoteButton;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.label.INoteLabel;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.label.NoteLabelSupplier;
import com.cstav.genshinstrument.util.LabelUtil;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public enum DrumNoteLabel implements INoteLabel {
	KEYBOARD_LAYOUT((note) ->
		INoteLabel.upperComponent(dn(note).getKey().getDisplayName())
	),
	QWERTY((note) ->
		INoteLabel.getQwerty(dn(note).getKey())
	),

	DON_KA((note) ->
		Component.translatable(dn(note).btnType.getTransKey())
	),
	NOTE_NAME((note) -> Component.literal(
		note.getCutNoteName()
	)),
	DO_RE_MI((note) ->
        LabelUtil.toDoReMi(note.getCutNoteName())
    ),
    NONE(NoteLabelSupplier.EMPTY);


    private final NoteLabelSupplier labelSupplier;
    private DrumNoteLabel(final NoteLabelSupplier supplier) {
        labelSupplier = supplier;
    }

	public static INoteLabel[] availableVals() {
        return INoteLabel.filterQwerty(values(), QWERTY);
    }


	@Override
	public NoteLabelSupplier getLabelSupplier() {
        return labelSupplier;
	}


	private static DrumNoteButton dn(final NoteButton btn) {
        return (DrumNoteButton)btn;
    }
}