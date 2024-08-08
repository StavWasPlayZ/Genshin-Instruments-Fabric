package com.cstav.genshinstrument.networking.buttonidentifier;

import java.util.List;
import java.util.function.Function;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.NoteButton;
import com.cstav.genshinstrument.networking.packet.INoteIdentifierSender;
import com.cstav.genshinstrument.networking.packet.instrument.util.InstrumentPacketUtil;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;

/**
 * <p>
 * A class used for identifying {@link NoteButton note button}s' UI placement over network.
 * </p>
 * All implementors must include a constructor that gets a type {@link FriendlyByteBuf}.
 */
public abstract class NoteButtonIdentifier {

    public void writeToNetwork(final FriendlyByteBuf buf) {
        buf.writeUtf(getClass().getName());
    }


    public abstract boolean matches(final NoteButtonIdentifier other);

    @Environment(EnvType.CLIENT)
    public boolean matches(final NoteButton note) {
        return matches(note.getIdentifier());
    }
    public boolean matches(final Object obj) {
        return equals(obj);
    }


    @Override
    public boolean equals(Object other) {
        return (this == other) || (
            (other instanceof NoteButtonIdentifier _other)
                && matches(_other)
        );
    }

    public static NoteButtonIdentifier readFromNetwork(FriendlyByteBuf buf) {
        try {
            return NoteButtonIdentifiers.getIdentifier(buf.readUtf())
                .getDeclaredConstructor(FriendlyByteBuf.class).newInstance(buf);
        } catch (Exception e) {
            GInstrumentMod.LOGGER.error("Error initializing button identifier", e);
            return null;
        }
    }

    

    /**
     * A class holding methods to simplify the usage of the {@link NoteButtonIdentifier#matches matches} function
     */
    public static abstract class MatchType {
        /**
         * <p>Executes the match methods such that if the current {@code preferredFunction} returned {@code false},
         * the {@code secondaryFunction} will execute in its stead.</p>
         * If the type of {@code other} and {@code T} do not match, then {@code secondaryFunction} will be executed.
         * @param <T> The type of the identifier to expect
         * @param other
         * @param preferredFunction The function for when the type is as expected
         * @param secondaryFunction The function for when the type is unexpected (generic, {@link NoteButtonIdentifier})
         * @return The result of the identification process
         */
        @SuppressWarnings("unchecked")
        public static <T extends NoteButtonIdentifier> boolean hierarchyMatch(NoteButtonIdentifier other,
                Function<T, Boolean> preferredFunction, Function<NoteButtonIdentifier, Boolean> secondaryFunction) {

            try {
                return preferredFunction.apply((T)other) || secondaryFunction.apply(other);
            } catch (ClassCastException e) {
                return secondaryFunction.apply(other);
            }
        }
        /**
         * <p>Executes the match methods such that only that if {@code other} did not match the type of {@code matchFunction},
         * the {@code secondaryFunction} will execute in its stead.</p>
         * @param <T> The type of the identifier to expect
         * @param other
         * @param preferredFunction The function for when the type is as expected
         * @param secondaryFunction The function for when the type is unexpected (generic, {@link NoteButtonIdentifier})
         * @return The result of the identification process
         */
        @SuppressWarnings("unchecked")
        public static <T extends NoteButtonIdentifier> boolean perferMatch(NoteButtonIdentifier other,
                Function<T, Boolean> preferredFunction, Function<NoteButtonIdentifier, Boolean> secondaryFunction) {

            try {
                return preferredFunction.apply((T)other);
            } catch (ClassCastException e) {
                return secondaryFunction.apply(other);
            }
        }

        /**
         * Executes the given match method such that if the expected type does not match {@code other},
         * {@code false} will be returned.
         * @param <T> The type of the identifier to expect
         * @param other
         * @param matchFunction The function for when the type is as expected
         * @return The result of the identification process, or {@code false} if the expected type does not match
         * 
         * 
         * @apiNote It is generally recommended not to use this match method,
         * because a lower type of identifier may be triggered as a result of 3rd-party initiation of notes.
         */
        @SuppressWarnings("unchecked")
        public static <T extends NoteButtonIdentifier> boolean forceMatch(NoteButtonIdentifier other,
                Function<T, Boolean> matchFunction) {

            try {
                return matchFunction.apply((T)other);
            } catch (ClassCastException e) {
                return false;
            }
        }
    }

}