package com.cstav.genshinstrument.event.impl;

@FunctionalInterface
public interface ModEvent<T extends EventArgs> {

    void triggered(T args);


    public static <E extends ModEvent<T>, T extends EventArgs> void handleEvent(E[] listeners, T args) {
        for (final E listener : listeners) {

            if (args.isCanceled())
                return;

            listener.triggered(args);
            
        }
    }

}
