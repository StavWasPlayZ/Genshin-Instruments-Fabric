package com.cstav.genshinstrument.networking.packet.instrument.s2c;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.networking.packet.instrument.NoteSoundMetadata;
import com.cstav.genshinstrument.sound.NoteSound;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Optional;

/**
 * A S2C packet notifying the client to play
 * a specific {@link NoteSound}.
 */
public class S2CNoteSoundPacket extends S2CNotePacket<NoteSound> {
    public static final String MOD_ID = GInstrumentMod.MODID;
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CNoteSoundPacket> CODEC = CustomPacketPayload.codec(
        S2CNoteSoundPacket::write,
        S2CNoteSoundPacket::new
    );


    /**
     * Constructs a new {@link S2CNoteSoundPacket}.
     * @param initiatorID The UUID of the player initiating the sound.
     *                    May be empty for a non-player trigger.
     */
    public S2CNoteSoundPacket(Optional<Integer> initiatorID, NoteSound sound, NoteSoundMetadata meta) {
        super(initiatorID, sound, meta);
    }
    public S2CNoteSoundPacket(RegistryFriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    protected void writeSound(RegistryFriendlyByteBuf buf) {
        sound.writeToNetwork(buf);
    }
    @Override
    protected NoteSound readSound(RegistryFriendlyByteBuf buf) {
        return NoteSound.readFromNetwork(buf);
    }
}