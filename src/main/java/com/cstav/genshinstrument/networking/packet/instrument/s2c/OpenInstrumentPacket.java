package com.cstav.genshinstrument.networking.packet.instrument.s2c;

import java.util.Map;
import java.util.function.Supplier;

import com.cstav.genshinstrument.client.gui.screen.instrument.drum.AratakisGreatAndGloriousDrumScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.floralzither.FloralZitherScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.nightwind_horn.NightwindHornScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.vintagelyre.VintageLyreScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.windsonglyre.WindsongLyreScreen;
import com.cstav.genshinstrument.networking.IModPacket;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class OpenInstrumentPacket implements IModPacket {
    
    private static final Map<String, Supplier<Supplier<Screen>>> INSTRUMENT_MAP = Map.of(
        "windsong_lyre", () -> WindsongLyreScreen::new,
        "vintage_lyre", () -> VintageLyreScreen::new,
        "floral_zither", () -> FloralZitherScreen::new,
        "glorious_drum", () -> AratakisGreatAndGloriousDrumScreen::new,
        "nightwind_horn", () -> NightwindHornScreen::new
    );

    protected Map<String, Supplier<Supplier<Screen>>> getInstrumentMap() {
        return INSTRUMENT_MAP;
    }


    private final String instrumentType;
    public OpenInstrumentPacket(final String instrumentScreen) {
        this.instrumentType = instrumentScreen;
    }

    public OpenInstrumentPacket(FriendlyByteBuf buf) {
        instrumentType = buf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(instrumentType);
    }


    @Override
    public void handle(Player player, PacketSender responseSender) {
        Minecraft.getInstance().setScreen(getInstrumentMap().get(instrumentType).get().get());
    }
}