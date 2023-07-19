package com.cstav.genshinstrument.networking;

import java.util.List;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.networking.buttonidentifier.DrumNoteIdentifier;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteButtonIdentifier;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteGridButtonIdentifier;
import com.cstav.genshinstrument.networking.packets.instrument.CloseInstrumentPacket;
import com.cstav.genshinstrument.networking.packets.instrument.InstrumentPacket;
import com.cstav.genshinstrument.networking.packets.instrument.NotifyInstrumentOpenPacket;
import com.cstav.genshinstrument.networking.packets.instrument.OpenInstrumentPacket;
import com.cstav.genshinstrument.networking.packets.instrument.PlayNotePacket;
import com.cstav.genshinstrument.util.ServerUtil;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class ModPacketHandler {

    @SuppressWarnings("unchecked")
    private static final List<Class<ModPacket>>
        S2C_PACKETS = List.of(new Class[] {
            PlayNotePacket.class, OpenInstrumentPacket.class, NotifyInstrumentOpenPacket.class
        }),
        C2S_PACKETS = List.of(new Class[] {
            InstrumentPacket.class, CloseInstrumentPacket.class
        })
    ;

    @SuppressWarnings("unchecked")
    private static final List<Class<? extends NoteButtonIdentifier>> ACCEPTABLE_IDENTIFIERS = List.of(new Class[] {
        NoteButtonIdentifier.class, NoteGridButtonIdentifier.class, DrumNoteIdentifier.class
    });

    /**
     * @see ServerUtil#getValidNoteIdentifier
     */
    public static Class<? extends NoteButtonIdentifier> getValidIdentifier(String classType)
            throws ClassNotFoundException {
        return ServerUtil.getValidNoteIdentifier(classType, ACCEPTABLE_IDENTIFIERS);
    }


    public static void registerClientPackets() {
        for (final Class<ModPacket> packetClass : S2C_PACKETS) {

            ClientPlayNetworking.registerGlobalReceiver(
                getPacketType(packetClass),
                ModPacket::handle
            );

        }
    }
    public static void registerServerPackets() {
        for (final Class<ModPacket> packetClass : C2S_PACKETS) {

            ServerPlayNetworking.registerGlobalReceiver(
                getPacketType(packetClass),
                ModPacket::handle
            );

        }
    }

    @SuppressWarnings("unchecked")
    private static PacketType<ModPacket> getPacketType(final Class<ModPacket> packetClass) {
        try {

            return (PacketType<ModPacket>)(packetClass.getField("TYPE").get(null));

        } catch (Exception e) {
            GInstrumentMod.LOGGER.error("Failed to register packet type " + packetClass.getSimpleName(), e);
            return null;
        }
    }


    public static void sendToServer(final FabricPacket packet) {
        ClientPlayNetworking.send(packet);
    }
    public static void sendToClient(final FabricPacket packet, final ServerPlayer player) {
        ServerPlayNetworking.send(player, packet);
    }
    

}
