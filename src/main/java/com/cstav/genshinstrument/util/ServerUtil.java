package com.cstav.genshinstrument.util;

import com.cstav.genshinstrument.networking.IModPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

public class ServerUtil {

    public static void registerCodecs(PayloadTypeRegistry<RegistryFriendlyByteBuf> registry, List<Class<IModPacket>> packetTypes) {
        for (final Class<IModPacket> packetClass : packetTypes) {
            registry.register(
                IModPacket.type(packetClass),
                IModPacket.codec(packetClass)
            );
        }
    }

    public static void registerServerPackets(final List<Class<IModPacket>> packetTypes) {
        for (final Class<IModPacket> packetClass : packetTypes) {
            ServerPlayNetworking.registerGlobalReceiver(
                IModPacket.type(packetClass),
                IModPacket::handleServer
            );
        }
    }

    @SuppressWarnings("unchecked")
    public static void registerClientPackets(
        final List<Class<IModPacket>> packetTypes,
        Map<String, BiConsumer<? extends IModPacket, Context>> packetSwitch
    ) {
        for (final Class<IModPacket> packetClass : packetTypes) {
            ClientPlayNetworking.registerGlobalReceiver(
                IModPacket.type(packetClass),
                (packet, context) -> {
                    final BiConsumer<? extends IModPacket, Context> packetHandler = packetSwitch.get(packet.type().id().getPath());

                    // Trust me bro, it HAS to be extending IModPacket.
                    // It's an abstract class.
                    ((BiConsumer<IModPacket, Context>) packetHandler)
                        .accept(packet, context);
                }
            );
        }
    }

    public static <T extends IModPacket> Entry<String, BiConsumer<T, Context>> switchEntry(
        final Class<T> packetType,
        BiConsumer<T, ClientPlayNetworking.Context> handler
    ) {
        return Map.entry(IModPacket.path(packetType), handler);
    }

}
