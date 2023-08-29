package com.cstav.genshinstrument.client.gui.screen.instrument.partial;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.Logger;

import com.cstav.genshinstrument.event.impl.EventArgs;
import com.cstav.genshinstrument.event.impl.EventArgs.Empty;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

/**
 * <p>
 * Responsible for loading and processing the instrument style JSON object, used as {@code instrument_style.json}.
 * See {@link InstrumentThemeLoader#loadColorTheme(JsonObject) implementations} to learn more about built-in properties.
 * </p>
 * 
 * This class must be initialized during mod setup.
 */
@Environment(EnvType.CLIENT)
public class InstrumentThemeLoader {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String JSON_STYLER_NAME = "instrument_style.json";

    public static final ResourceLocation
        INSTRUMENTS_META_LOC = AbstractInstrumentScreen.getInternalResourceFromGlob("instruments.meta.json"),
        GLOBAL_LOC = AbstractInstrumentScreen.getInternalResourceFromGlob("global")
    ;

    private static boolean isGlobalThemed;
    public static boolean isGlobalThemed() {
        return isGlobalThemed;
    }
        

    private static final HashMap<ResourceLocation, JsonObject> CACHES = new HashMap<>();


    private static final ArrayList<InstrumentThemeLoader> LOADERS = new ArrayList<>();
    private static final Color DEF_NOTE_PRESSED_THEME = new Color(255, 249, 239);

    public final ResourceLocation resourcesRootDir, instrumentId;
    private final boolean ignoreGlobal;

    private Color noteTheme, pressedNoteTheme, labelTheme, noteRingTheme;

    private final ArrayList<Consumer<JsonObject>> listeners = new ArrayList<>();
    
    /**
     * Initializes a new Instrument Theme Loader and subscribes it to the resource load event.
     * @param resourceRootDir The location of the root resources folder to derive styles from
     * @param ignoreGlobal When a global resource pack is enabled, defines whether this theme loader ignores it
     */
    public InstrumentThemeLoader(ResourceLocation resourceRootDir, ResourceLocation instrumentId, boolean ignoreGlobal) {
        this.resourcesRootDir = resourceRootDir;
        this.instrumentId = instrumentId;
        this.ignoreGlobal = ignoreGlobal;

        LOADERS.add(this);
        addListener(this::loadColorTheme);
    }
    /**
     * Initializes a new Instrument Theme Loader and subscribes it to the resource load event.
     * @param resourceRootDir The location of the root resources folder to derive styles from
     */
    public InstrumentThemeLoader(ResourceLocation resourceRootDir, ResourceLocation instrumentId) {
        this(resourceRootDir, instrumentId, false);
    }
    /**
     * Initializes a new Instrument Theme Loader and subscribes it to the resource load event.
     * @param instrumentId The ID of the instrument in question,
     * as well as the location of the root resources directory to derive styles from
     */
    public InstrumentThemeLoader(ResourceLocation instrumentId) {
        this(getRootPath(instrumentId), instrumentId);
    }

    public static InstrumentThemeLoader fromOther(ResourceLocation otherInstrumentId, ResourceLocation instrumentId) {
        return new InstrumentThemeLoader(getRootPath(otherInstrumentId), instrumentId);
    }
    private static ResourceLocation getRootPath(final ResourceLocation instrumentId) {
        return instrumentId.withPath(AbstractInstrumentScreen.getGlobalRootPath() + instrumentId.getPath());
    }


    public void addListener(final Consumer<JsonObject> themeLoader) {
        listeners.add(themeLoader);
    }

    public void loadColorTheme(final JsonObject theme) {
        setNoteTheme(getTheme(theme, "note_theme", Color.BLACK));
        setLabelTheme(getTheme(theme, "label_theme", Color.BLACK));
        setPressedNoteTheme(getTheme(theme, "note_pressed_theme", DEF_NOTE_PRESSED_THEME));
        setNoteRingTheme(getTheme(theme, "note_ring_theme", getNoteTheme()));
    }

