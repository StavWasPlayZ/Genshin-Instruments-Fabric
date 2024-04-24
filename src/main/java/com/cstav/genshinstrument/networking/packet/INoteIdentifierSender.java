// package com.cstav.genshinstrument.networking.packet;
// Applies to all the other residents of the packets package
package com.cstav.genshinstrument.networking.packet;

import java.util.List;

import com.cstav.genshinstrument.networking.IModPacket;
import com.cstav.genshinstrument.networking.GIPacketHandler;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteButtonIdentifier;

import net.minecraft.network.FriendlyByteBuf;

public interface INoteIdentifierSender extends IModPacket {

    default List<Class<? extends NoteButtonIdentifier>> acceptableIdentifiers() {
        return GIPacketHandler.ACCEPTABLE_IDENTIFIERS;
    }

    default NoteButtonIdentifier readNoteIdentifierFromNetwork(final FriendlyByteBuf buf) {
        return NoteButtonIdentifier.readFromNetwork(buf, acceptableIdentifiers());
    }

}