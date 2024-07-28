package com.cstav.genshinstrument.sound.held;

import com.cstav.genshinstrument.event.HeldNoteSoundPlayedEvent;
import com.cstav.genshinstrument.event.HeldNoteSoundPlayedEvent.HeldNoteSoundPlayedEventArgs;
import com.cstav.genshinstrument.networking.packet.instrument.NoteSoundMetadata;
import com.cstav.genshinstrument.networking.packet.instrument.util.HeldSoundPhase;
import com.cstav.genshinstrument.sound.NoteSound;
import com.cstav.genshinstrument.sound.registrar.HeldNoteSoundRegistrar;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

/**
 * A container for {@link NoteSound}s that act together
 * as a holdable note sound.
 * <p>The attack sound plays once at the beginning, and the hold
 * sound is held and repeated until released.</p>
 */
public record HeldNoteSound(
    ResourceLocation baseSoundLocation, int index,
    NoteSound attack, NoteSound hold, float holdDuration,
    float holdDelay,
    float chainedHoldDelay, float decay,
    float releaseFadeOut,
    float fullHoldFadeoutTime
) {

    public NoteSound getSound(final Phase phase) {
        return switch (phase) {
            case HOLD -> hold;
            case ATTACK -> attack;
        };
    }

    public static NoteSound[] getSounds(final HeldNoteSound[] sounds, final Phase phase) {
        return Arrays.stream(sounds)
            .map((sound) -> sound.getSound(phase))
            .toArray(NoteSound[]::new);
    }


    /**
     * A held note sound instance for 3rd party trigger
     */
    @Environment(EnvType.CLIENT)
    public void startPlaying(int notePitch, float volume, Entity initiator, BlockPos pos) {
        new HeldNoteSoundInstance(
            this, Phase.ATTACK,
            notePitch, volume,
            initiator, pos
        ).queueAndAddInstance();
    }
    /**
     * A held note sound instance for 3rd party trigger
     */
    @Environment(EnvType.CLIENT)
    public void startPlaying(int notePitch, float volume, Entity initiator) {
        startPlaying(notePitch, volume, initiator, null);
    }
    /**
     * A held note sound instance for 3rd party trigger
     */
    @Environment(EnvType.CLIENT)
    public void startPlaying(int notePitch, float volume, BlockPos pos) {
        startPlaying(notePitch, volume, null, pos);
    }
    /**
     * A held note sound instance for local playing
     */
    @Environment(EnvType.CLIENT)
    public void startPlaying(int notePitch, float volume) {
        startPlaying(notePitch, volume, Minecraft.getInstance().player);
    }


    //#region Play From Server

    /**
     * A method for packets to use for playing this note on the client's end.
     * Will also stop the client's background music per preference.
     * @param initiatorID The ID of the player who initiated the sound. Empty for when it wasn't a player.
     * @param meta Additional metadata of the Note Sound being played
     */
    @Environment(EnvType.CLIENT)
    public void playFromServer(Optional<Integer> initiatorID, NoteSoundMetadata meta, HeldSoundPhase phase) {
        final Player localPlayer = Minecraft.getInstance().player;
        final Level level = localPlayer.level();

        if (initiatorID.isPresent()) {
            final Entity initiator = level.getEntity(initiatorID.get());

            HeldNoteSoundPlayedEvent.EVENT.invoker().triggered(
                new HeldNoteSoundPlayedEventArgs(initiator, this, meta, phase)
            );

            // Don't play sound for ourselves
            if (localPlayer.equals(initiator))
                return;
        }

        HeldNoteSoundPlayedEvent.EVENT.invoker().triggered(
            new HeldNoteSoundPlayedEventArgs(level, this, meta, phase)
        );

        switch (phase) {
            case ATTACK -> attackFromServer(initiatorID, meta);
            case RELEASE -> releaseFromServer(initiatorID, meta);
        }
    }
    @Environment(EnvType.CLIENT)
    private void attackFromServer(Optional<Integer> initiatorID, NoteSoundMetadata meta) {
        initiatorID.ifPresentOrElse(
            // Sound was played by player
            (id) -> startPlaying(
                meta.pitch(), meta.volume() / 100f,
                Minecraft.getInstance().level.getEntity(id)
            ),
            // Sound is by some other thing
            () -> startPlaying(meta.pitch(), meta.volume(), meta.pos())
        );
    }
    @Environment(EnvType.CLIENT)
    private void releaseFromServer(Optional<Integer> initiatorID, NoteSoundMetadata meta) {
        HeldNoteSounds.release(
            InitiatorID.fromObj(
                initiatorID
                    .map(Minecraft.getInstance().level::getEntity)
                    .map((id) -> (Object) id)
                    .orElse(meta.pos())
            ),
            this, meta.pitch()
        );
    }

    //#endregion

    public void writeToNetwork(final FriendlyByteBuf buf) {
        buf.writeResourceLocation(baseSoundLocation);
        buf.writeInt(index);
    }
    public static HeldNoteSound readFromNetwork(final FriendlyByteBuf buf) {
        return HeldNoteSoundRegistrar.getSounds(buf.readResourceLocation())[buf.readInt()];
    }


    public static enum Phase {
        ATTACK, HOLD
    }

}