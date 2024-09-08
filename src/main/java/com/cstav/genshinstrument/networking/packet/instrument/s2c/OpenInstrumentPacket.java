package com.cstav.genshinstrument.networking.packet.instrument.s2c;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.networking.IModPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class OpenInstrumentPacket extends IModPacket {
    public static final String MOD_ID = GInstrumentMod.MODID;
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenInstrumentPacket> CODEC = CustomPacketPayload.codec(
        OpenInstrumentPacket::write,
        OpenInstrumentPacket::new
    );

    public final ResourceLocation instrumentType;
    public OpenInstrumentPacket(final ResourceLocation instrumentScreen) {
        this.instrumentType = instrumentScreen;
    }

    public OpenInstrumentPacket(RegistryFriendlyByteBuf buf) {
        instrumentType = buf.readResourceLocation();
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeResourceLocation(instrumentType);
    }
}