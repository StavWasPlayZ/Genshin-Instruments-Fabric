package com.cstav.genshinstrument.networking;

import com.cstav.genshinstrument.GInstrumentMod;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface ModPacket extends FabricPacket {
    public boolean handle(LocalPlayer player, PacketSender responseSender);

    public static <T extends ModPacket> PacketType<T> type(final Class<T> packetType) {
        return PacketType.create(
            new ResourceLocation(GInstrumentMod.MODID, packetType.getSimpleName().toLowerCase()),

            (buf) -> {
                try {
                    return packetType.getDeclaredConstructor(FriendlyByteBuf.class).newInstance(buf);
                } catch (Exception e) {
                    GInstrumentMod.LOGGER.error("Failed to construct PacketType for " + packetType.getSimpleName(), e);
                    return null;
                }
            }
        );
    }
}
