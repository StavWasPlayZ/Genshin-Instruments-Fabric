package com.cstav.genshinstrument.event.impl;

@FunctionalInterface
public interface ModEvent<T extends EventArgs> {

    void triggered(T args);


    public static <E extends ModEvent<T>, T extends EventArgs> void handleEvent(E[] listeners, T args) {
        for (final E listener : listeners) {
            listener.triggered(args);
            
            if (args.isCanceled())
                return;
        }
    }

}
