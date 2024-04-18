package com.cstav.genshinstrument.sound;

import java.util.Optional;
import java.util.UUID;

import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.config.enumType.InstrumentChannelType;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.ByPlayer.ByPlayerArgs;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.InstrumentPlayedEventArgs;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteButtonIdentifier;
import com.cstav.genshinstrument.util.CommonUtil;
import com.cstav.genshinstrument.util.LabelUtil;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * A class holding sound information for an instrument's note
 */
public class NoteSound {
    /**
     * The range at which players with Mixed instrument sound type will start to hear Mono.
    */
    public static final double STEREO_RANGE = 5.5;
    /**
     * The range from which players will stop hearing Minecraft's background music on playing
     */
    public static final double STOP_SOUND_DISTANCE = 10;
    /**
     * The range from which players will hear instruments from their local sound output rather than the level's
     */
    public static final double LOCAL_RANGE = STEREO_RANGE;

    public static final int
        MIN_PITCH = -LabelUtil.NOTES_PER_SCALE,
        MAX_PITCH = LabelUtil.NOTES_PER_SCALE
    ;



    public final int index;
    public final ResourceLocation baseSoundLocation;

    SoundEvent mono;
    SoundEvent stereo;
    
    NoteSound(int index, ResourceLocation baseSoundLocation, SoundEvent mono, SoundEvent stereo) {
        this.index = index;
        this.baseSoundLocation = baseSoundLocation;

        this.mono = mono;
        this.stereo = stereo;
    }
    

    public SoundEvent getMono() {
        return mono;
    }

    public boolean hasStereo() {
        return stereo != null;
    }
    public SoundEvent getStereo() {
        return stereo;
    }

    public NoteSound[] getSoundsArr() {
        return NoteSoundRegistrar.getSounds(baseSoundLocation);
    }

    /**
     * Transposes this note by getting the note that is {@code amount} units
     * away from this note. Utilizes this note's {@link NoteSound#getSoundsArr() sound array}.
     * @param amount The unit amount of transposition. Can be positive or negative.
     * @return The note corresponding to the transposition
     */
    public NoteSoundReuslt transpose(final int amount) {
        final NoteSound[] sounds = getSoundsArr();
        int newIndex = amount + index;

        final int delta = newIndex / sounds.length;

        if (delta != 0) {
            // We can only go up/down 1 octave
            if ((delta < -1) || (delta > 1))
                return new NoteSoundReuslt(null, delta);

            newIndex += sounds.length * delta;
        }

        return new NoteSoundReuslt(sounds[newIndex], delta);
    }


    /**
     * Determines which sound type should play based on this player's distance from the instrument player.
     * <p>This method is fired from the server.</p>
     * @param distanceFromPlayer The distance between this player and the position of the note's sound
     * @return Either the Mono or Stereo sound, based on the client's preference.
     */
    @Environment(EnvType.CLIENT)
    public SoundEvent getByPreference(final double distanceFromPlayer) {
        if (!hasStereo())
            return mono;
        
        final InstrumentChannelType preference = ModClientConfigs.CHANNEL_TYPE.get();

        return switch(preference) {
            case MIXED -> (metInstrumentVolume() && (distanceFromPlayer <= STEREO_RANGE)) ? stereo : mono;

            case STEREO -> stereo;
            case MONO -> mono;
        };
    }
    /**
     * Returns the literal preference of the client. Defaults to Stereo.
     * <p>This method is fired from the client.</p>
     * @return Either the Mono or Stereo sound, based on the client's preference
     */
    @Environment(EnvType.CLIENT)
    public SoundEvent getByPreference() {
        if (!hasStereo())
            return mono;
        
        final InstrumentChannelType preference = ModClientConfigs.CHANNEL_TYPE.get();

        return switch (preference) {
            case MIXED -> metInstrumentVolume() ? stereo : mono;

            case STEREO -> stereo;
            case MONO -> mono;
        };
    }

