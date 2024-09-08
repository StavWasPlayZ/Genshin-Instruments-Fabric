package com.cstav.genshinstrument.networking.packet.instrument.s2c;

import com.cstav.genshinstrument.networking.IModPacket;
import com.cstav.genshinstrument.networking.packet.instrument.NoteSoundMetadata;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.util.Optional;

/**
 * A generic S2C packet notifying the client to play
 * a specific note.
 * @param <T> The sound object type
 */
public abstract class S2CNotePacket<T> extends IModPacket {
    public final Optional<Integer> initiatorID;
    public final T sound;
    public final NoteSoundMetadata meta;

    /**
     * Constructs a new {@link S2CNoteSoundPacket}.
     * @param initiatorID The UUID of the player initiating the sound.
     *                      May be empty for a non-player trigger.
     */
    public S2CNotePacket(Optional<Integer> initiatorID, T sound, NoteSoundMetadata meta) {
        this.initiatorID = initiatorID;
        this.sound = sound;
        this.meta = meta;
    }
    public S2CNotePacket(RegistryFriendlyByteBuf buf) {
        initiatorID = buf.readOptional(FriendlyByteBuf::readInt);
        sound = readSound(buf);
        meta = NoteSoundMetadata.read(buf);
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeOptional(initiatorID, FriendlyByteBuf::writeInt);
        writeSound(buf);
        meta.write(buf);
    }

    protected abstract T readSound(RegistryFriendlyByteBuf buf);
    protected abstract void writeSound(RegistryFriendlyByteBuf buf);
}