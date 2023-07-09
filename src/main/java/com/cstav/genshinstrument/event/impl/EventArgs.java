package com.cstav.genshinstrument.event.impl;

import com.cstav.genshinstrument.GInstrumentMod;

public abstract class EventArgs {
    
    private boolean canceled = false;

    public void setCanceled(final boolean isCanceled) {
        if (isCancelable())
            this.canceled = isCanceled;
        else if (isCanceled)
            GInstrumentMod.LOGGER.warn("Attempted to cancel uncancelable event" + getClass().getSimpleName() + "!", this);
    }
    public boolean isCanceled() {
        return canceled;
    }


    public static boolean isCancelable(final Class<?> eventArgs) {
        final Class<?> superclass = eventArgs.getClass().getSuperclass();

        return eventArgs.getClass().isAnnotationPresent(Cancelable.class)
            || ((superclass != null) && isCancelable(superclass));
    }
    public boolean isCancelable() {
        return isCancelable(getClass());
    }

}