    /**
     * @return True if the instrument volume is set to 100%
     */
    @SuppressWarnings("resource")
    private static boolean metInstrumentVolume() {
        return Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.RECORDS) == 1;
    }


    /**
     * A method for packets to use for playing this note on the client's end.
     * Will also stop the client's background music per preference.
     * @param playerUUID The UUID of the player who initiated the sound. Empty for when it wasn't a player.
     * @param playPos The position at which the sound was fired from. Empty for the player's.
     */
    @Environment(EnvType.CLIENT)
    public void play(int pitch, int volume, Optional<UUID> playerUUID,
            ResourceLocation instrumentId, Optional<NoteButtonIdentifier> buttonIdentifier, Optional<BlockPos> playPos) {
        final Minecraft minecraft = Minecraft.getInstance();
        final Player player = minecraft.player;

        final Level level = minecraft.level;
        final Player initiator = playerUUID.map(level::getPlayerByUUID).orElse(null);

        final BlockPos pos = CommonUtil.getPlayeredPosition(initiator, playPos);


        final double distanceFromPlayer = Math.sqrt(pos.distToCenterSqr(player.position()));
        
        if (ModClientConfigs.STOP_MUSIC_ON_PLAY.get() && (distanceFromPlayer < NoteSound.STOP_SOUND_DISTANCE))
            minecraft.getMusicManager().stopPlaying();


        if (initiator == null)
            InstrumentPlayedEvent.EVENT.invoker().triggered(
                new InstrumentPlayedEventArgs(this, pitch, volume, level, pos, instrumentId, buttonIdentifier.orElse(null))
            );
        else
            InstrumentPlayedEvent.ByPlayer.EVENT.invoker().triggered(
                new ByPlayerArgs(this, pitch, volume,
                    initiator, pos,
                    instrumentId, buttonIdentifier.orElse(null)
                )
            );
        

        if (player.equals(initiator))
            return;


        final float mcPitch = getPitchByNoteOffset(clampPitch(pitch));
            
        if (distanceFromPlayer > LOCAL_RANGE)
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(),
                getByPreference(distanceFromPlayer), SoundSource.RECORDS,
                1, mcPitch
            , false);
        else
            playLocally(mcPitch, volume / 100f);
    }

    /**
     * Plays this sound locally. Treats the given {@code pitch} as a Minecraft pitch.
     */
    @Environment(EnvType.CLIENT)
    public void playLocally(final float pitch, final float volume) {
        Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(
            getByPreference().getLocation(), SoundSource.RECORDS,
            volume, pitch, SoundInstance.createUnseededRandom(),
            false, 0, SoundInstance.Attenuation.NONE,
            0, 0, 0, true
        ));
    }
    /**
     * <p>Plays this note locally.</p>
     * Treats the given {@code pitch} as a note offset pitch,
     * thus performs a conversion from note offset pitch to Minecraft pitch.
     * @see NoteSound#getPitchByNoteOffset
     */
    @Environment(EnvType.CLIENT)
    public void playLocally(final int pitch, final float volume) {
        playLocally(getPitchByNoteOffset(clampPitch(pitch)), volume);
    }


    /**
     * Clams the given {@code pitch} between the set range
     */
    public static int clampPitch(final int pitch) {
        return (int)Mth.clamp(pitch, MIN_PITCH, MAX_PITCH);
    }
    /**
     * Converts the given note offset to Minecraft pitch.
     * @apiNote Formula taken from
     * <a href="https://github.com/Specy/genshin-music/blob/bb8229a279c7e5885ad3ab270b7afbe41f00d1c2/src/lib/Utilities.ts#L207C4-L207C4">
     * Specy's Genshin music app
     * </a>
     */
    public static float getPitchByNoteOffset(final int pitch) {
        return (float)Math.pow(2, (double)pitch / LabelUtil.NOTES_PER_SCALE);
    }



    public void writeToNetwork(final FriendlyByteBuf buf) {
        buf.writeResourceLocation(baseSoundLocation);
        buf.writeInt(index);
    }
    public static NoteSound readFromNetwork(final FriendlyByteBuf buf) {
        return NoteSoundRegistrar.getSounds(buf.readResourceLocation())[buf.readInt()];
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NoteSound other))
            return false;

        return baseSoundLocation.equals(other.baseSoundLocation) && (index == other.index);
    }
}
