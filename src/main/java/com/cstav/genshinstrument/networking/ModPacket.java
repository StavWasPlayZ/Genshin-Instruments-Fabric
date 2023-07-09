package com.cstav.genshinstrument.networking;

import com.cstav.genshinstrument.GInstrumentMod;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * An interface for all packets under the Genshin Instruments mod.
 * All its implementers must have a {@code TYPE} field of type {@link PacketType} (see {@link ModPacket#type})
 * and a constructor that takes a {@link FriendlyByteBuf}.
 */
public interface ModPacket extends FabricPacket {
    public void handle(LocalPlayer player, PacketSender responseSender);

    @Override
    default PacketType<?> getType() {
        try {
            return (PacketType<?>)getClass().getField("TYPE").get(null);
        } catch (Exception e) {
            GInstrumentMod.LOGGER.info("Failed to fetch packet type of "+getClass().getSimpleName()+". Perhaps a TYPE field is absent?");
            return null;
        }
    }


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
