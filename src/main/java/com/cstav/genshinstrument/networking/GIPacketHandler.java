package com.cstav.genshinstrument.networking;

import java.util.List;

import com.cstav.genshinstrument.networking.buttonidentifier.DrumNoteIdentifier;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteButtonIdentifier;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteGridButtonIdentifier;
import com.cstav.genshinstrument.networking.packet.instrument.c2s.C2SHeldNoteSoundPacket;
import com.cstav.genshinstrument.networking.packet.instrument.c2s.C2SNoteSoundPacket;
import com.cstav.genshinstrument.networking.packet.instrument.c2s.CloseInstrumentPacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.NotifyInstrumentOpenPacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.OpenInstrumentPacket;

import com.cstav.genshinstrument.networking.packet.instrument.s2c.S2CHeldNoteSoundPacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.S2CNoteSoundPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class GIPacketHandler {

    @SuppressWarnings("unchecked")
    private static final List<Class<IModPacket>>
        S2C_PACKETS = List.of(new Class[] {
            S2CNoteSoundPacket.class, OpenInstrumentPacket.class,
            NotifyInstrumentOpenPacket.class,
            S2CHeldNoteSoundPacket.class
        }),
        C2S_PACKETS = List.of(new Class[] {
            CloseInstrumentPacket.class,
            C2SNoteSoundPacket.class, C2SHeldNoteSoundPacket.class
        })
    ;

    @SuppressWarnings("unchecked")
    public static final List<Class<? extends NoteButtonIdentifier>> ACCEPTABLE_IDENTIFIERS = List.of(new Class[] {
        NoteButtonIdentifier.class, NoteGridButtonIdentifier.class, DrumNoteIdentifier.class
    });


    public static void registerClientPackets() {
        for (final Class<IModPacket> packetClass : S2C_PACKETS) {

            ClientPlayNetworking.registerGlobalReceiver(
                IModPacket.type(packetClass),
                IModPacket::handle
            );

        }
    }
    public static void registerServerPackets() {
        for (final Class<IModPacket> packetClass : C2S_PACKETS) {

            ServerPlayNetworking.registerGlobalReceiver(
                IModPacket.type(packetClass),
                IModPacket::handle
            );

        }
    }


    public static void sendToServer(final FabricPacket packet) {
        ClientPlayNetworking.send(packet);
    }
    public static void sendToClient(final FabricPacket packet, final ServerPlayer player) {
        ServerPlayNetworking.send(player, packet);
    }
    
}
