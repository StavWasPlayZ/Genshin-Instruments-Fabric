package com.cstav.genshinstrument.networking;

import com.cstav.genshinstrument.util.CommonUtil;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * An interface for all packets under the Genshin Instruments mod.
 * All its implementers must a constructor that takes a {@link FriendlyByteBuf}.
 */
public abstract class IModPacket implements CustomPacketPayload {
    // Cache this because it's using reflections
    private final Type<? extends CustomPacketPayload> type = type(getClass());

    public void handleServer(ServerPlayNetworking.Context context) {}

    public void write(RegistryFriendlyByteBuf buf) {}

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return type;
    }


    @SuppressWarnings("unchecked")
    public static <T extends IModPacket> StreamCodec<RegistryFriendlyByteBuf, T> codec(final Class<T> packetType) {
        return CommonUtil.getStaticFinalField(packetType, "CODEC", StreamCodec.class);
    }

    public static <T extends IModPacket> CustomPacketPayload.Type<T> type(final Class<T> packetType) {
        return new Type<>(new ResourceLocation(
            CommonUtil.getStaticFinalField(packetType, "MOD_ID", String.class),
            path(packetType)
        ));
    }
    public static String path(final Class<? extends IModPacket> packetType) {
        return packetType.getSimpleName().toLowerCase(Locale.ENGLISH);
    }
}
