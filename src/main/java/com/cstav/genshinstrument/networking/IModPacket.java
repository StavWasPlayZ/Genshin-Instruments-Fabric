package com.cstav.genshinstrument.networking;

import com.cstav.genshinstrument.GInstrumentMod;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * An interface for all packets under the Genshin Instruments mod.
 * All its implementers must have a {@code TYPE} field of type {@link PacketType} (see {@link IModPacket#type})
 * and a constructor that takes a {@link FriendlyByteBuf}.
 */
public interface IModPacket extends FabricPacket {
    void handle(Player player, PacketSender responseSender);
    
    @Override
    default void write(FriendlyByteBuf buf) {}


    @Override
    default PacketType<?> getType() {
        return type(getClass());
    }

    public static <T extends IModPacket> PacketType<T> type(final Class<T> packetType) {
        return PacketType.create(
            // Cannot serve custom mod IDs because FabricPacket#getType requires no arguments
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