    /**
     * @param rgbArray The array represenation of an RGB value
     * @param def The default value of the theme
     * @return The theme as specified in the RGB array, or the default if 
     * any exception occured.
     * 
     * @see tryGetProperty
     */
    public Color getTheme(JsonObject theme, String propertyName, Color def) {
        final JsonElement rgbArray = theme.get(propertyName);

        if (rgbArray == null || !rgbArray.isJsonArray())
            return def;

        return tryGetProperty(propertyName, rgbArray.getAsJsonArray(), (rgb) -> new Color(
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
    public <T, J extends JsonElement> T tryGetProperty(String property, J element, Function<J, T> getter, T def) {
        try {
            return getter.apply(element);
        } catch (Exception e) {
            LOGGER.error("Error retrieving JSON property for "+instrumentId, e);
            return def;
        }
    }


    public static void onResourcesReload(final EventArgs.Empty args) {
        final ResourceManager rManager = Minecraft.getInstance().getResourceManager();
        
        // Handle global resource packs
        isGlobalThemed = false;

        try {
            isGlobalThemed = JsonParser.parseReader(rManager.getResource(INSTRUMENTS_META_LOC).get().openAsReader())
                .getAsJsonObject().get("is_global_pack").getAsBoolean();

            if (isGlobalThemed)
                LOGGER.info("Instrument global themes enabled; loading all instrument resources from "+GLOBAL_LOC);
        } catch (Exception e) {}


        for (final InstrumentThemeLoader instrumentLoader : LOADERS)
            instrumentLoader.performReload(rManager);

        CACHES.clear();
    }

    private void performReload(final ResourceManager resourceManager) {
        final String logSuffix = " for "+instrumentId;

        final ResourceLocation styleLocation = getStylerLocation();
        JsonObject styleInfo;
        
        try {

            // If it is already cached, then let it be
            if (CACHES.containsKey(styleLocation)) {
                styleInfo = CACHES.get(styleLocation);
    
                for (final Consumer<JsonObject> listener : listeners)
                    listener.accept(styleInfo);
    
                LOGGER.info("Loaded instrument style from already cached "+styleLocation + logSuffix);
                return;
            }
    
    
            styleInfo = JsonParser.parseReader(
                resourceManager.getResource(styleLocation).get().openAsReader()
            ).getAsJsonObject();
    
            // Call all load listeners on the current loader
            for (final Consumer<JsonObject> listener : listeners)
                listener.accept(styleInfo);
    
            
            CACHES.put(styleLocation, styleInfo);
            LOGGER.info("Loaded and cached instrument style from "+styleLocation + logSuffix);

        } catch (Exception e) {
            LOGGER.error("Met an exception upon loading the instrument styler from "+styleLocation + logSuffix, e);
        }

    }



    public ResourceLocation getResourcesRootDir() {
        return resourcesRootDir;
    }

    public ResourceLocation getStylerLocation() {
        return ((!ignoreGlobal && isGlobalThemed) ? GLOBAL_LOC : getResourcesRootDir())
            .withSuffix("/"+JSON_STYLER_NAME);
    }

    
    public Color getNoteTheme() {
        return getTheme(() -> noteTheme);
    }
    public void setNoteTheme(Color noteTheme) {
        this.noteTheme = noteTheme;
    }
    
    public Color getPressedNoteTheme() {
        return getTheme(() -> pressedNoteTheme);
    }
    public void setPressedNoteTheme(Color pressedNoteTheme) {
        this.pressedNoteTheme = pressedNoteTheme;
    }

    public Color getLabelTheme() {
        return getTheme(() -> labelTheme);
    }
    public void setLabelTheme(Color labelTheme) {
        this.labelTheme = labelTheme;
    }

    public Color getNoteRingTheme() {
        return getTheme(() -> noteRingTheme);
    }
    public void setNoteRingTheme(Color noteRingTheme) {
        this.noteRingTheme = noteRingTheme;
    }


    protected Color getTheme(final Supplier<Color> theme) {
        return getTheme(theme, Color.BLACK);
    }

    protected <T> T getTheme(final Supplier<T> theme, final T def) {
        T _theme = theme.get();

        if (_theme == null) {
            LOGGER.warn("Requested theme not found, performing reload!");
            onResourcesReload(new Empty());

            _theme = theme.get();
            if (_theme == null) {
                LOGGER.error("Failed to load instrument resources!");
                return def;
            }
        }

        return _theme;
    }

}