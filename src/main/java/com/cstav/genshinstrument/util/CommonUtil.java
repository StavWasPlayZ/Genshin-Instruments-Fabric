package com.cstav.genshinstrument.util;

import com.cstav.genshinstrument.GInstrumentMod;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public abstract class CommonUtil {

    /**
     * @return What the default level should've returned, but without any conditions
     */
    public static List<Player> getPlayersInArea(final Level level, final AABB area) {
        final List<Player> list = Lists.newArrayList();

        for(Player player : level.players()) {
            if (area.contains(player.getX(), player.getY(), player.getZ()))
                list.add(player);
        }

        return list;
    }
    

    /**
     * @param dir The directory location at which to grab the specified resource
     * @param path The desired path to obtain from the {@code dir}
     * @return The resource contained in the specified directory
     */
    public static ResourceLocation getResourceFrom(final ResourceLocation dir, final String path) {
        return ResourceLocation.fromNamespaceAndPath(
            dir.getNamespace(),
            dir.getPath() + "/" + path
        );
    }


    /**
     * Retrieves a constructor from the provided {@code clazz}.
     * Failure will result in a {@link RuntimeException}.
     * @param clazz The class to reflect the constructor from
     * @param paramTypes The parameter types the function should accept
     * @return The parameterless constructor of the provided class
     * @param <T> The class type
     */
    public static <T> Constructor<T> getExpectedConstructor(final Class<T> clazz, Class<?>... paramTypes) {
        try {
            return clazz.getDeclaredConstructor(paramTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Could not find a matching constructor for " + clazz.getName(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error getting constructor for " + clazz.getName(), e);
        }
    }

    

    /**
     * Provides a similar behaviour to Python's indexing,
     * where negatives are counted backwards.
     */
    public static int pyWrap(int index, final int arrLength) {
        while (index < 0)
            index += arrLength;

        return index;
    }
    /**
     * Wraps the index around an array
     */
    public static int wrapAround(int index, final int arrLength) {
        return index % arrLength;
    }
    /**
     * Performs both {@link CommonUtil#pyWrap} and {@link CommonUtil#wrapAround}
     */
    public static int doublyPyWrap(int index, final int arrLength) {
        return wrapAround(pyWrap(index, arrLength), arrLength);
    }


    public static void loadClasses(final Class<?>[] classes) {
        for (final Class<?> loadMe : classes) {

            try {
                Class.forName(loadMe.getName());
            } catch (ClassNotFoundException e) {
                GInstrumentMod.LOGGER.error("Failed to load class "+ loadMe.getSimpleName() +": class not found", e);
            }

        }
    }


    @SuppressWarnings("unchecked")
    public static <T> T getStaticFinalField(final Class<?> clazz, final String name, Class<T> type) {
        try {
            return (T) clazz.getDeclaredField(name).get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                "Invalid %s field for %s. Please mark it public static final."
                    .formatted(name, clazz.getSimpleName()),
            e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(
                "No %s field found for %s. Please make a public static final %s %s in your packet class."
                    .formatted(name, clazz.getSimpleName(), name, type.getSimpleName()),
            e);
        } catch (ClassCastException e) {
            throw new RuntimeException(
                "Invalid type found for field %s. It should be of type %s."
                    .formatted(name, type.getSimpleName()),
            e);
        }
    }


    /**
     * @return The given {@code value} rounded by the provided {@code places}.
     */
    public static double round(double value, int places) {
        return BigDecimal.valueOf(value)
            .setScale(places, RoundingMode.HALF_UP)
            .doubleValue();
    }

}
