package com.cstav.genshinstrument.networking.packets.instrument;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.cstav.genshinstrument.client.gui.screens.instrument.drum.AratakisGreatAndGloriousDrumScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.floralzither.FloralZitherScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.AbstractInstrumentScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.vintagelyre.VintageLyreScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.windsonglyre.WindsongLyreScreen;
import com.cstav.genshinstrument.networking.ModPacket;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class OpenInstrumentPacket implements ModPacket {
    
    private static final Map<String, Supplier<Function<InteractionHand, AbstractInstrumentScreen>>> OPEN_INSTRUMENT = Map.of(
        "windsong_lyre", () -> WindsongLyreScreen::new,
        "vintage_lyre", () -> VintageLyreScreen::new,
        "floral_zither", () -> FloralZitherScreen::new,
        "glorious_drum", () -> AratakisGreatAndGloriousDrumScreen::new
    );


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


    @Override
    public void handle(Player player, PacketSender responseSender) {
        Minecraft.getInstance().setScreen(OPEN_INSTRUMENT.get(instrumentType).get().apply(hand));
    }
    
}