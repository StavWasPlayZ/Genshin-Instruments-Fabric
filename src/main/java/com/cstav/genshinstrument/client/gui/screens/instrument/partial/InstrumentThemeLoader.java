package com.cstav.genshinstrument.client.gui.screens.instrument.partial;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.event.impl.EventArgs;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

/**
 * <p>Responsible for loading and processing the instrument style JSON object
 * (commonly used as {@code instrument_style.json}).</p>
 * It contains:
 * <ul>
 * <li><b>note_theme</b> - An array representing RGB values. Used for the Note Grid's text.</li>
 * <li><b>note_pressed_theme</b> - An array representing RGB values. Used for the Note Grid's text when pressed.</li>
 * </ul>
 * This class must be initialized during mod setup.
 */
@Environment(EnvType.CLIENT)
public class InstrumentThemeLoader {
    private static final ArrayList<InstrumentThemeLoader> LOADERS = new ArrayList<>();
    private static final Color DEF_NOTE_PRESSED_THEME = new Color(255, 249, 239);

    private final ResourceLocation InstrumentStyleLocation;
    private Color noteTheme, pressedNoteTheme, labelTheme;

    private ArrayList<Consumer<JsonObject>> listeners = new ArrayList<>();
    
    /**
     * Initializes a new Instrument Theme Loader and subsribes it to the resource load event.
     * @param instrumentStyleLocation The location of the instrument's JSON styler
     */
    public InstrumentThemeLoader(final ResourceLocation instrumentStyleLocation) {
        this.InstrumentStyleLocation = instrumentStyleLocation;

        LOADERS.add(this);
        addListener(this::loadColorTheme);
    }


    public void addListener(final Consumer<JsonObject> themeLoader) {
        listeners.add(themeLoader);
    }

    public void loadColorTheme(final JsonObject theme) {
        setNoteTheme(getTheme(theme.get("note_theme"), Color.BLACK));
        setLabelTheme(getTheme(theme.get("label_theme"), Color.BLACK));
        setPressedNoteTheme(getTheme(theme.get("note_pressed_theme"), DEF_NOTE_PRESSED_THEME));
    }

    /**
     * @param rgbArray The array represenation of an RGB value
     * @param def The default value of the theme
     * @return The theme as specified in the RGB array, or the default if 
     * any exception occured.
     * 
     * @see tryGetProperty
     */
    public static Color getTheme(final JsonElement rgbArray, final Color def) {
        if (rgbArray == null || !rgbArray.isJsonArray())
            return def;

        return tryGetProperty(rgbArray.getAsJsonArray(), (rgb) -> new Color(
            rgb.get(0).getAsInt(), rgb.get(1).getAsInt(), rgb.get(2).getAsInt()
        ), def);
    }
    /**
     * @param <T> The type of property to return
     * @param <J> The type of the json element
     * @param element The element to try and take the value from
     * @param getter The method for getting the desired element
     * @param def The default value of the theme
     * @return Either the value of the getter, or the default if 
     * any exception occured.
     */
    public static <T, J extends JsonElement> T tryGetProperty(final J element, Function<J, T> getter, final T def) {
        try {
            return getter.apply(element);
        } catch (Exception e) {
            GInstrumentMod.LOGGER.error("Error retrieving property from JSON element", e);
            return def;
        }
    }


    public static void onResourcesReload(EventArgs.Empty empty) {
        final ResourceManager rManager = Minecraft.getInstance().getResourceManager();

        for (final InstrumentThemeLoader instrumentLoader : LOADERS) {
            final ResourceLocation styleLocation = instrumentLoader.getInstrumentStyleLocation();
            try {

                // Call all load listeners on the current loader
                for (final Consumer<JsonObject> listener : instrumentLoader.listeners)
                    listener.accept(JsonParser.parseReader(
                        rManager.getResource(styleLocation).get().openAsReader()
                    ).getAsJsonObject());

                GInstrumentMod.LOGGER.info("Loaded instrument style from "+styleLocation);

            } catch (IOException e) {
                GInstrumentMod.LOGGER.error("Unable to load instrument styler for " + styleLocation, e);
                continue;
            }
        }
    }


    public ResourceLocation getInstrumentStyleLocation() {
        return InstrumentStyleLocation;
    }

    public Color getNoteTheme() {
        return noteTheme;
    }
    public void setNoteTheme(Color noteTheme) {
        this.noteTheme = noteTheme;
    }
    
    public Color getPressedNoteTheme() {
        return pressedNoteTheme;
    }
    public void setPressedNoteTheme(Color pressedNoteTheme) {
        this.pressedNoteTheme = pressedNoteTheme;
    }

    public Color getLabelTheme() {
        return labelTheme;
    }
    public void setLabelTheme(Color labelTheme) {
        this.labelTheme = labelTheme;
    }

}