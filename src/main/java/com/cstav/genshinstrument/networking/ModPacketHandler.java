package com.cstav.genshinstrument.networking;

import java.util.List;

import com.cstav.genshinstrument.networking.buttonidentifier.DefaultNoteButtonIdentifier;
import com.cstav.genshinstrument.networking.buttonidentifier.DrumNoteIdentifier;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteButtonIdentifier;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteGridButtonIdentifier;
import com.cstav.genshinstrument.networking.packet.instrument.CloseInstrumentPacket;
import com.cstav.genshinstrument.networking.packet.instrument.InstrumentPacket;
import com.cstav.genshinstrument.networking.packet.instrument.NotifyInstrumentOpenPacket;
import com.cstav.genshinstrument.networking.packet.instrument.OpenInstrumentPacket;
import com.cstav.genshinstrument.networking.packet.instrument.PlayNotePacket;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class ModPacketHandler {

    @SuppressWarnings("unchecked")
    private static final List<Class<IModPacket>>
        S2C_PACKETS = List.of(new Class[] {
            PlayNotePacket.class, OpenInstrumentPacket.class,
            NotifyInstrumentOpenPacket.class
        }),
        C2S_PACKETS = List.of(new Class[] {
            InstrumentPacket.class, CloseInstrumentPacket.class
        })
    ;

    @SuppressWarnings("unchecked")
    public static final List<Class<? extends NoteButtonIdentifier>> ACCEPTABLE_IDENTIFIERS = List.of(new Class[] {
        DefaultNoteButtonIdentifier.class,
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
