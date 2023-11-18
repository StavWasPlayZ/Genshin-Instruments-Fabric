package com.cstav.genshinstrument.event.impl;

import com.cstav.genshinstrument.GInstrumentMod;

public abstract class EventArgs {
    
    private boolean canceled = false;

    public void setCanceled(final boolean isCanceled) {
        if (isCancelable())
            this.canceled = isCanceled;
        else if (isCanceled)
            GInstrumentMod.LOGGER.warn("Attempted to cancel uncancellable event " + getClass().getSimpleName() + "!", this);
    }
    public boolean isCanceled() {
        return canceled;
    }

    public boolean isCancelable() {
        return getClass().isAnnotationPresent(Cancelable.class);
    }


    /**
     * Empty arguments representing a non-cancelable event
     */
    public static final class Empty extends EventArgs {}
    /**
     * Empty arguments representing a cancelable event
     */
    @Cancelable
    public static final class EmptyCancelable extends EventArgs {}

}
