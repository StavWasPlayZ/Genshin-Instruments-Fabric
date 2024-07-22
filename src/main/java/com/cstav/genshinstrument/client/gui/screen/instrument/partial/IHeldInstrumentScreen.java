package com.cstav.genshinstrument.client.gui.screen.instrument.partial;

import com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.NoteButton;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.held.IHoldableNoteButton;
import com.cstav.genshinstrument.event.HeldNoteSoundPlayedEvent;
import com.cstav.genshinstrument.event.HeldNoteSoundPlayedEvent.HeldNoteSoundPlayedEventArgs;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.InstrumentPlayedEventArgs;
import com.cstav.genshinstrument.sound.held.HeldNoteSound;
import com.cstav.genshinstrument.sound.held.HeldNoteSound.Phase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface IHeldInstrumentScreen {
    HeldNoteSound[] getHeldNoteSounds();
    void setHeldNoteSounds(final HeldNoteSound[] heldNoteSounds);

    default InstrumentScreen asScreen() {
        return (InstrumentScreen) this;
    }

    default void foreignPlayHeld(final InstrumentPlayedEventArgs<?> event) {
        if (!(event instanceof HeldNoteSoundPlayedEventArgs e))
            return;

        try {

            final NoteButton note = asScreen().getNoteButton(
                event.soundMeta().noteIdentifier(),
                e.sound().getSound(Phase.ATTACK),
                event.soundMeta().pitch()
            );

            final IHoldableNoteButton heldNote = (IHoldableNoteButton) note;

            switch (e.phase) {
                case ATTACK -> heldNote.playAttackAnimation(true);
                case RELEASE -> heldNote.playReleaseAnimation(true);
            }

        } catch (Exception ignore) {
            // Button was prolly just not found
        }
    }
}