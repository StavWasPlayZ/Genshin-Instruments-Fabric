package com.cstav.genshinstrument.networking.packet.instrument.util;

import com.cstav.genshinstrument.client.gui.screen.instrument.InstrumentScreenRegistry;
import com.cstav.genshinstrument.event.InstrumentOpenStateChangedEvent;
import com.cstav.genshinstrument.event.InstrumentOpenStateChangedEvent.InstrumentOpenStateChangedEventArgs;
import com.cstav.genshinstrument.networking.IModPacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.NotifyInstrumentOpenPacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.OpenInstrumentPacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.S2CHeldNoteSoundPacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.S2CNoteSoundPacket;
import com.cstav.genshinstrument.util.InstrumentEntityData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.function.BiConsumer;

import static com.cstav.genshinstrument.util.ServerUtil.switchEntry;

public class ClientDistExec {

    public static final Map<String, BiConsumer<? extends IModPacket, ClientPlayNetworking.Context>> PACKET_SWITCH = Map.ofEntries(
        switchEntry(S2CNoteSoundPacket.class, ClientDistExec::handleNoteSoundPacket),
        switchEntry(S2CHeldNoteSoundPacket.class, ClientDistExec::handleHeldNoteSoundPacket),
        switchEntry(OpenInstrumentPacket.class, ClientDistExec::handleOpenInstrumentPacket),
        switchEntry(NotifyInstrumentOpenPacket.class, ClientDistExec::handleNotifyInstrumentOpenPacket)
    );


    private static void handleNoteSoundPacket(final S2CNoteSoundPacket packet, final Context context) {
        packet.sound.playFromServer(packet.initiatorID, packet.meta);
    }

    private static void handleHeldNoteSoundPacket(final S2CHeldNoteSoundPacket packet, final Context context) {
        packet.sound.playFromServer(packet.initiatorID, packet.oInitiatorID, packet.meta, packet.phase);
    }

    public static void handleOpenInstrumentPacket(final OpenInstrumentPacket packet, final Context context) {
        InstrumentScreenRegistry.setScreenByID(packet.instrumentType);
    }

    public static void handleNotifyInstrumentOpenPacket(final NotifyInstrumentOpenPacket packet, final Context context) {
        final Player _player = Minecraft.getInstance().level.getPlayerByUUID(packet.playerUUID);

        if (packet.isOpen) {

            if (packet.pos.isPresent()) // is block instrument
                InstrumentEntityData.setOpen(_player, packet.pos.get());
            else // is item instrument
                InstrumentEntityData.setOpen(_player, packet.hand.get());

        } else {
            InstrumentEntityData.setClosed(_player);
        }

        InstrumentOpenStateChangedEvent.EVENT.invoker().triggered(
            new InstrumentOpenStateChangedEventArgs(packet.isOpen, context.player(), packet.pos, packet.hand)
        );
    }

}
