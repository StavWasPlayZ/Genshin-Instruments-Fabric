package com.cstav.genshinstrument.networking.packet.instrument.s2c;

import com.cstav.genshinstrument.networking.IModPacket;
import com.cstav.genshinstrument.networking.packet.instrument.util.ClientDistExec;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class OpenInstrumentPacket implements IModPacket {

    private final ResourceLocation instrumentType;
    public OpenInstrumentPacket(final ResourceLocation instrumentScreen) {
        this.instrumentType = instrumentScreen;
    }

    public OpenInstrumentPacket(FriendlyByteBuf buf) {
        instrumentType = buf.readResourceLocation();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(instrumentType);
    }


    @Override
    public void handle(Player player, PacketSender responseSender) {
        ClientDistExec.setScreenByInstrumentId(instrumentType);
    }
}