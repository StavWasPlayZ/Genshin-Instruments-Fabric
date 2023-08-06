package com.cstav.genshinstrument.client.config;

import com.cstav.genshinstrument.client.config.enumType.InstrumentChannelType;
import com.cstav.genshinstrument.client.config.enumType.ZitherSoundType;
import com.cstav.genshinstrument.client.config.enumType.label.DrumNoteLabel;
import com.cstav.genshinstrument.client.config.enumType.label.NoteGridLabel;
import com.cstav.genshinstrument.sound.NoteSound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

@Environment(EnvType.CLIENT)
public class ModClientConfigs {
    public static final ForgeConfigSpec CONFIGS;

    public static final IntValue PITCH;
    public static final EnumValue<NoteGridLabel> GRID_LABEL_TYPE;
    public static final EnumValue<InstrumentChannelType> CHANNEL_TYPE;
    public static final BooleanValue STOP_MUSIC_ON_PLAY, EMIT_RING_ANIMATION, SHARED_INSTRUMENT,
        RENDER_BACKGROUND, ACCEPTED_GENSHIN_CONSENT, ACCURATE_NOTES;

    public static final EnumValue<ZitherSoundType> ZITHER_SOUND_TYPE;
    public static final EnumValue<DrumNoteLabel> DRUM_LABEL_TYPE;


    static {
        final ForgeConfigSpec.Builder configBuilder = new Builder();
    

        PITCH = configBuilder.defineInRange("instrument_pitch",
            0, NoteSound.MIN_PITCH, NoteSound.MAX_PITCH
        );
        GRID_LABEL_TYPE = configBuilder.defineEnum("label_type", NoteGridLabel.KEYBOARD_LAYOUT);
        CHANNEL_TYPE = configBuilder.defineEnum("channel_type", InstrumentChannelType.MIXED);

        STOP_MUSIC_ON_PLAY = configBuilder.comment(
            "Stops all background music when you or someone else within "+NoteSound.STOP_SOUND_DISTANCE+" blocks of range plays an instrument"
        ).define("stop_music_on_play", true);
        EMIT_RING_ANIMATION = configBuilder.define("emit_ring_animation", true);
        SHARED_INSTRUMENT = configBuilder.comment("Defines whether you will see others playing on your instrument's screen")
            .define("display_other_players", true);
        RENDER_BACKGROUND = configBuilder.define("render_background", true);
        ACCURATE_NOTES = configBuilder.define("accurate_notes", true);

        ACCEPTED_GENSHIN_CONSENT = configBuilder.define("accepted_genshin_consent", false);


        ZITHER_SOUND_TYPE = configBuilder.defineEnum("zither_sound_type", ZitherSoundType.NEW);
        DRUM_LABEL_TYPE = configBuilder.defineEnum("drum_label_type", DrumNoteLabel.KEYBOARD_LAYOUT);


        CONFIGS = configBuilder.build();
    }
}
