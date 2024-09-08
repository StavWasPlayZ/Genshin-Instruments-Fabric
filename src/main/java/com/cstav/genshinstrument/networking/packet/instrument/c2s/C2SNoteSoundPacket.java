package com.cstav.genshinstrument.networking.packet.instrument.c2s;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.NoteButton;
import com.cstav.genshinstrument.networking.packet.instrument.NoteSoundMetadata;
import com.cstav.genshinstrument.networking.packet.instrument.util.NoteSoundPacketUtil;
import com.cstav.genshinstrument.sound.NoteSound;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

/**
 * A C2S packet notifying the server that a
 * specific {@link NoteSound} should be played in the level
 */
public class C2SNoteSoundPacket extends C2SNotePacket<NoteSound> {
    public static final String MOD_ID = GInstrumentMod.MODID;
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SNoteSoundPacket> CODEC = CustomPacketPayload.codec(
        C2SNoteSoundPacket::write,
        C2SNoteSoundPacket::new
    );

    public C2SNoteSoundPacket(NoteSound sound, NoteSoundMetadata meta) {
        super(sound, meta);
    }
    @Environment(EnvType.CLIENT)
    public C2SNoteSoundPacket(NoteButton noteButton, NoteSound sound, int pitch) {
        super(noteButton, sound, pitch);
    }

    public C2SNoteSoundPacket(RegistryFriendlyByteBuf buf) {
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

    protected void sendPlayNotePackets(final ServerPlayer player) {
        NoteSoundPacketUtil.sendPlayerPlayNotePackets(player, sound, meta);
    }
}