package com.cstav.genshinstrument.networking;

import java.util.List;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.networking.packets.instrument.OpenInstrumentPacket;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class ModPacketHandler {

    @SuppressWarnings("unchecked")
    private static final List<Class<ModPacket>> ACCEPTABLE_PACKETS = List.of(new Class[] {
        // InstrumentPacket.class, PlayNotePacket.class, OpenInstrumentPacket.class, CloseInstrumentPacket.class,
        // NotifyInstrumentOpenPacket.class
        OpenInstrumentPacket.class
    });


    @SuppressWarnings("unchecked")
    public static void registerClientPackets() {
        
        for (final Class<ModPacket> packetType : ACCEPTABLE_PACKETS) {
            try {

                ClientPlayNetworking.registerGlobalReceiver(
                    (PacketType<ModPacket>)packetType.getField("TYPE").get(null),
                    ModPacket::handle
                );

            } catch (Exception e) {
                GInstrumentMod.LOGGER.error("Failed to register packet type " + packetType.getSimpleName(), e);
            }
        }
    }


    public static void sendToServer(final FabricPacket packet) {
        ClientPlayNetworking.send(packet);
    }
    public static void sendToClient(final FabricPacket packet, final ServerPlayer player) {
        ServerPlayNetworking.send(player, packet);
    }
    

}
