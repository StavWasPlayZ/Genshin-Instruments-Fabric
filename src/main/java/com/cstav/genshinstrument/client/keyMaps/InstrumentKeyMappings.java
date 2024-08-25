package com.cstav.genshinstrument.client.keyMaps;

import org.lwjgl.glfw.GLFW;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.grid.GridInstrumentScreen;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;
import com.mojang.blaze3d.platform.InputConstants.Type;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

@Environment(EnvType.CLIENT)
public class InstrumentKeyMappings {
    public static final String CATEGORY = GInstrumentMod.MODID+".keymaps";
    
    public static void load() {}

    public static final KeyMapping
        TRANSPOSE_UP_MODIFIER = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(CATEGORY+".transpose_up_modifier",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_ALT
            , CATEGORY)
        ),
        TRANSPOSE_DOWN_MODIFIER = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(CATEGORY+".transpose_down_modifier",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT
            , CATEGORY)
        )
    ;

    
    /* --------------- Builtin Keys --------------- */
    
    public static final Key[][] GRID_INSTRUMENT_MAPPINGS = createInstrumentMaps(new int[][] {
        {81, 87, 69, 82, 84, 89, 85, 73},
        {65, 83, 68, 70, 71, 72, 74, 75},
        {90, 88, 67, 86, 66, 78, 77, 44}
    });

    // Glorious drum
    public static final DrumKeys
        DON = new DrumKeys(83, 75),
        KA = new DrumKeys(65, 76)
    ;

    @Environment(EnvType.CLIENT)
    public static final class DrumKeys {
        public final Key left, right;

        private DrumKeys(final int left, final int right) {
            this.left = create(left);
            this.right = create(right);
        }

        public Key getKey(final boolean isRight) {
            return isRight ? right : left;
        }
    }



    /**
     * Creates a grid of keys.
     * used by {@link GridInstrumentScreen} for managing keyboard input.
     * @param keyCodes A 2D array representing a key grid. Each cell should correspond to a note.
     * @return A 2D key array as described in {@code keyCodes}.
     */
    public static Key[][] createInstrumentMaps(final int[][] keyCodes) {
        final int rows = keyCodes[0].length, columns = keyCodes.length;

        final Key[][] result = new Key[columns][rows];
        for (int i = 0; i < columns; i++)
            for (int j = 0; j < rows; j++)
                result[i][j] = create(keyCodes[i][j]);

        return result;
    }

    private static Key create(final int keyCode) {
        return Type.KEYSYM.getOrCreate(keyCode);
    }

}
