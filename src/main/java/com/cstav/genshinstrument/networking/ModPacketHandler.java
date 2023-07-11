package com.cstav.genshinstrument.networking;

import java.util.List;

import com.cstav.genshinstrument.networking.packets.instrument.CloseInstrumentPacket;
import com.cstav.genshinstrument.networking.packets.instrument.InstrumentPacket;
import com.cstav.genshinstrument.networking.packets.instrument.NotifyInstrumentOpenPacket;
import com.cstav.genshinstrument.networking.packets.instrument.OpenInstrumentPacket;
import com.cstav.genshinstrument.networking.packets.instrument.PlayNotePacket;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
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


    public static void registerClientPackets() {
        for (final Class<ModPacket> packetClass : S2C_PACKETS) {

            ClientPlayNetworking.registerGlobalReceiver(
                ModPacket.getChannelName(packetClass),
                (client, handler, buf, sender) -> client.execute(() ->
                    ModPacket.createPacket(packetClass, buf).handle(client.player, sender)
                )
            );

        }
    }
    public static void registerServerPackets() {
        for (final Class<ModPacket> packetClass : C2S_PACKETS) {

            ServerPlayNetworking.registerGlobalReceiver(
                ModPacket.getChannelName(packetClass),
                (server, player, handler, buf, sender) -> server.execute(() ->
                    ModPacket.createPacket(packetClass, buf).handle(player, sender)
                )
            );

        }
    }


    public static void sendToServer(final ModPacket packet) {
        ClientPlayNetworking.send(packet.getChannelName(), getPacketBuf(packet));
    }
    public static void sendToClient(final ModPacket packet, final ServerPlayer player) {
        ServerPlayNetworking.send(player, packet.getChannelName(), getPacketBuf(packet));
    }

    private static FriendlyByteBuf getPacketBuf(final ModPacket packet) {
        final FriendlyByteBuf buf = PacketByteBufs.create();
        packet.write(buf);
        return buf;
    }

}
