package com.cstav.genshinstrument.networking;

import com.cstav.genshinstrument.GInstrumentMod;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * An interface for all packets under the Genshin Instruments mod.
 * All its implementers must have a constructor that takes a {@link FriendlyByteBuf}.
 */
public interface ModPacket {
    void handle(Player player, PacketSender responseSender);
    
    default void write(FriendlyByteBuf buf) {}
    


    public static ResourceLocation getChannelName(final Class<? extends ModPacket> packetType) {
        return new ResourceLocation(GInstrumentMod.MODID, packetType.getSimpleName().toLowerCase());
    }
    public default ResourceLocation getChannelName() {
        return getChannelName(getClass());
    }


    public static ModPacket createPacket(final Class<? extends ModPacket> packetType, final FriendlyByteBuf buf) {
        try {
            return packetType.getDeclaredConstructor(FriendlyByteBuf.class).newInstance(buf);
        } catch (Exception e) {
            GInstrumentMod.LOGGER.error("Failed to construct a packet of type " + packetType.getSimpleName(), e);
            return null;
        }
    }
    
}
