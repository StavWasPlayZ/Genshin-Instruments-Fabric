package com.cstav.genshinstrument.networking;

import com.cstav.genshinstrument.networking.packet.instrument.c2s.C2SHeldNoteSoundPacket;
import com.cstav.genshinstrument.networking.packet.instrument.c2s.C2SNoteSoundPacket;
import com.cstav.genshinstrument.networking.packet.instrument.c2s.CloseInstrumentPacket;
import com.cstav.genshinstrument.networking.packet.instrument.c2s.ReqInstrumentOpenStatePacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.NotifyInstrumentOpenPacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.OpenInstrumentPacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.S2CHeldNoteSoundPacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.S2CNoteSoundPacket;
import com.cstav.genshinstrument.networking.packet.instrument.util.ClientDistExec;
import com.cstav.genshinstrument.util.ServerUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

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
            C2SNoteSoundPacket.class, C2SHeldNoteSoundPacket.class,
            ReqInstrumentOpenStatePacket.class
        })
    ;


    public static void registerCodecs() {
        ServerUtil.registerCodecs(C2S_PACKETS, S2C_PACKETS);
    }

    public static void registerClientPackets() {
        ServerUtil.registerClientPackets(S2C_PACKETS, ClientDistExec.PACKET_SWITCH);
    }
    public static void registerServerPackets() {
        ServerUtil.registerServerPackets(C2S_PACKETS);
    }


    public static void sendToServer(final CustomPacketPayload packet) {
        ClientPlayNetworking.send(packet);
    }
    public static void sendToClient(final CustomPacketPayload packet, final ServerPlayer player) {
        ServerPlayNetworking.send(player, packet);
    }
    
}
