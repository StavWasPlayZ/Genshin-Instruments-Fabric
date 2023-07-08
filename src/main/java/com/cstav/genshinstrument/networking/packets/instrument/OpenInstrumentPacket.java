package com.cstav.genshinstrument.networking.packets.instrument;

import com.cstav.genshinstrument.networking.ModPacket;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;

public class OpenInstrumentPacket implements ModPacket {
    public static final PacketType<OpenInstrumentPacket> TYPE = ModPacket.type(OpenInstrumentPacket.class);

    // private static final Map<String, Supplier<Function<InteractionHand, AbstractInstrumentScreen>>> OPEN_INSTRUMENT = Map.of(
    //     "windsong_lyre", () -> WindsongLyreScreen::new,
    //     "vintage_lyre", () -> VintageLyreScreen::new,
    //     "floral_zither", () -> FloralZitherScreen::new,
    //     "glorious_drum", () -> AratakisGreatAndGloriousDrumScreen::new
    // );


    private final String instrumentType;
    private final InteractionHand hand;
    public OpenInstrumentPacket(final String instrumentScreen, final InteractionHand hand) {
        this.instrumentType = instrumentScreen;
        this.hand = hand;
    }

    public OpenInstrumentPacket(FriendlyByteBuf buf) {
        instrumentType = buf.readUtf();
        hand = buf.readEnum(InteractionHand.class);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(instrumentType);
        buf.writeEnum(hand);
    }


    @SuppressWarnings("resource")
    @Override
    public boolean handle(LocalPlayer player, PacketSender responseSender) {
        // Minecraft.getInstance().setScreen(OPEN_INSTRUMENT.get(instrumentType).get().apply(hand));
        Minecraft.getInstance().player.sendSystemMessage(Component.literal("test!"));

        return true;
    }

    @Override
    public PacketType<OpenInstrumentPacket> getType() {
        return TYPE;
    }
}