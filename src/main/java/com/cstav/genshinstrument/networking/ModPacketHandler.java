package com.cstav.genshinstrument.networking;

import java.util.List;
import java.util.function.Consumer;

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
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ModPacketHandler {

    @SuppressWarnings("unchecked")
    public static final List<Class<ModPacket>>
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


    public static void registerServerPackets() {
        for (final Class<ModPacket> packetClass : C2S_PACKETS) {

            ServerPlayNetworking.registerGlobalReceiver(
                ModPacket.getChannelName(packetClass),
                (server, player, handler, buf, sender) ->
                    handlePacket(player, sender, buf, packetClass, server::execute)
            );

        }
    }

    public static void handlePacket(Player player, PacketSender sender, FriendlyByteBuf buf,
            Class<? extends ModPacket> packetClass, Consumer<Runnable> executor) {
        ModPacket packet = ModPacket.createPacket(packetClass, buf);

        executor.accept(() -> packet.handle(player, sender));
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
