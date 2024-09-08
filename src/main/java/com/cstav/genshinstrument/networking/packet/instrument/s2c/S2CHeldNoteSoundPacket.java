package com.cstav.genshinstrument.networking.packet.instrument.s2c;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.networking.packet.instrument.NoteSoundMetadata;
import com.cstav.genshinstrument.networking.packet.instrument.util.HeldSoundPhase;
import com.cstav.genshinstrument.sound.held.HeldNoteSound;
import com.cstav.genshinstrument.sound.held.InitiatorID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Optional;

/**
 * A S2C packet notifying the client to play
 * a specific {@link HeldNoteSound}.
 */
public class S2CHeldNoteSoundPacket extends S2CNotePacket<HeldNoteSound> {
    public static final String MOD_ID = GInstrumentMod.MODID;
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CHeldNoteSoundPacket> CODEC = CustomPacketPayload.codec(
        S2CHeldNoteSoundPacket::write,
        S2CHeldNoteSoundPacket::new
    );


    public final HeldSoundPhase phase;
    /**
     * An initiator ID for when the initiator is not an entity
     */
    public final Optional<InitiatorID> oInitiatorID;

    /**
     * Constructs a new {@link S2CHeldNoteSoundPacket}.
     * @param initiatorID The UUID of the player initiating the sound.
     *                      May be empty for a non-player trigger.
     */
    public S2CHeldNoteSoundPacket(Optional<Integer> initiatorID, Optional<InitiatorID> oInitiatorID,
                                  HeldNoteSound sound, NoteSoundMetadata meta,
                                  HeldSoundPhase phase) {
        super(initiatorID, sound, meta);
        this.phase = phase;
        this.oInitiatorID = oInitiatorID;
    }

    public S2CHeldNoteSoundPacket(RegistryFriendlyByteBuf buf) {
        super(buf);
        this.phase = buf.readEnum(HeldSoundPhase.class);
        this.oInitiatorID = buf.readOptional(InitiatorID::readFromNetwork);
    }
    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        super.write(buf);
        buf.writeEnum(phase);
        buf.writeOptional(oInitiatorID, (fbb, initId) -> initId.writeToNetwork(fbb));
    }

    @Override
    protected void writeSound(RegistryFriendlyByteBuf buf) {
        sound.writeToNetwork(buf);
    }
    @Override
    protected HeldNoteSound readSound(RegistryFriendlyByteBuf buf) {
        return HeldNoteSound.readFromNetwork(buf);
    }
}