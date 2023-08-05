package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.event.PosePlayerArmEvent.PosePlayerArmEventArgs;
import com.cstav.genshinstrument.event.impl.Cancelable;
import com.cstav.genshinstrument.event.impl.EventArgs;
import com.cstav.genshinstrument.event.impl.ModEvent;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public interface PosePlayerArmEvent extends ModEvent<PosePlayerArmEventArgs> {

    @Environment(EnvType.CLIENT)
    @Cancelable
    public static class PosePlayerArmEventArgs extends EventArgs {

        public final Player player;
        public final HandType hand;
        public final ModelPart arm;

        public PosePlayerArmEventArgs(final Player player, final HandType hand, final ModelPart arm) {
            this.player = player;
            this.hand = hand;
            this.arm = arm;
        }

    }
    @Environment(EnvType.CLIENT)
    public enum HandType {
        LEFT, RIGHT
    }

    Event<PosePlayerArmEvent> EVENT = EventFactory.createArrayBacked(PosePlayerArmEvent.class,
        (listeners) -> args -> ModEvent.handleEvent(listeners, args)
    );

}
